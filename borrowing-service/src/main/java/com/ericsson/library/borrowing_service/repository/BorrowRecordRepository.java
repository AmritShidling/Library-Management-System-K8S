package com.ericsson.library.borrowing_service.repository;

import com.ericsson.library.borrowing_service.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserIdAndReturnDateIsNull(Long userId);
    List<BorrowRecord> findByBookIdAndReturnDateIsNull(Long bookId);
    List<BorrowRecord> findByUserId(Long userId);
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
}
