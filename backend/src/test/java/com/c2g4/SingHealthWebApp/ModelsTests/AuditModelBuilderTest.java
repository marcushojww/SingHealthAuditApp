package com.c2g4.SingHealthWebApp.ModelsTests;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.AuditModelBuilder;
import com.c2g4.SingHealthWebApp.Admin.Models.CompletedAuditModel;
import com.c2g4.SingHealthWebApp.Admin.Models.OpenAuditModel;
import com.c2g4.SingHealthWebApp.Others.ResourceString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class AuditModelBuilderTest {

    private final String REPORTID = "REPORTID" ;
    private final String TENANTID = "TENANTID";
    private final String AUDITORID = "AUDITORID";
    private final String MANAGERID = "MANAGERID";
    //Dates
    private final String STARTDATE = "STARTDATE";
    private final String LASTUPDATEDATE = "LASTUPDATEDATE";
    private final String ENDDATE = "ENDDATE";
    //Results, Status and Data
    private final String OVERALLSCORE = "OVERALLSCORE";
    private final String OVERALLSTATUS= "OVERALLSTATUS";
    private final String OVERALLREMARKS ="OVERALLREMARKS";
    private final String REPORTTYPE = "REPORTTYPE";
    private final String REPORTDATA = "REPORTDATA";
    //Follow-up (if necessary)
    private final String NEEDTANTNT = "NEEDTANTNT";
    private final String NEEDAUDITOR = "NEEDAUDITOR";
    private final String NEEDMANAGER = "NEEDMANAGER";

    public static AuditModelBuilder auditModelBuilder;

    @BeforeEach
    public void beforeFunc(){
        auditModelBuilder = new AuditModelBuilder();
    }

    @Test
    public void createEmptyAuditBuilder(){
        assertAuditModelBuilder(new HashMap<String,String>());
    }

    @Test
    public void setUserIDs(){
        auditModelBuilder.setUserIDs(1,2,3);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(TENANTID,"1");
        editedVals.put(AUDITORID,"2");
        editedVals.put(MANAGERID,"3");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeed(){
        auditModelBuilder.setNeed(1,1,0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(NEEDTANTNT,"1");
        editedVals.put(NEEDAUDITOR,"1");
        editedVals.put(NEEDMANAGER,"0");
        assertAuditModelBuilder(editedVals);
    }
    @Test
    public void setTypeIsOpenAudit() {
        auditModelBuilder.setTypeIsOpenAudit();
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLSTATUS,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setTypeIsCompletedAudit() {
        auditModelBuilder.setTypeIsCompletedAudit();
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLSTATUS,"1");
        assertAuditModelBuilder(editedVals);
    }
    @Test
    public void setReportId() {
        auditModelBuilder.setReportId(1);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(REPORTID,"1");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeedTenant() {
        auditModelBuilder.setNeedTenant(1);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(NEEDTANTNT,"1");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeedAuditor() {
        HashMap<String,String> editedVals = new HashMap<>();
        auditModelBuilder.setNeedAuditor(1);
        editedVals.put(NEEDAUDITOR,"1");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setNeedManager() {
        auditModelBuilder.setNeedManager(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(NEEDMANAGER,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setTenantId() {
        auditModelBuilder.setTenantId(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(TENANTID,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setAuditorId() {
        auditModelBuilder.setAuditorId(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(AUDITORID,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setManagerId() {
        auditModelBuilder.setManagerId(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(MANAGERID,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReport_typeFB() {
        auditModelBuilder.setReport_type(ResourceString.FB_KEY);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(REPORTTYPE,ResourceString.FB_KEY);
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReport_typeCompleted() {
        auditModelBuilder.setReport_type(ResourceString.NFB_KEY);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(REPORTTYPE,ResourceString.NFB_KEY);
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setOverallRemarks() {
        auditModelBuilder.setOverallRemarks("REMARKS");
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLREMARKS,"REMARKS");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setOverallScore() {
        auditModelBuilder.setOverallScore(0);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(OVERALLSCORE,"0");
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setReportDataString() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key","value");
        HashMap<String,String> editedVals = new HashMap<>();
        try {
            String nodeString = objectMapper.writeValueAsString(node);
            auditModelBuilder.setReportData(nodeString);
            editedVals.put(REPORTDATA,nodeString);
            assertAuditModelBuilder(editedVals);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void setReportDataJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key","value");
        HashMap<String,String> editedVals = new HashMap<>();
        auditModelBuilder.setReportData(node);
        try {
            editedVals.put(REPORTDATA,objectMapper.writeValueAsString(node));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setStartDate() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        auditModelBuilder.setStartDate(date);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(STARTDATE,date.toString());
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setEnd_date() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        auditModelBuilder.setEnd_date(date);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(ENDDATE,date.toString());
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void setLastUpdateDate() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        auditModelBuilder.setLastUpdateDate(date);
        HashMap<String,String> editedVals = new HashMap<>();
        editedVals.put(LASTUPDATEDATE,date.toString());
        assertAuditModelBuilder(editedVals);
    }

    @Test
    public void getStart_date() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        auditModelBuilder.setStartDate(date);
        assert (auditModelBuilder.getStart_date().equals(date));
    }

    @Test
    public void getLastUpdateDate() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        auditModelBuilder.setLastUpdateDate(date);
        assert (auditModelBuilder.getLastUpdateDate().equals(date));
    }

    @Test
    public void getEnd_date() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        auditModelBuilder.setEnd_date(date);
        assert (auditModelBuilder.getEnd_date().equals(date));
    }

    @Test
    public void getOverallRemarks() {
        String remarks = "REMARKS";
        auditModelBuilder.setOverallRemarks(remarks);
        assert (auditModelBuilder.getOverallRemarks().equals(remarks));
    }
    @Test
    public void getReport_type() {
        auditModelBuilder.setReport_type(ResourceString.FB_KEY);
        assert (auditModelBuilder.getReport_type().equals(ResourceString.FB_KEY));
    }

    @Test
    public void getOverallScore() {
        int score = 12;
        auditModelBuilder.setOverallScore(score);
        assert (auditModelBuilder.getOverallScore()==score);
    }

    @Test
    public void getTenantId() {
        int id = 1;
        auditModelBuilder.setTenantId(id);
        assert (auditModelBuilder.getTenantId()==id);
    }
    @Test
    public void getAuditorId() {
        int id = 1;
        auditModelBuilder.setAuditorId(id);
        assert (auditModelBuilder.getAuditorId()==id);
    }

    @Test
    public void getManagerId() {
        int id = 1;
        auditModelBuilder.setManagerId(id);
        assert (auditModelBuilder.getManagerId()==id);
    }

    @Test
    public void getReportData() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key","value");
        HashMap<String,String> editedVals = new HashMap<>();

        auditModelBuilder.setReportData(node);
        assert(auditModelBuilder.getReportData().has("key"));
        assert(auditModelBuilder.getReportData().get("key") == node.get("key"));
    }

    @Test
    public void getReportId() {
        int reportID = 1;
        auditModelBuilder.setReportId(reportID);
        assert (auditModelBuilder.getReportId()==reportID);
    }

    @Test
    public void getNeedManager(){
        int need = 1;
        auditModelBuilder.setNeedManager(need);
        assert (auditModelBuilder.getNeedManager()==need);
    }

    @Test
    public void getNeedAuditor(){
        int need = 1;
        auditModelBuilder.setNeedAuditor(need);
        assert (auditModelBuilder.getNeedAuditor()==need);
    }

    @Test
    public void getNeedTenant(){
        int need = 1;
        auditModelBuilder.setNeedTenant(need);
        assert (auditModelBuilder.getNeedTenant()==need);
    }

    @Test
    public void getReportStatusOpen() {
        auditModelBuilder.setTypeIsOpenAudit();
        assert (auditModelBuilder.getReportStatus().equals("Open Audit"));
    }

    @Test
    public void getReportStatusClosed() {
        auditModelBuilder.setTypeIsCompletedAudit();
        assert (auditModelBuilder.getReportStatus().equals("Completed Audit"));
    }

    @Test
    public void buildNoReportId(){
        try {
            setAuditReportBuilder();
            auditModelBuilder.setReportId(-1);
            auditModelBuilder.build();

        }catch (IllegalArgumentException e){
            System.out.println("PASS");
            return;
        }
        fail();
    }

    @Test
    public void buildNoTenantId(){
        try {
            setAuditReportBuilder();
            auditModelBuilder.setTenantId(-1);
            auditModelBuilder.build();

        }catch (IllegalArgumentException e){
            System.out.println("PASS");
            return;
        }
        fail();
    }

    @Test
    public void buildNoAuditorId(){
        try {
            setAuditReportBuilder();
            auditModelBuilder.setAuditorId(-1);
            auditModelBuilder.build();

        }catch (IllegalArgumentException e){
            System.out.println("PASS");
            return;
        }
        fail();
    }

    @Test
    public void buildNoScore(){
        try {
            setAuditReportBuilder();
            auditModelBuilder.setOverallScore(-1);
            auditModelBuilder.build();

        }catch (IllegalArgumentException e){
            System.out.println("PASS");
            return;
        }
        fail();
    }

    @Test
    public void buildNoReportType(){
        try {
            setAuditReportBuilder();
            auditModelBuilder.setReport_type("-1");
            auditModelBuilder.build();

        }catch (IllegalArgumentException e){
            System.out.println("PASS");
            return;
        }
        fail();
    }

    @Test
    public void buildNoOverallstatus0NoNeed(){
        try {
            setAuditReportBuilder();
            auditModelBuilder.setTypeIsOpenAudit();
            auditModelBuilder.setNeed(0,0,0);
            auditModelBuilder.build();
        }catch (IllegalArgumentException e){
            System.out.println("PASS");
            return;
        }
        fail();
    }
    @Test
    public void buildOpenOK(){
        setAuditReportBuilder();
        auditModelBuilder.setTypeIsOpenAudit();
        auditModelBuilder.setNeed(1,1,0);
        assert(auditModelBuilder.build().getClass()==OpenAuditModel.class);
    }


    @Test
    public void buildCompletedOK(){
        setAuditReportBuilder();
        auditModelBuilder.setTypeIsCompletedAudit();
        assert(auditModelBuilder.build().getClass()==CompletedAuditModel.class);
    }

    private void assertAuditModelBuilder(HashMap<String,String> editedValues) {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> defaultValues = new HashMap<>(){{
            put(REPORTID,"-1");
            put(TENANTID,"-1");
            put(AUDITORID,"-1");
            put(MANAGERID,"-1");
            put(OVERALLSCORE,"-1");
            put(OVERALLREMARKS,"Nil");
            put(OVERALLSTATUS,"-1");
            put(REPORTTYPE,"-1");
            put(REPORTDATA,null);
            put(STARTDATE,null);
            put(LASTUPDATEDATE,null);
            put(ENDDATE,null);
            put(NEEDTANTNT,"0");
            put(NEEDAUDITOR,"0");
            put(NEEDMANAGER,"0");
        }};

        for(String editedKey: editedValues.keySet()){
            defaultValues.put(editedKey,editedValues.get(editedKey));
        }

        assert(auditModelBuilder.getReportId()==Integer.parseInt(defaultValues.get(REPORTID)));
        assert(auditModelBuilder.getTenantId()==Integer.parseInt(defaultValues.get(TENANTID)));
        assert(auditModelBuilder.getAuditorId()==Integer.parseInt(defaultValues.get(AUDITORID)));
        assert(auditModelBuilder.getManagerId()==Integer.parseInt(defaultValues.get(MANAGERID)));
        assert(auditModelBuilder.getOverallRemarks().equals(defaultValues.get(OVERALLREMARKS)));
        assert(auditModelBuilder.getReport_type().equals(defaultValues.get(REPORTTYPE)));
        assert(auditModelBuilder.getOverallScore()==Integer.parseInt(defaultValues.get(OVERALLSCORE)));
        if(defaultValues.get(REPORTDATA)==null) {
            assert (auditModelBuilder.getReportData() == null);
        } else {
            try {
                assert (objectMapper.writeValueAsString(auditModelBuilder.getReportData()).equals(defaultValues.get(REPORTDATA)));
            } catch (JsonProcessingException e){
                fail();
            }
        }
        assert(auditModelBuilder.getNeedTenant()==Integer.parseInt(defaultValues.get(NEEDTANTNT)));
        assert(auditModelBuilder.getNeedAuditor()==Integer.parseInt(defaultValues.get(NEEDAUDITOR)));
        assert(auditModelBuilder.getNeedManager()==Integer.parseInt(defaultValues.get(NEEDMANAGER)));

        if(editedValues.containsKey(STARTDATE)){
            assert (auditModelBuilder.getStart_date().toString().equals(defaultValues.get(STARTDATE)));
        }
        if(editedValues.containsKey(ENDDATE)){
            assert (auditModelBuilder.getEnd_date().toString().equals(defaultValues.get(ENDDATE)));
        }
        if(editedValues.containsKey(LASTUPDATEDATE)){
            assert (auditModelBuilder.getLastUpdateDate().toString().equals(defaultValues.get(LASTUPDATEDATE)));
        }
        if(editedValues.containsKey(OVERALLSTATUS)){
            switch(Integer.parseInt(editedValues.get(OVERALLSTATUS))) {
                case 0:
                    assert(auditModelBuilder.getReportStatus().equals("Open Audit"));
                    break;
                case 1:
                    assert(auditModelBuilder.getReportStatus().equals("Completed Audit"));
                    break;
            }

        }
    }

    public void setAuditReportBuilder(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode dataNode = objectMapper.createObjectNode();
        dataNode.put("key","value");

        auditModelBuilder.setReportId(0);
        auditModelBuilder.setNeed(0,0,0);
        auditModelBuilder.setTenantId(0);
        auditModelBuilder.setAuditorId(0);
        auditModelBuilder.setManagerId(0);
        auditModelBuilder.setOverallScore(0);
        auditModelBuilder.setReport_type(ResourceString.FB_KEY);

        auditModelBuilder.setReportData(dataNode);
    }

}