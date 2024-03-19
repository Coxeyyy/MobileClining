package ru.coxey.diplom.model.enums;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum Role {

    ADMIN("ROLE_ADMIN", "Администратор"),
    SPECIALIST("ROLE_SPECIALIST", "Специалист"),
    CUSTOMER("ROLE_CUSTOMER", "Заказчик");

    private final String value;
    private final String code;

    Role(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static Role getByCode(String code) {
        return Stream.of(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }
}
