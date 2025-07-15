package com.etm571.capacitor.emdk

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.EMDKManager.EMDKListener
import com.symbol.emdk.EMDKResults
import com.symbol.emdk.personalshopper.CradleException
import com.symbol.emdk.personalshopper.CradleLedFlashInfo
import com.symbol.emdk.personalshopper.CradleResults
import com.symbol.emdk.personalshopper.PersonalShopper

@CapacitorPlugin(name = "EMDK")
class EMDKPlugin : Plugin(), EMDKListener {
    private var emdkManager: EMDKManager? = null
    private var personalShopper: PersonalShopper? = null
    private var isCradleReady = false
    private var smoothFlash = false

    override fun load() {
        val results = EMDKManager.getEMDKManager(getContext(), this)
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            val errorMessage = "EMDK initialization failed: " + results.statusCode
            Log.e("EMDK", errorMessage)

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                notifyListeners("emdkError", JSObject().put("error", errorMessage))
            }, 1000)
        }
    }

    override fun onOpened(manager: EMDKManager?) {
        this.emdkManager = manager
        try {
            personalShopper =
                emdkManager!!.getInstance(EMDKManager.FEATURE_TYPE.PERSONALSHOPPER) as PersonalShopper?

            if (personalShopper != null && personalShopper!!.cradle != null) {
                if (!personalShopper!!.cradle.isEnabled()) {
                    personalShopper!!.cradle.enable()
                }
                isCradleReady = true
                notifyListeners("emdkReady", JSObject().put("ready", true))
            } else {
                notifyListeners(
                    "emdkError",
                    JSObject().put("error", "PersonalShopper not available")
                )
            }
        } catch (e: Exception) {
            Log.e("EMDK", "Initialization error", e)
            notifyListeners("emdkError", JSObject().put("error", e.message))
        }
    }

    @PluginMethod
    fun unlockCradle(call: PluginCall) {
        if (!isCradleReady) {
            call.reject("Cradle not ready")
            return
        }

        try {
            val onDuration: Int = call.getInt("onDuration", 500)!!
            val offDuration: Int = call.getInt("offDuration", 500)!!
            val unlockDuration: Int = call.getInt("unlockDuration", 15)!!
            smoothFlash = call.getBoolean("smoothFlash", false)!!

            val flashInfo = CradleLedFlashInfo(onDuration, offDuration, smoothFlash)
            val result = personalShopper!!.cradle.unlock(unlockDuration, flashInfo)

            if (result == CradleResults.SUCCESS) {
                val ret = JSObject()
                ret.put("status", "unlocked")
                call.resolve(ret)
            } else {
                call.reject("Unlock failed: " + result.getDescription())
            }
        } catch (e: CradleException) {
            call.reject("Unlock error: " + e.message)
        }
    }


    @PluginMethod
    fun getCradleInfo(call: PluginCall) {
        if (!isCradleReady) {
            call.reject("Cradle not ready")
            return
        }

        try {
            val info = personalShopper!!.cradle.getCradleInfo()
            val result = JSObject()
            result.put("firmwareVersion", info.getFirmwareVersion())
            result.put("dateOfManufacture", info.getDateOfManufacture())
            result.put("hardwareID", info.getHardwareID())
            result.put("partNumber", info.getPartNumber())
            result.put("serialNumber", info.getSerialNumber())
            call.resolve(result)
        } catch (e: CradleException) {
            call.reject("Cradle info error: " + e.message)
        }
    }

    override fun onClosed() {
        isCradleReady = false
        if (personalShopper != null && personalShopper!!.cradle != null) {
            try {
                personalShopper!!.cradle.disable()
            } catch (e: CradleException) {
                Log.e("EMDK", "Disable error", e)
            }
        }
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }

    protected override fun handleOnDestroy() {
        onClosed()
    }
}