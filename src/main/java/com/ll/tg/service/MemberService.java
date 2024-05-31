package com.ll.tg.service;

import com.ll.tg.domain.Member;
import com.ll.tg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        Optional<Member> opMember = memberRepository.findByName(member.getName());
        if (opMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findMember(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public void update(Long id, String name) {
        Optional<Member> opMember = memberRepository.findById(id);

        if (opMember.isPresent()) {
            Member member = opMember.get();
            member.setName(name);

            memberRepository.save(member);
        }
    }
}