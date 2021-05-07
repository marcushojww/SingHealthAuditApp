package com.c2g4.SingHealthWebApp.Admin.Models;

import java.sql.Date;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Object Model/Representation of an entry of the SQL CompletedAudits Table
 * @author LunarFox
 *
 */
@Table("Completed_Audits")
@AccessType(AccessType.Type.PROPERTY)
public class CompletedAuditModel extends AuditModel{
	
	@Id
    private int report_id;
    private int tenant_id;
    private int auditor_id;
    private int manager_id;
    private Date start_date;
    private Date end_date;
    private String overall_remarks;
    private String report_type;
    private int overall_score;
    @Transient
    private JsonNode report_data;
    
    public CompletedAuditModel() {}
    
	public CompletedAuditModel(int report_id, int tenant_id, int auditor_id, int manager_id, Date start_date,
			Date end_date, String overall_remarks,String report_type , int overall_score, JsonNode report_data) {
		super();
		this.report_id = report_id;
		this.tenant_id = tenant_id;
		this.auditor_id = auditor_id;
		this.manager_id = manager_id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.overall_remarks = overall_remarks;
		this.report_type = report_type;
		this.overall_score = overall_score;
		this.report_data = report_data;
	}
	
	public CompletedAuditModel(int report_id, int tenant_id, int auditor_id, int manager_id, Date start_date,
			Date end_date, String overall_remarks, String report_type, int overall_score, String report_data) {
		super();
		this.report_id = report_id;
		this.tenant_id = tenant_id;
		this.auditor_id = auditor_id;
		this.manager_id = manager_id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.overall_remarks = overall_remarks;
		this.report_type = report_type;
		this.overall_score = overall_score;
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			this.report_data = objectmapper.readTree(report_data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
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

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getOverall_remarks() {
		return overall_remarks;
	}

	public void setOverall_remarks(String overall_remarks) {
		this.overall_remarks = overall_remarks;
	}

	public int getOverall_score() {
		return overall_score;
	}

	public void setOverall_score(int overall_score) {
		this.overall_score = overall_score;
	}

	@Transient
	public JsonNode getReport_data() {
		return report_data;
	}

	@Transient
	public void setReport_data(JsonNode report_data) {
		this.report_data = report_data;
	}
	
	@Transient
	public void setReport_data(String report_data) {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			this.report_data = objectmapper.readTree(report_data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Column(value="report_data")
	public String getReport_data_for_MySql() {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			return objectmapper.writeValueAsString(report_data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Column(value="report_data")
	public void setReport_data_for_MySql(String JsonString) {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			this.report_data = objectmapper.readTree(JsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}  
}
