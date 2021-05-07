package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;

/**
 * Repository of SQL queries to interact with the OpenAudits Table
 * @author LunarFox
 *
 */
@Repository
public interface OpenAuditRepo extends CrudRepository<OpenAuditModel, Integer> {
    @Query("SELECT * FROM Open_Audits WHERE report_id = :report_id LIMIT 1")
    OpenAuditModel getOpenAuditById(@Param("report_id") int report_id);

    @Query("SELECT report_id FROM Open_Audits")
    List<Integer> getAllOpenAuditsIds();

    //by right there should only be one open report per tenant
    @Query("SELECT report_id FROM Open_Audits WHERE tenant_id= :tenant_id LIMIT 1")
    int getReportIdFromTenantId(@Param("tenant_id") int tenant_id);
    
    @Modifying
    @Query("DELETE FROM Open_Audits WHERE report_id = :report_id ")
    void deleteAuditById(@Param("report_id") int report_id);
    
    @Query("SELECT tenant_id FROM Completed_Audits WHERE start_date BETWEEN CURDATE() - INTERVAL :days DAY AND CURDATE();")
    List<Integer> getTenantIds(@Param("days") int days);
}
