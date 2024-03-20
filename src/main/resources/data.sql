insert into users(login, password, role, dtype)
values ('Artem', '$2a$10$iU6eKBLGoyqPvYHEHczMRexDhmHzoCA9E1M0IMpHYR2diozsz5ZN2', 'ADMIN', 'Employee'),
       ('Vasek', '$2a$10$uumlsACCWeEwQLIyELEWC.5WHtkN7HdLQgTHXAFsF1Do9s/UvAIbq', 'SPECIALIST', 'Employee'),
       ('Vitalik', '$2a$10$85jstNk0dFxCLcsg/b5CuuWtZAxDRXLcestYDeHqjSQbgfpPX0bVe', 'CUSTOMER', 'Customer'),
       ('Petya', '$2a$10$AXZzIxaXkz0574ZstT7XRufR9reJF9PIYMODTxSzFTpH2t7CKjSrm', 'CUSTOMER', 'Customer'),
       ('Gena', '$2a$10$3F0lCau1mQfu4Cpm0y3ynuix25ag9uBDskOAiLIrSB8RSU5TATfwi', 'SPECIALIST', 'Employee');


insert into items(item_name, item_price)
values ('Chair', 500),
       ('Sofa', 1000),
       ('Bed', 2000);

insert into orders(id_customer, id_specialist, status_order, order_price)
values (3, 2, 'IN_PROCESS', 3500.0),
       (3, 5, 'READY', 1000.0),
       (4, 2, 'READY', 3000.0),
       (4, 2, 'IN_PROCESS', 500.0);

insert into orders_items(id_order, id_item)
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 2),
       (3, 2),
       (3, 3),
       (4, 1);
