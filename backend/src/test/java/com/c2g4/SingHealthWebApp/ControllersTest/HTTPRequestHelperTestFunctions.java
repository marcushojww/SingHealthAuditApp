package com.c2g4.SingHealthWebApp.ControllersTest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class HTTPRequestHelperTestFunctions {
    public static ResultActions performGetRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(requestURL)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON);
        if(params!=null){
            for(String key:params.keySet()){
                mockHttpServletRequestBuilder.param(key,params.get(key));
            }
        }
        return mvc.perform(mockHttpServletRequestBuilder);
    }

    public static void getHttpOk(MockMvc mvc, String requestURL, HashMap<String,String> params, int jsonSize, String compareJson) throws Exception {
        ResultActions resultActions = performGetRequest(mvc, requestURL,params);
        resultActions.andExpect(status().isOk());
        if(jsonSize!=-1) {
        	System.out.println("HELLOO");
        	resultActions.andExpect(jsonPath("$",hasSize(jsonSize)));
        } 
        if(compareJson!=null){
            resultActions.andExpect(content().json(compareJson));
        }
    }

    public static void getHttpBadRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(mvc, requestURL,params);
        resultActions.andExpect(status().isBadRequest());
    }

    public static void getHttpNotFoundRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(mvc, requestURL,params);
        resultActions.andExpect(status().isNotFound());
    }

    public static void getHttpUnauthorizedRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performGetRequest(mvc,requestURL,params);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    public static ResultActions performPostRequest(MockMvc mvc, String url, HashMap<String,String> multipartForm, HashMap<String,String> params) throws Exception {
        ArrayList<MockMultipartFile> mockMultipartFiles = new ArrayList<>();
        if(multipartForm!=null) {
            for (String key : multipartForm.keySet()) {
                MockMultipartFile postFile = new MockMultipartFile(key, key, MediaType.APPLICATION_JSON_VALUE, multipartForm.get(key).getBytes());
                mockMultipartFiles.add(postFile);
            }
        }
        MockMultipartHttpServletRequestBuilder mockMultipartBuilder = MockMvcRequestBuilders.multipart(url);
        for(MockMultipartFile mockMultipartFile: mockMultipartFiles){
            mockMultipartBuilder.file(mockMultipartFile);
        }
        if(params!=null){
            for(String key:params.keySet()){
                mockMultipartBuilder.param(key,params.get(key));
            }
        }

        return mvc.perform(mockMultipartBuilder.characterEncoding("utf-8"));
    }

    public static void postHttpOK(MockMvc mvc, String url, HashMap<String,String> multipartForm, HashMap<String,String> params, String checkOutput, boolean isOutputString) throws Exception{
        ResultActions resultActions = performPostRequest(mvc, url,multipartForm, params);
        resultActions.andExpect(status().isOk());
        if(checkOutput!=null){
            if(!isOutputString) resultActions.andExpect(content().json(checkOutput));
            else assert(resultActions.andReturn().getResponse().getContentAsString().equals(checkOutput));
        }

    }

    public static void postHttpBadRequest(MockMvc mvc, String url, HashMap<String,String> multipartForm, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performPostRequest(mvc, url,multipartForm,params);
        resultActions.andExpect(status().isBadRequest());
    }

    public static void postHttpUnauthorizedRequest(MockMvc mvc, String url, HashMap<String,String> multipartForm, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performPostRequest(mvc, url,multipartForm,params);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    public static ResultActions performDeleteRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.delete(requestURL)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON);
        if(params!=null){
            for(String key:params.keySet()){
                mockHttpServletRequestBuilder.param(key,params.get(key));
            }
        }
        return mvc.perform(mockHttpServletRequestBuilder);
    }

    public static void deleteHttpOk(MockMvc mvc, String requestURL, HashMap<String,String> params, String checkOutput) throws Exception {
        ResultActions resultActions = performDeleteRequest(mvc, requestURL,params);
        resultActions.andExpect(status().isOk());
        assert checkOutput == null || (resultActions.andReturn().getResponse().getContentAsString().equals(checkOutput));
    }

    public static void deleteHttpBadRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performDeleteRequest(mvc, requestURL,params);
        resultActions.andExpect(status().isBadRequest());
    }

    public static void deleteHttpNotFoundRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performDeleteRequest(mvc, requestURL,params);
        resultActions.andExpect(status().isNotFound());
    }

    public static void deleteHttpUnauthorizedRequest(MockMvc mvc, String requestURL, HashMap<String,String> params) throws Exception{
        ResultActions resultActions = performDeleteRequest(mvc,requestURL,params);
        resultActions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
}
