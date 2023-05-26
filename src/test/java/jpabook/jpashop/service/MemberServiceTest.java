package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) // 스프링이랑 같이 엮어서 실행
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {

        System.out.println(memberRepository);

        //given
        Member member = new Member();
        member.setName("아가은주");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush(); // flush
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void  중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("아가은주");
        Member member2 = new Member();
        member2.setName("아가은주");

        //when
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2); // 이름이 같으므로 예외발생
        });
    }
}