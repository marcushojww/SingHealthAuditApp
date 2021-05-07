package com.c2g4.SingHealthWebApp.Notifications;

import com.c2g4.SingHealthWebApp.Others.ResourceString;

import java.util.List;

public class OverDueRectificationEmailTemplate {
/*
    private final List<OverDueAuditEntries> overDueAuditEntires;
    private int numDiffAudits;
    private String user_type;

    OverDueRectificationEmailTemplate(List<OverDueAuditEntries> overDueAuditEntires, String user_type){
        this.overDueAuditEntires = overDueAuditEntires;
        this.numDiffAudits = overDueAuditEntires.size();
        this.user_type = user_type;
    }

    String getEntryInfo(OverDueAuditEntries overDueAuditEntry){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(overDueAuditEntry.numEntries());
        stringBuilder.append(" rectifications overdue for tenant with id");
        stringBuilder.append(overDueAuditEntry.getTenantId());
        if(user_type.equals(ResourceString.MANAGER_ROLE_KEY)){
            stringBuilder.append("\nAuditor in charge: ");
            stringBuilder.append(overDueAuditEntry.getAuditorId());
        }

        return stringBuilder.toString();
    }

    String getBody(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(numDiffAudits);
        stringBuilder.append(" overdue");
        for(OverDueAuditEntries entires: overDueAuditEntires){
            stringBuilder.append("\n");
            stringBuilder.append(getEntryInfo(entires));
        }
        return stringBuilder.toString();
    }
*/


}
