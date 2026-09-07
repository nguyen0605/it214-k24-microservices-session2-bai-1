package com.librax.library.borrowing;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class BorrowingRepository {
    private final Map<Integer, BorrowingRecord> records = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public BorrowingRecord save(BorrowingRecord record) {
        if (record.getId() == 0) {
            record.setId(idGenerator.getAndIncrement());
        }
        records.put(record.getId(), record);
        return record;
    }

    public List<BorrowingRecord> findActiveBorrowingsByMember(int memberId) {
        return records.values().stream()
                .filter(r -> r.getMemberId() == memberId && !r.isReturned())
                .collect(Collectors.toList());
    }

    public List<BorrowingRecord> findAll() {
        return new ArrayList<>(records.values());
    }
}