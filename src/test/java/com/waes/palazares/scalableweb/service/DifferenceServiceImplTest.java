package com.waes.palazares.scalableweb.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.domain.DifferenceType;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.exception.InvalidBase64Exception;
import com.waes.palazares.scalableweb.exception.InvalidRecordContentException;
import com.waes.palazares.scalableweb.repository.DifferenceRepository;

@RunWith(MockitoJUnitRunner.class)
public class DifferenceServiceImplTest {
    @InjectMocks
    private DifferenceServiceImpl diffService;

    @Mock
    private DifferenceRepository repository;

    @Test(expected = InavlidIdException.class)
    public void shouldThrowInvalidIdWhenLeftIdIsNull() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft(null, null);
    }

    @Test(expected = InavlidIdException.class)
    public void shouldThrowInvalidIdWhenLeftIdIsEmpty() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("", null);
    }

    @Test(expected = InavlidIdException.class)
    public void shouldThrowInvalidIdWhenRightIdIsNull() throws InavlidIdException, InvalidBase64Exception {
        diffService.putRight(null, null);
    }

    @Test(expected = InavlidIdException.class)
    public void shouldThrowInvalidIdWhenRightIdIsEmpty() throws InavlidIdException, InvalidBase64Exception {
        diffService.putRight("", null);
    }

    @Test(expected = InavlidIdException.class)
    public void shouldThrowInvalidIdWhenDifIdIsNull() throws InavlidIdException, InvalidRecordContentException {
        diffService.getDifference(null);
    }

    @Test(expected = InavlidIdException.class)
    public void shouldThrowInvalidIdWhenDifIdIsEmpty() throws InavlidIdException, InvalidRecordContentException {
        diffService.getDifference("");
    }

    @Test(expected = InvalidBase64Exception.class)
    public void shouldThrowInvalidBase64WhenLeftDocIsNull() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("testID", null);
    }

    @Test(expected = InvalidBase64Exception.class)
    public void shouldThrowInvalidBase64WhenLeftDocIsEmpty() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("testID", "");
    }

    @Test(expected = InvalidBase64Exception.class)
    public void shouldThrowInvalidBase64WhenLeftDocIsInvalidBase64() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("testID", "_- &^%");
    }

    @Test(expected = InvalidBase64Exception.class)
    public void shouldThrowInvalidBase64WhenRightDocIsNull() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("testID", null);
    }

    @Test(expected = InvalidBase64Exception.class)
    public void shouldThrowInvalidBase64WhenRightDocIsEmpty() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("testID", "");
    }

    @Test(expected = InvalidBase64Exception.class)
    public void shouldThrowInvalidBase64WhenRightDocIsInvalidBase64() throws InavlidIdException, InvalidBase64Exception {
        diffService.putLeft("testID", "_- &^%");
    }

    @Test(expected = InvalidRecordContentException.class)
    public void shouldThrowInvalidRecordContentWhenRecordWasNotFound() throws InavlidIdException, InvalidRecordContentException {
        //when
        when(repository.findById(eq("testID"))).thenReturn(Optional.empty());
        diffService.getDifference("testID");
    }

    @Test(expected = InvalidRecordContentException.class)
    public void shouldThrowInvalidRecordContentWhenRightDocIsNull() throws InavlidIdException, InvalidRecordContentException {
        //given
        DifferenceRecord testIdRecord = DifferenceRecord.builder().id("testID").left("content".getBytes()).build();
        //when
        when(repository.findById(eq("testID"))).thenReturn(Optional.of(testIdRecord));
        diffService.getDifference("testID");
    }

    @Test(expected = InvalidRecordContentException.class)
    public void shouldThrowInvalidRecordContentWhenRightDocIsEmpty() throws InavlidIdException, InvalidRecordContentException {
        //given
        DifferenceRecord testIdRecord = DifferenceRecord.builder()
                .id("testID")
                .left("content".getBytes())
                .right(new byte[0])
                .build();
        //when
        when(repository.findById(eq("testID"))).thenReturn(Optional.of(testIdRecord));
        diffService.getDifference("testID");
    }

    @Test(expected = InvalidRecordContentException.class)
    public void shouldThrowInvalidRecordContentWhenLeftDocIsNull() throws InavlidIdException, InvalidRecordContentException {
        //given
        DifferenceRecord testIdRecord = DifferenceRecord.builder()
                .id("testID")
                .right("content".getBytes())
                .build();
        //when
        when(repository.findById(eq("testID"))).thenReturn(Optional.of(testIdRecord));
        diffService.getDifference("testID");
    }

    @Test(expected = InvalidRecordContentException.class)
    public void shouldThrowInvalidRecordContentWhenLeftDocIsEmpty() throws InavlidIdException, InvalidRecordContentException {
        //given
        DifferenceRecord testIdRecord = DifferenceRecord.builder()
                .id("testID")
                .right("content".getBytes())
                .left(new byte[0])
                .build();
        //when
        when(repository.findById(eq("testID"))).thenReturn(Optional.of(testIdRecord));
        diffService.getDifference("testID");
    }

    @Test
    public void shouldNotFailWhenCorrectBase64() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        String leftContent = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.empty());
        diffService.putLeft(testId, leftContent);
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertNotNull(arg.getLeft());
            assertNull(arg.getRight());
            assertNull(arg.getResult());
            return true;
        }));
    }

    @Test
    public void shouldSaveNewRecordWhenLeftDoc() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        byte[] leftContent = "testContent".getBytes();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.empty());
        diffService.putLeft(testId, Base64.getEncoder().encodeToString(leftContent));
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(leftContent, arg.getLeft());
            assertNull(arg.getRight());
            assertNull(arg.getResult());
            return true;
        }));
    }

    @Test
    public void shouldSaveNewRecordWhenRightDoc() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        byte[] rightContent = "testContent".getBytes();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.empty());
        diffService.putRight(testId, Base64.getEncoder().encodeToString(rightContent));
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(rightContent, arg.getRight());
            assertNull(arg.getLeft());
            assertNull(arg.getResult());
            return true;
        }));
    }

    @Test
    public void shouldUpdateRecordWhenLeftDoc() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        byte[] leftContent = "leftTestContent".getBytes();
        byte[] rightContent = "rightTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left("oldContent".getBytes())
                .right(rightContent)
                .result(DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("testResult").build())
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        diffService.putLeft(testId, Base64.getEncoder().encodeToString(leftContent));
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(leftContent, arg.getLeft());
            assertArrayEquals(rightContent, arg.getRight());
            assertNull(arg.getResult());
            return true;
        }));
    }

    @Test
    public void shouldUpdateRecordWhenRightDoc() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        byte[] leftContent = "leftTestContent".getBytes();
        byte[] rightContent = "rightTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(leftContent)
                .right("oldContent".getBytes())
                .result(DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("testResult").build())
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        diffService.putRight(testId, Base64.getEncoder().encodeToString(rightContent));
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(leftContent, arg.getLeft());
            assertArrayEquals(rightContent, arg.getRight());
            assertNull(arg.getResult());
            return true;
        }));
    }

    @Test
    public void shouldNotUpdateRecordResultWhenRightDocIsTheSame() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        byte[] leftContent = "leftTestContent".getBytes();
        byte[] rightContent = "rightTestContent".getBytes();
        DifferenceResult testResult = DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("testResult").build();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(leftContent)
                .right(rightContent)
                .result(testResult)
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        DifferenceRecord difference = diffService.putRight(testId, Base64.getEncoder().encodeToString(rightContent)).block();
        //then
        verify(repository).findById(eq(testId));
        verify(repository, times(0)).save(any());
        assertEquals(differenceRecord, difference);
    }

    @Test
    public void shouldNotUpdateRecordResultWhenLeftDocIsTheSame() throws InavlidIdException, InvalidBase64Exception {
        //given
        String testId = "testID";
        byte[] leftContent = "leftTestContent".getBytes();
        byte[] rightContent = "rightTestContent".getBytes();
        DifferenceResult testResult = DifferenceResult.builder().type(DifferenceType.DIFFERENT_SIZE).message("testResult").build();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(leftContent)
                .right(rightContent)
                .result(testResult)
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        DifferenceRecord difference = diffService.putLeft(testId, Base64.getEncoder().encodeToString(leftContent)).block();
        //then
        verify(repository).findById(eq(testId));
        verify(repository, times(0)).save(any());
        assertEquals(differenceRecord, difference);
    }

    @Test
    public void shouldReturnEqualResultWhenDocsAreTheSame() throws InavlidIdException, InvalidRecordContentException {
        //given
        String testId = "testID";
        byte[] equalContent = "equalTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(equalContent)
                .right(equalContent)
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        diffService.getDifference(testId);
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(equalContent, arg.getLeft());
            assertArrayEquals(equalContent, arg.getRight());
            assertEquals(DifferenceType.EQUALS, arg.getResult().getType());
            assertNotNull(arg.getResult().getMessage());
            return true;
        }));
    }

    @Test
    public void shouldReturnDifferentSizeResultWhenDocsAreDifferent() throws InavlidIdException, InvalidRecordContentException {
        //given
        String testId = "testID";
        byte[] leftContent = "leftTestContent".getBytes();
        byte[] rightContent = "rightTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(leftContent)
                .right(rightContent)
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        diffService.getDifference(testId);
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(leftContent, arg.getLeft());
            assertArrayEquals(rightContent, arg.getRight());
            assertEquals(DifferenceType.DIFFERENT_SIZE, arg.getResult().getType());
            assertNotNull(arg.getResult().getMessage());
            return true;
        }));
    }

    @Test
    public void shouldReturnDifferentContentResultWhenDocsContentsAreDifferent() throws InavlidIdException, InvalidRecordContentException {
        //given
        String testId = "testID";
        byte[] leftContent = "leftTestContent".getBytes();
        byte[] rightContent = "ightTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(leftContent)
                .right(rightContent)
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        diffService.getDifference(testId);
        //then
        verify(repository).findById(eq(testId));
        verify(repository).save(ArgumentMatchers.argThat(arg -> {
            assertEquals(testId, arg.getId());
            assertArrayEquals(leftContent, arg.getLeft());
            assertArrayEquals(rightContent, arg.getRight());
            assertEquals(DifferenceType.DIFFERENT_CONTENT, arg.getResult().getType());
            assertNotNull(arg.getResult().getMessage());
            return true;
        }));
    }

    @Test
    public void shouldNotSaveWhenLeftDocIsTheSame() throws InavlidIdException, InvalidRecordContentException {
        //given
        String testId = "testID";
        byte[] equalContent = "equalTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(equalContent)
                .right(equalContent)
                .result(DifferenceResult.builder().type(DifferenceType.EQUALS).message("equals").build())
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        DifferenceRecord difference = diffService.getDifference(testId).block();
        //then
        verify(repository).findById(eq(testId));
        verify(repository, times(0)).save(any());
        assertEquals(differenceRecord, difference);
    }

    @Test
    public void shouldNotCompareWhenResultExists() throws InavlidIdException, InvalidRecordContentException {
        //given
        String testId = "testID";
        byte[] equalContent = "equalTestContent".getBytes();
        DifferenceRecord differenceRecord = DifferenceRecord.builder()
                .id(testId)
                .left(equalContent)
                .right(equalContent)
                .result(DifferenceResult.builder().type(DifferenceType.EQUALS).message("equals").build())
                .build();
        //when
        when(repository.findById(eq(testId))).thenReturn(Optional.of(differenceRecord));
        DifferenceRecord difference = diffService.getDifference(testId).block();
        //then
        verify(repository).findById(eq(testId));
        verify(repository, times(0)).save(any());
        assertEquals(differenceRecord, difference);
    }
}