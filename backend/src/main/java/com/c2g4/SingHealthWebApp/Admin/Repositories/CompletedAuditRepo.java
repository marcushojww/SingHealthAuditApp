package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;

/**
 * Repository of SQL queries to interact with the CompletedAudit Table
 * @author LunarFox
 *
 */
@Repository
public interface CompletedAuditRepo extends CrudRepository<CompletedAuditModel, Integer> {
    @Query("SELECT * FROM Completed_Audits WHERE report_id = :report_id LIMIT 1")
    CompletedAuditModel getCompletedAuditById(@Param("report_id") int report_id);
    
    @Modifying
    @Query("DELETE FROM Completed_Audits WHERE report_id = :report_id ")
    void deleteAuditById(@Param("report_id") int report_id);
    
    @Modifying
    @Query("INSERT INTO Completed_Audits (report_id) VALUES (:report_id)")
    void createNewEntryWithId(@Param("report_id") int report_id);
    
    @Query("SELECT tenant_id FROM Completed_Audits WHERE start_date BETWEEN CURDATE() - INTERVAL :days DAY AND CURDATE();")
    List<Integer> getTenantIds(@Param("days") int days);
}
