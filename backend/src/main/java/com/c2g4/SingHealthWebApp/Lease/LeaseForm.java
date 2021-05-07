package com.c2g4.SingHealthWebApp.Lease;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("LeaseForm")
public class LeaseForm {
    @Id
    private int lease_form_id;
    private int tenant_id;
    private int manager_id;
    private String OTHERLEASEINFORMATION;
    private String approval_status;
}
