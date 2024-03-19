package ru.coxey.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.coxey.diplom.model.Person;

import java.util.Optional;

public interface PersonRepository<T extends Person> extends JpaRepository<T, Integer> {

    Optional<Person> findByLogin(String login);
}
