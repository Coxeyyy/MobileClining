package ru.coxey.diplom.model;

import ru.coxey.diplom.model.enums.Role;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Customer")
public class Customer extends Person {

    @Column(name = "number_phone")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "can_create_order")
    private Boolean canCreateOrder = true;

    public Customer() {

    }

    public Customer(String login, String password, Role role, String phoneNumber, String address, Long telegramUserId) {
        super(login, password, role);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.telegramUserId = telegramUserId;
    }

    public Customer(String phoneNumber, String address, Boolean canCreateOrder) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.canCreateOrder = canCreateOrder;
    }

    public Customer(String login, String password, Role role, String phoneNumber, String address, Boolean canCreateOrder) {
        super(login, password, role);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.canCreateOrder = canCreateOrder;
    }

    public Customer(String login, Role role, String phoneNumber, String address, Boolean canCreateOrder) {
        super(login, role);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.canCreateOrder = canCreateOrder;
    }

    public Customer(String login, String phoneNumber, String address, Boolean canCreateOrder) {
        super(login);
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.canCreateOrder = canCreateOrder;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isCanCreateOrder() {
        return canCreateOrder;
    }

    public void setCanCreateOrder(Boolean canCreateOrder) {
        this.canCreateOrder = canCreateOrder;
    }
}
