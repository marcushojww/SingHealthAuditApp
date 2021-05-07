package com.c2g4.SingHealthWebApp.Admin.Repositories;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListSMAModel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository of SQL queries to interact with the AuditCheckListSMARepo
 */
@Repository
public interface AuditCheckListSMARepo extends CrudRepository<AuditCheckListSMAModel, Integer>, AuditCheckListRepo {
    @Query("SELECT * FROM SMACheckList")
    List<AuditCheckListSMAModel> getAllQuestions();

    @Query("SELECT * FROM SMACheckList WHERE sma_qn_id =:sma_qn_id")
    AuditCheckListSMAModel getQuestion(@Param("sma_qn_id") int qn_id);

    @Query("SELECT * from SMACheckList where category = :category")
    List<AuditCheckListSMAModel> getQuestionByCategory(@Param("category") String category);

    @Override
    @Query("SELECT category FROM SMACheckList WHERE sma_qn_id= :sma_qn_id")
    String getCategoryByQnID(@Param("sma_qn_id") int category);

    @Override
    @Query("SELECT weight FROM SMACheckList WHERE sma_qn_id= :sma_qn_id")
    double getWeightByQnID(@Param("sma_qn_id") int category);
}
