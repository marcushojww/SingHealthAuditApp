package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;

/**
 * Repository of SQL queries to interact with the Manager Table
 * @author LunarFox
 *
 */
@Repository
public interface ManagerRepo extends CrudRepository<ManagerModel, Integer> {


    @Query("SELECT * FROM Managers WHERE acc_id = :acc_id LIMIT 1")
    ManagerModel getManagerById(@Param("acc_id") int acc_id);

    @Query("SELECT acc_id FROM Managers WHERE branch_id= :branch_id")
    int getManagerIdFromBranchId(@Param("branch_id") String branch_id);

    @Query("SELECT * FROM Managers")
    List<ManagerModel> getAllManagers();

}
