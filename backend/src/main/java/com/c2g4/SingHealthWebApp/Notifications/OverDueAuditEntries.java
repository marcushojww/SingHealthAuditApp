package com.c2g4.SingHealthWebApp.Notifications;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

//notifications
public class OverDueAuditEntries {
    public final List<ReportEntry> overDueEntries;
    public final int reportId;
    public final int managerId;
    public final int auditorId;
    public final int tenantId;
    public Date auditDate;
    public int auditScore;
    public int numOverDue;
    public AccountModel tenant;
    public AccountModel auditor;
    public String tenantName;
    public String tenantBranch;
    public String auditorName;
    public String earliestOverDue;

    AccountRepo accountRepo;

    public OverDueAuditEntries(List<ReportEntry> overDueEntries, Date auditDate,
                               int auditScore, int reportId, int managerId, int auditorId, int tenantId, AccountRepo accountRepo) {
        this.overDueEntries = overDueEntries;
        this.auditDate = auditDate;
        this.auditScore = auditScore;
        this.reportId = reportId;
        this.managerId = managerId;
        this.auditorId = auditorId;
        this.tenantId = tenantId;
        this.numOverDue = overDueEntries.size();
        this.accountRepo = accountRepo;
        getTenantAuditor();
        getEarliestOverdue();
    }

    private void getTenantAuditor(){
        this.tenant = accountRepo.findByAccId(tenantId);
        this.auditor = accountRepo.findByAccId(auditorId);
        this.tenantName = String.format("%s %s",tenant.getFirst_name(),tenant.getLast_name());
        this.tenantBranch = tenant.getBranch_id();
        this.auditorName = String.format("%s %s",auditor.getFirst_name(),auditor.getLast_name());
    }

    private void getEarliestOverdue(){
        Date earliest = new Date(Calendar.getInstance().getTime().getTime());
        for(ReportEntry re:overDueEntries){
            if(re.getDueDate().before(earliest))
                earliest =re.getDueDate();
        }
        earliestOverDue = earliest.toString();
    }

    public List<ReportEntry> getOverDueEntries() {
        return overDueEntries;
    }

    public int getReportId() {
        return reportId;
    }

    public int getManagerId() {
        return managerId;
    }

    public int getAuditorId() {
        return auditorId;
    }

    public int getTenantId() {
        return tenantId;
    }

    @Override
    public String toString() {
        return "OverDueAuditEntires{" +
                "num overDueEntries=" + overDueEntries.size() +
                ", reportId=" + reportId +
                ", managerId=" + managerId +
                ", auditorId=" + auditorId +
                ", tenantId=" + tenantId +
                '}';
    }

    public String getReportStats(){
        return String.format("Report ID %d : Audit Score - %d",reportId,auditScore);
    }

    public String getTenantInfo(){
        return String.format("Tenant: %s %s %s",tenantName,tenantId,tenantBranch);
    }

    public String getAuditorInfo(){
        return String.format("Auditor: %s %s",auditorName,auditorId);
    }


    public String getAuditDate() {
        return String.format("Audited on: %s",auditDate);
    }

    public int getAuditScore() {
        return auditScore;
    }

    public AccountModel getTenant() {
        return tenant;
    }

    public AccountModel getAuditor() {
        return auditor;
    }

    public String getEarliestOverDue() {
        return String.format("Overdue Since: %s",earliestOverDue);
    }

    public String getNumOverDue() {
        return numOverDue +" Rectification Overdue";
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getTenantBranch() {
        return tenantBranch;
    }

    public String getAuditorName() {
        return auditorName;
    }
}