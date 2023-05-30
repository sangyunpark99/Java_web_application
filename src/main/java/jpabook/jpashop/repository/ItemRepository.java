package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.controller.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) { // 상품 저장

        if(item.getId() == null){ // insert
            em.persist(item);
        }else{ // 이미 있는 경우 update 느낌으로
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class,id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
