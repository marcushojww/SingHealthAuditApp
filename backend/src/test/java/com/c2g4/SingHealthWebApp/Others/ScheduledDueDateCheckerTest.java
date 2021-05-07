package com.c2g4.SingHealthWebApp.Others;

import com.c2g4.SingHealthWebApp.Admin.Models.*;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import com.c2g4.SingHealthWebApp.Admin.Report.ReportEntry;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.CompletedAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Notifications.EmailServiceImpl;
import com.c2g4.SingHealthWebApp.Notifications.OverDueAuditEntries;
import com.c2g4.SingHealthWebApp.Notifications.ScheduledDueDateChecker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ScheduledDueDateCheckerTest {

    @MockBean
    OpenAuditRepo openAuditRepo;
    @MockBean
    CompletedAuditRepo completedAuditRepo;
    @MockBean
    AccountRepo accountRepo;
    @MockBean
    EmailServiceImpl emailService;

    @Autowired
    ScheduledDueDateChecker scheduledDueDateChecker;

    public static ObjectMapper objectMapper = new ObjectMapper();


    private static final String MANAGERUSENAME = "managerUsername";
    private static final String KNOWN_USER_PASSWORD = "test123";
    private static final String AUDITORUSENAME = "auditorUsername";
    private static final String TENANTUSENAME = "tenantUsername";

    private static final String MANAGER = "Manager";
    private static final String AUDITOR = "Auditor";
    private static final String TENANT = "Tenant";

    private static final int MANAGERID = 300;
    private static final int AUDITORID = 200;
    private static final int TENANTID = 100;

    @Test
    public void checkDueDatesOK(){
        List<Integer> allOpenAuditIds = new ArrayList<>();
        OpenAuditModel auditModel = (OpenAuditModel)setAuditReportBuilder(false);

        auditModel.setReport_data_for_MySql(" {\"entries\": [{\"date\": 1617880496861, \"time\": \"19:14:56\", \"qn_id\": 0, \"images\": null, \"status\": \"PASS\", \"remarks\": null,\n" +
                "  \"entry_id\": 0, \"severity\": 0, \"from_account_id\": 1003},\n" +
                " {\"date\": 1617880496861, \"time\": \"19:14:56\", \"qn_id\": 20, \"images\": [\"abc\", \"bca\"],\n" +
                "  \"status\": \"PASS\", \"remarks\": \"null\", \"entry_id\": 1, \"severity\": 0, \"from_account_id\": 1003},\n" +
                " {\"date\": 1617880496861, \"time\": \"19:14:56\", \"qn_id\": 30, \"images\": [\"abc\", \"bca\"],\n" +
                "  \"status\": \"PASS\", \"remarks\": \"dirty bowl\", \"entry_id\": 2, \"severity\": 0, \"from_account_id\": 1003},\n" +
                "  {\"date\": 1617880496861, \"time\": \"19:14:56\", \"qn_id\": 67, \"images\": [\"abc\", \"bca\"], \"status\": \"PASS\",\n" +
                "  \"remarks\": \"null\", \"entry_id\": 3, \"severity\": 0, \"from_account_id\": 1003},\n" +
                " {\"date\": 1617880496861, \"time\": \"19:14:56\", \"qn_id\": 88, \"images\": [\"abc\", \"bca\"],\n" +
                "  \"status\": \"FAIL\", \"remarks\": \"null\", \"entry_id\": 4, \"severity\": 3040421,\n" +
                "  \"from_account_id\": 1003}, {\"date\": 1617880496861, \"time\": \"19:14:56\",\n" +
                "  \"qn_id\": 89, \"images\": [\"abc\", \"bca\"], \"status\": \"FAIL\", \"remarks\": \"null\",\n" +
                "  \"entry_id\": 5, \"severity\": 3040421, \"from_account_id\": 1003}],\n" +
                "  \"open_date\": 1617880496861, \"report_id\": 0, \"tenant_id\": 1004, \"auditor_id\": 1003,\n" +
                "  \"manager_id\": 1001, \"need_tenant\": 1, \"report_type\": \"FB\", \"need_auditor\": 0,\n" +
                "  \"need_manager\": 0, \"overall_score\": 80, \"overall_status\": 0, \"overall_remarks\":\n" +
                "  \"nothing\", \"last_update_date\": 1617880497000}");

        allOpenAuditIds.add(0);
        given(openAuditRepo.getAllOpenAuditsIds()).willReturn(allOpenAuditIds);
        given(openAuditRepo.existsById(0)).willReturn(true);
        given(openAuditRepo.getOpenAuditById(0)).willReturn(auditModel);
        AccountModel tenantAccount = createAccount(TENANT, TENANTID, "firstName", "lastName", "branch_id");
        AccountModel auditorAccount = createAccount(AUDITOR, AUDITORID, "firstName", "lastName", "branch_id");

        given(accountRepo.findByAccId(TENANTID)).willReturn(tenantAccount);
        given(accountRepo.findByAccId(AUDITORID)).willReturn(auditorAccount);

        scheduledDueDateChecker.checkDueDates();
    }

    @Test
    public void checkDueDatesReportNotFound(){
        List<Integer> allOpenAuditIds = new ArrayList<>();
        allOpenAuditIds.add(0);
        given(openAuditRepo.getAllOpenAuditsIds()).willReturn(allOpenAuditIds);
        given(openAuditRepo.existsById(0)).willReturn(false);
        given(openAuditRepo.getOpenAuditById(0)).willReturn(null);
        AccountModel tenantAccount = createAccount(TENANT, TENANTID, "firstName", "lastName", "branch_id");
        AccountModel auditorAccount = createAccount(AUDITOR, AUDITORID, "firstName", "lastName", "branch_id");

        given(accountRepo.findByAccId(TENANTID)).willReturn(tenantAccount);
        given(accountRepo.findByAccId(AUDITORID)).willReturn(auditorAccount);

        scheduledDueDateChecker.checkDueDates();
    }

    @Test
    public void NoOverdueNotFound(){
        List<Integer> allOpenAuditIds = new ArrayList<>();
        allOpenAuditIds.add(0);
        given(openAuditRepo.getAllOpenAuditsIds()).willReturn(allOpenAuditIds);
        given(openAuditRepo.existsById(0)).willReturn(false);
        given(openAuditRepo.getOpenAuditById(0)).willReturn(null);
        AccountModel tenantAccount = createAccount(TENANT, TENANTID, "firstName", "lastName", "branch_id");
        AccountModel auditorAccount = createAccount(AUDITOR, AUDITORID, "firstName", "lastName", "branch_id");

        given(accountRepo.findByAccId(TENANTID)).willReturn(tenantAccount);
        given(accountRepo.findByAccId(AUDITORID)).willReturn(auditorAccount);

        scheduledDueDateChecker.checkDueDates();
    }

    public AuditModel setAuditReportBuilder(boolean completed){
        AuditModelBuilder auditModelBuilder = new AuditModelBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode dataNode = objectMapper.createObjectNode();
        dataNode.put("key","value");

        auditModelBuilder.setReportId(0);
        auditModelBuilder.setNeed(1,0,0);
        auditModelBuilder.setTenantId(TENANTID);
        auditModelBuilder.setAuditorId(AUDITORID);
        auditModelBuilder.setManagerId(MANAGERID);
        auditModelBuilder.setOverallScore(0);
        auditModelBuilder.setReport_type(ResourceString.FB_KEY);
        if(completed){
            auditModelBuilder.setTypeIsCompletedAudit();
        }
        auditModelBuilder.setReportData(dataNode);
        return auditModelBuilder.build();
    }


    private AccountModel createAccount(String accountType, int acc_id, String firstName, String lastName, String branch_id) {
        AccountModel newAccount = new AccountModel();
        newAccount.setAccount_id(acc_id);
        newAccount.setBranch_id(branch_id);
        newAccount.setEmail("something@email.com");
        newAccount.setEmployee_id(123);
        newAccount.setFirst_name(firstName);
        newAccount.setLast_name(lastName);
        newAccount.setHp("234");
        newAccount.setUsername(firstName + lastName);
        newAccount.setRole_id(accountType);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(KNOWN_USER_PASSWORD);
        newAccount.setPassword(password);

        return newAccount;
    }

}

/*
public class ScheduledDueDateChecker {
    private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
    private static final String EMAIL_SUBJECT = "Overdue Rectifications";


    //runs at 6am everyday
    @Scheduled(cron = "10 55 02 * * ?")
    public void checkDueDates(){
        logger.info("AUTOMATED CHECK DUE DATE START");
        List<Integer> openAuditModelIds = openAuditRepo.getAllOpenAuditsIds();
        //user id, entries
        HashMap<Integer, ArrayList<OverDueAuditEntries>> usersToNotify = new HashMap<>();
        for(int openAuditId: openAuditModelIds){
            logger.info("Openaudit id {}",openAuditId);
            ReportBuilder builder = ReportBuilder.getLoadedReportBuilder(openAuditRepo,
                    completedAuditRepo, openAuditId);
            List<ReportEntry> overDueEntries = builder.getOverDueEntries();
            if(overDueEntries.size()==0){
                logger.info("nothing is overdue");
                continue;
            }
            addAllUsersToNotify(usersToNotify,builder,overDueEntries);
        }
        emailUsers(usersToNotify);
    }

    private void addAllUsersToNotify(HashMap<Integer,ArrayList<OverDueAuditEntries>> usersToNotify,
                                     ReportBuilder builder, List<ReportEntry> overDueEntries){
        int managerId = builder.getManager_id();
        int auditorId = builder.getAuditor_id();
        int tenantId = builder.getTenant_id();
        logger.info("{} {} {}", managerId,auditorId,tenantId);

        OverDueAuditEntries overDueAuditEntries = new OverDueAuditEntries(overDueEntries,builder.getOpen_date(),
                builder.getOverall_score(), builder.getReport_id(), managerId, auditorId, tenantId,accountRepo);
        logger.info(overDueAuditEntries.toString());

        addUserToNotify(usersToNotify, overDueAuditEntries,managerId);
        addUserToNotify(usersToNotify, overDueAuditEntries,auditorId);
        addUserToNotify(usersToNotify, overDueAuditEntries,tenantId);
    }

    private void addUserToNotify(HashMap<Integer,ArrayList<OverDueAuditEntries>> usersToNotify,
                                 OverDueAuditEntries overDueAuditEntries, int user_id ){

        if(usersToNotify.containsKey(user_id)){
            usersToNotify.get(user_id).add(overDueAuditEntries);
            logger.info("appending to userstonotify for user {}",user_id);

        } else{
            ArrayList<OverDueAuditEntries> arrayList = new ArrayList<>();
            arrayList.add(overDueAuditEntries);
            usersToNotify.put(user_id, arrayList);
            logger.info("adding to userstonotify for user {}",user_id);
        }
    }


    //email them
    public void emailUsers(HashMap<Integer, ArrayList<OverDueAuditEntries>> usersToNotify) {
        logger.info("num users to notify {}", usersToNotify.size());
        for (int userId : usersToNotify.keySet()) {
            AccountModel accountModel = accountRepo.findByAccId(userId);
            if (accountModel == null) {
                logger.warn("USER WITH ID {} NOT FOUND", userId);
                continue;
            }
            String email = accountModel.getEmail();
            ArrayList<OverDueAuditEntries> overDueAuditEntriesList = usersToNotify.get(userId);

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("overdueEntries", overDueAuditEntriesList);

            try {
                emailService.sendMessageUsingThymeleafTemplate(email, EMAIL_SUBJECT, templateModel);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            logger.info("emailed to {}", userId);
        }
    }
}

*/