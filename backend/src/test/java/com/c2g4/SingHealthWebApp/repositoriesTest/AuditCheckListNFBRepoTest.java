package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListNFBModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListNFBRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Transactional
@SpringBootTest
@AutoConfigureJdbc
public class AuditCheckListNFBRepoTest {
    @Autowired
    private AuditCheckListNFBRepo auditCheckListNFBRepo;

    private static final int ACCOUNT_ID = 9000;
    private static final String CATEGORY = "CATEGORY";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String BRANCHID = "CGH";

    @BeforeEach
    public void clearRepo(){
        auditCheckListNFBRepo.deleteAll();
    }

    @Test
    public void getAllQuestions(){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = createAndSaveList();
        List<AuditCheckListNFBModel> actualList = auditCheckListNFBRepo.getAllQuestions();
        assert(AuditCheckListNFBModels.size()==actualList.size());
        for(int i=0;i<AuditCheckListNFBModels.size();i++){
            assert(compareQuestions(AuditCheckListNFBModels.get(i),actualList.get(i)));
        }
    }

    @Test
    public void getAllQuestionsNoQuestions(){
        List<AuditCheckListNFBModel> actualList = auditCheckListNFBRepo.getAllQuestions();
        assert(actualList.size()==0);
    }

    @Test
    public void getQuestion(){
        List<AuditCheckListNFBModel> expectedModels = createAndSaveList();
        AuditCheckListNFBModel actual = auditCheckListNFBRepo.getQuestion(expectedModels.get(0).getNfb_qn_id());
        assert(compareQuestions(expectedModels.get(0),actual));
    }

    @Test
    public void getQuestionNotFound(){
        AuditCheckListNFBModel actual = auditCheckListNFBRepo.getQuestion(-10);
        assert(actual ==null);
    }

    @Test
    public void getQuestionByCategory(){
        String thisCat = "thisCat";
        createAndSaveList();
        List<AuditCheckListNFBModel> AuditCheckListNFBModelsThisCat = createAndSaveList(thisCat);
        System.out.println(AuditCheckListNFBModelsThisCat.size());
        System.out.println(AuditCheckListNFBModelsThisCat.get(0).getNfb_qn_id());
        List<AuditCheckListNFBModel> actualList = auditCheckListNFBRepo.getQuestionByCategory(thisCat);
        assert(AuditCheckListNFBModelsThisCat.size()==actualList.size());
        for (AuditCheckListNFBModel AuditCheckListNFBModel : actualList) {
            assert (compareQuestions(AuditCheckListNFBModel, AuditCheckListNFBModel));
        }
    }

    @Test
    public void getQuestionByCategoryNoQuestions(){
        List<AuditCheckListNFBModel> actualList = auditCheckListNFBRepo.getQuestionByCategory(CATEGORY);
        assert(actualList.size()==0);
    }

    @Test
    public void getQuestionByCategoryNULLCat(){
        List<AuditCheckListNFBModel> actualList = auditCheckListNFBRepo.getQuestionByCategory(null);
        assert(actualList.size()==0);
    }

    @Test
    public void getCategoryByQnID(){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = createAndSaveList();
        String actual = auditCheckListNFBRepo.getCategoryByQnID(AuditCheckListNFBModels.get(0).getNfb_qn_id());
        assert(actual.equals(AuditCheckListNFBModels.get(0).getCategory()));
    }

    @Test
    public void getCategoryByQnIDNotFound(){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = createAndSaveList();
        String actual = auditCheckListNFBRepo.getCategoryByQnID(-10);
        assert(actual==null);
    }

    @Test
    public void getWeightByQnID(){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = createAndSaveList();
        double actual = auditCheckListNFBRepo.getWeightByQnID(AuditCheckListNFBModels.get(0).getNfb_qn_id());
        assert(actual == AuditCheckListNFBModels.get(0).getWeight());
    }

    @Test
    public void getWeightByQnIDNotFound(){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = createAndSaveList();
        try {
            double actual = auditCheckListNFBRepo.getWeightByQnID(-10);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    private boolean compareQuestions(AuditCheckListNFBModel expected, AuditCheckListNFBModel actual){
        boolean id = expected.getNfb_qn_id()==actual.getNfb_qn_id();
        boolean cat = expected.getCategory().equals(actual.getCategory());
        boolean subcat = expected.getSub_category().equals(actual.getSub_category());
        boolean weight = expected.getWeight() == actual.getWeight();
        boolean req = expected.getRequirement().equals(actual.getRequirement());
        boolean subreq = expected.getSub_requirement().equals(actual.getSub_requirement());
        return id && cat && subcat && weight && req && subreq;
    }

    private List<AuditCheckListNFBModel> createAuditChecklistList(String category){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = new ArrayList<>();
        for(int i=0;i<3;i++){
            AuditCheckListNFBModels.add(createChecklist(category));
        }
        return AuditCheckListNFBModels;
    }
    private List<AuditCheckListNFBModel> createAndSaveList(){
        return  createAndSaveList(CATEGORY);
    }
    private List<AuditCheckListNFBModel> createAndSaveList(String category){
        List<AuditCheckListNFBModel> AuditCheckListNFBModels = createAuditChecklistList(category);
        List<AuditCheckListNFBModel> AuditCheckListNFBModelsUpdated = new ArrayList<>();
        for (AuditCheckListNFBModel AuditCheckListNFBModel : AuditCheckListNFBModels) {
            AuditCheckListNFBModelsUpdated.add(auditCheckListNFBRepo.save(AuditCheckListNFBModel));
        }
        return AuditCheckListNFBModelsUpdated;
    }

    private AuditCheckListNFBModel createChecklist(String category){
        AuditCheckListNFBModel AuditCheckListNFBModel = new AuditCheckListNFBModel();
        AuditCheckListNFBModel.setNfb_qn_id(0);
        AuditCheckListNFBModel.setCategory(category);
        AuditCheckListNFBModel.setSub_category(category);
        AuditCheckListNFBModel.setWeight(0);
        AuditCheckListNFBModel.setRequirement("REQUIREMENT");
        AuditCheckListNFBModel.setSub_requirement("REQUIREMENT");
        return AuditCheckListNFBModel;
    }
}
