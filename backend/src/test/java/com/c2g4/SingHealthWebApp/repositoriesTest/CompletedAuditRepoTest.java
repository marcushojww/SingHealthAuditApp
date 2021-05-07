package com.c2g4.SingHealthWebApp.repositoriesTest;


import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class CompletedAuditRepoTest {
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

    private static final int tenant_id = 1004;

    private static final int COMPLETEDID = 166;

    @Test
    public void getCompletedAuditById() {
        CompletedAuditModel model  = completedAuditRepo.getCompletedAuditById(COMPLETEDID);
        assert(model.getReport_id()==COMPLETEDID);
    }

    @Test
    public void getCompletedAuditByIdNotFound() {
        completedAuditRepo.deleteAll();
        CompletedAuditModel model  = completedAuditRepo.getCompletedAuditById(COMPLETEDID);
        assert(model == null);
    }

    @Test
    public void deleteAuditById() {
        completedAuditRepo.deleteAuditById(COMPLETEDID);
        assert(!completedAuditRepo.existsById(COMPLETEDID));
    }

    @Test
    public void deleteAuditByIdNotFound() {
        completedAuditRepo.deleteAll();
        completedAuditRepo.deleteAuditById(COMPLETEDID);
    }

    @Test
    public void createNewEntryWithId() {
        completedAuditRepo.createNewEntryWithId(1000);
        assert(completedAuditRepo.existsById(1000));
    }
}
