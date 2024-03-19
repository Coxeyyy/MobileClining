package ru.coxey.diplom.model.enums;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

public enum Status {

    IN_PROCESS("IN_PROCESS", "В процессе"),
    READY("READY", "Готово");

    private final String value;
    private final String code;

    Status(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static Status getByCode(String code) {
        return Stream.of(Status.values())
                .filter(status -> status.getCode().equals(code))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }
}
