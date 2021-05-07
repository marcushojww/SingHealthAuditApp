package com.c2g4.SingHealthWebApp.Admin.Models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Object Model/Representation of an entry of the SQL Auditors Table
 * @author LunarFox
 *
 */
@Table("Auditors")
@AccessType(AccessType.Type.PROPERTY)
public class AuditorModel implements typeAccountModel{
	
	@Id
	private int acc_id;
	@Transient
	private JsonNode completed_audits;
	@Transient
	private JsonNode appealed_audits;
	@Transient
	private JsonNode outstanding_audit_ids;
	private String branch_id;
	private int mgr_id;

	//@Override
	public int getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(int acc_id) {
		this.acc_id = acc_id;
	}
	@Transient
	public JsonNode getCompleted_audits() {
		return completed_audits;
	}
	@Transient
	public void setCompleted_audits(JsonNode completed_audits) {
		this.completed_audits = completed_audits;
	}
	@Transient
	public JsonNode getAppealed_audits() {
		return appealed_audits;
	}
	@Transient
	public void setAppealed_audits(JsonNode appealed_audits) {
		this.appealed_audits = appealed_audits;
	}
	@Transient
	public JsonNode getOutstanding_audit_ids() {
		return outstanding_audit_ids;
	}
	@Transient
	public void setOutstanding_audit_ids(JsonNode outstanding_audit_ids) {
		this.outstanding_audit_ids = outstanding_audit_ids;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public int getMgr_id() {
		return mgr_id;
	}
	public void setMgr_id(int mgr_id) {
		this.mgr_id = mgr_id;
	}

	@Column("outstanding_audits")
	public String get_outstanding_audits_for_MySql() {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			return objectmapper.writeValueAsString(outstanding_audit_ids);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Column(value="outstanding_audits")
	public void set_outstanding_audits_for_MySql(String JsonString) {
		ObjectMapper objectmapper = new ObjectMapper();
		if(JsonString==null){
			this.outstanding_audit_ids =objectmapper.createObjectNode();
			return;
		}
		try {
			this.outstanding_audit_ids = objectmapper.readTree(JsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Column("completed_audits")
	public String get_completed_audits_for_MySql() {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			return objectmapper.writeValueAsString(completed_audits);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Column(value="completed_audits")
	public void set_completed_audits_for_MySql(String JsonString) {
		ObjectMapper objectmapper = new ObjectMapper();
		if(JsonString==null){
			this.completed_audits =objectmapper.createObjectNode();
			return;
		}
		try {
			this.completed_audits = objectmapper.readTree(JsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Column("appealed_audits")
	public String get_appealed_audits_for_MySql() {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			return objectmapper.writeValueAsString(appealed_audits);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Column(value="appealed_audits")
	public void set_appealed_audits_for_MySql(String JsonString) {
		ObjectMapper objectmapper = new ObjectMapper();
		if(JsonString==null){
			this.appealed_audits =objectmapper.createObjectNode();
			return;
		}
		try {
			this.appealed_audits = objectmapper.readTree(JsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	

}
