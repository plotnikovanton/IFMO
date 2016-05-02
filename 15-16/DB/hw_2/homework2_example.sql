-- В языке SQL строчные комментарии начинают с символов '--'

-- Данный пример работает с СУБД MySQL, но с небольшими изменениями его можно использовать
-- в любой другой СУБД

-- Для выполнения команд можно использовать командну mysql в командной строке или инструменты для управления СУБД.
-- Команда для исполнения пакетного файла с sql-командами в командной строке выглядит следующим образом:
--   mysql -u root -e "create database UniversityDb;"
--   mysql -u root -D UniversityDb -e "source homework2_example.sql;"
--
-- В качестве графической оболочки для доступа к СУБД можно использовать, например, MySQL Workbench:
-- https://www.mysql.com/products/workbench/

-- Рассмотрим пример простой базу данных для хранения персональных данных физических лиц,
-- а также студентах и университетах, в которых они обучаются

-- Описание таблицы Persons для хранения персональных данных "физических лиц"
create table Persons (
       personId int auto_increment not null,  -- ключевое поле
       name nvarchar(100) not null,           -- имя человека
       address nvarchar(300) not null,        -- адрес
       primary key (PersonId)                 -- описание основного ключа, состоящего из единственного поля PersonId
);

-- Описание таблицы Universities для хранения данных об университетах
create table Universities (
       universityId int auto_increment not null, -- ключевое поле
       name nvarchar(100) not null,              -- название университетах

       primary key (universityId)                -- основной ключ
);

-- Описание таблицы Students для хранения данных о студентах ВУЗов
create table Students (
       personId int not null,                  -- студент содержит все данные, которые есть у "физического лица",
                                               -- поле personId представляет из себя ссылку на соответствующую запись в таблице
                                               -- Persons.

       isWorking bit not null,                 -- булевое поле, совмещал ли студент учёбу с работой?

       primary key (personId),                 -- основной ключ

       foreign key (personId) references Persons(personId) -- внешний ключ, проверяющий наличие записи со значением personId в таблице Persons
);

-- NB! Отношение между таблицами Students и Persons аналагично "наследованию" в ООП


-- Описание отношения "студент обучается в университете"
create table StudentStudiesAtUniversity (
       studId int not null,   -- ссылка на поле studentId в таблице Students
       univId int not null,   -- ссылка на поле universityId в таблице Universities

       admissionDate date not null,  -- дата поступления

       foreign key (studId) references Students(personId), -- ограничение целостности(внешний ключ), проверяющее что
                                                           -- в таблице Students есть запись со studentId == studId.
       foreign key (univId) references Universities(universityId), -- внешний ключ, проверяющий ссылку на таблицу Universities

       primary key (studId, univId) -- основной ключ, запрещающий создавать несколько записей с
                                    -- одинаковыми значениями в ОБОИХ полях (studId, univId) одновременно
);


-- Пример синтаксиса для изменения таблицы
alter table StudentStudiesAtUniversity drop column admissionDate;                 -- удалить колонку admissionDate из таблицы StudentStudiesAtUniversity
alter table StudentStudiesAtUniversity add column admissionDate date;             -- снова добавить колонку admissionDate
alter table StudentStudiesAtUniversity modify column admissionDate date not null; -- добавить ограничение not null, запрещающее "пустое значение"(null), для колонки admissionDate
