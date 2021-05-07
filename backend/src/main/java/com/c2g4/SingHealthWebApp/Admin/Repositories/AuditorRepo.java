package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;

/**
 * Repository of SQL queries to interact with the Auditors Table
 * @author LunarFox
 *
 */
@Repository
public interface AuditorRepo extends CrudRepository<AuditorModel, Integer>{
	@Query("SELECT * FROM Auditors")
	List<AuditorModel> getAllAuditors();

	@Query("SELECT * FROM Auditors WHERE acc_id = :acc_id LIMIT 1")
	AuditorModel getAuditorById(@Param("acc_id") int acc_id);
	
	@Query("SELECT branch_id FROM Auditors WHERE acc_id= :acc_id")
	String getBranchIDfromAccountID(@Param("acc_id") int auditor_acc_id);
	
	@Query("SELECT mgr_id FROM Auditors WHERE acc_id= :acc_id")
	int getManagerIDfromAuditorID(@Param("acc_id") int auditor_acc_id);

	@Modifying
	@Query("UPDATE Auditors a SET a.outstanding_audits = :outstanding_audits WHERE a.acc_id = :acc_id")
	void updateLatestOutstandingAuditsByAuditorId(@Param("acc_id") int acc_id, @Param("outstanding_audits") String outstanding_audits);

	@Query("SELECT outstanding_audits FROM Auditors WHERE acc_id= :acc_id")
	String getOutstandingAuditsFromAuditorID(@Param("acc_id") int auditor_acc_id);

	@Modifying
	@Query("UPDATE Auditors a SET a.completed_audits = :completed_audits WHERE a.acc_id = :acc_id")
	void updateLatestCompletedAuditsByAuditorId(@Param("acc_id") int acc_id, @Param("completed_audits") String completed_audits);

	@Query("SELECT completed_audits FROM Auditors WHERE acc_id= :acc_id")
	String getCompletedAuditsFromAuditorID(@Param("acc_id") int auditor_acc_id);

}
