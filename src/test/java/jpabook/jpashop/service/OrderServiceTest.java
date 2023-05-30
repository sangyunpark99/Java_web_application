package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.controller.domain.Address;
import jpabook.jpashop.controller.domain.Member;
import jpabook.jpashop.controller.domain.Order;
import jpabook.jpashop.controller.domain.OrderStatus;
import jpabook.jpashop.controller.domain.item.Book;
import jpabook.jpashop.controller.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughtStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("아가은주", new Address("서울", "감가", "123-123"));

        Book book = createBook("시골 JPA", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고가 줄어야한다.", 8, book.getStockQuantity());

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("아가은주", new Address("서울", "감가", "123-123"));
        Book item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId); // 주문 취소

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL 이다.",OrderStatus.CANCLE, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.",10,item.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량_초과() throws Exception {
        //given
        Member member = createMember("아가은주", new Address("서울", "감가", "123-123"));
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;
        //when

        assertThrows(NotEnoughtStockException.class,()->{
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);
        return member;
    }
}