package ru.coxey.diplom.util;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.coxey.diplom.model.Person;
import ru.coxey.diplom.service.impl.PersonDetailsService;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        try {
            personDetailsService.loadUserByUsername(person.getLogin());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("login", "", "Пользователь с таким логином уже существует");
    }

}
