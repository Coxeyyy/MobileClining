package ru.coxey.diplom.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;
import ru.coxey.diplom.model.Item;
import ru.coxey.diplom.service.ItemService;
import ru.coxey.diplom.util.ItemValidator;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemValidator itemValidator;

    private final int ID = 1;

    @Test
    void getAllItems() throws Exception {
        when(itemService.getAllItems()).thenReturn(getListItems());
        mockMvc.perform(get("/adminpanel/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/item/items"))
                .andExpect(model().attributeExists("items"));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt())).thenReturn(getItem());
        mockMvc.perform(get("/adminpanel/items/" + ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("adminpanel/item/showItemByIndex"));

    }

    @Test
    void editItem() throws Exception {
        when(itemService.getItemById(anyInt())).thenReturn(getItem());
        mockMvc.perform(get("/adminpanel/items/" + ID + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("adminpanel/item/editItem"));
    }

    @Test
    void updateItem_notUpdateAndRedirect() throws Exception {
        mockMvc.perform(patch("/adminpanel/items/" + ID))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/item/editItem"));
    }

    @Test
    void deleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(anyInt());
        mockMvc.perform(delete("/adminpanel/items/" + ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminpanel"));
    }

    @Test
    void testGetMappingCreateNewItem() throws Exception {
        mockMvc.perform(get("/adminpanel/items/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("adminpanel/item/newItem"));
    }

    @Test
    void testPostMappingCreateNewItem_ItemNotCreated() throws Exception {
        doNothing().when(itemValidator).validate(any(Item.class), any(Errors.class));
        mockMvc.perform(post("/adminpanel/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminpanel/item/newItem"));
    }

    private Item getItem() {
        return new Item("Sofa", 2537.1);
    }

    private List<Item> getListItems() {
        Item item1 = new Item("Sofa", 2537.1);
        Item item2 = new Item("Carpet", 3671.8);
        return List.of(item1, item2);
    }
}