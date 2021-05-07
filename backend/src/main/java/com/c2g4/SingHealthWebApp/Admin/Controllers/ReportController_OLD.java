package com.c2g4.SingHealthWebApp.Admin.Controllers;

/*

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListFBModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListNFBModel;
import com.c2g4.SingHealthWebApp.Admin.Report.AuditorReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Report.ClosedReport;
import com.c2g4.SingHealthWebApp.Admin.Report.CustomAuditorEntryDeserializer;
import com.c2g4.SingHealthWebApp.Admin.Report.OpenReport;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.ManagerRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class ReportController_OLD {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditCheckListFBRepo auditCheckListFBRepo;
    @Autowired
    private AuditCheckListNFBRepo auditCheckListNFBRepo;
    @Autowired
    private CompletedAuditRepo completedAuditRepo;
    @Autowired
    private OpenAuditRepo openAuditRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private AccountRepo accountRepo;

    //Get questions
    @GetMapping("/a/auditchecklist/{tenantId}/fbchecklist/{category}")
    public ResponseEntity<?> getFBCheckListCategoryQuestions(@PathVariable("category") String category) {
        List<AuditCheckListFBModel> questions = auditCheckListFBRepo.getQuestionByCategory(category);
        if (questions == null || questions.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(questions);
    }

    //Get questions
    @GetMapping("/a/auditchecklist/{tenantId}/nfbchecklist/{category}")
    public ResponseEntity<?> getNFBCheckListCategoryQuestions(@PathVariable("category") String category) {
        List<AuditCheckListNFBModel> questions = auditCheckListNFBRepo.getQuestionByCategory(category);
        if (questions == null || questions.size() == 0){
            logger.warn("no such category {}",category);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        logger.info(questions.get(0).getRequirement());
        return ResponseEntity.ok(questions);
    }


    //expected for filledCheckList
    // "key:[{ \"qn_id\" : \"12321\", \"passFail\" : true , \"remarks\" : null, \"severity\": 0123},
    //      { \"qn_id\" : \"12322\", \"passFail\" : true , \"remarks\" : null,\"severity\": 0123}]";
    @PostMapping(value = "/a/{tenantId}/{checklistType}/submission", consumes = {"multipart/form-data"})
    public ResponseEntity<?> submitFBChecklist(
            @RequestParam(value = "files", required = true) MultipartFile[] images,
            @RequestPart(value = "filledChecklist", required = true) String filledChecklist,
            @AuthenticationPrincipal UserDetails auditorUser,
            @PathVariable("tenantId") int tenant_id,
            @PathVariable("checklistType") String checklistType)  {
        AccountModel auditorAccount = accountRepo.findByUsername(auditorUser.getUsername());
        int auditor_id = auditorAccount.getAccount_id();

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AuditorReportEntry.class, new CustomAuditorEntryDeserializer());
        objectMapper.registerModule(module);

        JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class, AuditorReportEntry.class);
        List<ReportEntry> auditorEntryList;
        try {
            auditorEntryList = objectMapper.readValue(filledChecklist, customClassCollection);
        } catch (JsonProcessingException e) {
            logger.warn("JSON PROCESSING EXCEPTION {} POST",checklistType);
            return ResponseEntity.badRequest().body(null);
        }

        int manager_id = auditorRepo.getManagerIDfromAuditorID(auditor_id);
        ReportBuilder builder = ReportBuilder.getNewReportBuilder(openAuditRepo, completedAuditRepo);
        builder.setUserIDs(tenant_id, auditor_id, manager_id)
                .setEntries(auditorEntryList);

        int auditScore = (int) builder.markEntries_OLD(auditCheckListFBRepo, auditCheckListNFBRepo, images, checklistType);
        if(auditScore == -1) {
            return ResponseEntity.badRequest().body("UPLOADED IMAGE CANNOT OPEN FILE CHECKLIST POST");
        }

        if(auditScore<100){
            builder.setOverall_remarks("Idk Open?").setOverall_score(auditScore).setNeed(1, 1, 0);
            OpenReport report = (OpenReport) builder.build();
            if(!builder.saveReport(report)) {
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            builder.setOverall_remarks("Idk Closed?").setOverall_score(auditScore).setOverall_statusAsClosed();
            ClosedReport report = (ClosedReport) builder.build();
            if(!builder.saveReport(report)) {
                return ResponseEntity.badRequest().body(null);
            }

            //TODO: add completed audit to tenant
        }
        return ResponseEntity.ok(auditScore);
    }

    //TODO: implement
    @GetMapping("/a/tenantid/score")
    public ResponseEntity<?> getLastAuditScore(@PathVariable("tenantId") int tenantId){
        return ResponseEntity.ok(0);
    }

}

 */