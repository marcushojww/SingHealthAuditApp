package com.c2g4.SingHealthWebApp.repositoriesTest;

import com.c2g4.SingHealthWebApp.Admin.Models.AuditCheckListSMAModel;
import com.c2g4.SingHealthWebApp.Admin.Repositories.AuditCheckListSMARepo;
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
public class AuditCheckListSMATest {
    @Autowired
    private AuditCheckListSMARepo auditCheckListSMARepo;

    private static final int ACCOUNT_ID = 9000;
    private static final String CATEGORY = "CATEGORY";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String BRANCHID = "CGH";

    @BeforeEach
    public void clearRepo(){
        auditCheckListSMARepo.deleteAll();
    }

    @Test
    public void getAllQuestions(){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = createAndSaveList();
        List<AuditCheckListSMAModel> actualList = auditCheckListSMARepo.getAllQuestions();
        assert(AuditCheckListSMAModels.size()==actualList.size());
        for(int i=0;i<AuditCheckListSMAModels.size();i++){
            assert(compareQuestions(AuditCheckListSMAModels.get(i),actualList.get(i)));
        }
    }

    @Test
    public void getAllQuestionsNoQuestions(){
        List<AuditCheckListSMAModel> actualList = auditCheckListSMARepo.getAllQuestions();
        assert(actualList.size()==0);
    }

    @Test
    public void getQuestion(){
        List<AuditCheckListSMAModel> expectedModels = createAndSaveList();
        AuditCheckListSMAModel actual = auditCheckListSMARepo.getQuestion(expectedModels.get(0).getSma_qn_id());
        assert(compareQuestions(expectedModels.get(0),actual));
    }

    @Test
    public void getQuestionNotFound(){
        AuditCheckListSMAModel actual = auditCheckListSMARepo.getQuestion(-10);
        assert(actual ==null);
    }

    @Test
    public void getQuestionByCategory(){
        String thisCat = "thisCat";
        createAndSaveList();
        List<AuditCheckListSMAModel> AuditCheckListSMAModelsThisCat = createAndSaveList(thisCat);
        System.out.println(AuditCheckListSMAModelsThisCat.size());
        System.out.println(AuditCheckListSMAModelsThisCat.get(0).getSma_qn_id());
        List<AuditCheckListSMAModel> actualList = auditCheckListSMARepo.getQuestionByCategory(thisCat);
        assert(AuditCheckListSMAModelsThisCat.size()==actualList.size());
        for (AuditCheckListSMAModel AuditCheckListSMAModel : actualList) {
            assert (compareQuestions(AuditCheckListSMAModel, AuditCheckListSMAModel));
        }
    }

    @Test
    public void getQuestionByCategoryNoQuestions(){
        List<AuditCheckListSMAModel> actualList = auditCheckListSMARepo.getQuestionByCategory(CATEGORY);
        assert(actualList.size()==0);
    }

    @Test
    public void getQuestionByCategoryNULLCat(){
        List<AuditCheckListSMAModel> actualList = auditCheckListSMARepo.getQuestionByCategory(null);
        assert(actualList.size()==0);
    }

    @Test
    public void getCategoryByQnID(){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = createAndSaveList();
        String actual = auditCheckListSMARepo.getCategoryByQnID(AuditCheckListSMAModels.get(0).getSma_qn_id());
        assert(actual.equals(AuditCheckListSMAModels.get(0).getCategory()));
    }

    @Test
    public void getCategoryByQnIDNotFound(){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = createAndSaveList();
        String actual = auditCheckListSMARepo.getCategoryByQnID(-10);
        assert(actual==null);
    }

    @Test
    public void getWeightByQnID(){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = createAndSaveList();
        double actual = auditCheckListSMARepo.getWeightByQnID(AuditCheckListSMAModels.get(0).getSma_qn_id());
        assert(actual == AuditCheckListSMAModels.get(0).getWeight());
    }

    @Test
    public void getWeightByQnIDNotFound(){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = createAndSaveList();
        try {
            double actual = auditCheckListSMARepo.getWeightByQnID(-10);
        } catch (AopInvocationException e){
            return;
        }
        fail();
    }

    private boolean compareQuestions(AuditCheckListSMAModel expected, AuditCheckListSMAModel actual){
        boolean id = expected.getSma_qn_id()==actual.getSma_qn_id();
        boolean cat = expected.getCategory().equals(actual.getCategory());
        boolean subcat = expected.getSub_category().equals(actual.getSub_category());
        boolean weight = expected.getWeight() == actual.getWeight();
        boolean req = expected.getRequirement().equals(actual.getRequirement());
        boolean subreq = expected.getSub_requirement().equals(actual.getSub_requirement());
        return id && cat && subcat && weight && req && subreq;
    }

    private List<AuditCheckListSMAModel> createAuditChecklistList(String category){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = new ArrayList<>();
        for(int i=0;i<3;i++){
            AuditCheckListSMAModels.add(createChecklist(category));
        }
        return AuditCheckListSMAModels;
    }
    private List<AuditCheckListSMAModel> createAndSaveList(){
        return  createAndSaveList(CATEGORY);
    }
    private List<AuditCheckListSMAModel> createAndSaveList(String category){
        List<AuditCheckListSMAModel> AuditCheckListSMAModels = createAuditChecklistList(category);
        List<AuditCheckListSMAModel> AuditCheckListSMAModelsUpdated = new ArrayList<>();
        for (AuditCheckListSMAModel AuditCheckListSMAModel : AuditCheckListSMAModels) {
            AuditCheckListSMAModelsUpdated.add(auditCheckListSMARepo.save(AuditCheckListSMAModel));
        }
        return AuditCheckListSMAModelsUpdated;
    }

    private AuditCheckListSMAModel createChecklist(String category){
        AuditCheckListSMAModel AuditCheckListSMAModel = new AuditCheckListSMAModel();
        AuditCheckListSMAModel.setSma_qn_id(0);
        AuditCheckListSMAModel.setCategory(category);
        AuditCheckListSMAModel.setSub_category(category);
        AuditCheckListSMAModel.setWeight(0);
        AuditCheckListSMAModel.setRequirement("REQUIREMENT");
        AuditCheckListSMAModel.setSub_requirement("REQUIREMENT");
        return AuditCheckListSMAModel;
    }
}
