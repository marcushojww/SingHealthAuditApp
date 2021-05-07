package com.c2g4.SingHealthWebApp.Admin.Repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;

/**
 * Repository of SQL queries to interact with the Account Table
 * @author LunarFox
 *
 */
public interface AccountRepo extends CrudRepository<AccountModel, Integer>{
    @Query("SELECT * FROM Accounts WHERE username = :username LIMIT 1")
    AccountModel findByUsername(@Param("username") String username);

    @Query("SELECT * FROM Accounts WHERE account_id = :account_id LIMIT 1")
    AccountModel findByAccId(@Param("account_id") int account_id);

    @Query("SELECT role_id FROM Accounts WHERE username= :username")
    String getRoleFromUsername(@Param("username") String username);

    @Query("SELECT * FROM Accounts")
    List<AccountModel> getAllAccounts();

    @Query("SELECT account_id FROM Accounts WHERE first_name= :first_name AND last_name=:last_name")
    int getAccIdFromNames(@Param("first_name") String first_name, @Param("last_name") String last_name);

    @Query("SELECT * FROM Accounts WHERE branch_id= :branch_id")
    List<AccountModel> getAllAccountsByBranchId(@Param("branch_id") String branch_id);

    @Query("SELECT * FROM Accounts WHERE branch_id= :branch_id AND role_id= \"Tenant\"")
    List<AccountModel> getAllTenantAccountsByBranchId(@Param("branch_id") String branch_id);

    @Query("SELECT branch_id FROM Accounts WHERE account_id=:account_id LIMIT 1")
    String getBranchIdFromAccountId(@Param("account_id") int account_id);
    
    @Modifying
    @Query("UPDATE Accounts acc SET acc.password = :password WHERE acc.account_id = :account_id")
    void changePasswordByAccId(@Param("account_id") int account_id, @Param("password") String password);

    @Modifying
    @Query("UPDATE Accounts a SET a.username = :username, a.first_name = :first_name, " +
            "a.last_name = :last_name, a.email = :email, a.hp = :hp WHERE a.account_id = :account_id")
    void changeAccountFields(@Param("account_id") int account_id, @Param("username") String username,
                             @Param("first_name") String first_name, @Param("last_name") String last_name,
                             @Param("email") String email, @Param("hp") String hp);

    @Modifying
    @Query("UPDATE Accounts a Set a.failed_login_attempts = :failed_login_attempts WHERE a.username =:username")
    void changeFailedLoginAttemptsByUsername(String username, int failed_login_attempts);

    @Modifying
    @Query("UPDATE Accounts a Set a.failed_login_attempts = :failed_login_attempts, a.is_locked =:is_locked" +
            " ,a.lock_start_datetime =:lock_start_datetime WHERE a.username =:username")
    void changeFailedLoginAndLockAttemptsByUsername(String username, int failed_login_attempts, int is_locked, Date lock_start_datetime);

}
