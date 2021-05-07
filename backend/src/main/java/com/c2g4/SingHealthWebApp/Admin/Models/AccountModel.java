package com.c2g4.SingHealthWebApp.Admin.Models;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Object Model/Representation of an entry of the SQL Account Table
 * @author LunarFox
 *
 */
@Table("Accounts")
public class AccountModel {

	@Id
	private int account_id;
	private int employee_id;
	private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private String hp;
    private String role_id;
    private String branch_id;
    private int failed_login_attempts;
    private int is_locked;
    private Date lock_start_datetime;

	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public int getFailed_login_attempts() {
		return failed_login_attempts;
	}

	public void setFailed_login_attempts(int failed_login_attempts) {
		this.failed_login_attempts = failed_login_attempts;
	}


	public void setIs_locked(int is_locked) {
		this.is_locked = is_locked;
	}

	@JsonIgnore
	public Date getLock_start_datetime() {
		return lock_start_datetime;
	}

	public void setLock_start_datetime(Date lock_start_datetime) {
		this.lock_start_datetime = lock_start_datetime;
	}

	@JsonIgnore
	public boolean isIs_locked(){
		if(is_locked==0) {
			if(failed_login_attempts>=5){
				is_locked =1;
			}
			return is_locked==1;
		}
		Calendar now = Calendar.getInstance(); //now
		System.out.println("now"+now.getTime());
		System.out.println(lock_start_datetime);

		Calendar lock_start_datetime_cal = Calendar.getInstance();
		System.out.println("lock_start_datetime_cal "+lock_start_datetime_cal.getTime());

		lock_start_datetime_cal.setTime(lock_start_datetime);
		System.out.println("lock_start_datetime_cal "+lock_start_datetime_cal.getTime());

		//lock for 5min
		//undo after testing
		lock_start_datetime_cal.add(Calendar.SECOND,2);
		if(lock_start_datetime_cal.getTime().before(now.getTime())){
			System.out.println("reset attempts 0");
			failed_login_attempts=0;
			is_locked = 0;
//			accountRepo.changeFailedLoginAndLockAttemptsByUsername(username,failed_login_attempts,is_locked,null);
		}
		return is_locked==1;
	}

	@JsonIgnore
	public int incrementLockAttempts(){
		return ++failed_login_attempts;
	}

	public void resetLockAttempts(){
		failed_login_attempts = 0;
	}


}
