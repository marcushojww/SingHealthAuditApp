package com.c2g4.SingHealthWebApp.Admin.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Object Model/Representation of an entry of the SQL Managers Table
 * @author LunarFox
 *
 */
@Table("Managers")
public class ManagerModel implements typeAccountModel{
    @Id
    private int acc_id;
    private String branch_id;

	@Override
	public int getAcc_id() {
		return acc_id;
	}
	public void setAcc_id(int acc_id) {
		this.acc_id = acc_id;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
    
    
}