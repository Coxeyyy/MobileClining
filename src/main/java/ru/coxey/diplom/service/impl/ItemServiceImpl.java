package ru.coxey.diplom.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.repository.ItemRepository;
import ru.coxey.diplom.service.ItemService;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item getItemById(int id) {
        Optional<Item> itemById = itemRepository.findById(id);
        if (itemById.isPresent()) {
            return itemById.get();
        } else {
            throw new IllegalArgumentException("Такой услуги не существует");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateItem(Item item, int id) {
        Optional<Item> itemById = itemRepository.findById(id);
        if (itemById.isPresent()) {
            Item itemFromDB = itemById.get();
            itemFromDB.setId(item.getId());
            itemFromDB.setName(item.getName());
            itemFromDB.setPrice(item.getPrice());
            itemRepository.save(itemFromDB);
        } else {
            throw new IllegalArgumentException("Услуга не найдена");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item createNewItem(Item item) {
        return itemRepository.save(item);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item findItemByName(String name) {
        Optional<Item> itemByName = itemRepository.findItemByName(name);
        if (itemByName.isPresent()) {
            return itemByName.get();
        } else {
            throw new IllegalArgumentException("Такой услуги не существует");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item findItemByNameForValidator(String name) {
        Optional<Item> itemByName = itemRepository.findItemByName(name);
        return itemByName.orElse(null);
    }
}
