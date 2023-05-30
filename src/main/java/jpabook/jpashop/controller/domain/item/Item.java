package jpabook.jpashop.controller.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.controller.domain.Category;
import jpabook.jpashop.exception.NotEnoughtStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    /**
     * stock 증가 / setter가지고 하는 것이아닌 비즈니스 로직을 갖고 해야한다.
     */
    public void addStock(int quantity){ // 데이터를 가지고 있는 부분에 비즈니스 로직이 있는게 가장 관리하기 좋다.
        this.stockQuantity += quantity;
    } // 재고 수량 추가하기

    public void removeStock(int quantity){ // 재고 수량 지우기
        int resStock = this.stockQuantity - quantity;

        if(resStock < 0){
            throw new NotEnoughtStockException("need more stock");
        }
        this.stockQuantity = resStock;
    }

    public void change(String name, int price, int stockQuantity){ // 아이템 업데이트시 값 변경하기
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
