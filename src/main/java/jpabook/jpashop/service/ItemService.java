package jpabook.jpashop.service;

import jpabook.jpashop.controller.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    @Transactional
    public void updateItem(UpdateItemDto itemDto){ // 영속상태의 값을 변경하므로 저장하는 메소드를 사용하지 않아도 된다.
        Item findItem = itemRepository.findOne(itemDto.getId());
        findItem.change(itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity());
    }
}
