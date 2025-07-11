package com.etm571.capacitor.emdk;

import com.getcapacitor.Logger;

public class EMDK {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
