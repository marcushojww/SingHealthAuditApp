package com.c2g4.SingHealthWebApp.Admin.Report;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditModelBuilder;
import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListSMARepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * ReportBuilder serves as a builder for the ReportObject as well as provides utility functions
 * for the handling of ReportObjects.
 * @author LunarFox
 *
 */
public class ReportBuilder {
	//These to repos cannot be autowired from here, idk why it doesn't work
	//The workaround is to pass the auditRepo when calling the relevant methods
    private OpenAuditRepo openAuditRepo;
    private CompletedAuditRepo completedAuditRepo;
    private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
    private static ObjectMapper objectmapper = new ObjectMapper();
	
    //IDs
	private int report_id;
    private int tenant_id;
    private int auditor_id;
    private int manager_id;
    //Dates
    private Date open_date;
    private Date last_update_date;
    private Date close_date;
    //Results, Status and Data
    private int overall_score;
    private String overall_remarks;
    private List<ReportEntry> entries;
    private String report_type;
    //Follow-up (if necessary)
    private int need_tenant;
    private int need_auditor;
    private int need_manager;
    //0 means open, 1 means completed
    private int overall_status;
    
    //Constructors
    private ReportBuilder() {
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		this.report_id = -1;
		this.tenant_id = -1;
		this.auditor_id = -1;
		this.manager_id = -1;
		this.open_date = sqlCurrentDate;
		this.overall_remarks = "Nil";
		this.report_type = "-1";
		this.overall_score = -1;
		this.entries = new ArrayList<>();
		this.need_tenant = 0;
		this.need_auditor = 0;
		this.need_manager = 0;
		
		//Attributes specific to one of the classes
		this.last_update_date = sqlCurrentDate;
		this.close_date = sqlCurrentDate;
		
		this.overall_status = -1;
		
		this.completedAuditRepo = null;
		this.openAuditRepo = null;
    }
    
    private ReportBuilder(OpenAuditModel auditModel) {
    	this.report_id = auditModel.getReport_id();
    	this.tenant_id = auditModel.getTenant_id();
    	this.auditor_id = auditModel.getAuditor_id();
    	this.manager_id = auditModel.getManager_id();
    	this.open_date = auditModel.getStart_date();
    	this.overall_remarks = auditModel.getOverall_remarks();
    	this.report_type = auditModel.getReport_type();
    	this.overall_score = auditModel.getOverall_score();
    	try {
			this.entries = objectmapper.treeToValue(auditModel.getReport_data(), OpenReport.class).getEntries();
    	} catch (JsonProcessingException e) {
			logger.error("Could not wrap auditModel into Report due to malformed report_data!");
			e.printStackTrace();
		}
    	this.need_auditor = auditModel.getNeed_auditor();
    	this.need_tenant = auditModel.getNeed_tenant();
    	this.need_manager = auditModel.getNeed_manager();
    	this.last_update_date = auditModel.getLast_update_date();
    	this.overall_status = 0;
    }
    
    private ReportBuilder(Report report) {
    	this.report_id = report.getReport_id();
    	this.tenant_id = report.getTenant_id();
    	this.auditor_id = report.getAuditor_id();
    	this.manager_id = report.getManager_id();
    	this.open_date = report.getOpen_date();
    	this.overall_remarks = report.getOverall_remarks();
    	this.report_type = report.getReport_type();
    	this.overall_score = report.getOverall_score();
    	this.entries = report.getEntries();
    	
    	//if(this.report_type.matches(ResourceString.REPORT_STATUS_OPEN)) {
    	if(report.getClass().equals(OpenReport.class)){
    		OpenReport openReport = (OpenReport) report;
        	this.need_auditor = openReport.getNeed_auditor();
        	this.need_tenant = openReport.getNeed_tenant();
        	this.need_manager = openReport.getNeed_manager();
        	this.last_update_date = openReport.getLast_update_date();
        	this.overall_status = 0;
    	}
    	//else if (this.report_type.matches(ResourceString.REPORT_STATUS_CLOSED)) {
		else if (report.getClass().equals(ClosedReport.class)) {
    		ClosedReport closedReport = (ClosedReport) report;
    		this.close_date = closedReport.getClose_date();
    		this.overall_status = 1;
    	}
    	    }
    
    private ReportBuilder(CompletedAuditModel auditModel) {
    	this.report_id = auditModel.getReport_id();
    	this.tenant_id = auditModel.getTenant_id();
    	this.auditor_id = auditModel.getAuditor_id();
    	this.manager_id = auditModel.getManager_id();
    	this.open_date = auditModel.getStart_date();
    	this.overall_remarks = auditModel.getOverall_remarks();
    	this.report_type = auditModel.getReport_type();
    	this.overall_score = auditModel.getOverall_score();
    	try {
			this.entries = objectmapper.treeToValue(auditModel.getReport_data(), ClosedReport.class).getEntries();
    	} catch (JsonProcessingException e) {
			logger.error("Could not wrap auditModel into Report due to malformed report_data!");
			e.printStackTrace();
		}
    	this.close_date = auditModel.getEnd_date();
    	this.overall_status = 1;
    }
    
    /**
     * Builds and returns a Report Object from the parameters contained in the ReportBuilder.
     * 
     * @return Either an OpenReport or a CloseReport object
     */
    
    public Report build() {
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
		if (this.report_type.matches("-1")) {
			logger.error("Report type not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_score == -1) {
			logger.error("overall_score not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_status == -1) {
			logger.error("overall_status not set!");
			throw new IllegalArgumentException();
		}
		if (this.overall_status == 0 && (this.need_auditor + this.need_tenant + this.need_manager < 1)) {
			logger.error("This report is open but no 'need_user' bit has been set!");
			throw new IllegalArgumentException();
		}
		
		Report report = null;
		switch(this.overall_status) {
		case 0:
			report = new OpenReport(report_id, tenant_id, auditor_id, manager_id, open_date, overall_score, overall_remarks,
					report_type, entries, need_tenant,
					need_auditor, need_manager, overall_status, last_update_date);
			break;
		case 1:
			report = new ClosedReport(report_id, tenant_id, auditor_id, manager_id, open_date, overall_score, overall_remarks,
					report_type, entries, need_tenant,
					need_auditor, need_manager, overall_status, close_date);
			break;
		default:
			logger.error("Report is of an invalid type!");
			throw new IllegalArgumentException();
		}
		return report;
    	
    }
    
    //Methods for creating, modifying and loading reports
    /**
     * Returns a ReportBuilder for creating a custom Report Object.
     * @param openAuditRepo repo object for interfacing with the database
     * @param completedAuditRepo repo object for interfacing with the database
     * @return ReportBuilder object with default values.
     */
    public static ReportBuilder getReportBuilder(OpenAuditRepo openAuditRepo, CompletedAuditRepo completedAuditRepo) {
    	ReportBuilder builder = new ReportBuilder();
    	builder.setOpenAuditRepo(openAuditRepo);
    	builder.setCompletedAuditRepo(completedAuditRepo);
    	return builder;
    }
    
    /**
     * Returns a ReportBuilder for creating a new Report.
     * @param openAuditRepo repo object for interfacing with the database
     * @param completedAuditRepo repo object for interfacing with the database
     * @return ReportBuilder object with values set for creating a new OpenReport.
     */
    public static ReportBuilder getNewReportBuilder(OpenAuditRepo openAuditRepo, CompletedAuditRepo completedAuditRepo) {
    	ReportBuilder builder = new ReportBuilder();
    	builder.setReport_id(0);
    	builder.setOverall_statusAsOpen();
    	builder.setOpenAuditRepo(openAuditRepo);
    	builder.setCompletedAuditRepo(completedAuditRepo);
    	return builder;
    }
    
    /**
     * Returns a ReportBuilder with values loaded from an existing report
     * @param openAuditRepo repo object for interfacing with the database
     * @param completedAuditRepo repo object for interfacing with the database
     * @param report report to be loaded
     * @return ReportBuilder object with values loaded from an existing report.
     */
    public static ReportBuilder getLoadedReportBuilder(OpenAuditRepo openAuditRepo, CompletedAuditRepo completedAuditRepo,
    		Report report) {
    	ReportBuilder builder = new ReportBuilder(report);
    	builder.setOpenAuditRepo(openAuditRepo);
    	builder.setCompletedAuditRepo(completedAuditRepo);
    	return builder;
    }
    
    /**
     * Returns a ReportBuilder with values loaded from an existing report
     * @param openAuditRepo repo object for interfacing with the database
     * @param completedAuditRepo repo object for interfacing with the database
     * @param report_id report_id of the report
     * @return ReportBuilder object with values loaded from an existing report.
     */
    public static ReportBuilder getLoadedReportBuilder(OpenAuditRepo openAuditRepo, CompletedAuditRepo completedAuditRepo,
    		int report_id) {
    	ReportBuilder builder = ReportBuilder.getNewReportBuilder(openAuditRepo, completedAuditRepo);
    	if(builder.checkOpenReportExists(report_id)) {
    		builder = new ReportBuilder(builder.loadOpenReport(report_id));
    	}else if (builder.checkClosedReportExists(report_id)) {
    		builder = new ReportBuilder(builder.loadClosedReport(report_id));
    	}else {
    		logger.warn("Report with given ID does not exist!");
    		return null;
    	}
    	builder.setOpenAuditRepo(openAuditRepo);
    	builder.setCompletedAuditRepo(completedAuditRepo);
    	return builder;
    }
    
    /**
     * [POTENTIALLY DEPRECIATED] Closes an openreport into a closedreport.
     * Might not be a fantastic idea to use this, in fact at line 213 of report controller
     * I seem to have inadvertently bypassed this.
     * @param report object to be converted into a closedreport
     * @return ClosedReport generated from the openreport object
     */
    public ClosedReport closeReport(OpenReport report) {
    	ReportBuilder builder = new ReportBuilder();
    	builder.setReport_id(report.getReport_id())
    	.setUserIDs(report.getTenant_id(), report.getAuditor_id(), report.getManager_id())
    	.setOpen_date(report.getOpen_date()).setOverall_remarks(report.getOverall_remarks())
    	.setReportType(report.getReport_type())
    	.setOverall_score(report.getOverall_score()).setEntries(report.getEntries());
    	ClosedReport closedreport = (ClosedReport)builder.build();
    	return closedreport;
    }
    
    /**
     * Deletes an existing open report.
     * @param report_id report_id of the to be deleted.
     * @return true if the deletion was successful
     */
    public boolean deleteOpenReport(int report_id) {
    	if(!this.checkOpenReportExists(report_id)) {
    		logger.error("Report with id " + report_id + "could not be deleted because it does not exist.");
    		return false;
    	}
    		openAuditRepo.deleteAuditById(report_id);
        	if(this.checkOpenReportExists(report_id)) {
        		logger.error("Report of id " + report_id + " could not be deleted!");
        		return false;
        	}
        return true;
    }

	public boolean deleteOpenAuditsFromUsers(Report report, TenantRepo tenantRepo, AuditorRepo auditorRepo, ManagerRepo managerRepo) {
		tenantRepo.removeLatestAuditByTenantId(report.getTenant_id());
		String outstandingAudits = auditorRepo.getOutstandingAuditsFromAuditorID(report.getAuditor_id());
		String updatedJsonAuditor = removeFromJson(outstandingAudits,
				ResourceString.AUDITOR_OUTSTANDING_AUDITS_JSON_KEY, String.valueOf(report.getReport_id()));
		logger.info("UPDATEDJSONAUDITOR :{}",updatedJsonAuditor);
		if(updatedJsonAuditor==null) return false;
		auditorRepo.updateLatestOutstandingAuditsByAuditorId(report.getAuditor_id(), updatedJsonAuditor);
		return true;
    }

	private String removeFromJson(String jsonString, String key, String valueToRemove){
//		logger.info("val to remove {}",valueToRemove);
    	try {
			ObjectNode root = (ObjectNode) objectmapper.readTree(jsonString);
			ArrayNode keyNode = (ArrayNode) root.get(key);
			int index = -1;
			for(int i=0;i<keyNode.size();i++){
//				logger.info("keynode {}: {}",i,keyNode.get(i));
				if(keyNode.get(i).asText().equals(valueToRemove)){
					index = i;
					break;
				}
			}
			if(index==-1) return null;
			keyNode.remove(index);
			return objectmapper.writeValueAsString(root);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * Deletes an existing closed report.
     * @param report_id report_id of the to be deleted.
     * @return true if the deletion was successful
     */
    public boolean deleteClosedReport(int report_id) {
    	if(!this.checkClosedReportExists(report_id)) {
    		logger.error("Report with id " + report_id + "could not be deleted because it does not exist.");
    		return false;
    	}
		completedAuditRepo.deleteAuditById(report_id);
    	if(this.checkClosedReportExists(report_id)) {
    		logger.error("Report of id " + report_id + "could not be deleted!");
    		return false;
    	}
    	return true;
    }
    
    //TODO Implement a way to reopen a closedReport if necessary
    
    /**
     * Checks if an openreport by the given id exists in the database
     * @param report_id ID of the report
     * @return True if the report exists in the database and false otherwise
     */
    public boolean checkOpenReportExists(int report_id) {
    	return openAuditRepo.existsById(report_id);
    }
    
    /**
     * Checks if a closedreport by the given id exists in the database
     * @param report_id ID of the report
     * @return True if the report exists in the database and false otherwise
     */
    public boolean checkClosedReportExists(int report_id) {
    	return completedAuditRepo.existsById(report_id);
    }
    
    /**
     * Returns the openreport by the given id from the database if it exists.
     * This method actually obtains an AuditModel from the db and converts it into
     * a Report object for returning.
     * @param report_id ID of the report
     * @return OpenReport of the given id
     */
    public OpenReport loadOpenReport(int report_id) {
    	OpenAuditModel auditModel = openAuditRepo.getOpenAuditById(report_id);
    	if (auditModel == null) {
    		logger.error("A report with the id " + report_id + " could not be found in the database.");
    		throw new IllegalArgumentException();
    	}else {
    		logger.info("Report with id " + report_id + " has been found.");
    	}
    	ReportBuilder builder = new ReportBuilder(auditModel);
    	return (OpenReport) builder.build();
    }
    
    /**
     * Returns the closedreport by the given id from the database if it exists.
     * This method actually obtains an AuditModel from the db and converts it into
     * a Report object for returning.
     * @param report_id ID of the report
     * @return ClosedReport of the given id
     */
    public ClosedReport loadClosedReport(int report_id) {
    	CompletedAuditModel auditModel = completedAuditRepo.getCompletedAuditById(report_id);
    	if (auditModel == null) {
    		logger.error("A report with the id " + report_id + " could not be found in the database.");
    		throw new IllegalArgumentException();
    	}else {
    		logger.info("Report with id " + report_id + " has been found.");
    	}
    	ReportBuilder builder = new ReportBuilder(auditModel);
    	return (ClosedReport) builder.build();
    }
    
    //Methods for saving reports
    private boolean saveOpenReport(OpenReport report) {
    	//Just to prevent any cheating/automate date updates ^_^
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		report.setLast_update_date(sqlCurrentDate);
		
    	AuditModelBuilder builder = new AuditModelBuilder();
        builder.setReportId(report.getReport_id()).setUserIDs(report.getTenant_id(), report.getAuditor_id()
        		, report.getManager_id()).setOverallRemarks(report.getOverall_remarks())
        .setReport_type(report.getReport_type()).setOverallScore(report.getOverall_score())
        .setReportData(objectmapper.valueToTree(report))
        .setNeed(report.getNeed_tenant(),report.getNeed_auditor(),report.getNeed_manager())
        .setStartDate(report.getOpen_date()).setLastUpdateDate(report.getLast_update_date())
        .setTypeIsOpenAudit();
        
        OpenAuditModel audit = (OpenAuditModel) builder.build();
        try {
        	OpenAuditModel openAuditModel = openAuditRepo.save(audit);
        	logger.info("GENERATED REPORT ID: {}",openAuditModel.getReport_id());
        	report.setReport_id(openAuditModel.getReport_id());
        }catch (IllegalArgumentException e) {
        	return false;
        }
        return true;
    }
    
    private boolean saveClosedReport(ClosedReport report) {
    	//Just to prevent any cheating/automate date updates ^_^
		java.util.Date utilCurrentDate = Calendar.getInstance().getTime();
		Date sqlCurrentDate = new Date(utilCurrentDate.getTime());
		report.setClose_date(sqlCurrentDate);
		
    	AuditModelBuilder builder = new AuditModelBuilder();
        builder.setReportId(report.getReport_id()).setUserIDs(report.getTenant_id(), report.getAuditor_id()
        		, report.getManager_id()).setOverallRemarks(report.getOverall_remarks())
        .setReport_type(report.getReport_type()).setOverallScore(report.getOverall_score())
        .setReportData(objectmapper.valueToTree(report)).setNeed(1,1,1)
        .setStartDate(report.getOpen_date()).setEnd_date(report.getClose_date())
        .setTypeIsCompletedAudit();
        
        CompletedAuditModel audit = (CompletedAuditModel) builder.build();
        try {
        	if(!completedAuditRepo.existsById(audit.getReport_id())) {
            	completedAuditRepo.createNewEntryWithId(audit.getReport_id());
        	}
			CompletedAuditModel completedAuditModel = completedAuditRepo.save(audit);
			logger.info("GENERATED REPORT ID: {}",completedAuditModel.getReport_id());
			report.setReport_id(completedAuditModel.getReport_id());

        }catch (IllegalArgumentException e) {
        	return false;
        }
        return true;
    }

    
    public boolean updateLatestReportIds(Report report, TenantRepo tenantRepo, AuditorRepo auditorRepo, ManagerRepo managerRepo) {
    	//int report_id = openAuditRepo.getReportIdFromTenantId(tenant_id);
		logger.info("UPDATING for report {}", report.getReport_id());
		if(report.getClass().equals(OpenReport.class)) {
			tenantRepo.updateLatestAuditByTenantId(report.getTenant_id(), report.getReport_id());
			String outstandingAudits = auditorRepo.getOutstandingAuditsFromAuditorID(report.getAuditor_id());
			String updatedJsonAuditor = appendToJson(outstandingAudits,
					ResourceString.AUDITOR_OUTSTANDING_AUDITS_JSON_KEY, String.valueOf(report.getReport_id()));
			if (updatedJsonAuditor==null) return false;
			auditorRepo.updateLatestOutstandingAuditsByAuditorId(report.getAuditor_id(), updatedJsonAuditor);
			logger.info("updating outstanding");
			return true;
		}else if(report.getClass().equals(ClosedReport.class)) {
			String TenantPastAudits = tenantRepo.getPastAuditsById(report.getTenant_id());
			String updatedJsonTenant = appendToJson(TenantPastAudits,
					ResourceString.TENANT_PAST_AUDITS_JSON_KEY, String.valueOf(report.getReport_id()));
			if(updatedJsonTenant==null) return false;
			tenantRepo.updatePastAuditsByTenantId(report.getTenant_id(),updatedJsonTenant);
			String completedAudits = auditorRepo.getCompletedAuditsFromAuditorID(report.getAuditor_id());
			String updatedJsonAuditor = appendToJson(completedAudits,
					ResourceString.AUDITOR_COMPLETED_AUDITS_JSON_KEY, String.valueOf(report.getReport_id()));
			if(updatedJsonAuditor==null) return false;
			auditorRepo.updateLatestCompletedAuditsByAuditorId(report.getAuditor_id(), updatedJsonAuditor);
			logger.info("updating closed");

			return true;
		} else{
			logger.warn("Report is of an invalid type. Unable to save.");
		}
		return false;
    }

    private String appendToJson(String jsonString, String key, String valueToAppend){
    	if(jsonString==null|| jsonString.equals("-1")){
    		ObjectNode root = objectmapper.createObjectNode();
    		ArrayNode arrayNode = objectmapper.createArrayNode();
    		arrayNode.add(valueToAppend);
    		root.set(key,arrayNode);
			try {
				return objectmapper.writeValueAsString(root);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ObjectNode root = (ObjectNode) objectmapper.readTree(jsonString);
				ArrayNode keyNode = (ArrayNode) root.get(key);
				keyNode.add(valueToAppend);
				return objectmapper.writeValueAsString(root);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
    
    /**
     * Saves a given report object into the database, regardless of its subclass.
     * This method abstracts away the need to convert the report into an AuditModel which is the
     * representation of the report in the database.
     * @param report object to be saved
     * @return True if the save was successfull, and false otherwise.
     */
    public boolean saveReport (Report report, TenantRepo tenantRepo, 
    		AuditorRepo auditorRepo, ManagerRepo managerRepo) {
    	boolean success = false;
    	if(report.getClass().equals(OpenReport.class)) {
    		success = saveOpenReport((OpenReport) report);
    	}else if(report.getClass().equals(ClosedReport.class)) {
    		success = saveClosedReport((ClosedReport) report);
    	}else {
    		logger.warn("Report is of an invalid type. Unable to save.");
    	}
    	return success;
    }

    //save if the report is never open
    public boolean saveImmediatelyCompletedReport(Report report, TenantRepo tenantRepo,
												  AuditorRepo auditorRepo, ManagerRepo managerRepo){
		boolean success = false;
		success = saveOpenReport((OpenReport) report);
		if(!success) return false;
		setOverall_statusAsClosed();
		setReport_id(report.getReport_id());
		report = build();
		success = saveClosedReport((ClosedReport) report);

		if(!success) return false;
		success = updateLatestReportIds(report, tenantRepo, auditorRepo, managerRepo);
		if(!success) return false;
		return deleteOpenReport(report.getReport_id());
	}

	public List<ReportEntry> getOverDueEntries(){
    	List<ReportEntry> overDueEntries = new ArrayList<>();
		List<Integer> checked_qns = new ArrayList<>();

		sortEntries();
		Calendar calendar = Calendar.getInstance();
		logger.info("calendar instance {}",calendar.getTime().toString());
		for(ReportEntry entry: entries) {
			if (!checked_qns.contains(entry.getQn_id())) {
				checked_qns.add(entry.getQn_id());
			} else {
				continue;
			}

			if(entry.getStatus() == Component_Status.FAIL){
				Date dueDate = entry.getDueDate();
				logger.info("Component status {}, due date {}, time {}",entry.getStatus(),dueDate,dueDate.toLocalDate());
				if(calendar.getTime().after(dueDate)){
					overDueEntries.add(entry);
				}
			}
		}
		return overDueEntries;
	}
    
   
    //getters and setters
	public int getReport_id() {
		return report_id;
	}

	public ReportBuilder setReport_id(int report_id) {
		this.report_id = report_id;
		return this;
	}

	public int getTenant_id() {
		return tenant_id;
	}

	public ReportBuilder setTenant_id(int tenant_id) {
		this.tenant_id = tenant_id;
		return this;
	}

	public int getAuditor_id() {
		return auditor_id;
	}

	public ReportBuilder setAuditor_id(int auditor_id) {
		this.auditor_id = auditor_id;
		return this;
	}

	public int getManager_id() {
		return manager_id;
	}

	public ReportBuilder setManager_id(int manager_id) {
		this.manager_id = manager_id;
		return this;
	}

	public Date getOpen_date() {
		return open_date;
	}

	public ReportBuilder setOpen_date(Date open_date) {
		this.open_date = open_date;
		return this;
	}

	public Date getLast_update_date() {
		return last_update_date;
	}

	public ReportBuilder setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
		return this;
	}

	public Date getClose_date() {
		return close_date;
	}

	public ReportBuilder setClose_date(Date close_date) {
		this.close_date = close_date;
		return this;
	}

	public int getOverall_score() {
		return overall_score;
	}

	public ReportBuilder setOverall_score(int overall_score) {
		this.overall_score = overall_score;
		return this;
	}

	public String getOverall_remarks() {
		return overall_remarks;
	}

	public ReportBuilder setOverall_remarks(String overall_remarks) {
		this.overall_remarks = overall_remarks;
		return this;
	}
	
	public ReportBuilder setReportType(String report_type) {
		if(report_type.matches(ResourceString.FB_KEY) || 
				report_type.matches(ResourceString.NFB_KEY) ||
				report_type.matches(ResourceString.SMA_KEY)) {
			this.report_type = report_type;
			return this;
		}else {
			logger.error("Invalid report type!");
			return null;
		}
	}
	
	public String getReportType() {
		return this.report_type;
	}

	public List<ReportEntry> getEntries() {
		return entries;
	}

	public ReportBuilder setEntries(List<ReportEntry> entries) {
		this.entries = entries;
        for(int i = 0; i < this.entries.size(); i++) {
        	this.entries.get(i).setEntry_id(i);
        }
		return this;
	}
	
	public ReportBuilder addEntry(ReportEntry entry) {
		this.entries.add(entry);
		entry.setEntry_id(this.entries.size()-1);
		return this;
	}

	public int getNeed_tenant() {
		return need_tenant;
	}

	public ReportBuilder setNeed_tenant(int need_tenant) {
		this.need_tenant = need_tenant;
		return this;
	}

	public int getNeed_auditor() {
		return need_auditor;
	}

	public ReportBuilder setNeed_auditor(int need_auditor) {
		this.need_auditor = need_auditor;
		return this;
	}

	public int getNeed_manager() {
		return need_manager;
	}

	public ReportBuilder setNeed_manager(int need_manager) {
		this.need_manager = need_manager;
		return this;
		
	}

	public int getOverall_status() {
		return overall_status;
	}
	
	public ReportBuilder setOverall_statusAsOpen() {
		this.overall_status = 0;
		return this;
	}

	public ReportBuilder setOverall_statusAsClosed() {
		this.overall_status = 1;
		return this;
	}
	
	public OpenAuditRepo getOpenAuditRepo() {
		return openAuditRepo;
	}

	public void setOpenAuditRepo(OpenAuditRepo openAuditRepo) {
		this.openAuditRepo = openAuditRepo;
	}

	public CompletedAuditRepo getCompletedAuditRepo() {
		return completedAuditRepo;
	}

	public void setCompletedAuditRepo(CompletedAuditRepo completedAuditRepo) {
		this.completedAuditRepo = completedAuditRepo;
	}
	
	//Condensed Builders Setters
	
	public ReportBuilder setUserIDs(int tenant_id, int auditor_id, int manager_id) {
		this.tenant_id = tenant_id;
		this.auditor_id = auditor_id;
		this.manager_id = manager_id;
		return this;
	}
	
	public ReportBuilder setNeed(int need_tenant, int need_auditor, int need_manager) {
		this.need_tenant = need_tenant;
		this.need_auditor = need_auditor;
		this.need_manager = need_manager;
		return this;
	}
	
	//New Marker that does not handle images
	public double markExternalReport(Report report, AuditCheckListFBRepo auditCheckListFBRepo,
									 AuditCheckListNFBRepo auditCheckListNFBRepo, AuditCheckListSMARepo auditCheckListSMARepo,
			String category) {
		return markEntries(report.getEntries(), auditCheckListFBRepo, auditCheckListNFBRepo, auditCheckListSMARepo, category);
	}
	
	public double markReport(AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
							 AuditCheckListSMARepo auditCheckListSMARepo) {
		return markEntries(this.getEntries(), auditCheckListFBRepo, auditCheckListNFBRepo, auditCheckListSMARepo, this.getReportType());
	}
	
	private double markEntries(List<ReportEntry> entries, AuditCheckListFBRepo auditCheckListFBRepo,
							   AuditCheckListNFBRepo auditCheckListNFBRepo, AuditCheckListSMARepo auditCheckListSMARepo,
							   String report_type){
		        HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap = new HashMap<>();
        AuditCheckListRepo repo = null;
        if(report_type.matches(ResourceString.FB_KEY)) {
        	repo = auditCheckListFBRepo;
        }else if (report_type.matches(ResourceString.NFB_KEY)) {
        	repo = auditCheckListNFBRepo;
        }else if(report_type.matches(ResourceString.SMA_KEY)) {
			repo = auditCheckListSMARepo;
        }
                if(repo == null) {
        	logger.error("Unknown report type!");
        	return -1;
        }
        List<Integer> checked_qns = new ArrayList<>();
		sortEntries();
        for(ReportEntry entry: entries){
        	if(!checked_qns.contains(entry.getQn_id())) {
        		checked_qns.add(entry.getQn_id());
        	}else {
        		continue;
        	}
            String qnCategory = repo.getCategoryByQnID(entry.getQn_id());
            double qnWeight = repo.getWeightByQnID(entry.getQn_id());
            
            if(entry.getStatus() == Component_Status.FAIL){               
                //Update Score
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertWrong();
                }else{
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,qnWeight,1,1);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            }else{
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertCorrect();
                }else{
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,qnWeight,1,0);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            }
        }
        String str_score = String.valueOf(calculateScore(checklistCategoryScoresHashMap));
        setOverall_score((int) (Double.parseDouble(str_score) *100.0));
        return Double.parseDouble(str_score) *100.0;
    }

	private void sortEntries(){
		Comparator<ReportEntry> compareByDateTime = new Comparator<ReportEntry>() {
			@Override
			public int compare(ReportEntry r1, ReportEntry r2) {
				int dateCompare = r1.getDate().compareTo(r2.getDate());
				if(dateCompare==0){
					return r1.getTime().compareTo(r2.getTime());
				}
				return dateCompare;
			}
		};
		entries.sort(compareByDateTime.reversed());
	}
	
	//Start of logic for processing Report entries
	/**
	 * 
	 * @param report
	 * @param auditCheckListFBRepo
	 * @param auditCheckListNFBRepo
	 * @param images
	 * @param category
	 * @return
	 */
	public double markReport_OLD(Report report, AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
			MultipartFile[] images, String category) {
		return markEntries_OLD(report.getEntries(), auditCheckListFBRepo, auditCheckListNFBRepo, images, category);
	}
	
	public double markEntries_OLD(AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
			MultipartFile[] images, String category) {
		return markEntries_OLD(this.getEntries(), auditCheckListFBRepo, auditCheckListNFBRepo, images, category);
	}
	
	public double markEntries_OLD(List<ReportEntry> entries, AuditCheckListFBRepo auditCheckListFBRepo, AuditCheckListNFBRepo auditCheckListNFBRepo,
			MultipartFile[] images, String category) {
        HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap = new HashMap<>();
        int imageCounter = 0;
        
        //TODO: figure out if this conversion is gonna screw things up
        for(ReportEntry entry: entries){
            String qnCategory = category.equals("fbchecklist") ?
                    auditCheckListFBRepo.getCategoryByQnID(entry.getQn_id())
                    : auditCheckListNFBRepo.getCategoryByQnID(entry.getQn_id());

            if(entry.getStatus() == Component_Status.FAIL){
                MultipartFile uploadedImage = images[imageCounter];
                //No img checking
                if(uploadedImage == null) {
                    logger.warn("UPLOADED IMAGE NUM {} NULL CHECKLIST POST",imageCounter);
                    return -1;
                }
                
                //Save img as Base64
                try {
                    logger.warn("UPLOADED IMAGE NAME {} FBCHECKLIST POST",uploadedImage.getOriginalFilename());
                    String base64img = Base64.getEncoder().encodeToString(uploadedImage.getBytes());
                    entry.addImage(base64img);
                    imageCounter++;
                } catch (IOException e) {
                    logger.warn("UPLOADED IMAGE NUM {} CANNOT OPEN FILE CHECKLIST POST",imageCounter);
                    return -1;
                }
                
                //Update Score
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertWrong();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepo.getWeightByQnID(entry.getQn_id())
                            : auditCheckListNFBRepo.getWeightByQnID(entry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,1);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
                
            } else{
                if(checklistCategoryScoresHashMap.containsKey(qnCategory)){
                    checklistCategoryScoresHashMap.get(qnCategory).insertCorrect();
                } else{
                    double weight = category.equals("fbchecklist") ?
                            auditCheckListFBRepo.getWeightByQnID(entry.getQn_id())
                            : auditCheckListNFBRepo.getWeightByQnID(entry.getQn_id());
                    ChecklistCategoryScores checklistCategoryScores = new ChecklistCategoryScores(qnCategory,weight,1,0);
                    checklistCategoryScoresHashMap.put(qnCategory,checklistCategoryScores);
                }
            }
        }
        String str_score = String.valueOf(calculateScore(checklistCategoryScoresHashMap));
        return Double.parseDouble(str_score) *100.0;
    }
	
    private double calculateScore(HashMap<String,ChecklistCategoryScores> checklistCategoryScoresHashMap){
        if(checklistCategoryScoresHashMap.isEmpty()) return 0;
        double totalScore = 0;
        for(ChecklistCategoryScores categoryScores: checklistCategoryScoresHashMap.values()){
            totalScore+= categoryScores.getScore();
        }
        return totalScore;
    }
}

class ChecklistCategoryScores{

    private final String categoryName;
    private final double weight;
    private double totalNumQn;
    private double numWrong;

    public ChecklistCategoryScores(String categoryName, double weight, int totalNumQn, int numWrong) {
        this.categoryName = categoryName;
        this.weight = weight;
        this.totalNumQn = totalNumQn;
        this.numWrong = numWrong;
    }

    public double getScore(){
        return ((totalNumQn-numWrong)/totalNumQn)*weight;
    }

    public void insertWrong(){
        numWrong++;
        totalNumQn++;
    }

    public void insertCorrect(){
        totalNumQn++;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
