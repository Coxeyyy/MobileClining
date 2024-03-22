insert into users(login, password, role, dtype)
values ('Artem', '$2a$10$iU6eKBLGoyqPvYHEHczMRexDhmHzoCA9E1M0IMpHYR2diozsz5ZN2', 'ADMIN', 'Employee'),
       ('Vasek', '$2a$10$uumlsACCWeEwQLIyELEWC.5WHtkN7HdLQgTHXAFsF1Do9s/UvAIbq', 'SPECIALIST', 'Employee'),
       ('Gena', '$2a$10$3F0lCau1mQfu4Cpm0y3ynuix25ag9uBDskOAiLIrSB8RSU5TATfwi', 'SPECIALIST', 'Employee');


insert into users(login, password, role, number_phone, address, telegram_user_id, can_create_order, dtype)
values ('Pavel', 'defaultPass', 'CUSTOMER', '89158674123', 'Ryazan, st. Zubkovoi 30 k.1 kv. 4', 651770723, true,
        'Customer'),
       ('Tema', 'defaultPass', 'CUSTOMER', '89568761253', 'Ryazan, st. Novoselov 58 kv. 1', 184672608, true,
        'Customer');

insert into items(item_name, item_price)
values ('Chair', 500),
       ('Sofa', 1000),
       ('Bed', 2000);

insert into orders(id_customer, id_specialist, status_order, order_price)
values (4, 2, 'READY', 500),
       (4, 3, 'IN_PROCESS', 4000),
       (5, 2, 'IN_PROCESS', 1000),
       (5, 2, 'IN_PROCESS', 3000);

insert into orders_items(id_order, id_item)
values (1, 1),
       (2, 3),
       (2, 2),
       (2, 2),
       (3, 1),
       (3, 1),
       (4, 2),
       (4, 2),
       (4, 2);
