package com.c2g4.SingHealthWebApp.ModelsTests;

import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TenantModelTest {
    private static TenantModel tenantModel;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectNode past_audits;

    @BeforeEach
    public void beforeTests(){
        tenantModel = new TenantModel();
        past_audits = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add(1);
        arrayNode.add(0);
        past_audits.put(ResourceString.TENANT_PAST_AUDITS_JSON_KEY,arrayNode);
    }

    @Test
    public void get_past_audits_for_MySql() throws JsonProcessingException {
        tenantModel.setPast_audits(past_audits);
        String stringPastAudits = tenantModel.get_past_audits_for_MySql();
        assert(stringPastAudits.equals(objectMapper.writeValueAsString(past_audits)));
    }

    @Test
    public void get_past_audits_for_MySqlBad() throws JsonProcessingException {
        tenantModel.setPast_audits(null);
        String stringPastAudits = tenantModel.get_past_audits_for_MySql();
        System.out.println(stringPastAudits);
        assert(stringPastAudits.equals("null"));
    }

    @Test
    public void set_past_audits_for_MySql() throws JsonProcessingException {
        tenantModel.set_past_audits_for_MySql(objectMapper.writeValueAsString(past_audits));
        assert(objectMapper.writeValueAsString(tenantModel.getPast_audits()).
                equals(objectMapper.writeValueAsString(past_audits)));
    }

    @Test
    public void set_past_audits_for_MySqlBad() throws JsonProcessingException {
        tenantModel.set_past_audits_for_MySql(null);
    }
}
