package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class ManagerRepoTest {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private CompletedAuditRepo completedAuditRepo;
    @Autowired
    private OpenAuditRepo openAuditRepo;

    private static final int auditor_id = 1003;
    private static final int tenant_id = 1004;
    private static final int manager_id = 1001;
    private static final String branch_id = "CGH";

    @Test
    public void getAllManagers() {
        List<ManagerModel> managerModels  = managerRepo.getAllManagers();
        assert(managerModels.size() >0);
    }

    @Test
    public void getAllManagersNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        List<ManagerModel> managerModels  = managerRepo.getAllManagers();
        assert(managerModels.size()==0);
    }

    @Test
    public void getManagerById() {
        ManagerModel managerModel  = managerRepo.getManagerById(manager_id);
        assert(managerModel!=null);
        assert(managerModel.getAcc_id()==manager_id);
    }

    @Test
    public void getManagerByIdNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ManagerModel managerModels  = managerRepo.getManagerById(manager_id);
        assert(managerModels==null);
    }

    @Test
    public void getManagerIdFromBranchId() {
        int id  = managerRepo.getManagerIdFromBranchId("HQ");
        assert(id ==manager_id);
    }

    @Test
    public void getManagerIdFromBranchIdNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        try {
            int id  = managerRepo.getManagerIdFromBranchId("HQ");
        } catch (AopInvocationException e){
            return;
        }
    }

}
