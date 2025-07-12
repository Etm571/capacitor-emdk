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

@CapacitorPlugin(name = "EMDK")
public class EMDKPlugin extends Plugin implements EMDKManager.EMDKListener {

    private EMDKManager emdkManager;
    private PersonalShopper personalShopper;

    @Override
    public void load() {
        EMDKResults results = EMDKManager.getEMDKManager(getContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Log.e("EMDK", "Failed to get EMDKManager: " + results.statusCode);
        } else {
            Log.i("EMDK", "getEMDKManager success");
        }
    }

    @Override
    public void onOpened(EMDKManager manager) {
        Log.i("EMDK", "EMDK opened");
        this.emdkManager = manager;
        try {
            personalShopper = (PersonalShopper) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PERSONALSHOPPER);
            if (personalShopper != null && personalShopper.cradle != null) {
                personalShopper.cradle.enable();
            }
        } catch (Exception e) {
            Log.e("EMDK", "Error initializing PersonalShopper: " + e.getMessage(), e);
        }
    }

    @Override
    public void onClosed() {
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
        personalShopper = null;
    }

    @PluginMethod
    public void enable(PluginCall call) {
        try {
            if (personalShopper != null && personalShopper.cradle != null) {
                personalShopper.cradle.enable();
                call.resolve();
            } else {
                call.reject("EMDK or cradle not ready");
            }
        } catch (CradleException e) {
            Log.e("EMDK", "Enable failed", e);
            call.reject("Enable failed: " + e.getMessage());
        }
    }

    @PluginMethod
    public void disable(PluginCall call) {
        try {
            if (personalShopper != null && personalShopper.cradle != null) {
                personalShopper.cradle.disable();
                call.resolve();
            } else {
                call.reject("EMDK or cradle not ready");
            }
        } catch (CradleException e) {
            Log.e("EMDK", "Disable failed", e);
            call.reject("Disable failed: " + e.getMessage());
        }
    }

    @PluginMethod
    public void cradleInfo(PluginCall call) {
        try {
            if (personalShopper != null && personalShopper.cradle != null) {
                CradleInfo info = personalShopper.cradle.getCradleInfo();

                JSObject result = new JSObject();
                result.put("firmwareVersion", info.getFirmwareVersion());
                result.put("dateOfManufacture", info.getDateOfManufacture());
                result.put("hardwareID", info.getHardwareID());
                result.put("partNumber", info.getPartNumber());
                result.put("serialNumber", info.getSerialNumber());

                call.resolve(result);
            } else {
                call.reject("EMDK or cradle not ready");
            }
        } catch (CradleException e) {
            Log.e("EMDK", "Cradle info failed", e);
            call.reject("Cradle info failed: " + e.getMessage());
        }
    }

    @PluginMethod
    public void unlockCradle(PluginCall call) {
        try {
            if (personalShopper != null && personalShopper.cradle != null) {
                int onDuration = 500;
                int offDuration = 500;
                int unlockDuration = 15;
                boolean smoothFlash = false;

                CradleLedFlashInfo flashInfo = new CradleLedFlashInfo(onDuration, offDuration, smoothFlash);
                CradleResults result = personalShopper.cradle.unlock(unlockDuration, flashInfo);

                if (result == CradleResults.SUCCESS) {
                    JSObject res = new JSObject();
                    res.put("status", "unlocked");
                    call.resolve(res);
                } else {
                    call.reject("Unlock failed: " + result.getDescription());
                }
            } else {
                call.reject("EMDK or cradle not ready");
            }
        } catch (CradleException e) {
            Log.e("EMDK", "Unlock failed", e);
            call.reject("Unlock failed: " + e.getMessage());
        }
    }

    @Override
    protected void handleOnDestroy() {
        try {
            if (personalShopper != null && personalShopper.cradle != null) {
                personalShopper.cradle.disable();
            }
        } catch (CradleException e) {
            Log.e("EMDK", "Disable on destroy failed", e);
        }

        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }
}
