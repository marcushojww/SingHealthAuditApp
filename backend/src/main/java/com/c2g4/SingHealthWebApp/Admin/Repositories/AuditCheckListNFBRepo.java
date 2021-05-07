package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListNFBModel;


/**
 * Repository of SQL queries to interact with the AuditCheckListNFBRepo
 * @author LunarFox
 *
 */
@Repository
public interface AuditCheckListNFBRepo extends CrudRepository<AuditCheckListNFBModel, Integer>, AuditCheckListRepo {
    @Query("SELECT * FROM NFBCheckList")
    List<AuditCheckListNFBModel> getAllQuestions();
    
    @Query("SELECT * FROM NFBCheckList WHERE nfb_qn_id =:nfb_qn_id")
    AuditCheckListNFBModel getQuestion(@Param("nfb_qn_id") int nfb_qn_id);
    
    @Query("SELECT * from NFBCheckList where category = :category")
    List<AuditCheckListNFBModel> getQuestionByCategory(@Param("category") String category);

    //Consider making a question class s.t. we get all the details of a qn with one query
    //then we obtain info from the qn object
    //Might be better for the DB
    @Override
	@Query("SELECT category FROM NFBCheckList WHERE nfb_qn_id= :nfb_qn_id")
    String getCategoryByQnID(@Param("nfb_qn_id") int category);

    @Override
    @Query("SELECT weight FROM NFBCheckList WHERE nfb_qn_id= :nfb_qn_id")
    double getWeightByQnID(@Param("nfb_qn_id") int category);

}
