package com.c2g4.SingHealthWebApp.Admin.Controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.c2g4.SingHealthWebApp.Admin.Models.AccountModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AccountRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditorRepo;
import com.c2g4.SingHealthWebApp.Admin.Repositories.TenantRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin("http://localhost:3000")
@RestController
public class AuditorController_OLD {
	
    @Autowired
    private TenantRepo tenantRepo;
    @Autowired
    private AuditorRepo auditorRepo;
    @Autowired
    private AccountRepo accountRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    //Return all the tenants of an auditor as a JSON arrary
    @GetMapping("/a/alltenants")
    public ResponseEntity<?> getTenantForAuditor(@AuthenticationPrincipal UserDetails auditorUser) {

        AccountModel auditorAccount = accountRepo.findByUsername(auditorUser.getUsername());
        if(auditorAccount==null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        int auditorId = auditorAccount.getAccount_id();
        logger.info("found auditor id {}", auditorId);
        String branchId = auditorAccount.getBranch_id();//auditorRepository.getBranchIDfromAuditorID(auditorId);
        logger.info("found branch id {}", branchId);
        //branch id could be null

        List<TenantModel> tenants = tenantRepo.getAllTenantsByBranchId(branchId);
        if(tenants==null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);	
        } 
        logger.info("found tenant id {}", tenants.get(0).getAcc_id());
        
        ObjectMapper objectmapper = new ObjectMapper();
        String tenantsAsJSONString = null;
		try {
			tenantsAsJSONString = objectmapper.writeValueAsString(tenants);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return ResponseEntity.ok(tenantsAsJSONString);
    }
    
    //Return the information of a tenant as a JSON array
    @GetMapping("/a/tenant/{tenantId}")
    public ResponseEntity<?> geTenantInfo(@PathVariable("tenantId") int tenantId) {
        TenantModel tenant = tenantRepo.getTenantById(tenantId);
        if(tenant==null) {
            logger.warn("tenant with id {} not found",tenantId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else{
            ObjectMapper objectmapper = new ObjectMapper();
            String tenantInfoAsJSONString = null;
			try {
				tenantInfoAsJSONString = objectmapper.writeValueAsString(tenant);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
            return ResponseEntity.ok(tenantInfoAsJSONString);
        }

    }
}
