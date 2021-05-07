package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.*;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class AccountRepoTest {
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

    private static final int ACCOUNT_ID = 9000;
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String BRANCHID = "CGH";

    @BeforeEach
    public void clearRepo(){
        CommonRepoTestFunctions.clearAllTables(accountRepo, managerRepo,
                auditorRepo, tenantRepo, completedAuditRepo, openAuditRepo);
    }


    @Test
    public void findByUsername() {
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        AccountModel actualModel = accountRepo.findByUsername(USERNAME);
        assert (accountIdentical(expectedAccount,actualModel));
    }

    @Test
    public void findByUsernameNotFound(){
        AccountModel actualModel = accountRepo.findByUsername(USERNAME);
        assert(actualModel==null);
    }

    @Test
    public void findByUsernameNullUsername(){
        AccountModel actualModel = accountRepo.findByUsername(null);
        assert(actualModel==null);
    }

    @Test
    public void findByAccId() {
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        AccountModel actualModel = accountRepo.findByAccId(expectedAccount.getAccount_id());
        assert (accountIdentical(expectedAccount,actualModel));
    }

    @Test
    public void findByAccIdNotFound(){
        AccountModel actualModel = accountRepo.findByAccId(ACCOUNT_ID);
        assert(actualModel==null);
    }

    @Test
    public void getRoleFromUsername() {
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        String role = accountRepo.getRoleFromUsername(USERNAME);
        System.out.println(role);
        System.out.println(expectedAccount.getUsername());
        assert(role.equals(expectedAccount.getRole_id()));
    }

    @Test
    public void getRoleFromUsernameNotFound(){
        String role = accountRepo.getRoleFromUsername(USERNAME);
        assert (role == null);
    }

    @Test
    public void getRoleFromUsernameNULL(){
        String role = accountRepo.getRoleFromUsername(null);
        assert (role == null);
    }

    @Test
    public void getAllAccounts() {
        List<AccountModel> expectedAccounts = new ArrayList<>();
        for(int i=0;i<4;i++){
            AccountModel expectedAccount = CommonRepoTestFunctions.createAccount(i, USERNAME+i,"email"+i,String.valueOf(i));
            expectedAccount = accountRepo.save(expectedAccount);
            expectedAccounts.add(expectedAccount);
        }
        List<AccountModel> actualModels = accountRepo.getAllAccounts();
        assert (actualModels.size()==expectedAccounts.size());
        for(int i = 0;i<actualModels.size();i++) {
            assert (accountIdentical(expectedAccounts.get(i),actualModels.get(i)));
        }
    }

    @Test
    public void getAllAccountsNoAccounts(){
        List<AccountModel> actualModels = accountRepo.getAllAccounts();
        assert(actualModels.size()==0);
    }

    @Test
    public void getAccIdFromNames() {
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        int acc_id = accountRepo.getAccIdFromNames(FIRSTNAME,LASTNAME);
        assert (acc_id == expectedAccount.getAccount_id());
    }

    @Test
    public void getAccIdFromNamesNotFound(){
        try {
            int acc_id = accountRepo.getAccIdFromNames(FIRSTNAME, LASTNAME);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    @Test
    public void getAccIdFromNamesFNameNull(){
        try {
            int acc_id = accountRepo.getAccIdFromNames(null, LASTNAME);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    @Test
    public void getAccIdFromNamesLNameNull(){
        try {
            int acc_id = accountRepo.getAccIdFromNames(FIRSTNAME, null);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    @Test
    public void getAllAccountsByBranchId() {
        List<AccountModel> expectedAccounts = new ArrayList<>();
        for(int i=0;i<4;i++){
            AccountModel expectedAccount = CommonRepoTestFunctions.createAccount(i, USERNAME+i,"email"+i,String.valueOf(i));
            expectedAccount = accountRepo.save(expectedAccount);
            expectedAccounts.add(expectedAccount);
        }
        List<AccountModel> actualModels = accountRepo.getAllAccountsByBranchId(BRANCHID);
        assert (actualModels.size()==expectedAccounts.size());
        for(int i = 0;i<actualModels.size();i++) {
            assert (accountIdentical(expectedAccounts.get(i),actualModels.get(i)));
        }
    }

    @Test
    public void getAllAccountsByBranchIdNotFound() {
        List<AccountModel> actualModels = accountRepo.getAllAccountsByBranchId(BRANCHID);
        assert (actualModels.size()==0);
    }
    @Test
    public void getAllAccountsByBranchIdNull() {
        List<AccountModel> actualModels = accountRepo.getAllAccountsByBranchId(null);
        assert (actualModels.size()==0);
    }

    @Test
    public void getAllTenantAccountsByBranchId() {
        List<AccountModel> tenantAccounts = new ArrayList<>();
        for(int i=0;i<4;i++){
            AccountModel expectedAccount = CommonRepoTestFunctions.createAccount(i, USERNAME+i,"email"+i,String.valueOf(i),ResourceString.AUDITOR_ROLE_KEY);
            accountRepo.save(expectedAccount);
        }
        for(int i=6;i<9;i++){
            AccountModel expectedAccount = CommonRepoTestFunctions.createAccount(i, USERNAME+i,"email"+i,String.valueOf(i),ResourceString.TENANT_ROLE_KEY);
            expectedAccount = accountRepo.save(expectedAccount);
            tenantAccounts.add(expectedAccount);
        }
        List<AccountModel> actualModels = accountRepo.getAllTenantAccountsByBranchId(BRANCHID);
        assert (actualModels.size()==tenantAccounts.size());
        for(int i = 0;i<actualModels.size();i++) {
            assert (accountIdentical(tenantAccounts.get(i),actualModels.get(i)));
        }
    }

    @Test
    public void getAllTenantAccountsByBranchIdNotFound() {
        for(int i=0;i<4;i++){
            AccountModel expectedAccount = CommonRepoTestFunctions.createAccount(i, USERNAME+i,"email"+i,String.valueOf(i),ResourceString.AUDITOR_ROLE_KEY);
            accountRepo.save(expectedAccount);
        }
        List<AccountModel> actualModels = accountRepo.getAllTenantAccountsByBranchId(BRANCHID);
        assert (actualModels.size()==0);
    }
    @Test
    public void getAllTenantAccountsByBranchIdNULl() {
        for(int i=0;i<4;i++){
            AccountModel expectedAccount = CommonRepoTestFunctions.createAccount(i, USERNAME+i,"email"+i,String.valueOf(i),ResourceString.AUDITOR_ROLE_KEY);
            accountRepo.save(expectedAccount);
        }
        List<AccountModel> actualModels = accountRepo.getAllTenantAccountsByBranchId(null);
        assert (actualModels.size()==0);
    }

    @Test
    public void changePasswordByAccId() {
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        accountRepo.changePasswordByAccId(expectedAccount.getAccount_id(),"newPassword");
        AccountModel retrievedModel = accountRepo.findByAccId(expectedAccount.getAccount_id());
        assert (retrievedModel.getPassword().equals("newPassword"));
    }

    @Test
    public void changePasswordByAccIdPasswordNull() {
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        try {
            accountRepo.changePasswordByAccId(expectedAccount.getAccount_id(), null);
        } catch (DataIntegrityViolationException e){
            return;
        }
        fail();
    }

    @Test
    public void changePasswordByAccIdAccountNotFound() {
        accountRepo.changePasswordByAccId(-10,"new_password");
    }

    @Test
    public void changeAccountFields() {
        String newU = "newUsername";
        String newFN = "newFirstName";
        String newLN = "newLastName";
        String newE = "newEmail";
        String newHp ="newHP";
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        accountRepo.changeAccountFields(expectedAccount.getAccount_id(),newU,newFN,newLN,newE,newHp);
        AccountModel retrievedModel = accountRepo.findByAccId(expectedAccount.getAccount_id());
        assert(retrievedModel.getUsername().equals(newU));
        assert(retrievedModel.getFirst_name().equals(newFN));
        assert(retrievedModel.getLast_name().equals(newLN));
        assert(retrievedModel.getEmail().equals(newE));
        assert(retrievedModel.getHp().equals(newHp));
    }

    @Test
    public void changeAccountFieldsNullUsername() {
        String newU = null;
        String newFN = "newFirstName";
        String newLN = "newLastName";
        String newE = "newEmail";
        String newHp ="newHP";
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        try {
            accountRepo.changeAccountFields(expectedAccount.getAccount_id(),newU,newFN,newLN,newE,newHp);
        } catch (DataIntegrityViolationException e){
            return;
        }
        fail();
    }
    @Test
    public void changeAccountFieldsNullFN() {
        String newU = "newUsername";
        String newFN = null;
        String newLN = "newLastName";
        String newE = "newEmail";
        String newHp ="newHP";
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        try {
            accountRepo.changeAccountFields(expectedAccount.getAccount_id(),newU,newFN,newLN,newE,newHp);
        } catch (DataIntegrityViolationException e){
            return;
        }
        fail();
    }
    @Test
    public void changeAccountFieldsNullLN() {
        String newU = "newUsername";
        String newFN = "newFirstName";
        String newLN = null;
        String newE = "newEmail";
        String newHp ="newHP";
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        try {
            accountRepo.changeAccountFields(expectedAccount.getAccount_id(),newU,newFN,newLN,newE,newHp);
        } catch (DataIntegrityViolationException e){
            return;
        }
        fail();
    }
    @Test
    public void changeAccountFieldsNullEmail() {
        String newU = "newUsername";
        String newFN = "newFirstName";
        String newLN = "newLastName";
        String newE = null;
        String newHp ="newHP";
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        try {
            accountRepo.changeAccountFields(expectedAccount.getAccount_id(),newU,newFN,newLN,newE,newHp);
        } catch (DataIntegrityViolationException e){
            return;
        }
        fail();
    }
    @Test
    public void changeAccountFieldsNullHP() {
        String newU = "newUsername";
        String newFN = "newFirstName";
        String newLN = "newLastName";
        String newE = "newEmail";
        String newHp = null;
        AccountModel expectedAccount = CommonRepoTestFunctions.createAccount();
        expectedAccount = accountRepo.save(expectedAccount);
        try {
            accountRepo.changeAccountFields(expectedAccount.getAccount_id(),newU,newFN,newLN,newE,newHp);
        } catch (DataIntegrityViolationException e){
            return;
        }
        fail();
    }

    @Test
    public void changeAccountFieldsAccountNotFound() {
        accountRepo.changePasswordByAccId(-10,"new_password");
    }


    private boolean accountIdentical(AccountModel accountModel1,AccountModel accountModel2 ){
        boolean acc_id = accountModel1.getAccount_id() == accountModel2.getAccount_id();
        boolean employee_id = accountModel1.getEmployee_id() == accountModel2.getEmployee_id();
        boolean username = accountModel1.getUsername().equals(accountModel2.getUsername());
        boolean password = accountModel1.getPassword().equals(accountModel2.getPassword());
        boolean firstname = accountModel1.getFirst_name().equals(accountModel2.getFirst_name());
        boolean lastname = accountModel1.getLast_name().equals(accountModel2.getLast_name());
        boolean email = accountModel1.getEmail().equals(accountModel2.getEmail());
        boolean hp = accountModel1.getHp().equals(accountModel2.getHp());
        boolean role_id = accountModel1.getRole_id().equals(accountModel2.getRole_id());
        boolean branch_id = accountModel1.getBranch_id().equals(accountModel2.getBranch_id());
        return acc_id && employee_id && username && password && firstname && lastname && email && hp && role_id && branch_id;
    }

}
