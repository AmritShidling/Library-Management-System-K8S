package com.ericsson.library.borrowing_service.conroller;

import com.ericsson.library.borrowing_service.dto.BorrowRecordDTO;
import com.ericsson.library.borrowing_service.model.BorrowRecord;
import com.ericsson.library.borrowing_service.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/services")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecord> borrowBook(@RequestParam("userId") Long userId, @RequestParam("bookId") Long bookId) {
        return ResponseEntity.ok(borrowService.borrowBook(userId, bookId));
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable("borrowId") Long borrowId) {

        return ResponseEntity.ok(borrowService.returnBook(borrowId));
    }


    @GetMapping("/borrow-history/{userId}")
    public ResponseEntity<List<BorrowRecord>> getBorrowingHistory(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(borrowService.getBorrowingHistory(userId));
    }

    @GetMapping("/borrow-history")
    public ResponseEntity<List<BorrowRecordDTO>> getAllBorrowingHistory() {
        return ResponseEntity.ok(borrowService.getAllBorrowingHistory());
    }

    @GetMapping("/borrow-history/search")
    public ResponseEntity<List<BorrowRecordDTO>> searchBorrowingHistory(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "bookTitle", required = false) String bookTitle
    ) {
        return ResponseEntity.ok(borrowService.searchBorrowingHistory(userName, bookTitle));
    }


}