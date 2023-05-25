package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 컴포넌트 스캔
public class MemberRepository {
    @PersistenceContext
    private EntityManager em; // EntityManager를 주입해준다.

    public void save(Member member){
        em.persist(member);
    }

    public Member find(Long id){ // id로 회원 찾기
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){ // 모든 회원 찾기
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name){ // 이름에 의해서 회원 조회
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
