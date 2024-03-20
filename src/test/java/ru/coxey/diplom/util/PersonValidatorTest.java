package ru.coxey.diplom.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import ru.coxey.diplom.model.Person;
import ru.coxey.diplom.model.enums.Role;
import ru.coxey.diplom.service.impl.PersonDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class PersonValidatorTest {

    @Mock
    private PersonDetailsService personDetailsService;

    private PersonValidator personValidator;

    @BeforeEach
    void setUp() {
        personValidator = new PersonValidator(personDetailsService);
    }

    @Test
    void supports() {
        assertTrue(personValidator.supports(Person.class));
        assertFalse(personValidator.supports(Object.class));
    }

    @Test
    void personFound() {
        Person person = new Person("test", "test", Role.SPECIALIST);
        Mockito.when(personDetailsService.loadUserByUsername(anyString())).thenReturn(null);
        BindException errors = new BindException(person, "item");
        ValidationUtils.invokeValidator(personValidator, person, errors);
        assertTrue(errors.hasErrors());
    }

}