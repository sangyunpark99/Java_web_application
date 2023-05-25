package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입
    private Address address;

    @OneToMany(mappedBy = "member") // 매핑 당한 애
    private List<Order> orders = new ArrayList<>();

}
