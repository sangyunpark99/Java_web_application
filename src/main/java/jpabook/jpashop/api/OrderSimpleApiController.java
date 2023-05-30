package jpabook.jpashop.api;

import jpabook.jpashop.controller.domain.Address;
import jpabook.jpashop.controller.domain.Order;
import jpabook.jpashop.controller.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-order")
    public List<Order> ordersV1(){ // entitiy 그대로 return
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-order")
    public List<SimpleOrderDto> ordersV2(){ // DTO로 return

        //ORDER 2개
        // N + 1 -> 1 + 회원 N + 배송 N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream().map(m -> new SimpleOrderDto(m))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-order") // fetch join 적용하기
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream().map(m -> new SimpleOrderDto(m))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-order")
    public List<OrderSimpleQueryDto> orderV4(){ // JPA에서 DTO 바로 조회하기
        return orderSimpleQueryRepository.findOrderDto();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            this.orderId = order.getId();
            this.name = order.getMember().getName(); // LAZY 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
