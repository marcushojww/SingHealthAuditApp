package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListFBModel;

/**
 * Repository of SQL queries to interact with the AuditCheckListFB Table
 * @author LunarFox
 *
 */
@Repository
public interface AuditCheckListFBRepo extends CrudRepository<AuditCheckListFBModel, Integer>, AuditCheckListRepo{
    @Query("SELECT * FROM FBCheckList")
    List<AuditCheckListFBModel> getAllQuestions();
    

    @Query("SELECT * FROM FBCheckList WHERE fb_qn_id =:fb_qn_id")
    AuditCheckListFBModel getQuestion(@Param("fb_qn_id") int fb_qn_id);

	
    @Query("SELECT * FROM FBCheckList WHERE category = :category")
    List<AuditCheckListFBModel> getQuestionByCategory(@Param("category") String category);
    
    //Consider create a question class that stores all this info
    //Might be more efficient for the DB by avoiding excessive queries
    @Override
	@Query("SELECT category FROM FBCheckList WHERE fb_qn_id= :fb_qn_id")
    String getCategoryByQnID(@Param("fb_qn_id") int fb_qn_id);

    @Override
    @Query("SELECT weight FROM FBCheckList WHERE fb_qn_id= :fb_qn_id")
    double getWeightByQnID(@Param("fb_qn_id") int fb_qn_id);

}
