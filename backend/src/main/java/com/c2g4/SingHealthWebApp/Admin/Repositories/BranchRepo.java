package com.c2g4.SingHealthWebApp.Admin.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.c2g4.SingHealthWebApp.Admin.Models.BranchModel;

/**
 * Repository of SQL queries to interact with the Branch Table
 * @author LunarFox
 *
 */
public interface BranchRepo extends CrudRepository<BranchModel, Integer> {
	
}
