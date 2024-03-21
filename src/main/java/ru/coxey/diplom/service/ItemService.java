package ru.coxey.diplom.service;

import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAllItems();

    Item getItemById(int id);

    @Transactional
    void updateItem(Item item, int id);

    @Transactional
    void deleteItem(int id);

    @Transactional
    Item createNewItem(Item item);

    Item findItemByName(String name);

    Item findItemByNameForValidator(String name);
}
