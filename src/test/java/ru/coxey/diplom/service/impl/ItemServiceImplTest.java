package ru.coxey.diplom.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getAllItems() {
        when(itemRepository.findAll()).thenReturn(List.of(new Item(), new Item()));
        List<Item> allItems = itemService.getAllItems();
        assertEquals(2, allItems.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void getItemById() {
        Item item = new Item("Chair", 500.0);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        Item itemById = itemService.getItemById(1);
        assertEquals(item.getPrice(), itemById.getPrice());
        assertEquals(item.getName(), itemById.getName());
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    void getItemById_throwIllegalArgumentException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> itemService.getItemById(anyInt()));
    }

    @Test
    void updateItem() {
        Item item = new Item("Chair", 500.0);
        item.setId(1);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        itemService.updateItem(item, 1);
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    void updateItem_throwIllegalArgumentException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(new Item(), anyInt()));
    }

    @Test
    void deleteItem() {
        doNothing().when(itemRepository).deleteById(anyInt());
        itemService.deleteItem(5);
        verify(itemRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void createNewItem() {
        Item item = new Item("Chair", 500.0);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item itemFromService = itemService.createNewItem(item);
        assertEquals(item.getName(), itemFromService.getName());
        assertEquals(item.getPrice(), itemFromService.getPrice());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void findItemByName() {
        Item item = new Item("Chair", 500.0);
        when(itemRepository.findItemByName(anyString())).thenReturn(Optional.of(item));
        Item itemFromService = itemService.findItemByName("test");
        assertEquals(item.getName(), itemFromService.getName());
        assertEquals(item.getPrice(), itemFromService.getPrice());
        verify(itemRepository, times(1)).findItemByName(anyString());
    }

    @Test
    void findItemByName_throwIllegalArgumentException() {
        when(itemRepository.findItemByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> itemService.findItemByName(anyString()));
    }

}