package com.c2g4.SingHealthWebApp.Admin.Repositories;

/**
 * Parent interface of the AuditCheckListRepos for generalisation
 * @author LunarFox
 *
 */
public interface AuditCheckListRepo{
	
    String getCategoryByQnID(int fb_qn_id);
    
    double getWeightByQnID(int category);

}
