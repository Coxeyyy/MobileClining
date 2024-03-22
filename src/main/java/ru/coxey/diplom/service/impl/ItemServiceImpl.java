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

    /** Метод возвращает список всех услуг */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /** Метод возвращает услугу по её ID */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item getItemById(int id) {
        Optional<Item> itemById = itemRepository.findById(id);
        if (itemById.isPresent()) {
            return itemById.get();
        } else {
            throw new IllegalArgumentException("Такой услуги не существует");
        }
    }

    /** Метод обновляет информацию по её ID */
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

    /** Метод удаляет услугу из БД */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteItem(int id) {
        itemRepository.deleteById(id);
    }

    /** Метод сохраняет новую услугу в БД */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item createNewItem(Item item) {
        return itemRepository.save(item);
    }

    /** Метод ищет услугу по её имени */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item findItemByName(String name) {
        Optional<Item> itemByName = itemRepository.findItemByName(name);
        if (itemByName.isPresent()) {
            return itemByName.get();
        } else {
            throw new IllegalArgumentException("Такой услуги не существует");
        }
    }

    /** Метод, который используется валидатором для поиска услуги по имени */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Item findItemByNameForValidator(String name) {
        Optional<Item> itemByName = itemRepository.findItemByName(name);
        return itemByName.orElse(null);
    }
}
