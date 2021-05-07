package com.c2g4.SingHealthWebApp.Admin.Models;

import java.sql.Date;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AuditModelBuilder is the builder class for AuditModel
 * Using this class directly is not recommended, I recommend using ReportBuilder
 * @author LunarFox
 *
 */
public class AuditModelBuilder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    //Handy-dandy little fact: If CrudRepo.save is ran with the ID set as 0, the CrudRepo auto
    //increments the ID.
    //If it is ran with the ID set to something non-existent, it will throw an error.
    //This also means that there is no way for us to set a custom report_id
    
    //IDs
    private int report_id;
    private int tenant_id;
    private int auditor_id;
    private int manager_id;
    //Dates
    private Date start_date;
    private Date last_update_date;
    private Date end_date;
    //Results, Status and Data
    private int overall_score;
    private String overall_remarks;
	private String report_type;
    private JsonNode report_data;
    //Follow-up (if necessary)
    private int need_tenant;
    private int need_auditor;
    private int need_manager;
    
    //0 means open, 1 means completed
    private int overall_status;
	
    //init default values
	public AuditModelBuilder() {
		//Attributes of the AuditModel
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		this.report_id = -1;
		this.tenant_id = -1;
		this.auditor_id = -1;
		this.manager_id = -1;
		this.start_date = sqlCurrentDate;
		this.overall_remarks = "Nil";
		this.report_type = "-1";
		this.overall_score = -1;
		this.report_data = null;
		this.need_tenant = 0;
		this.need_auditor = 0;
		this.need_manager = 0;
		
		//Attributes specific to one of the classes
		this.last_update_date = sqlCurrentDate;
		this.end_date = sqlCurrentDate;
		
		this.overall_status = 0;
	}
	
	//Builder
	/**
	 * Builds and returns a Report Object from the parameters contained in the builder object.
	 * 
	 * @return AuditModel Object
	 */

	public AuditModel build() {
		//Check for errors
		if (this.report_id == -1) {
			logger.error("Report_id not set!");
			throw new IllegalArgumentException();
		}
		if (this.tenant_id == -1) {
			logger.error("Tenant_id not set!");
			throw new IllegalArgumentException();
		}
		if (this.auditor_id == -1) {
			logger.error("Auditor_id not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_score == -1) {
			logger.error("overall_score not set!");
			throw new IllegalArgumentException();
		}
		if (this.report_type.matches("-1")) {
			logger.error("report_type not set!");
			throw new IllegalArgumentException();
		}
		if (this.report_data == null) {
			logger.error("There is no report data!");
			throw new IllegalArgumentException();
		}
		if (this.overall_status == 0 && (this.need_auditor + this.need_tenant + this.need_manager < 1)) {
			logger.error("This report is open but no 'need_user' bit has been set!");
			throw new IllegalArgumentException();
		}
		
		AuditModel audit = null;
		switch(this.overall_status) {
		case 0:
			audit = new OpenAuditModel(this.report_id, this.tenant_id, this.auditor_id,
					this.manager_id, this.start_date, this.last_update_date, this.overall_remarks,
					this.report_type, this.overall_score, this.report_data, 
					this.need_tenant, this.need_auditor,this.need_manager);
			break;
		case 1:
			audit = new CompletedAuditModel(this.report_id, this.tenant_id, this.auditor_id,
					this.manager_id, this.start_date, this.end_date, this.overall_remarks,
					this.report_type, this.overall_score, this.report_data);
			break;
		}
		return audit;
	}

	
	//Getters and Setters for the builder class specifically
	public String getReportStatus() {
		switch(this.overall_status) {
		case 0:
			return "Open Audit";
		case 1:
			return "Completed Audit";
		}
		return null;
	}
	
	public AuditModelBuilder setTypeIsOpenAudit() {
		this.overall_status = 0;
		return this;
	}
	
	public AuditModelBuilder setTypeIsCompletedAudit() {
		this.overall_status = 1;
		return this;
	}
	
	
	
	//Getters and Setters for attributes needed for the AuditModel
	public int getReportId() {
		return report_id;
	}

	public AuditModelBuilder setReportId(int report_id) {
		this.report_id = report_id;
		return this;
	}

	public int getTenantId() {
		return tenant_id;
	}

	public AuditModelBuilder setTenantId(int tenant_id) {
		this.tenant_id = tenant_id;
		return this;
	}

	public int getAuditorId() {
		return auditor_id;
	}

	public AuditModelBuilder setAuditorId(int auditor_id) {
		this.auditor_id = auditor_id;
		return this;
	}

	public int getManagerId() {
		return manager_id;
	}

	public AuditModelBuilder setManagerId(int manager_id) {
		this.manager_id = manager_id;
		return this;
	}

	public Date getStart_date() {
		return start_date;
	}

	public AuditModelBuilder setStartDate(Date start_date) {
		this.start_date = start_date;
		return this;
	}

	public Date getLastUpdateDate() {
		return last_update_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public AuditModelBuilder setEnd_date(Date end_date) {
		this.end_date = end_date;
		return this;
	}

	public AuditModelBuilder setLastUpdateDate(Date last_update_date) {
		this.last_update_date = last_update_date;
		return this;
	}

	public String getOverallRemarks() {
		return overall_remarks;
	}
	
	public String getReport_type() {
		return report_type;
	}

	public AuditModelBuilder setReport_type(String report_type) {

		this.report_type = report_type;
		return this;
	}

	public AuditModelBuilder setOverallRemarks(String overall_remarks) {
		this.overall_remarks = overall_remarks;
		return this;
	}

	public int getOverallScore() {
		return overall_score;
	}

	public AuditModelBuilder setOverallScore(int overall_score) {
		this.overall_score = overall_score;
		return this;
	}

	public JsonNode getReportData() {
		return report_data;
	}

	public AuditModelBuilder setReportData(JsonNode report_data) {
		this.report_data = report_data;
		return this;
	}
	
	public AuditModelBuilder setReportData(String report_data) {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			this.report_data = objectmapper.readTree(report_data);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return this;
	}

	public int getNeedAuditor() {
		return need_auditor;
	}

	public AuditModelBuilder setNeedAuditor(int need_auditor) {
		this.need_auditor = need_auditor;
		return this;
	}

	public int getNeedTenant() {
		return need_tenant;
	}

	public AuditModelBuilder setNeedTenant(int need_tenant) {
		this.need_tenant = need_tenant;
		return this;
	}

	public int getNeedManager() {
		return need_manager;
	}

	public AuditModelBuilder setNeedManager(int need_manager) {
		this.need_manager = need_manager;
		return this;
	}
	
	//Condensed Builders Setters
	
	public AuditModelBuilder setUserIDs(int tenant_id, int auditor_id, int manager_id) {
		this.tenant_id = tenant_id;
		this.auditor_id = auditor_id;
		this.manager_id = manager_id;
		return this;
	}
	
	public AuditModelBuilder setNeed(int need_tenant, int need_auditor, int need_manager) {
		this.need_tenant = need_tenant;
		this.need_auditor = need_auditor;
		this.need_manager = need_manager;
		return this;
	}


	
	

}
