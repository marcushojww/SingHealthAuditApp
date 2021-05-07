package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object that represents a open report for manipulation in the Java backend
 * @author LunarFox
 *
 */
public class OpenReport extends Report{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Date last_update_date;
    
	public OpenReport() {
		super();
	}

	public OpenReport(int report_id, int tenant_id, int auditor_id, int manager_id, Date open_date, int overall_score,
			String overall_remarks, String report_type, List<ReportEntry> entries, int need_tenant, int need_auditor, int need_manager,
			int overall_status, Date last_update_date) {
		super(report_id, tenant_id, auditor_id, manager_id, open_date, overall_score, overall_remarks, report_type, entries, need_tenant,
				need_auditor, need_manager, overall_status);
		this.last_update_date = last_update_date;
	}

	public Date getLast_update_date() {
		return last_update_date;
	}

	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}
}


