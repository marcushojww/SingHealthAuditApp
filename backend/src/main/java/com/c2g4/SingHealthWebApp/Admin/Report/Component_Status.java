package com.c2g4.SingHealthWebApp.Admin.Report;

/**
 * Enum for use in ReportEntry to track its status
 * @author LunarFox
 *
 */
public enum Component_Status {
    PASS(1),				//Component Passed
    FAIL(0),			//Component Failed, needs Tenant attention
    NA(-1);			//Component Not Applicable

    private final int status;
    Component_Status(int pass){
        status = pass;
    }

    public int isStatus() {
        return status;
    }
}