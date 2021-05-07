package com.c2g4.SingHealthWebApp.Admin.Report;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * ReportEntry is a representation of an entry of Report used for manipulation.
 * Entries are never modified or deleted, only added and entries with the same qn_id essentially "overrule" older entries with the same qn_id.
 * @author LunarFox
 *
 */

@Component
public class ReportEntry {
	  private int entry_id;
	  private int qn_id;
	  private Date date;
	  private Time time;
	  private int from_account_id;
	
	  @Nullable
	  private int severity; // 7 digits XDDMMYY - X 0-nothing, 1-low, 2-med, 3-high, DDMMYY = 020421 2nd of Apr 2021
	  @Nullable
	  private String remarks;
	  @Nullable
	  private List<String> images;
	  
	  private Component_Status status;
	  
	  public int getEntry_id() {
	      return entry_id;
	  }
	
	  public void setEntry_id(int entry_id) {
	      this.entry_id = entry_id;
	  }
	
	  public int getQn_id() {
	      return qn_id;
	  }
	
	  public void setQn_id(int qn_id) {
	      this.qn_id = qn_id;
	  }
	
	  public Date getDate() {
	      return date;
	  }
	
	  public void setDate(Date date) {
	      this.date = date;
	  }
	
	  public Time getTime() {
	      return time;
	  }
	
	  public void setTime(Time time) {
	      this.time = time;
	  }
	
	  public String getRemarks() {
	      return remarks;
	  }
	
	  public void setRemarks(String remarks) {
	      this.remarks = remarks;
	  }
	  
	  public List<String> getImages(){
		  return images;
	  }
	  
	  public void setImages(List<String> images) {
		  this.images = images;
	  }
	
	  public void addImage(String base64img) {
		  if(this.images == null) {
			  this.images = new ArrayList<String>();
		  }
		  this.images.add(base64img);
	  }
	
	  public int getSeverity() {
	      return severity;
	  }
	
	  public void setSeverity(int severity) {
	      this.severity = severity;
	  }
	  
	  public Component_Status getStatus() {
	      return status;
	  }
	  @JsonSetter
	  public void setStatus(String statusStr) {
		  switch (statusStr) {
			  case "PASS" -> status = Component_Status.PASS;
			  case "FAIL" -> status = Component_Status.FAIL;
			  default -> status = Component_Status.NA;
		  }
	  }
	
	  public void setStatus(int statusBool) {
		  switch (statusBool) {
			  case 1 -> status = Component_Status.PASS;
			  case 0 -> status = Component_Status.FAIL;
			  default -> status = Component_Status.NA;
		  }
	  }

	  @JsonIgnore
	  public Date getDueDate(){
	  // 7 digits XYYYYYY - X 0-nothing, 1-low, 2-med, 3-high, DDMMYY = 020421 2nd of Apr 2021
	  	if(severity ==0) return null;
	  	int DDMMYY = severity%1000000;
		  int DDMM = DDMMYY/100;
		  int YY = DDMMYY - DDMM*100;
		  int DD = DDMM/100;
		  int MM = DDMM - DD*100;
		  if(DD ==0|MM ==0|YY ==0){
	  		System.out.println("day,month,year something 0");
	  		return null;
		}

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR,YY+2000);
		c.set(Calendar.MONTH,MM-1); //jan is 0
		c.set(Calendar.DAY_OF_MONTH,DD);
		return new Date(c.getTimeInMillis());
	  }

	public int getFrom_account_id() {
		return from_account_id;
	}

	public void setFrom_account_id(int from_account_id) {
		this.from_account_id = from_account_id;
	}
}



