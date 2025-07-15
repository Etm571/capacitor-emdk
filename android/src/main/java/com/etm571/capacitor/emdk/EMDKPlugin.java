package com.etm571.capacitor.emdk;

import android.util.Log;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.personalshopper.*;
import android.os.Handler;
import android.os.Looper;

@CapacitorPlugin(name = "EMDK")
public class EMDKPlugin extends Plugin implements EMDKManager.EMDKListener {

    private EMDKManager emdkManager;
    private PersonalShopper personalShopper;
    private boolean isCradleReady = false;
    private boolean smoothFlash = false;

    @Override
    public void load() {
        EMDKResults results = EMDKManager.getEMDKManager(getContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            String errorMessage = "EMDK initialization failed: " + results.statusCode;
            Log.e("EMDK", errorMessage);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                notifyListeners("emdkError", new JSObject().put("error", errorMessage));
            }, 1000);
        }

    }

    @Override
    public void onOpened(EMDKManager manager) {
        this.emdkManager = manager;
        try {
            personalShopper = (PersonalShopper) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PERSONALSHOPPER);

            if (personalShopper != null && personalShopper.cradle != null) {
                if (!personalShopper.cradle.isEnabled()){
                    personalShopper.cradle.enable();
                }
                isCradleReady = true;
                notifyListeners("emdkReady", new JSObject().put("ready", true));
            } else {
                notifyListeners("emdkError", new JSObject().put("error", "PersonalShopper not available"));
            }
        } catch (Exception e) {
            Log.e("EMDK", "Initialization error", e);
            notifyListeners("emdkError", new JSObject().put("error", e.getMessage()));
        }
    }

    @PluginMethod
    public void unlockCradle(PluginCall call) {
        if (!isCradleReady) {
            call.reject("Cradle not ready");
            return;
        }

        try {
            int onDuration = call.getInt("onDuration", 500);
            int offDuration = call.getInt("offDuration", 500);
            int unlockDuration = call.getInt("unlockDuration", 15);
            smoothFlash = call.getBoolean("smoothFlash", false);

            CradleLedFlashInfo flashInfo = new CradleLedFlashInfo(onDuration, offDuration, smoothFlash);
            CradleResults result = personalShopper.cradle.unlock(unlockDuration, flashInfo);

            if (result == CradleResults.SUCCESS) {
                JSObject ret = new JSObject();
                ret.put("status", "unlocked");
                call.resolve(ret);
            } else {
                call.reject("Unlock failed: " + result.getDescription());
            }
        } catch (CradleException e) {
            call.reject("Unlock error: " + e.getMessage());
        }
    }

    @PluginMethod
    public void flashLeds(PluginCall call) {
        if (!isCradleReady) {
            call.reject("Cradle not ready");
            return;
        }

        try {
            int onDuration = call.getInt("onDuration", 2000);
            int offDuration = call.getInt("offDuration", 1000);
            int flashCount = call.getInt("flashCount", 5);
            smoothFlash = call.getBoolean("smoothFlash", false);

            CradleLedFlashInfo flashInfo = new CradleLedFlashInfo(onDuration, offDuration, smoothFlash);
            CradleResults result = personalShopper.cradle.flashLed(flashCount, flashInfo);

            if (result == CradleResults.SUCCESS) {
                JSObject ret = new JSObject();
                ret.put("status", "leds_flashed");
                call.resolve(ret);
            } else {
                call.reject("LED flash failed: " + result.getDescription());
            }
        } catch (CradleException e) {
            call.reject("LED flash error: " + e.getMessage());
        }
    }

    @PluginMethod
    public void getCradleInfo(PluginCall call) {
        if (!isCradleReady) {
            call.reject("Cradle not ready");
            return;
        }

        try {
            CradleInfo info = personalShopper.cradle.getCradleInfo();
            JSObject result = new JSObject();
            result.put("firmwareVersion", info.getFirmwareVersion());
            result.put("dateOfManufacture", info.getDateOfManufacture());
            result.put("hardwareID", info.getHardwareID());
            result.put("partNumber", info.getPartNumber());
            result.put("serialNumber", info.getSerialNumber());
            call.resolve(result);
        } catch (CradleException e) {
            call.reject("Cradle info error: " + e.getMessage());
        }
    }

    @Override
    public void onClosed() {
        isCradleReady = false;
        if (personalShopper != null && personalShopper.cradle != null) {
            try {
                personalShopper.cradle.disable();
            } catch (CradleException e) {
                Log.e("EMDK", "Disable error", e);
            }
        }
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    protected void handleOnDestroy() {
        onClosed();
    }
}