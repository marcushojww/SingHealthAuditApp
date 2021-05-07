package com.c2g4.SingHealthWebApp.Admin.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


/**
 * Object Model/Representation of an entry of the SQL Branch Table
 * @author LunarFox
 *
 */
@Table("branches")
public class BranchModel {
    @Id
    private String branch_id;
    private String branch_addr;
    private int no_of_tenants;
    private int no_of_auditors;
    
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public String getBranch_addr() {
		return branch_addr;
	}
	public void setBranch_addr(String branch_addr) {
		this.branch_addr = branch_addr;
	}
	public int getNo_of_tenants() {
		return no_of_tenants;
	}
	public void setNo_of_tenants(int no_of_tenants) {
		this.no_of_tenants = no_of_tenants;
	}
	public int getNo_of_auditors() {
		return no_of_auditors;
	}
	public void setNo_of_auditors(int no_of_auditors) {
		this.no_of_auditors = no_of_auditors;
	}
    
    
}
