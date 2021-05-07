package com.c2g4.SingHealthWebApp.Admin.Report;

/**
 * [Maybe Depreciated] ENUM
 * @author LunarFox
 *
 */
public enum Service_Status {
    SERVICED(true),			//Auditor attended entry
    NEEDS_SERVICING(false);	//Auditor attention needed

    private final boolean status;
    Service_Status(boolean pass){
        status = pass;
    }
    public boolean isStatus() {
        return status;
    }
}
