package com.librax.library.member;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemberRepository {
    private final Map<Integer, Member> members = new ConcurrentHashMap<>();

    public MemberRepository() {
        members.put(101, new Member(101, "Alice"));
        members.put(102, new Member(102, "Bob"));
    }

    public Member findById(int id) {
        return members.get(id);
    }

    public void save(Member member) {
        members.put(member.getId(), member);
    }
}