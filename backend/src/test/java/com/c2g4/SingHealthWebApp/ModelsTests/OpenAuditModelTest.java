package com.c2g4.SingHealthWebApp.ModelsTests;

import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class OpenAuditModelTest {

    private static OpenAuditModel openAuditModel;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectNode report_data;

    @BeforeEach
    public void beforeTests(){
        openAuditModel = new OpenAuditModel();
        report_data = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add(1);
        arrayNode.add(0);
        report_data.put("entries",arrayNode);
    }

    @Test
    public void getReport_data_for_MySql() throws JsonProcessingException {
        openAuditModel.setReport_data(report_data);
        String stringRe = openAuditModel.getReport_data_for_MySql();
        assert(stringRe.equals(objectMapper.writeValueAsString(report_data)));
    }

    @Test
    public void getReport_data_for_MySqlBad(){
        openAuditModel.setReport_data(null);
        String stringPastAudits = openAuditModel.getReport_data_for_MySql();
        System.out.println(stringPastAudits);
        assert(stringPastAudits.equals("null"));
    }

    @Test
    public void setReport_data_for_MySql() throws JsonProcessingException {
        openAuditModel.setReport_data_for_MySql(objectMapper.writeValueAsString(report_data));
        assert(objectMapper.writeValueAsString(openAuditModel.getReport_data()).
                equals(objectMapper.writeValueAsString(report_data)));
    }

    @Test
    public void setReport_data_for_MySqlBad() {
        try {
            openAuditModel.setReport_data_for_MySql(null);
        }catch (IllegalArgumentException e){
            return;
        }
        fail();
    }
}
