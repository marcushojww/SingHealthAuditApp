package com.c2g4.SingHealthWebApp.ControllersTest;

import com.c2g4.SingHealthWebApp.Admin.Models.*;
import com.c2g4.SingHealthWebApp.Admin.Report.CustomReportEntryDeserializer;
import com.c2g4.SingHealthWebApp.Admin.Report.OpenReport;
import com.c2g4.SingHealthWebApp.Admin.Report.Report;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.util.NestedServletException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ReportControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuditCheckListFBRepo auditCheckListFBRepo;
    @MockBean
    private AuditCheckListNFBRepo auditCheckListNFBRepo;
    @MockBean
    private AuditCheckListSMARepo auditCheckListSMARepo;
    @MockBean
    private OpenAuditRepo openAuditRepo;
    @MockBean
    private CompletedAuditRepo completedAuditRepo;
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private AuditorRepo auditorRepo;
    @MockBean
    private TenantRepo tenantRepo;
    @MockBean
    private ManagerRepo managerRepo;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String statusOK = "ok";
    private static final String statusBad = "bad";
    private static final String statusNotFound = "notFound";
    private static final String statusUnauthorized = "unauthorized";

    private static final String MANAGERUSENAME = "managerUsername";
    private static final String KNOWN_USER_PASSWORD = "test123";
    private static final String AUDITORUSENAME = "auditorUsername";
    private static final String TENANTUSENAME = "tenantUsername";

    private static final int MANAGERID = 100;
    private static final int AUDITORID = 100;
    private static final int TENANTID = 100;

    private static final int SAVEDREPORTID = 110;

    @BeforeEach
    public void before() {
        AccountModel managerAccount = createAccount(ResourceString.MANAGER_ROLE_KEY,MANAGERID,"Marcus","Ho","HQ");
        AccountModel auditorAccount = createAccount(ResourceString.AUDITOR_ROLE_KEY,AUDITORID,"Hannah","Mah","Branch_A");
        AccountModel tenantAccount = createAccount(ResourceString.TENANT_ROLE_KEY,TENANTID,"Gregory","Mah","Branch_A");
        given(accountRepo.findByUsername(MANAGERUSENAME)).willReturn(managerAccount);
        given(accountRepo.findByUsername(AUDITORUSENAME)).willReturn(auditorAccount);
        given(accountRepo.findByUsername(TENANTUSENAME)).willReturn(tenantAccount);
        when(auditCheckListFBRepo.getWeightByQnID(Mockito.any(Integer.class))).thenReturn(0.2);
        when(completedAuditRepo.save(Mockito.any(CompletedAuditModel.class))).thenAnswer(i -> {
            Object[] args = i.getArguments();
            return args[0];
        });
        when(openAuditRepo.save(Mockito.any(OpenAuditModel.class))).thenAnswer(i -> {
            Object[] args = i.getArguments();
            OpenAuditModel openAuditModel = (OpenAuditModel) args[0];
            openAuditModel.setReport_id(SAVEDREPORTID);
            return openAuditModel;
        });

    }

    @Test
    public void getAllQuestionsFB()
            throws Exception {
        List<AuditCheckListFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createFBChecklist(auditCheckListModels.size()));
        given(auditCheckListFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusOK,ResourceString.FB_KEY,"[{\"fb_qn_id\":0,\"category\":" +
                "\"cat\",\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"fb_qn_id\":1,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"fb_qn_id\":2,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"}]");
    }

    @Test
    public void getAllQuestionsNFB()
            throws Exception {
        List<AuditCheckListNFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        given(auditCheckListNFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusOK,ResourceString.NFB_KEY,"[{\"nfb_qn_id\":0,\"category\":" +
                "\"cat\",\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"nfb_qn_id\":1,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"nfb_qn_id\":2,\"category\":\"cat\"," +
                "\"sub_category\":\"dog\",\"weight\":0.2,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"}]");
    }

    @Test
    public void getAllQuestionsSMA()
            throws Exception {
        List<AuditCheckListSMAModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createSMAChecklist(auditCheckListModels.size()));
        given(auditCheckListSMARepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusOK,ResourceString.SMA_KEY,"[{\"sma_qn_id\":0,\"category\":" +
                "\"cat\",\"sub_category\":\"dog\",\"weight\":1.0,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"},{\"sma_qn_id\":1,\"category\":\"cat\",\"sub_category" +
                "\":\"dog\",\"weight\":1.0,\"requirement\":\"sdf\",\"sub_requirement\":\"sfa\"}," +
                "{\"sma_qn_id\":2,\"category\":\"cat\",\"sub_category\":\"dog\",\"weight\":1.0," +
                "\"requirement\":\"sdf\",\"sub_requirement\":\"sfa\"}]");
    }

    @Test
    public void getAllQuestionsWrongInput()
            throws Exception {
        List<AuditCheckListNFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        given(auditCheckListNFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusNotFound,"wrongType",null);
    }

    @Test
    public void getAllQuestionsNoParam()
            throws Exception {
        List<AuditCheckListNFBModel> auditCheckListModels = new ArrayList<>();
        for(int i=0;i<3;i++)
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        given(auditCheckListNFBRepo.getAllQuestions()).willReturn(auditCheckListModels);
        getAllQuestions(statusBad, null,null);
    }

    private void getAllQuestions(String statusExpected, String checkListType, String compareJson) throws Exception {
        String url = "/report/getAllQuestions";
        HashMap<String,String> params = new HashMap<>();
        if(checkListType!=null) params.put("type",checkListType);
        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.getHttpOk(mvc,url, params, 3, compareJson);
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc,url, params);
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
            case statusNotFound -> HTTPRequestHelperTestFunctions.getHttpNotFoundRequest(mvc,url,params);
        }
    }
    @Test
    public void getQuestionSMA()
            throws Exception {
        AuditCheckListSMAModel auditCheckListModel = createSMAChecklist(1);
        given(auditCheckListSMARepo.getQuestion(1)).willReturn(auditCheckListModel);
        getQuestion(statusOK,ResourceString.SMA_KEY,"1","{\"sma_qn_id\":1,\"category\":" +
                "\"cat\",\"sub_category\":\"dog\",\"weight\":1.0,\"requirement\":\"sdf\"," +
                "\"sub_requirement\":\"sfa\"}");
    }

    @Test
    public void getQuestionInvalidQnId()
            throws Exception {
        given(auditCheckListNFBRepo.getQuestion(1)).willReturn(null);
        getQuestion(statusNotFound,ResourceString.FB_KEY,"1",null);
    }

    @Test
    public void getQuestionsWrongType()
            throws Exception {
        getQuestion(statusNotFound,"wrongType","1",null);
    }

    @Test
    public void getQuestionNoQnId() throws Exception {
        getQuestion(statusBad,ResourceString.NFB_KEY,null,null);
    }

    @Test
    public void getQuestionNoParam() throws Exception {
        getQuestion(statusBad, null,null,null);
    }

    private void getQuestion(String statusExpected, String checkListType, String qn_id, String compareJson) throws Exception {
        String url = "/report/getQuestion";
        HashMap<String,String> params = new HashMap<>();
        if(checkListType!=null) params.put("type",checkListType);
        if(qn_id!=null) params.put("qn_id",qn_id);
        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, params, -1, compareJson);
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc,url, params);
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
            case statusNotFound -> HTTPRequestHelperTestFunctions.getHttpNotFoundRequest(mvc,url,params);
        }
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportSubmissionOKFullMarks()
            throws Exception {
        String checklist = createAuditedChecklist(true,false,0);
        System.out.println(checklist);
        given(openAuditRepo.existsById(SAVEDREPORTID)).willReturn(true);
        doAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return count++ == 0;
            }
        }).when(openAuditRepo).existsById(SAVEDREPORTID);
        postReportSubmission(statusOK,ResourceString.FB_KEY,checklist,String.valueOf(TENANTID),"REMARKS","100");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportSubmissionOKNotFullMarks()
            throws Exception {
        String checklist = createAuditedChecklist(false,false,3210421);
        System.out.println(checklist);
        postReportSubmission(statusOK,ResourceString.FB_KEY,checklist,String.valueOf(TENANTID),"REMARKS","80");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportSubmissionNotFullMarksSeverityWrong()
            throws Exception {
        String checklist = createAuditedChecklist(false,false,23);
        System.out.println(checklist);
        postReportSubmission(statusBad,ResourceString.FB_KEY,checklist,String.valueOf(TENANTID),"REMARKS",null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportSubmissionNotFullMarksTypeWrong()
            throws Exception {
        String checklist = createAuditedChecklist(false,false,3210421);
        System.out.println(checklist);
        postReportSubmission(statusBad,"wrong type",checklist,String.valueOf(TENANTID),"REMARKS",null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportSubmissionNotFullMarksMissingFields()
            throws Exception {
        String checklist = createAuditedChecklist(false,false,3210421);
        System.out.println(checklist);
        postReportSubmission(statusBad,"wrong type",checklist,String.valueOf(TENANTID),null,null);
    }

    private void postReportSubmission(String statusExpected, String report_type, String checklist,
                                      String tenant_id, String remarks, String compareJson) throws Exception {

        for(int i=0;i<5;i++){
            given(auditCheckListFBRepo.getCategoryByQnID(i)).willReturn(String.valueOf(i));
        }
        given(auditorRepo.getManagerIDfromAuditorID(AUDITORID)).willReturn(MANAGERID);
        String url = "/report/postReportSubmission";
        HashMap<String,String> postBody = new HashMap<>();
        if(checklist!=null) postBody.put("checklist",checklist);
        HashMap<String,String> params = new HashMap<>();
        if(report_type!=null) params.put("type",report_type);
        if(tenant_id!=null) params.put("tenant_id",tenant_id);
        if(remarks!=null) params.put("remarks",remarks);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.postHttpOK(mvc,url,postBody,params,compareJson,true);
            case statusBad -> HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, postBody,params);
        }
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateOKGroupFullMarks()
            throws Exception {
        String entry = createAuditedChecklist(true,false,0);
        System.out.println(entry);
        given(openAuditRepo.existsById(SAVEDREPORTID)).willReturn(true);
        postReportUpdate(statusOK,true, String.valueOf(SAVEDREPORTID),entry,String.valueOf(true),"REMARKS","100");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateOKGroupNotFullMarks()
            throws Exception {
        String entry = createAuditedChecklist(false,false,3210421);
        System.out.println(entry);
        postReportUpdate(statusOK,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(true),"REMARKS","80");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateGroupNotFullMarksSeverityWrong()
            throws Exception {
        String entry = createAuditedChecklist(false,false,23);
        System.out.println(entry);
        postReportUpdate(statusBad,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(true),"REMARKS",null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateNotFullMarksReportEntryNotExist()
            throws Exception {
        String entry = createAuditedChecklist(false,false,3210421);
        System.out.println(entry);
        try {
            postReportUpdate(statusBad, false,String.valueOf(SAVEDREPORTID), entry, String.valueOf(true), "REMARKS", null);
        } catch (NestedServletException e){
            System.out.println(e.getCause().getClass());
            if(e.getCause().getClass().equals(IllegalArgumentException.class))
                System.out.println("pass");
            else{
                fail();
            }
        }
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateGroupNotFullMarksMissingFields()
            throws Exception {
        String entry = createAuditedChecklist(false,false,3210421);
        System.out.println(entry);
        postReportUpdate(statusBad,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(true),null,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateOKNotGroupFullMarks()
            throws Exception {
        String entry = objectMapper.writeValueAsString(createEntry(true,1,0));
        System.out.println(entry);
        given(openAuditRepo.existsById(SAVEDREPORTID)).willReturn(true);
        postReportUpdate(statusOK,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(false),"REMARKS","80");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateNotGroupOKNotFullMarks()
            throws Exception {
        String entry = objectMapper.writeValueAsString(createEntry(false,0,3210421));
        System.out.println(entry);
        postReportUpdate(statusOK,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(false),"REMARKS","60");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateNotGroupNotFullMarksSeverityWrong()
            throws Exception {
        String entry = objectMapper.writeValueAsString(createEntry(false,1,23));
        System.out.println(entry);
        try {
            postReportUpdate(statusBad,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(false),"REMARKS",null);
        } catch (NestedServletException e){
            System.out.println(e.getCause().getClass());
            if(e.getCause().getClass().equals(IllegalArgumentException.class))
                System.out.println("pass");
            else{
                fail();
            }
        }
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { ResourceString.AUDITOR_ROLE_KEY })
    public void postReportUpdateNotGroupNotFullMarksMissingFields()
            throws Exception {
        String entry = objectMapper.writeValueAsString(createEntry(false,1,3210421));
        System.out.println(entry);
        postReportUpdate(statusBad,true,String.valueOf(SAVEDREPORTID),entry,String.valueOf(false),null,null);
    }

    private void postReportUpdate(String statusExpected, boolean reportExist, String report_id, String entry,
                                      String group_update, String remarks, String compareJson) throws Exception {

        for(int i=0;i<5;i++){
            given(auditCheckListFBRepo.getCategoryByQnID(i)).willReturn(String.valueOf(i));
        }
        if(reportExist) {
            OpenAuditModel openAuditModel = createOpenAuditModel();
            given(openAuditRepo.getOpenAuditById(SAVEDREPORTID)).willReturn(openAuditModel);
        } else {
            given(openAuditRepo.getOpenAuditById(SAVEDREPORTID)).willReturn(null);
        }
        given(auditorRepo.getOutstandingAuditsFromAuditorID(AUDITORID)).willReturn("{\"outstanding_audits\": [\""+String.valueOf(SAVEDREPORTID)+"\"]}");

        doAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return (count++ !=2);
            }
        }).when(openAuditRepo).existsById(SAVEDREPORTID);

        given(auditorRepo.getManagerIDfromAuditorID(AUDITORID)).willReturn(MANAGERID);
        String url = "/report/postReportUpdate";
        HashMap<String,String> postBody = new HashMap<>();
        if(entry!=null) postBody.put("entry",entry);
        HashMap<String,String> params = new HashMap<>();
        if(report_id!=null) params.put("report_id",report_id);
        if(group_update!=null) params.put("group_update",group_update);
        if(remarks!=null) params.put("remarks",remarks);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.postHttpOK(mvc,url,postBody,params,compareJson,true);
            case statusBad -> HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, postBody,params);
        }
    }

    private void createArbitraryChecklists(String checklistType, List<AuditCheckListModel> auditCheckListModels){
        if(checklistType.equals(ResourceString.FB_KEY)){
            auditCheckListModels.add(createFBChecklist(auditCheckListModels.size()));
        } else if(checklistType.equals(ResourceString.NFB_KEY)){
            auditCheckListModels.add(createNFBChecklist(auditCheckListModels.size()));
        } else {
            auditCheckListModels.add(createSMAChecklist(auditCheckListModels.size()));
        }
    }

    private AuditCheckListFBModel createFBChecklist(int qn_id){
        AuditCheckListFBModel auditCheckListFBModel = new AuditCheckListFBModel();
        auditCheckListFBModel.setFb_qn_id(qn_id);
        auditCheckListFBModel.setCategory("cat");
        auditCheckListFBModel.setRequirement("sdf");
        auditCheckListFBModel.setSub_category("dog");
        auditCheckListFBModel.setWeight(0.2);
        auditCheckListFBModel.setSub_requirement("sfa");
        return auditCheckListFBModel;
    }

    private AuditCheckListNFBModel createNFBChecklist(int qn_id){
        AuditCheckListNFBModel auditCheckListNFBModel = new AuditCheckListNFBModel();
        auditCheckListNFBModel.setNfb_qn_id(qn_id);
        auditCheckListNFBModel.setCategory("cat");
        auditCheckListNFBModel.setRequirement("sdf");
        auditCheckListNFBModel.setSub_category("dog");
        auditCheckListNFBModel.setWeight(0.2);
        auditCheckListNFBModel.setSub_requirement("sfa");
        return auditCheckListNFBModel;
    }

    private AuditCheckListSMAModel createSMAChecklist(int qn_id){
        AuditCheckListSMAModel auditCheckListSMAModel = new AuditCheckListSMAModel();
        auditCheckListSMAModel.setSma_qn_id(qn_id);
        auditCheckListSMAModel.setCategory("cat");
        auditCheckListSMAModel.setRequirement("sdf");
        auditCheckListSMAModel.setSub_category("dog");
        auditCheckListSMAModel.setWeight(1);
        auditCheckListSMAModel.setSub_requirement("sfa");
        return auditCheckListSMAModel;
    }

    private ObjectNode createEntry(boolean isFullMarks, int qn_id, int severity){
        ObjectNode subNode = objectMapper.createObjectNode();
        subNode.put("qn_id",qn_id);
        if(isFullMarks) {
            subNode.put("status",1);
        } else {
            subNode.put("status",0);
            subNode.put("remarks","nothing");
            subNode.put("severity",severity);
            subNode.put("images","[\"abc\",\"bca\"]");
        }
        return subNode;
    }

    private OpenAuditModel createOpenAuditModel(){
        Date date = new Date(Calendar.getInstance().getTime().getTime());

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ReportEntry.class, new CustomReportEntryDeserializer());
        objectMapper.registerModule(module);
        JavaType customClassCollection = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ReportEntry.class);
        String checklist = createAuditedChecklist(false,false,2210423);
        List<ReportEntry> entries = null;
        try {
            entries = objectMapper.readValue(checklist, customClassCollection);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        OpenReport openReport = new OpenReport(SAVEDREPORTID, TENANTID, AUDITORID, MANAGERID, date, 80,
        "overall_remarks", ResourceString.FB_KEY, entries, 1, 0, 0, 1, date);

        try {
            OpenAuditModel openAuditModel = new OpenAuditModel(SAVEDREPORTID, TENANTID, AUDITORID, MANAGERID, date,date
                    , "overall_remarks", ResourceString.FB_KEY, 70, objectMapper.writeValueAsString(openReport), 1, 0, 0);
            return openAuditModel;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new OpenAuditModel();
        }
    }

    private String createAuditedChecklist(boolean isFullMarks, boolean hasNA, int severity){
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for(int i=0;i<5;i++){
            if(i!=4||isFullMarks){
                arrayNode.add(createEntry(true,i,severity));
            } else{
                arrayNode.add(createEntry(false,i,severity));
            }
        }

        try {
            return objectMapper.writeValueAsString(arrayNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private AccountModel createAccount(String accountType, int acc_id, String firstName, String lastName, String branch_id){
        AccountModel newAccount = new AccountModel();
        newAccount.setAccount_id(acc_id);
        newAccount.setBranch_id(branch_id);
        newAccount.setEmail("something@email.com");
        newAccount.setEmployee_id(123);
        newAccount.setFirst_name(firstName);
        newAccount.setLast_name(lastName);
        newAccount.setHp("234");
        newAccount.setUsername(firstName+lastName);
        newAccount.setRole_id(accountType);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(KNOWN_USER_PASSWORD);
        newAccount.setPassword(password);

        return newAccount;
    }


}
