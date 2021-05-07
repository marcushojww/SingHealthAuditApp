package com.c2g4.SingHealthWebApp.repositoriesTest;

import java.util.ArrayList;
import java.util.List;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonRepoTestFunctions {

    private static final int ACCOUNT_ID = 9000;
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String BRANCHID = "CGH";

    public static void clearAllTables(AccountRepo accountRepo, ManagerRepo managerRepo, AuditorRepo auditorRepo,
                                      TenantRepo tenantRepo, CompletedAuditRepo completedAuditRepo, OpenAuditRepo openAuditRepo){
        completedAuditRepo.deleteAll();
        openAuditRepo.deleteAll();
        managerRepo.deleteAll();
        auditorRepo.deleteAll();
        tenantRepo.deleteAll();
        accountRepo.deleteAll();
    }
    public static void createUsersmanager(AccountRepo accountRepo, ManagerRepo managerRepo, AuditorRepo auditorRepo,
                                   TenantRepo tenantRepo, List<AccountModel> accountModels,
                                   List<ManagerModel> managerModels){
        createUsers(accountRepo, managerRepo, auditorRepo,
                tenantRepo, new ArrayList<AccountModel>(), managerModels, new ArrayList<AuditorModel>(),
                new ArrayList<TenantModel>());
    }

    public static void createUsersAuditor(AccountRepo accountRepo, ManagerRepo managerRepo, AuditorRepo auditorRepo,
                                   TenantRepo tenantRepo, List<AccountModel> accountModels,
                                   List<AuditorModel> auditorModels){
        createUsers(accountRepo, managerRepo, auditorRepo,
                tenantRepo, new ArrayList<AccountModel>(),  new ArrayList<ManagerModel>(), auditorModels,
                new ArrayList<TenantModel>());

    }

    public static void createUsersTenant(AccountRepo accountRepo, ManagerRepo managerRepo, AuditorRepo auditorRepo,
                                   TenantRepo tenantRepo, List<AccountModel> accountModels,
                                   List<TenantModel> tenantModels){
        createUsers(accountRepo, managerRepo, auditorRepo,
                tenantRepo, new ArrayList<AccountModel>(),  new ArrayList<ManagerModel>(), new ArrayList<AuditorModel>(),
               tenantModels);
    }


    public static void createUsers(AccountRepo accountRepo, ManagerRepo managerRepo, AuditorRepo auditorRepo,
                                   TenantRepo tenantRepo, List<AccountModel> accountModels,
                                   List<ManagerModel> managerModels, List<AuditorModel> auditorModels,
                                   List<TenantModel> tenantModels){
        //manager
        for(int i=0;i<3;i++){
            AccountModel accountModel = accountRepo.save(createAccount(i, USERNAME+i, "email" +i,  "hp"+i, ResourceString.MANAGER_ROLE_KEY));
            accountModels.add(accountModel);
            ManagerModel managerModel = managerRepo.save(createManager(accountModel.getAccount_id()));
            managerModels.add(managerModel);
        }
        //auditor
        for(int i=3;i<6;i++){
            AccountModel accountModel = accountRepo.save(createAccount(i, USERNAME+i, "email" +i,  "hp"+i, ResourceString.AUDITOR_ROLE_KEY));
            accountModels.add(accountModel);
            AuditorModel auditorModel = auditorRepo.save(createAuditor(accountModel.getAccount_id()));
            auditorModels.add(auditorModel);
        }
        //tenant
        for(int i=6;i<9;i++){
            AccountModel accountModel = accountRepo.save(createAccount(i, USERNAME+i, "email" +i,  "hp"+i, ResourceString.TENANT_ROLE_KEY));
            accountModels.add(accountModel);
            TenantModel tenantModel = tenantRepo.save(createTenant(accountModel.getAccount_id()));
            tenantModels.add(tenantModel);
        }
    }

    public static AuditorModel createAuditor(){
        return createAuditor(ACCOUNT_ID);
    }

    public static AuditorModel createAuditor(int acc_id){
        ObjectMapper objectMapper = new ObjectMapper();
        AuditorModel auditorModel = new AuditorModel();
        auditorModel.setAcc_id(acc_id);
        auditorModel.setBranch_id("CGH");
        auditorModel.setAppealed_audits(objectMapper.createObjectNode());
        auditorModel.setCompleted_audits(objectMapper.createObjectNode());
        auditorModel.setOutstanding_audit_ids(objectMapper.createObjectNode());
        auditorModel.setMgr_id(0);
        return auditorModel;
    }

    public static ManagerModel createManager(){
        return createManager(ACCOUNT_ID);
    }

    public static ManagerModel createManager(int acc_id){
        return createManager(acc_id,BRANCHID);
    }

    public static ManagerModel createManager(int acc_id, String branchID){
        ManagerModel managerModel = new ManagerModel();
        managerModel.setBranch_id(branchID);
        managerModel.setAcc_id(acc_id);
        return managerModel;
    }

    public static TenantModel createTenant(){
        return createTenant(ACCOUNT_ID);
    }

    public static TenantModel createTenant(int acc_id){
        ObjectMapper objectMapper = new ObjectMapper();
        TenantModel tenantModel = new TenantModel();
        tenantModel.setAcc_id(acc_id);
        tenantModel.setLatest_audit(-1);
        tenantModel.setStore_addr("store_addr");
        tenantModel.setType_id(ResourceString.FB_KEY);
        tenantModel.setAudit_score(90);
        tenantModel.setBranch_id("CGH");
        tenantModel.setStore_name("storeName");
        tenantModel.setPast_audits(objectMapper.createObjectNode());
        return tenantModel;
    }

    public static AccountModel createAccount(){
        return createAccount(USERNAME);
    }

    public static AccountModel createAccount(String username){
        return createAccount(0,username,"email","90");
    }

    public static AccountModel createAccount(int employeeId, String username, String email, String hp){
        return createAccount(employeeId, username, email, hp, ResourceString.AUDITOR_ROLE_KEY);
    }

    public static AccountModel createAccount(int employeeId, String username, String email, String hp,String rold_id){
        AccountModel accountModel = new AccountModel();
        accountModel.setAccount_id(0);
        accountModel.setEmployee_id(employeeId);
        accountModel.setUsername(username);
        accountModel.setPassword(PASSWORD);
        accountModel.setFirst_name(FIRSTNAME);
        accountModel.setLast_name(LASTNAME);
        accountModel.setEmail(email);
        accountModel.setHp(hp);
        accountModel.setRole_id(rold_id);
        accountModel.setBranch_id(BRANCHID);
        return accountModel;
    }

}
