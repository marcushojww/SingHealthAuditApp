package com.c2g4.SingHealthWebApp.Admin.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.OpenAuditRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;


@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class TenantController_OLD {

	
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private OpenAuditRepo openAuditRepo;

    //This method confuses me, why is it returning openAudits when the mapping is to pastaudits? - Jia Wei
    //TODO: make this for all audits
    @GetMapping("/t/view/pastaudits")
    public ResponseEntity<?> auditorHome(@AuthenticationPrincipal UserDetails tenantUser) {
        AccountModel tenantAccount = accountRepo.findByUsername(tenantUser.getUsername());
        int tenantId = tenantAccount.getAccount_id();
        //String tenantAudits = tenantRepository.getPastAuditById(tenantId);
        int openAuditId = tenantRepo.getOpenAuditById(tenantId);
        OpenAuditModel openAudit = openAuditRepo.getOpenAuditById(openAuditId);
        boolean hasAudit = true;

        Map<String, Object> response = new HashMap<>();

        if(openAudit==null){
            response.put("openAudits","no audits");

            return ResponseEntity.ok(response);
        }
        List<OpenAuditModel> openAuditsList = new ArrayList<>();
        openAuditsList.add(openAudit);
        response.put("openAudits",openAuditsList);
        return ResponseEntity.ok(response);
    }
}
