package com.ericsson.library.borrowing_service;


import com.ericsson.library.borrowing_service.conroller.BorrowController;
import com.ericsson.library.borrowing_service.enums.BorrowStatus;
import com.ericsson.library.borrowing_service.model.BorrowRecord;
import com.ericsson.library.borrowing_service.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class BorrowControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BorrowService borrowService;

    @InjectMocks
    private BorrowController borrowController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(borrowController).build();
    }

    @Test
    void borrowBook_Success() throws Exception {
        Long userId = 1L;
        Long bookId = 2L;

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(1L);
        borrowRecord.setUserId(userId);
        borrowRecord.setBookId(bookId);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        when(borrowService.borrowBook(userId, bookId)).thenReturn(borrowRecord);

        mockMvc.perform(post("/v1/services/borrow")
                        .param("userId", userId.toString())
                        .param("bookId", bookId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId.intValue())))
                .andExpect(jsonPath("$.bookId", is(bookId.intValue())))
                .andExpect(jsonPath("$.status", is("BORROWED")));

        verify(borrowService, times(1)).borrowBook(userId, bookId);
    }

    @Test
    void returnBook_Success() throws Exception {
        Long borrowId = 1L;

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(borrowId);
        borrowRecord.setBookId(2L);
        borrowRecord.setUserId(1L);
        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecord.setReturnDate(LocalDateTime.now());

        when(borrowService.returnBook(borrowId)).thenReturn(borrowRecord);

        mockMvc.perform(post("/v1/services/return/{borrowId}", borrowId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(borrowId.intValue())))
                .andExpect(jsonPath("$.status", is("RETURNED")));

        verify(borrowService, times(1)).returnBook(borrowId);
    }

    @Test
    void getBorrowingHistory_Success() throws Exception {
        Long userId = 1L;

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(1L);
        borrowRecord.setUserId(userId);
        borrowRecord.setBookId(2L);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        List<BorrowRecord> history = Collections.singletonList(borrowRecord);

        when(borrowService.getBorrowingHistory(userId)).thenReturn(history);

        mockMvc.perform(get("/v1/services/borrow-history/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userId.intValue())))
                .andExpect(jsonPath("$[0].bookId", is(2)))
                .andExpect(jsonPath("$[0].status", is("BORROWED")));

        verify(borrowService, times(1)).getBorrowingHistory(userId);
    }
}