package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class TenantRepoTest {

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
    public void getAllTenants() {
        List<TenantModel> actualModels  = tenantRepo.getAllTenants();
        assert(actualModels.size() >0);
    }

    @Test
    public void getAllTenantsNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        List<TenantModel> actualModels  = tenantRepo.getAllTenants();
        assert(actualModels.size()==0);
    }

    @Test
    public void getAllTenantsByBranchId() {
        List<TenantModel> actualModels  = tenantRepo.getAllTenantsByBranchId(branch_id);
        assert(actualModels.size() >0);
        assert(actualModels.get(0).getBranch_id().equals(branch_id));
    }

    @Test
    public void getAllTenantsByBranchIdNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        List<TenantModel> actualModels  = tenantRepo.getAllTenantsByBranchId(branch_id);
        assert(actualModels.size()==0);
    }

    @Test
    public void getTenantById() {
        TenantModel actualModel  = tenantRepo.getTenantById(tenant_id);
        assert(actualModel.getAcc_id() ==tenant_id);
        assert(actualModel.getStore_name().equals("Kopitiam"));
    }

    @Test
    public void getTenantByIdNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        TenantModel actualModel  = tenantRepo.getTenantById(tenant_id);
        assert(actualModel==null);
    }


    @Test
    public void updatePastAuditsByTenantId() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode pastAudits = objectMapper.createObjectNode();
        pastAudits.put("testString",123);
        tenantRepo.updatePastAuditsByTenantId(tenant_id,objectMapper.writeValueAsString(pastAudits));
        TenantModel tenantModel = tenantRepo.getTenantById(tenant_id);
        assert(tenantModel.getPast_audits().get("testString").asInt() == pastAudits.get("testString").asInt());
    }

    @Test
    public void updatePastAuditsByTenantIdIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode pastAudits = objectMapper.createObjectNode();
        pastAudits.put("testString",123);
        tenantRepo.updatePastAuditsByTenantId(tenant_id,objectMapper.writeValueAsString(pastAudits));
    }

    @Test
    public void getPastAuditsById() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode pastAudits = objectMapper.createObjectNode();
        pastAudits.put("testString",123);
        tenantRepo.updatePastAuditsByTenantId(tenant_id,objectMapper.writeValueAsString(pastAudits));
        String past = tenantRepo.getPastAuditsById(tenant_id);
        assert(objectMapper.readTree(past).get("testString").asInt() == pastAudits.get("testString").asInt());
    }

    @Test
    public void getPastAuditsByIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode pastAudits = objectMapper.createObjectNode();
        pastAudits.put("testString",123);
        tenantRepo.updatePastAuditsByTenantId(tenant_id,objectMapper.writeValueAsString(pastAudits));
        String past = tenantRepo.getPastAuditsById(tenant_id);
        assert(past==null);
    }

    @Test
    public void updateLatestAuditByTenantId() throws JsonProcessingException {
        tenantRepo.updateLatestAuditByTenantId(tenant_id,123);
        TenantModel tenantModel = tenantRepo.getTenantById(tenant_id);
        assert(tenantModel.getLatest_audit()== 123);
    }

    @Test
    public void updateLatestAuditByTenantIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        tenantRepo.updateLatestAuditByTenantId(tenant_id,123);
    }

    @Test
    public void getOpenAuditById() throws JsonProcessingException {
        tenantRepo.updateLatestAuditByTenantId(tenant_id,123);
        int past = tenantRepo.getOpenAuditById(tenant_id);
        assert(past == 123);
    }

    @Test
    public void getOpenAuditByIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        try {
            int past = tenantRepo.getOpenAuditById(tenant_id);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    @Test
    public void getStoreNameById() throws JsonProcessingException {
        String store = tenantRepo.getStoreNameById(tenant_id);
        assert(store.equals("Kopitiam"));
    }

    @Test
    public void getStoreNameByIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        String store = tenantRepo.getStoreNameById(tenant_id);
        assert(store == null);
    }

    @Test
    public void updateAuditScoreByTenantId() throws JsonProcessingException {
        tenantRepo.updateAuditScoreByTenantId(tenant_id,12);
        TenantModel tenantModel = tenantRepo.getTenantById(tenant_id);
        assert(tenantModel.getAudit_score() == 12);
    }

    @Test
    public void updateAuditScoreByTenantIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        tenantRepo.updateAuditScoreByTenantId(tenant_id,0);
    }
}
