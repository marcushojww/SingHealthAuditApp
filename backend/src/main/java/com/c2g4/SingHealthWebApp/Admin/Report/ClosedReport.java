package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.util.List;

/**
 * Object that represents a closed report for manipulation in the Java backend
 * @author LunarFox
 *
 */
public class ClosedReport extends Report{
    private Date close_date;
    
    

	public ClosedReport() {
		super();
	}

	public ClosedReport(int report_id, int tenant_id, int auditor_id, int manager_id, Date open_date, int overall_score,
			String overall_remarks, String report_type, List<ReportEntry> entries, int need_tenant, int need_auditor, int need_manager,
			int overall_status, Date close_date) {
		super(report_id, tenant_id, auditor_id, manager_id, open_date, overall_score, overall_remarks, report_type, entries, need_tenant,
				need_auditor, need_manager, overall_status);
		this.close_date = close_date;
	}

	public Date getClose_date() {
		return close_date;
	}

	public void setClose_date(Date close_date) {
		this.close_date = close_date;
	}

    
    
}