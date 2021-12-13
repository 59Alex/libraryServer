-- add author table
create table if not exists author
(
    id          bigint auto_increment
        primary key,
    first_name  varchar(127) null,
    last_name   varchar(127) null,
    description varchar(255) null
);
-- add book table
create table if not exists book
(
    id           bigint auto_increment
        primary key,
    name         varchar(127) not null,
    description  varchar(255) null,
    created_date datetime     not null,
    body         longtext     not null,
    author       bigint       not null,
    constraint book_name_uindex
        unique (name),
    constraint book_author_id_fk
        foreign key (author) references author (id)
);
-- add journal table
create table if not exists journal
(
    id           bigint auto_increment
        primary key,
    name         varchar(127) not null,
    description  varchar(255) null,
    created_date datetime     not null,
    body         longtext     not null,
    author       bigint       not null,
    constraint journal_name_uindex
        unique (name),
    constraint journal_author_id_fk
        foreign key (author) references author (id)
);
-- add newspaper table
create table if not exists newspaper
(
    id           bigint auto_increment
        primary key,
    name         varchar(127) not null,
    description  varchar(255) null,
    created_date datetime     not null,
    body         longtext     not null,
    author       bigint       not null,
    constraint newspaper_name_uindex
        unique (name),
    constraint newspaper_author_id_fk
        foreign key (author) references author (id)
);
-- add role table
create table if not exists role
(
    id          bigint auto_increment
        primary key,
    name        varchar(127) null,
    description varchar(255) null
);
-- add user table
create table if not exists user
(
    id         bigint auto_increment
        primary key,
    first_name varchar(127) null,
    username   varchar(127) null,
    last_name  varchar(127) null,
    u_password varchar(255) null,
    email      varchar(127) null,
    enabled    tinyint(1)   not null,
    constraint user_email_uindex
        unique (email),
    constraint user_username_uindex
        unique (username)
);
-- bind user & book
create table if not exists user_book
(
    user_id bigint not null,
    book_id bigint not null,
    constraint book_id_fk
        foreign key (book_id) references book (id),
    constraint user_b_id_fk
        foreign key (user_id) references user (id)
);
-- bind user & journal
create table if not exists user_journal
(
    user_id    bigint not null,
    journal_id bigint not null,
    constraint journal_id_u_fk
        foreign key (journal_id) references journal (id),
    constraint user_id_j_fk
        foreign key (user_id) references user (id)
);
-- bind user & newspaper
create table if not exists user_newspaper
(
    user_id      bigint not null,
    newspaper_id bigint not null,
    constraint user_newspaper_newspaper_id_fk
        foreign key (newspaper_id) references newspaper (id),
    constraint user_newspaper_user_id_fk
        foreign key (user_id) references user (id)
);
-- bind user & role
create table if not exists user_role
(
    user_id bigint not null,
    role_id bigint not null,
    constraint role_id_fk
        foreign key (role_id) references role (id),
    constraint user_id_fk
        foreign key (user_id) references user (id)
);

-- clean
set foreign_key_checks = 0;
truncate table author;
truncate table book;
truncate table journal;
truncate table newspaper;
truncate table role;
truncate table user;
truncate table user_book;
truncate table user_journal;
truncate table user_newspaper;
truncate table user_role;
set foreign_key_checks = 1;

-- add user
INSERT INTO library.user (first_name, username, last_name, u_password, email, enabled)
VALUES ('Alex', 'user', 'Ivanov', '123', 'alex@mail.ru', 1);

INSERT INTO library.user (first_name, username, last_name, u_password, email, enabled)
VALUES ('Ivan', 'user1', 'Gogen', '123', 'ivan@mail.ru', 1);

-- add admin
INSERT INTO library.user (first_name, username, last_name, u_password, email, enabled)
VALUES ('Pasha', 'admin', 'Kogevnikov', '123', 'pasha@mail.ru', 1);

-- add role admin
INSERT INTO library.role (name, description)
VALUES ('ADMIN', 'Has admin');

-- add role user
INSERT INTO library.role (name, description)
VALUES ('USER', 'Has admin');

-- bind user & role
INSERT INTO library.user_role (user_id, role_id)
VALUES (1, 2);
INSERT INTO library.user_role (user_id, role_id)
VALUES (2, 2);
INSERT INTO library.user_role (user_id, role_id)
VALUES (3, 1);

-- add author
INSERT INTO library.author (first_name, last_name, description)
VALUES ('Aleksandr', 'Pushkin', 'Писатель');
INSERT INTO library.author (first_name, last_name, description)
VALUES ('Lev', 'Tolsoy', 'Писатель');
INSERT INTO library.author (first_name, last_name, description)
VALUES ('Fedor', 'Dostoevskiy', 'Писатель');
INSERT INTO library.author (first_name, last_name, description)
VALUES ('Gosha', 'Petrov', 'Создатель журнала');
INSERT INTO library.author (first_name, last_name, description)
VALUES ('Ilya', 'Bordin', 'Создатель газеты');


-- add book
INSERT INTO library.book (name, description, created_date, body, author)
VALUES ('Чудное мгновение', 'Стихотворение', '2021-12-11 19:09:58',
        'Я помню чудное мгновенье: Передо мной явилась ты, Как мимолётное виденье, Как гений чистой красоты. В томленьях грусти безнадежной, В тревогах шумной суеты, Звучал мне долго голос нежный, И снились милые черты. Шли годы. Бурь порыв мятежный. Рассеял прежние мечты, И я забыл твой голос нежный, Твои небесные черты.',
        1);
INSERT INTO library.book (name, description, created_date, body, author)
VALUES ('Дурень', 'Стихотворение', '2021-12-11 19:09:58',
        'Задумал дурень
На Русь гуляти,
Людей видати,
Себя казати.
Увидел дурень
Две избы пусты;
Глянул в подполье:
В подполье черти,
Востроголовы,
Глаза, что ложки,
Усы, что вилы,
Руки, что грабли,
В карты играют,
Костью бросают,
Деньги считают.
Дурень им молвил:
«Бог да на помочь
Вам, добрым людям».
Черти не любят,—
Схватили дурня,
Зачали бити.
Стали давити,
Еле живого
Дурня пустили.
Приходит дурень
Домой, сам плачет,
На голос воет.
А мать бранити,
Жена пеняти,
Сестра-то тоже:
«Дурень ты дурень,
Глупый ты Бабин,
То же ты слово
Не так бы молвил;
А ты бы молвил:
«Будь ты, враг, проклят
Имем господним!»
Черти ушли бы,
Тебе бы, дурню,
Деньги достались
Заместо клада».
«Добро же, баба,
Ты, бабариха.
Матерь Лукерья,
Сестра Чернава,
Вперед я, дурень,
Таков не буду».
Пошел он, дурень,
На Русь гуляти,
Людей видати,
Себя казати.
Увидел дурень,—
Четырех братов,—
Ячмень молотят.
Он братьям молвил:
«Будь ты, враг, проклят
Имем господним!»
Как сграбят дурня
Четыре брата,
Зачали бити,
Еле живого
Дурня пустили.
Приходит дурень
Домой, сам плачет,
На голос воет.
А мать бранити,
Жена пеняти,
Сестра-то также:
«Дурень ты дурень,
Глупый ты Бабин,
То же ты слово
Не так бы молвил.
Ты бы им молвил:
«Бог вам на помочь,
Чтоб по сту на день,
Чтоб не сносити».
«Добро же, баба,
Ты, бабаряха,
Матерь Лукерья,
Сестра Чернава,
Вперед я, дурень,
Таков не буду».
Пошел он, дурень,
На Русь гуляти,
Людей видати,
Себя казати.
Увидел дурень,—
Семеро братьев
Матерь хоронят;
Все они плачут,
Голосом воют.
Он им и молвил:
«Бог вам на помочь,
Семеро братьев,
Мать хоронити,
Чтоб по сту на день,
Чтоб не сносити».
Сграбили дурня
Семеро братьев,
Зачали бити,
Стали таскати,
В грязи валяти,
Еле живого
Дурня пустили.
Идет он, дурень,
Домой да плачет,
На голос воет.
А мать бранити,
Жена пеняти,
Сестра-то также:
«Дурень ты дурень,
То же ты слово
Не так бы молвил,
А ты бы молвил:
«Канун да ладан,
Дай же господь бог
Царство небесно,
Пресветлый рай ей».
Тебя бы, дурня,
Там накормили
Кутьей с блинами».
«Добро же, баба,
Ты, бабариха,
Матерь Лукерья,
Вперед я, дурень,
Таков не буду».
Пошел он, дурень,
На Русь гуляти,
Людей видати,
Себя казати;
Навстречу свадьба,—
Он им и молвил:
«Канун да ладан,
Дай господь бог вам
Царство небесно,
Пресветлый рай всем».
Скочили дружки,
Схватили дурня,
Зачали бити,
Плетьми стегати,
В лицо хлестати.
Пошел, заплакал,
Идет да воет.',
        2);

INSERT INTO library.book (name, description, created_date, body, author)
VALUES ('Таракан', 'Стихотворение', '2021-12-11 19:09:58',
           'Жил на свете таракан,
Таракан от детства,
И потом попал в стакан,
Полный мухоедства.',
           3);

-- add journal
INSERT INTO library.journal (name, description, created_date, body, author)
VALUES ('Cosmopolitan', 'Журнал', '2021-12-11 19:16:28',
        'Последние модные тренды и бьюти-советы на каждый день, обзоры и мнения экспертов моды, красоты и психологии, новости звезд и эксклюзивные интервью',
        4);

-- add newspaper
INSERT INTO library.newspaper (name, description, created_date, body, author)
VALUES ('Газета.ру', 'газета', '2021-12-11 19:19:56',
        'Главные новости - Газета.Ru. Картина дня. У подростков хотят отобрать петарды.',
        5);

