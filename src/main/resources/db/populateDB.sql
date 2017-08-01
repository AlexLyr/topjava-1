DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seqmeal RESTART WITH 1;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
    VALUES (100000,'2017-07-06 08:00:00','Завтрак',1000);

INSERT INTO meals (user_id,date_time, description, calories)
VALUES (100000,'2017-07-06 13:00:00','Обед',902);

INSERT INTO meals (user_id, date_time,description, calories)
VALUES (100000,'2017-07-06 22:00:00','Ужин',100);

INSERT INTO meals (user_id,date_time, description, calories)
VALUES (100001,'2017-07-06 08:00:00','Завтрак',500);

INSERT INTO meals (user_id,date_time, description, calories)
VALUES (100001,'2017-07-06 13:00:00','Обед',510);

INSERT INTO meals (user_id, date_time,description, calories)
VALUES (100001,'2017-07-06 22:00:00','Ужин',502);

INSERT INTO meals (user_id, date_time,description, calories)
VALUES (100000,'2017-07-07 08:00:00','Завтрак',600);

INSERT INTO meals (user_id,date_time, description, calories)
VALUES (100000,'2017-07-07 14:00:00','Обед',310);

INSERT INTO meals (user_id,date_time, description, calories)
VALUES (100000,'2017-07-07 21:00:00','Ужин',202);

