package com.c2g4.SingHealthWebApp.ControllersTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.c2g4.SingHealthWebApp.Chat.ChatRepo;
import com.c2g4.SingHealthWebApp.Notifications.NotificationsModel;
import com.c2g4.SingHealthWebApp.Notifications.NotificationsRepo;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private AccountRepo accountRepo;
    @MockBean
    private ChatRepo chatRepo;
    @MockBean
    private NotificationsRepo notificationsRepo;
    @MockBean
    private TenantRepo tenantRepo;
    @MockBean
    private AuditorRepo auditorRepo;

    private static final String MANAGERUSENAME = "managerUsername";
    private static final String KNOWN_USER_PASSWORD = "test123";
    private static final String AUDITORUSENAME = "auditorUsername";
    private static final String TENANTUSENAME = "tenantUsername";

    private static final String MANAGER = "Manager";
    private static final String AUDITOR = "Auditor";
    private static final String TENANT = "Tenant";

    private static final int MANAGERID = 100;
    private static final int AUDITORID = 101;
    private static final int TENANTID = 102;

    private static final String statusOK = "ok";
    private static final String statusBad = "bad";
    private static final String statusUnauthorized = "unauthorized";

    @BeforeEach
    public void before() {
        AccountModel managerAccount = createAccount(MANAGER,MANAGERID,"Marcus","Ho","HQ");
        AccountModel auditorAccount = createAccount(AUDITOR,AUDITORID,"Hannah","Mah","Branch_A");
        AccountModel tenantAccount = createAccount(TENANT,TENANTID,"Gregory","Mah","Branch_A");
        given(accountRepo.findByUsername(MANAGERUSENAME)).willReturn(managerAccount);
        given(accountRepo.findByUsername(AUDITORUSENAME)).willReturn(auditorAccount);
        given(accountRepo.findByUsername(TENANTUSENAME)).willReturn(tenantAccount);
    }


    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllAvailableNotificationsTenantSelf() throws Exception {
        getAllAvailableNotifications(statusOK,null,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":4,\"forManager\":" +
                "false,\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":5,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":6," +
                "\"forManager\":false,\"forAuditor\":true,\"forTenant\":true},{\"notification_id\":7," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllAvailableNotificationsAuditorSelf() throws Exception{
        getAllAvailableNotifications(statusOK,null,"[{\"notification_id\":2," +
            "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
            "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":2,\"forManager\":" +
            "false,\"forAuditor\":true,\"forTenant\":false},{\"notification_id\":3,\"creator_id\":100," +
            "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
            "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forManager\":true,\"forAuditor" +
            "\":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
            "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
            "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true," +
            "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\"," +
            "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
            "\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
            "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllAvailableNotificationsManagerSelf() throws Exception {
        getAllAvailableNotifications(statusOK,null,"[{\"notification_id\":1," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":1," +
                "\"forManager\":true,\"forAuditor\":false,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllAvailableNotificationsTenantTenant() throws Exception {
        getAllAvailableNotifications(statusOK,ResourceString.TENANT_ROLE_KEY,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":4,\"forManager\":" +
                "false,\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":5,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":6," +
                "\"forManager\":false,\"forAuditor\":true,\"forTenant\":true},{\"notification_id\":7," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllAvailableNotificationsAuditorAuditor() throws Exception {
        getAllAvailableNotifications(statusOK,ResourceString.AUDITOR_ROLE_KEY,"[{\"notification_id\":2," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":2,\"forManager\":" +
                "false,\"forAuditor\":true,\"forTenant\":false},{\"notification_id\":3,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forManager\":true,\"forAuditor" +
                "\":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true," +
                "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");

    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getAllAvailableNotificationsTenantNotTenant() throws Exception {
        getAllAvailableNotifications(statusUnauthorized,ResourceString.AUDITOR_ROLE_KEY,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getAllAvailableNotificationsAuditorNotAuditor() throws Exception {
        getAllAvailableNotifications(statusUnauthorized,ResourceString.TENANT_ROLE_KEY,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllAvailableNotificationsManagerTenant() throws Exception {
        getAllAvailableNotifications(statusOK,ResourceString.TENANT_ROLE_KEY,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":4,\"forAuditor\":false,\"forManager\":false,\"forTenant\":true}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":" +
                "\"2021-04-05\",\"to_role_ids\":5,\"forAuditor\":false,\"forManager\":true," +
                "\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forAuditor\":true,\"forManager\":false," +
                "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\",\"message\":" +
                "\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":" +
                "\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}]\n");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllAvailableNotificationsManagerAuditor() throws Exception {
        getAllAvailableNotifications(statusOK,ResourceString.AUDITOR_ROLE_KEY,"[{\"notification_id\":2," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":2,\"forManager\":" +
                "false,\"forAuditor\":true,\"forTenant\":false},{\"notification_id\":3,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forManager\":true,\"forAuditor" +
                "\":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true," +
                "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllAvailableNotificationsManagerManager() throws Exception {
        getAllAvailableNotifications(statusOK,ResourceString.MANAGER_ROLE_KEY,"[{\"notification_id\":1," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":1," +
                "\"forManager\":true,\"forAuditor\":false,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllAvailableNotificationsManagerInvalid() throws Exception {
        getAllAvailableNotifications(statusBad,"not valid role_id",null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getAllAvailableNotificationsManagerNoNotifsAtAll() throws Exception {
        getAllAvailableNotifications(statusBad+"noNotifs",ResourceString.MANAGER_ROLE_KEY,null);
    }

    private void getAllAvailableNotifications(String statusExpected, String role_id, String compareJson) throws Exception {
        String url = "/notifications/getAllAvailableNotifications";
        HashMap<String,String> params = new HashMap<>();
        if(role_id!=null) params.put("role_id",role_id);
        List<NotificationsModel> notificationsModels = createNotificationList();
        given(notificationsRepo.getAllAvailableNotifications()).willReturn(notificationsModels);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, params, 4, compareJson);
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            case (statusBad+"noNotifs") -> {
                given(notificationsRepo.getAllAvailableNotifications()).willReturn(null);
                HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            }
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
        }
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getCurrentNotificationsTenantSelf() throws Exception {
        getCurrentNotifications(statusOK,null,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":4,\"forManager\":" +
                "false,\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":5,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":6," +
                "\"forManager\":false,\"forAuditor\":true,\"forTenant\":true},{\"notification_id\":7," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getCurrentNotificationsAuditorSelf() throws Exception {
        getCurrentNotifications(statusOK,null,"[{\"notification_id\":2," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":2,\"forAuditor\":true,\"forManager\":false,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\",\"message\":" +
                "\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forAuditor\":true,\"forManager\"" +
                ":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":" +
                "\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\"" +
                ":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forAuditor\":true," +
                "\"forManager\":false,\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":7,\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getCurrentNotificationsManagerSelf() throws Exception {
        getCurrentNotifications(statusOK,null,"[{\"notification_id\":1," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":1," +
                "\"forManager\":true,\"forAuditor\":false,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getCurrentNotificationsTenantTenant() throws Exception {
        getCurrentNotifications(statusOK,ResourceString.TENANT_ROLE_KEY,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":4,\"forManager\":" +
                "false,\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":5,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":6," +
                "\"forManager\":false,\"forAuditor\":true,\"forTenant\":true},{\"notification_id\":7," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getCurrentNotificationsAuditorAuditor() throws Exception {
        getCurrentNotifications(statusOK,ResourceString.AUDITOR_ROLE_KEY,"[{\"notification_id\":2," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":2,\"forManager\":" +
                "false,\"forAuditor\":true,\"forTenant\":false},{\"notification_id\":3,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forManager\":true,\"forAuditor" +
                "\":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true," +
                "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");

    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getCurrentNotificationsTenantNotTenant() throws Exception {
        getCurrentNotifications(statusUnauthorized,ResourceString.AUDITOR_ROLE_KEY,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getCurrentNotificationsAuditorNotAuditor() throws Exception {
        getCurrentNotifications(statusUnauthorized,ResourceString.TENANT_ROLE_KEY,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getCurrentNotificationsManagerTenant() throws Exception {
        getCurrentNotifications(statusOK,ResourceString.TENANT_ROLE_KEY,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":4,\"forManager\":" +
                "false,\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":5,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":6," +
                "\"forManager\":false,\"forAuditor\":true,\"forTenant\":true},{\"notification_id\":7," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getCurrentNotificationsManagerAuditor() throws Exception {
        getCurrentNotifications(statusOK,ResourceString.AUDITOR_ROLE_KEY,"[{\"notification_id\":2," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":2,\"forManager\":" +
                "false,\"forAuditor\":true,\"forTenant\":false},{\"notification_id\":3,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forManager\":true,\"forAuditor" +
                "\":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true," +
                "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getCurrentNotificationsManagerManager() throws Exception {
        getCurrentNotifications(statusOK,ResourceString.MANAGER_ROLE_KEY,"[{\"notification_id\":1," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":1," +
                "\"forManager\":true,\"forAuditor\":false,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forManager\":true," +
                "\"forAuditor\":false,\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getCurrentNotificationsManagerInvalid() throws Exception {
        getCurrentNotifications(statusBad,"not valid role_id",null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getCurrentNotificationsManagerNoNotifsAtAll() throws Exception {
        getCurrentNotifications(statusBad+"noNotifs",ResourceString.MANAGER_ROLE_KEY,null);
    }

    private void getCurrentNotifications(String statusExpected, String role_id, String compareJson) throws Exception {
        String url = "/notifications/getCurrentNotifications";
        HashMap<String,String> params = new HashMap<>();
        if(role_id!=null) params.put("role_id",role_id);
        List<NotificationsModel> notificationsModels = createNotificationList();
        given(notificationsRepo.getCurrentNotifications()).willReturn(notificationsModels);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, params, 4, compareJson);
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            case (statusBad+"noNotifs") -> {
                given(notificationsRepo.getCurrentNotifications()).willReturn(null);
                HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            }
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
        }
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getNotificationByNotificationIdTenantOK() throws Exception {
        getNotificationByNotificationId(statusOK,"0","{\"notification_id\":0," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getNotificationByNotificationIdAuditorOK() throws Exception {
        getNotificationByNotificationId(statusOK,"0","{\"notification_id\":0," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}");

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationByNotificationIdManagerOK() throws Exception {
        getNotificationByNotificationId(statusOK,"0","{\"notification_id\":0," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getNotificationByNotificationIdTenantUnauthorised() throws Exception {
        NotificationsModel notificationsModel = createNotificationModel(0,true,true,false);
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
        getNotificationByNotificationId(statusUnauthorized,"0",null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getNotificationByNotificationIdAuditorUnauthorised() throws Exception {
        NotificationsModel notificationsModel = createNotificationModel(0,true,false,false);
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
        getNotificationByNotificationId(statusUnauthorized,"0",null);

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationByNotificationIdManagerCallA() throws Exception {
        NotificationsModel notificationsModel = createNotificationModel(0,false,true,false);
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
        getNotificationByNotificationId(statusOK,"0","{\"notification_id\":0," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationByNotificationIdManagerCallT() throws Exception {
        NotificationsModel notificationsModel = createNotificationModel(0,false,false,true);
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
        getNotificationByNotificationId(statusOK,"0","{\"notification_id\":0," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationByNotificationIdManagerNotifNotExist() throws Exception {
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(null);
        getNotificationByNotificationId(statusBad,"0",null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationByNotificationIdManagerNotifNotGiven() throws Exception {
        getNotificationByNotificationId(statusBad,null,null);
    }

    private void getNotificationByNotificationId(String statusExpected, String notification_id, String compareJson) throws Exception {
        String url = "/notifications/getNotificationByNotificationId";
        HashMap<String,String> params = new HashMap<>();
        if(notification_id!=null) params.put("notification_id",notification_id);

        switch (statusExpected) {
            case statusOK -> {
                NotificationsModel notificationsModel = createNotificationModel(0,true,true,true);
                given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
                HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, params, -1, compareJson);
            }
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
        }
    }


    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getNotificationsByCreatorIdTenantOK() throws Exception {
        getNotificationsByCreatorId(statusOK,String.valueOf(MANAGERID),4,"[{\"notification_id\":4," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":4,\"forAuditor\":" +
                "false,\"forManager\":false,\"forTenant\":true},{\"notification_id\":5,\"creator_id\":100,\"title\":" +
                "\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":5,\"forAuditor\":false,\"forManager\":true," +
                "\"forTenant\":true},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\",\"message\":" +
                "\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":" +
                "\"2021-04-05\",\"to_role_ids\":6,\"forAuditor\":true,\"forManager\":false,\"forTenant\":true}," +
                "{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forAuditor\":true,\"forManager\":true,\"forTenant\":true}]\n");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getNotificationsByCreatorIdAuditorOK() throws Exception {
        getNotificationsByCreatorId(statusOK,String.valueOf(MANAGERID),4,"[{\"notification_id\":2," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\"," +
                "\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":2,\"forManager\":" +
                "false,\"forAuditor\":true,\"forTenant\":false},{\"notification_id\":3,\"creator_id\":100," +
                "\"title\":\"title\",\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":" +
                "\"2021-04-05\",\"end_date\":\"2021-04-05\",\"to_role_ids\":3,\"forManager\":true,\"forAuditor" +
                "\":true,\"forTenant\":false},{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true," +
                "\"forTenant\":true},{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\"," +
                "\"message\":\"message\",\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\"," +
                "\"end_date\":\"2021-04-05\",\"to_role_ids\":7," +
                "\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");

    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationsByCreatorIdManagerOK() throws Exception {
        getNotificationsByCreatorId(statusOK,String.valueOf(MANAGERID),7,"[{\"notification_id\":1," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":1,\"forManager\":true,\"forAuditor\":false,\"forTenant\":false}," +
                "{\"notification_id\":2,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":2,\"forManager\":false,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":3,\"forManager\":true,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":4,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":4,\"forManager\":false,\"forAuditor\":false,\"forTenant\":true}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":5,\"forManager\":true,\"forAuditor\":false,\"forTenant\":true}," +
                "{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true,\"forTenant\":true}," +
                "{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":7,\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]\n");
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void getNotificationsByCreatorIdTenantNoCreatorId() throws Exception {
        getNotificationsByCreatorId(statusBad,null,0,null);
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void getNotificationsByCreatorIdAuditorNoCreatorId() throws Exception {
        getNotificationsByCreatorId(statusBad,null,0,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationsByCreatorIdManagerNoCreatorId() throws Exception {
        getNotificationsByCreatorId(statusOK,null,7,"[{\"notification_id\":1," +
                "\"creator_id\":100,\"title\":\"title\",\"message\":\"message\",\"create_date\":" +
                "\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":1,\"forManager\":true,\"forAuditor\":false,\"forTenant\":false}," +
                "{\"notification_id\":2,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":2,\"forManager\":false,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":3,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":3,\"forManager\":true,\"forAuditor\":true,\"forTenant\":false}," +
                "{\"notification_id\":4,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":4,\"forManager\":false,\"forAuditor\":false,\"forTenant\":true}," +
                "{\"notification_id\":5,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":5,\"forManager\":true,\"forAuditor\":false,\"forTenant\":true}," +
                "{\"notification_id\":6,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":6,\"forManager\":false,\"forAuditor\":true,\"forTenant\":true}," +
                "{\"notification_id\":7,\"creator_id\":100,\"title\":\"title\",\"message\":\"message\"," +
                "\"create_date\":\"2021-04-05\",\"receipt_date\":\"2021-04-05\",\"end_date\":\"2021-04-05\"," +
                "\"to_role_ids\":7,\"forManager\":true,\"forAuditor\":true,\"forTenant\":true}]");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void getNotificationsByCreatorIdManagerNoNotifs() throws Exception {
        getNotificationsByCreatorId(statusBad+"NoNotifs",String.valueOf(MANAGERID),0,null);
    }

    private void getNotificationsByCreatorId(String statusExpected, String creator_id, int jsonNum, String compareJson) throws Exception {
        String url = "/notifications/getNotificationsByCreatorId";
        HashMap<String,String> params = new HashMap<>();
        if(creator_id!=null) params.put("creator_id",creator_id);
        List<NotificationsModel> notificationsModels = createNotificationList();
        given(notificationsRepo.getNotificationsByCreatorId(MANAGERID)).willReturn(notificationsModels);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.getHttpOk(mvc, url, params, jsonNum, compareJson);
            case statusBad -> HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            case statusBad+"NoNotifs" -> {
                given(notificationsRepo.getNotificationsByCreatorId(MANAGERID)).willReturn(null);
                HTTPRequestHelperTestFunctions.getHttpBadRequest(mvc, url, params);
            }
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.getHttpUnauthorizedRequest(mvc,url, params);
        }
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void postNewNotificationTenantBad() throws Exception {
        postNewNotification(statusUnauthorized,"title", "message", "7");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void postNewNotificationAuditorBad() throws Exception {
        postNewNotification(statusUnauthorized,"title", "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postNewNotificationManagerOK() throws Exception {
        postNewNotification(statusOK,"title", "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postNewNotificationManagerMissingArg() throws Exception {
        postNewNotification(statusBad,null, "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postNewNotificationManagertoRoleId0() throws Exception {
        postNewNotification(statusBad,"title", "message", "0");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postNewNotificationManagertoRoleIdMore7() throws Exception {
        postNewNotification(statusBad,"title", "message", "10");
    }

    private void postNewNotification(String statusExpected, String title, String message,String to_role_ids) throws Exception {
        String url = "/notifications/postNewNotification";

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> postBody = new HashMap<>();
        ObjectNode body = objectMapper.createObjectNode();


        body.put("receipt_date","04/04/2021");
        body.put("end_date","04/04/2021");

        if(title!=null) body.put("title",title);
        if(message!=null) body.put("message",message);
        if(to_role_ids!=null) body.put("to_role_ids",to_role_ids);

        postBody.put("new_notification",objectMapper.writeValueAsString(body));

        switch (statusExpected) {
            case statusOK -> {
                when(notificationsRepo.save(Mockito.any(NotificationsModel.class))).thenAnswer(i -> {
                    Object[] args = i.getArguments();
                    NotificationsModel notificationsModel = (NotificationsModel) args[0];
                    notificationsModel.setNotification_id(0);
                    return notificationsModel;
                });
                HTTPRequestHelperTestFunctions.postHttpOK(mvc,url,postBody,null,"notification created with ID: 0",true);
            }
            case statusBad -> HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, postBody,null);
        }
    }

    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void postModifyNotificationTenantBad() throws Exception {
        postModifyNotification(statusUnauthorized,"0","title", "message", "7");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void postModifyNotificationAuditorBad() throws Exception {
        postModifyNotification(statusUnauthorized,"0","title", "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postModifyNotificationManagerOK() throws Exception {
        postModifyNotification(statusOK,"0","title", "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postModifyNotificationManagerNotCreator() throws Exception {
        postModifyNotification(statusUnauthorized+"notManager","0","title", "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postModifyNotificationManagerMissingArg() throws Exception {
        postModifyNotification(statusBad,"0",null, "message", "7");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postModifyNotificationManagertoRoleId0() throws Exception {
        postModifyNotification(statusBad,"0",null, "message", "0");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void postModifyNotificationManagertoRoleIdMore7() throws Exception {
        postModifyNotification(statusBad,"0",null, "message", "10");
    }

    private void postModifyNotification(String statusExpected, String notification_id, String title, String message,String to_role_ids) throws Exception {
        String url = "/notifications/postModifyNotification";
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> postBody = new HashMap<>();

        ObjectNode body = objectMapper.createObjectNode();

        body.put("receipt_date","04/04/2021");
        body.put("end_date","04/04/2021");

        if(notification_id!=null) body.put("notification_id",notification_id);
        if(title!=null) body.put("title",title);
        if(message!=null) body.put("message",message);
        if(to_role_ids!=null) body.put("to_role_ids",to_role_ids);

        postBody.put("modifiedNotification",objectMapper.writeValueAsString(body));

        NotificationsModel notificationsModel = createNotificationModel(0,true,true, true);
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.postHttpOK(mvc,url,postBody,null,"notification updated",true);
            case statusBad -> HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, postBody,null);
            case statusBad+"notManager" -> {
                notificationsModel = createNotificationModel(0,MANAGERID-10,true,true, true);
                given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
                HTTPRequestHelperTestFunctions.postHttpBadRequest(mvc,url, postBody,null);
            }
        }
    }


    @Test
    @WithMockUser(username = TENANTUSENAME, password = KNOWN_USER_PASSWORD, roles = { TENANT })
    public void deleteNotificationTenantBad() throws Exception {
        deleteNotification(statusUnauthorized,"0");
    }

    @Test
    @WithMockUser(username = AUDITORUSENAME, password = KNOWN_USER_PASSWORD, roles = { AUDITOR })
    public void deleteNotificationAuditorBad() throws Exception {
        deleteNotification(statusUnauthorized,"0");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void deleteNotificationManagerOK() throws Exception {
        deleteNotification(statusOK,"0");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void deleteNotificationManagerNotCreator() throws Exception {
        deleteNotification(statusUnauthorized+"notManager","0");
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void deleteNotificationManagerMissingArg() throws Exception {
        deleteNotification(statusBad,null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void deleteNotificationManagerNotifNotFound() throws Exception {
        deleteNotification("NotFound",null);
    }

    @Test
    @WithMockUser(username = MANAGERUSENAME, password = KNOWN_USER_PASSWORD, roles = { MANAGER })
    public void deleteNotificationManagerNotifCannotDelete() throws Exception {
        deleteNotification("CannotDelete",null);
    }

    private void deleteNotification(String statusExpected, String notification_id) throws Exception {
        String url = "/notifications/deleteNotification";
        HashMap<String,String> params = new HashMap<>();
        if(notification_id!=null) params.put("notification_id",notification_id);
        NotificationsModel notificationsModel = createNotificationModel(0,true,true,true);
        given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
        given(notificationsRepo.existsById(0)).willReturn(false);

        switch (statusExpected) {
            case statusOK -> HTTPRequestHelperTestFunctions.deleteHttpOk(mvc, url, params, "notification deleted, cannot be undone");
            case statusBad -> HTTPRequestHelperTestFunctions.deleteHttpBadRequest(mvc, url, params);
            case statusUnauthorized+"notManager" -> {
                notificationsModel = createNotificationModel(0,MANAGERID-10,true,true, true);
                given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(notificationsModel);
                HTTPRequestHelperTestFunctions.deleteHttpUnauthorizedRequest(mvc, url, params);
            }
            case statusUnauthorized -> HTTPRequestHelperTestFunctions.deleteHttpUnauthorizedRequest(mvc,url, params);
            case "NotFound" -> {
                given(notificationsRepo.getNotificationByNotificationId(0)).willReturn(null);
                HTTPRequestHelperTestFunctions.deleteHttpBadRequest(mvc,url, params);
            }
            case "CannotDelete" -> {
                given(notificationsRepo.existsById(0)).willReturn(true);
                HTTPRequestHelperTestFunctions.deleteHttpBadRequest(mvc,url, params);
            }
        }
    }

    private NotificationsModel createNotificationModel(int notification_id, boolean manager, boolean auditor, boolean tenant){
        return createNotificationModel(notification_id,MANAGERID,manager,auditor,tenant);
    }

    private NotificationsModel createNotificationModel(int notification_id, int creator_id, boolean manager, boolean auditor, boolean tenant){
        Calendar c = Calendar.getInstance();
        c.set(2021,Calendar.APRIL,5,14,20,30);
        Date date = new Date(c.getTime().getTime());
        //TAM
        int to_role_ids = 0;
        if(manager) to_role_ids+=1;
        if(auditor) to_role_ids+=2;
        if(tenant) to_role_ids+=4;

        return new NotificationsModel(notification_id, creator_id, "title", "message",
                date, date, date, to_role_ids);
    }

    private List<NotificationsModel> createNotificationList(){
        List<NotificationsModel> notificationsModels = new ArrayList<>();
        notificationsModels.add(createNotificationModel(1,true,false,false));
        notificationsModels.add(createNotificationModel(2,false,true,false));
        notificationsModels.add(createNotificationModel(3,true,true,false));
        notificationsModels.add(createNotificationModel(4,false,false,true));
        notificationsModels.add(createNotificationModel(5,true,false,true));
        notificationsModels.add(createNotificationModel(6,false,true,true));
        notificationsModels.add(createNotificationModel(7,true,true,true));
        return notificationsModels;
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
