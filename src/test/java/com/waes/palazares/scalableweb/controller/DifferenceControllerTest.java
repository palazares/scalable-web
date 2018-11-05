package com.waes.palazares.scalableweb.controller;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.domain.DifferenceType;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;
import com.waes.palazares.scalableweb.service.DifferenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DifferenceController.class)
public class DifferenceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DifferenceService differenceService;

    @Test
    public void shouldCallServiceWhenPutLeft() throws Exception, InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testId";
        String testContent = "testContent";
        DifferenceRecord testRecord = new DifferenceRecord(testId);
        testRecord.setLeft(testContent.getBytes());
        //when
        when(differenceService.putLeft(testId, testContent)).thenReturn(testRecord);
        //then
        mvc.perform(put("/v1/diff/" + testId + "/left").content(testContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(testId))
                //Spring mvc encodes byte array to base64 when creating json body of response
                .andExpect(jsonPath("left").value(Base64.getEncoder().encodeToString(testContent.getBytes())));
        verify(differenceService, times(1)).putLeft(testId, testContent);
    }

    @Test
    public void shouldCallServiceWhenPutRight() throws Exception, InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testId";
        String testContent = "testContent";
        DifferenceRecord testRecord = new DifferenceRecord(testId);
        testRecord.setRight(testContent.getBytes());
        //when
        when(differenceService.putRight(testId, testContent)).thenReturn(testRecord);
        //then
        mvc.perform(put("/v1/diff/" + testId + "/right").content(testContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(testId))
                //Spring mvc encodes byte array to base64 when creating json body of response
                .andExpect(jsonPath("right").value(Base64.getEncoder().encodeToString(testContent.getBytes())));
        verify(differenceService, times(1)).putRight(testId, testContent);
    }

    @Test
    public void shouldCallServiceWhenGetDifference() throws Exception, InavlidIdException, InvalidRecordContentException {
        //given
        String testId = "testId";
        String testMessage = "testEquals";
        DifferenceRecord testRecord = new DifferenceRecord(testId);
        testRecord.setResult(new DifferenceResult(DifferenceType.EQUALS, testMessage));
        //when
        when(differenceService.getDifference(testId)).thenReturn(testRecord);
        //then
        mvc.perform(get("/v1/diff/" + testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("type").value(DifferenceType.EQUALS.toString()))
                .andExpect(jsonPath("message").value(testMessage));

        verify(differenceService, times(1)).getDifference(testId);
    }
}