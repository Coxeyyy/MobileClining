package ru.coxey.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coxey.diplom.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findItemByName(String name);
}
