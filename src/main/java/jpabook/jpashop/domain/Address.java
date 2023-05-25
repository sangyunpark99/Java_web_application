package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable // 내장이 될 수 있다.
@Getter
public class Address {

    private String city;
    private String street;
    private String zipCode;

    protected Address(){
    }

    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
}
