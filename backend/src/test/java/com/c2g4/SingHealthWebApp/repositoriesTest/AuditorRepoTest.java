package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.ManagerModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class AuditorRepoTest {
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

    private static final int account_id = 1003;
    private static final int manager_id = 1001;

    @Test
    public void getAllAuditors() {
        List<AuditorModel> actualModels  = auditorRepo.getAllAuditors();
        assert(actualModels.size() >0);
    }

    @Test
    public void getAllAuditorsNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        List<AuditorModel> actualModels  = auditorRepo.getAllAuditors();
        assert(actualModels.size()==0);
    }

    @Test
    public void getAuditorById() {
        AuditorModel actualModels  = auditorRepo.getAuditorById(account_id);
        assert(actualModels.getAcc_id() == account_id);
    }

    @Test
    public void getAuditorByIdNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        AuditorModel actualModels  = auditorRepo.getAuditorById(account_id);
        assert(actualModels==null);
    }

    @Test
    public void getBranchIDfromAccountID() {
        String actual  = auditorRepo.getBranchIDfromAccountID(account_id);
        assert(actual.equals("*"));
    }

    @Test
    public void getBranchIDfromAccountIDNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        String actual  = auditorRepo.getBranchIDfromAccountID(account_id);
        assert(actual == null);
    }

    @Test
    public void getManagerIDfromAuditorID() {
        int actual  = auditorRepo.getManagerIDfromAuditorID(account_id);
        assert(actual == manager_id);
    }

    @Test
    public void getManagerIDfromAuditorIDNotFound() {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        try {
            int actual  = auditorRepo.getManagerIDfromAuditorID(account_id);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    @Test
    public void updateLatestOutstandingAuditsByAuditorId() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode outstandingAdudits = objectMapper.createObjectNode();
        outstandingAdudits.put("testString",123);
        auditorRepo.updateLatestOutstandingAuditsByAuditorId(account_id,objectMapper.writeValueAsString(outstandingAdudits));
        AuditorModel auditorModel = auditorRepo.getAuditorById(account_id);
        assert(auditorModel.getOutstanding_audit_ids().equals((JsonNode)outstandingAdudits));
    }

    @Test
    public void updateLatestOutstandingAuditsByAuditorIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode outstandingAdudits = objectMapper.createObjectNode();
        outstandingAdudits.put("testString",123);
        auditorRepo.updateLatestOutstandingAuditsByAuditorId(account_id,objectMapper.writeValueAsString(outstandingAdudits));
    }

    @Test
    public void getOutstandingAuditsFromAuditorID() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode outstandingAdudits = objectMapper.createObjectNode();
        outstandingAdudits.put("testString",123);
        auditorRepo.updateLatestOutstandingAuditsByAuditorId(account_id,objectMapper.writeValueAsString(outstandingAdudits));
        String oustanding = auditorRepo.getOutstandingAuditsFromAuditorID(account_id);
        assert(objectMapper.readTree(oustanding).get("testString").asInt() == outstandingAdudits.get("testString").asInt());
    }

    @Test
    public void getOutstandingAuditsFromAuditorIDNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode outstandingAdudits = objectMapper.createObjectNode();
        outstandingAdudits.put("testString",123);
        auditorRepo.updateLatestOutstandingAuditsByAuditorId(account_id,objectMapper.writeValueAsString(outstandingAdudits));
        String oustanding = auditorRepo.getOutstandingAuditsFromAuditorID(account_id);
        assert(oustanding==null);
    }

    @Test
    public void updateLatestCompletedAuditsByAuditorId() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode complete = objectMapper.createObjectNode();
        complete.put("testString",123);
        auditorRepo.updateLatestCompletedAuditsByAuditorId(account_id,objectMapper.writeValueAsString(complete));
        AuditorModel auditorModel = auditorRepo.getAuditorById(account_id);
        assert(auditorModel.getCompleted_audits().get("testString").asInt() == complete.get("testString").asInt());
    }

    @Test
    public void updateLatestCompletedAuditsByAuditorIdNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode complete = objectMapper.createObjectNode();
        complete.put("testString",123);
        auditorRepo.updateLatestCompletedAuditsByAuditorId(account_id,objectMapper.writeValueAsString(complete));
    }

    @Test
    public void getCompletedAuditsFromAuditorID() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode completed = objectMapper.createObjectNode();
        completed.put("testString",123);
        auditorRepo.updateLatestCompletedAuditsByAuditorId(account_id,objectMapper.writeValueAsString(completed));
        String actual = auditorRepo.getCompletedAuditsFromAuditorID(account_id);
        assert(objectMapper.readTree(actual).get("testString").asInt() == completed.get("testString").asInt());
    }

    @Test
    public void getCompletedAuditsFromAuditorIDNotFound() throws JsonProcessingException {
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode completed = objectMapper.createObjectNode();
        completed.put("testString",123);
        auditorRepo.updateLatestCompletedAuditsByAuditorId(account_id,objectMapper.writeValueAsString(completed));
        String actual = auditorRepo.getCompletedAuditsFromAuditorID(account_id);
        assert(actual==null);
    }

    private static boolean auditorIdentical(AuditorModel expectedModel, AuditorModel actualModel){
        boolean id = expectedModel.getAcc_id() == actualModel.getAcc_id();
        boolean mgr_id = expectedModel.getMgr_id() == actualModel.getMgr_id();
        boolean branch = expectedModel.getBranch_id().equals(actualModel.getBranch_id());
        boolean appealed = expectedModel.getAppealed_audits().equals(actualModel.getAppealed_audits());
        boolean completed = expectedModel.getCompleted_audits().equals(actualModel.getCompleted_audits());
        boolean outstanding = expectedModel.getOutstanding_audit_ids().equals(actualModel.getOutstanding_audit_ids());
        return id && mgr_id && branch && appealed &&completed && outstanding;
    }


}
