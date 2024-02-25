INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('RESTAURANT_1'),
       ('RESTAURANT_2'),
       ('RESTAURANT_3');

INSERT INTO DISH (restaurant_id, date_dish, name, price)
VALUES (1, '2024-02-04', 'dish_1_rest_1', 500),
       (1, '2024-02-04', 'dish_2_rest_1', 200),
       (1, '2024-02-04', 'dish_3_rest_1', 10),
       (2, '2024-02-04', 'dish_4_rest_2', 110),
       (2, '2024-02-04', 'dish_5_rest_2', 800),
       (1, CURRENT_DATE, 'dish_6_rest_1', 25),
       (1, CURRENT_DATE, 'dish_7_rest_1', 25),
       (1, CURRENT_DATE, 'dish_8_rest_1', 25),
       (2, CURRENT_DATE, 'dish_9_rest_2', 90),
       (3, '2024-02-04', 'dish_10_rest_3', 90);

INSERT INTO VOTE (user_id, restaurant_id, date_vote)
VALUES (1, 1, CURRENT_DATE),
       (2, 1, CURRENT_DATE),
       (1, 3, '2024-02-04'),
       (2, 2, '2024-02-04');