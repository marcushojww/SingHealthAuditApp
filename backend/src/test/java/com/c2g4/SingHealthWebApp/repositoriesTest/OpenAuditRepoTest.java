package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class OpenAuditRepoTest {
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

    private static final int OPENAUDITID = 165;

    @Test
    public void getOpenAuditById() {
        OpenAuditModel openAuditModel  = openAuditRepo.getOpenAuditById(OPENAUDITID);
        assert(openAuditModel.getReport_id()==OPENAUDITID);
    }

    @Test
    public void getOpenAuditByIdNotFound() {
        openAuditRepo.deleteAll();
        OpenAuditModel openAuditModel  = openAuditRepo.getOpenAuditById(OPENAUDITID);
        assert(openAuditModel == null);
    }

    @Test
    public void getAllOpenAuditsIds() {
        List<Integer> ids  = openAuditRepo.getAllOpenAuditsIds();
        assert(ids.size()>0);
    }

    @Test
    public void getAllOpenAuditsIdsNotFound() {
        openAuditRepo.deleteAll();
        List<Integer> ids  = openAuditRepo.getAllOpenAuditsIds();
        assert(ids.size()==0);
    }

    @Test
    public void getReportIdFromTenantId() {
        int id  = openAuditRepo.getReportIdFromTenantId(tenant_id);
        assert(id>0);
    }

    @Test
    public void getReportIdFromTenantIdNotFound() {
        openAuditRepo.deleteAll();
        try {
            int id  = openAuditRepo.getReportIdFromTenantId(tenant_id);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    @Test
    public void deleteAuditById() {
        openAuditRepo.deleteAuditById(OPENAUDITID);
        assert(!openAuditRepo.existsById(OPENAUDITID));
    }

    @Test
    public void deleteAuditByIdNotFound() {
        openAuditRepo.deleteAll();
        openAuditRepo.deleteAuditById(OPENAUDITID);
    }

}
