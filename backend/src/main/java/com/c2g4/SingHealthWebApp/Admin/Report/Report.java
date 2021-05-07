package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract parent class for Reports
 * @author LunarFox
 *
 */
public abstract class Report {
    private static final Logger logger = LoggerFactory.getLogger(Report.class);

    //IDs
	private int report_id;
    private int tenant_id;
    private int auditor_id;
    private int manager_id;
    //Dates
    private Date open_date;
    //Results, Status and Data
    private int overall_score;
    private String overall_remarks;
    private String report_type;
    private List<ReportEntry> entries;
    //Follow-up (if necessary)
    private int need_tenant;
    private int need_auditor;
    private int need_manager;
    //0 means open, 1 means completed
    private int overall_status;
    
    public Report() {
    	//display warning msg if caller is not builder
    }
    
	public Report(int report_id, int tenant_id, int auditor_id, int manager_id, Date open_date, int overall_score,
			String overall_remarks, String report_type, List<ReportEntry> entries, int need_tenant, int need_auditor, int need_manager,
			int overall_status) {
		this.report_id = report_id;
		this.tenant_id = tenant_id;
		this.auditor_id = auditor_id;
		this.manager_id = manager_id;
		this.open_date = open_date;
		this.overall_score = overall_score;
		this.overall_remarks = overall_remarks;
		this.report_type = report_type;
		this.entries = entries;
		this.need_tenant = need_tenant;
		this.need_auditor = need_auditor;
		this.need_manager = need_manager;
		this.overall_status = overall_status;
	}
	
	public int getReport_id() {
		return report_id;
	}

	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}

	public int getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(int tenant_id) {
		this.tenant_id = tenant_id;
	}

	public int getAuditor_id() {
		return auditor_id;
	}

	public void setAuditor_id(int auditor_id) {
		this.auditor_id = auditor_id;
	}

	public int getManager_id() {
		return manager_id;
	}

	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}

	public Date getOpen_date() {
		return open_date;
	}

	public void setOpen_date(Date open_date) {
		this.open_date = open_date;
	}

	public int getOverall_score() {
		return overall_score;
	}

	public void setOverall_score(int overall_score) {
		this.overall_score = overall_score;
	}

	public String getOverall_remarks() {
		return overall_remarks;
	}

	public void setOverall_remarks(String overall_remarks) {
		this.overall_remarks = overall_remarks;
	}

	public List<ReportEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ReportEntry> entries) {
		this.entries = entries;
	}

	public void addEntry(ReportEntry entry) {
		this.entries.add(entry);
	}
	
	public int getNeed_tenant() {
		return need_tenant;
	}

	public void setNeed_tenant(int need_tenant) {
		this.need_tenant = need_tenant;
	}

	public int getNeed_auditor() {
		return need_auditor;
	}

	public void setNeed_auditor(int need_auditor) {
		this.need_auditor = need_auditor;
	}

	public int getNeed_manager() {
		return need_manager;
	}

	public void setNeed_manager(int need_manager) {
		this.need_manager = need_manager;
	}

	public int getOverall_status() {
		return overall_status;
	}

	public void setOverall_status(int overall_status) {
		this.overall_status = overall_status;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

    
    

	
}
