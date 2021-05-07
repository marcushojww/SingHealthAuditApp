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
 * Object Model/Representation of an entry of the SQL Tenants Table
 * @author LunarFox
 *
 */
@Table("Tenant")
@AccessType(AccessType.Type.PROPERTY)
public class TenantModel implements typeAccountModel{
    @Id
    private int acc_id;
    private String type_id;
    private int audit_score;
    private int latest_audit;
	@Transient
	private JsonNode past_audits;
    private String branch_id;
    private String store_name;
    private String store_addr;

    @Override
	public int getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(int account_id) {
		this.acc_id = account_id;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public int getAudit_score() {
		return audit_score;
	}
	public void setAudit_score(int audit_score) {
		this.audit_score = audit_score;
	}
	public int getLatest_audit() {
		return latest_audit;
	}
	public void setLatest_audit(int latest_audit) {
		this.latest_audit = latest_audit;
	}
	@Transient
	public JsonNode getPast_audits() {
		return past_audits;
	}
	@Transient
	public void setPast_audits(JsonNode past_audits) {
		this.past_audits = past_audits;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public void setStore_name(String store_name) {this.store_name = store_name;}
	public String getStore_name() {return store_name; }
	public String getStore_addr() {
		return store_addr;
	}
	public void setStore_addr(String store_addr) {
		this.store_addr = store_addr;
	}

	@Column("past_audits")
	public String get_past_audits_for_MySql() {
		ObjectMapper objectmapper = new ObjectMapper();
		try {
			return objectmapper.writeValueAsString(past_audits);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Column("past_audits")
	public void set_past_audits_for_MySql(String JsonString) {
		ObjectMapper objectmapper = new ObjectMapper();
		if(JsonString==null){
			this.past_audits =objectmapper.createObjectNode();
			System.out.println("hafdosfjds");
			return;
		}
		System.out.println("hafdosfjds");

		try {
			this.past_audits = objectmapper.readTree(JsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
    
    

}
