package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // readOnly = true 를 주면 조회할 때 성능 더 좋음. 단 데이터 변경 안됨!
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository; // 변경할 일이 없으므로 final

    /*1. memberRepository에 접근할 수 있는 방법이 없음. 테스트 작성할 때 불편함.
    @Autowired
    private MemberRepository memberRepository;*/

    /*2. 런타임에 memberRepository가 바뀔 수 있음.
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    /*3. 생성자 injection! 생성자가 하나만 있는 경우에는 @Autowired 안해도 스프링이 알아서 주입함.
    @AllArgsConstructor, @RequiredArgsConstructor 가 아래 생성자를 만들어줌.
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());// 같은 이름이 있는지. 실무에서는 db에 member name을 unique로 잡는게 안전.
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
