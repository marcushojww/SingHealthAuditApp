package com.c2g4.SingHealthWebApp.ControllersTest;

import com.c2g4.SingHealthWebApp.Admin.Controllers.ReportController;
import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.JWT.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class JWTAuthenticationRestControllerTest {

    @Value("${com.c2g4.singHealthAudit.jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AccountRepo accountRepo;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;
    @Autowired
    private AppUserDetailsService appUserDetailsService;

    private static final String MANAGER = "Manager";
    private static final String AUDITOR = "Auditor";
    private static final String TENANT = "Tenant";

    private static final String KNOWN_USERNAME = "username";
    private static final String KNOWN_PASSWORD = "password";

    private static final String statusOK = "ok";
    private static final String statusBad = "bad";
    private static final String statusUnauthorized = "unauthorized";

    private ResultActions performPostRequest(String requestURL, String stringBody) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(requestURL)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringBody);
        return mvc.perform(mockHttpServletRequestBuilder);
    }

    private void postHttpOKCreateAuthenticationToken(String requestURL, String bodyString, String callerType) throws Exception{
        ResultActions resultActions = performPostRequest(requestURL,bodyString);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.token").exists());
        resultActions.andExpect(jsonPath("$.token").isNotEmpty());
        resultActions.andExpect(jsonPath("$.accountType").value(callerType));
    }

    private void postHttpBadCreateAuthenticationToken(String requestURL, String bodyString) throws Exception{
        ResultActions resultActions = performPostRequest(requestURL,bodyString);
        resultActions.andExpect(status().isBadRequest());
    }

    private void postHttpUnauthorizedCreateAuthenticationToken(String requestURL, String bodyString) throws Exception{
        ResultActions resultActions = performPostRequest(requestURL,bodyString);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    private ResultActions performGetRequestTokenRefresh(String requestURL, String token) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(requestURL)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization",token);
        return mvc.perform(mockHttpServletRequestBuilder);
    }

    private void getHttpOKTokenRefresh(String requestURL, String token, String callerType) throws Exception{
        ResultActions resultActions = performGetRequestTokenRefresh(requestURL,token);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.token").exists());
        resultActions.andExpect(jsonPath("$.token").isNotEmpty());
        resultActions.andExpect(jsonPath("$.accountType").value(callerType));
    }

    private void getHttpBadTokenRefresh(String requestURL, String token) throws Exception{
        ResultActions resultActions = performGetRequestTokenRefresh(requestURL,token);
        resultActions.andExpect(status().isBadRequest());
    }

    private void getHttpUnauthorizedTokenRefresh(String requestURL, String token) throws Exception{
        ResultActions resultActions = performGetRequestTokenRefresh(requestURL,token);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void createAuthenticationTokenManagerOK()
            throws Exception {
        AccountModel accountModel = createAccount(MANAGER,KNOWN_USERNAME);
        given(accountRepo.findByUsername(KNOWN_USERNAME)).willReturn(accountModel);
        createAuthenticationTokenTest(statusOK,MANAGER, KNOWN_USERNAME, KNOWN_PASSWORD);
    }

    @Test
    public void createAuthenticationTokenManagerUsernameFail()
            throws Exception {
        given(accountRepo.findByUsername("badUsername")).willReturn(null);
        createAuthenticationTokenTest(statusUnauthorized,MANAGER,"badUsername",KNOWN_PASSWORD);
    }

    @Test
    public void createAuthenticationTokenManagerPasswordFail()
            throws Exception {
        AccountModel accountModel = createAccount(MANAGER,KNOWN_USERNAME);
        given(accountRepo.findByUsername(KNOWN_USERNAME)).willReturn(accountModel);

        createAuthenticationTokenTest(statusUnauthorized,MANAGER,KNOWN_USERNAME,"badPassword");
    }

    @Test
    public void createAuthenticationTokenManagerNoUsername()
            throws Exception {
        try {
            createAuthenticationTokenTest(statusBad,MANAGER,null,KNOWN_PASSWORD);
        } catch(NestedServletException e){
            System.out.println(e.getCause().getClass());
            assertEquals(e.getCause().getClass(),NullPointerException.class);
        }
    }

    @Test
    public void createAuthenticationTokenManagerNoPassword()
            throws Exception {
        try {
            createAuthenticationTokenTest(statusBad,MANAGER,KNOWN_USERNAME,null);
        } catch(NestedServletException e){
            System.out.println(e.getCause().getClass());
            assertEquals(e.getCause().getClass(),NullPointerException.class);
        }
    }

    @Test
    public void createAuthenticationTokenManagerNoBody()
            throws Exception {
        try {
            createAuthenticationTokenTest(statusBad,MANAGER,null,null);
        } catch(NestedServletException e){
            System.out.println(e.getCause().getClass());
            assertEquals(e.getCause().getClass(),NullPointerException.class);
        }
    }


    public void createAuthenticationTokenTest(String statusExpected, String accountType, String username, String password) throws Exception {
        String url = "/authenticateP";

        given(accountRepo.getRoleFromUsername(KNOWN_USERNAME)).willReturn(accountType);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode checklistNode = mapper.createObjectNode();
        if(username!=null) checklistNode.put("username", username);
        if(password!=null) checklistNode.put("password", password);
        String bodyString = mapper.writeValueAsString(checklistNode);

        switch (statusExpected) {
            case statusOK -> postHttpOKCreateAuthenticationToken(url,bodyString, accountType);
            case statusBad -> postHttpBadCreateAuthenticationToken(url, bodyString);
            case statusUnauthorized -> postHttpUnauthorizedCreateAuthenticationToken(url,bodyString);
        }
    }

    @Test
    public void refreshAndGetAuthenticationTokenNULLToken()
            throws Exception {
        try {
            getHttpBadTokenRefresh("/refresh", JSONObject.NULL.toString());
        } catch(NestedServletException e){
            System.out.println(e.getCause().getClass());
            assertEquals(e.getCause().getClass(),StringIndexOutOfBoundsException.class);
        }
    }

    @Test
    public void refreshAndGetAuthenticationTokenEmptyToken()
            throws Exception {
        try {
            getHttpBadTokenRefresh("/refresh", "");
        } catch(NestedServletException e){
            System.out.println(e.getCause().getClass());
            assertEquals(e.getCause().getClass(),StringIndexOutOfBoundsException.class);
        }
    }
    @Test
    public void refreshAndGetAuthenticationTokenOKTokenAsManager()
            throws Exception {
        AccountModel accountModel = createAccount(MANAGER,KNOWN_USERNAME);
        given(accountRepo.findByUsername(KNOWN_USERNAME)).willReturn(accountModel);
        given(accountRepo.getRoleFromUsername(KNOWN_USERNAME)).willReturn(MANAGER);
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(KNOWN_USERNAME);
        String token = "bearer "+jwtTokenUtil.generateToken(userDetails);
        System.out.println(token);
        getHttpOKTokenRefresh("/refresh", token, MANAGER);
    }


    private AccountModel createAccount(String accountType, String username){
        AccountModel newAccount = new AccountModel();
        newAccount.setAccount_id(0);
        newAccount.setBranch_id("branch_id");
        newAccount.setEmail("something@email.com");
        newAccount.setEmployee_id(123);
        newAccount.setFirst_name("firstName");
        newAccount.setLast_name("lastName");
        newAccount.setHp("234");
        newAccount.setUsername(username);
        newAccount.setRole_id(accountType);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(KNOWN_PASSWORD);
        newAccount.setPassword(password);
        return newAccount;
    }
}
