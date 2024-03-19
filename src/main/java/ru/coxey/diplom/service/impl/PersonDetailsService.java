package ru.coxey.diplom.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.coxey.diplom.model.Person;
import ru.coxey.diplom.repository.PersonRepository;
import ru.coxey.diplom.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository<Person> personPersonRepository;

    public PersonDetailsService(PersonRepository<Person> personPersonRepository) {
        this.personPersonRepository = personPersonRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personPersonRepository.findByLogin(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }
}
