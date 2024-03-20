package ru.coxey.diplom.model;

import ru.coxey.diplom.model.enums.Role;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Employee")
public class Employee extends Person {

    public Employee() {
    }

    public Employee(String login, String password, Role role) {
        super(login, password, role);
    }

    public Employee(String login, Role role) {
        super(login, role);
    }

    public Employee(String login) {
        super(login);
    }
}
