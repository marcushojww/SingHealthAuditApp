package com.c2g4.SingHealthWebApp.ModelsTests;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditorModel;
import com.c2g4.SingHealthWebApp.Admin.Models.TenantModel;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.mapping.Column;

@SpringBootTest
public class AuditorModelTest {

    private static AuditorModel auditorModel;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeTests(){
        auditorModel = new AuditorModel();
    }

    @Test
    public void get_outstanding_audits_for_MySql() throws JsonProcessingException {
        ObjectNode expected = createJson(ResourceString.AUDITOR_OUTSTANDING_AUDITS_JSON_KEY);
        auditorModel.setOutstanding_audit_ids(expected);
        String actual = auditorModel.get_outstanding_audits_for_MySql();
        assert(actual.equals(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void get_outstanding_audits_for_MySqlBad() throws JsonProcessingException {
        auditorModel.setOutstanding_audit_ids(null);
        String actual = auditorModel.get_outstanding_audits_for_MySql();
        assert(actual.equals("null"));
    }

    @Test
    public void set_outstanding_audits_for_MySql() throws JsonProcessingException {
        ObjectNode expected = createJson(ResourceString.AUDITOR_OUTSTANDING_AUDITS_JSON_KEY);
        auditorModel.set_outstanding_audits_for_MySql(objectMapper.writeValueAsString(expected));
        assert(objectMapper.writeValueAsString(auditorModel.getOutstanding_audit_ids()).
                equals(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void set_outstanding_audits_for_MySqlBad() throws JsonProcessingException {
        auditorModel.set_outstanding_audits_for_MySql(null);
    }

    @Test
    public void get_completed_audits_for_MySql() throws JsonProcessingException {
        ObjectNode expected = createJson(ResourceString.AUDITOR_COMPLETED_AUDITS_JSON_KEY);
        auditorModel.setCompleted_audits(expected);
        String actual = auditorModel.get_completed_audits_for_MySql();
        assert(actual.equals(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void get_completed_audits_for_MySqlBad() throws JsonProcessingException {
        auditorModel.setCompleted_audits(null);
        String actual = auditorModel.get_completed_audits_for_MySql();
        assert(actual.equals("null"));
    }

    @Test
    public void set_completed_audits_for_MySql() throws JsonProcessingException {
        ObjectNode expected = createJson(ResourceString.AUDITOR_COMPLETED_AUDITS_JSON_KEY);
        auditorModel.set_completed_audits_for_MySql(objectMapper.writeValueAsString(expected));
        assert(objectMapper.writeValueAsString(auditorModel.getCompleted_audits()).
                equals(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void set_completed_audits_for_MySqlBad() throws JsonProcessingException {
        auditorModel.set_completed_audits_for_MySql(null);
    }


    @Test
    public void get_appealed_audits_for_MySql() throws JsonProcessingException {
        ObjectNode expected = createJson(ResourceString.AUDITOR_APPEALED_AUDITS_JSON_KEY);
        auditorModel.setAppealed_audits(expected);
        String actual = auditorModel.get_appealed_audits_for_MySql();
        assert(actual.equals(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void get_appealed_audits_for_MySqlBad() throws JsonProcessingException {
        auditorModel.setAppealed_audits(null);
        String actual = auditorModel.get_appealed_audits_for_MySql();
        assert(actual.equals("null"));
    }

    @Test
    public void set_appealed_audits_for_MySql() throws JsonProcessingException {
        ObjectNode expected = createJson(ResourceString.AUDITOR_COMPLETED_AUDITS_JSON_KEY);
        auditorModel.set_appealed_audits_for_MySql(objectMapper.writeValueAsString(expected));
        assert(objectMapper.writeValueAsString(auditorModel.getAppealed_audits()).
                equals(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void set_appealed_audits_for_MySqlBad() throws JsonProcessingException {
        auditorModel.set_appealed_audits_for_MySql(null);
    }

    private static ObjectNode createJson(String key){
        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add(1);
        arrayNode.add(0);
        objectNode.put(key,arrayNode);
        return objectNode;
    }

}
