-- vim: ts=2 sw=2 sts=2

CREATE INDEX Login_Idx              ON Login                    (login, passMd5);
CREATE INDEX LoginIdx               ON Login        USING hash  (PersonalDataId);
CREATE INDEX LoginIdIdx             ON Login                    (id);
CREATE INDEX BirthDateIdx           ON PersonalData             (birthDate);
CREATE INDEX LastFirstNameIdx       ON PersonalData             (LastName, FirstName);
CREATE INDEX PersonIdIdx            ON PersonalData USING hash  (id);
CREATE INDEX OwnerIdIdx             ON Blog         USING hash  (OwnerId);
CREATE INDEX BlogCreationDateIdx    ON Blog                     (CreationDate);
CREATE INDEX PosterIdIdx            ON BlogPost     USING hash  (posterId);
CREATE INDEX BlogPostIdIdx          ON BlogPost     USING hash  (blogId);
CREATE INDEX BlogReaderIdx          ON BlogReader   USING hash  (readerId);
CREATE INDEX BlogIdIdx              ON Blog         USING hash  (blogId);
CREATE INDEX BlogNameIdx            ON Blog                     (name);

-- Получить количество пользователей с определённым логином и паролем
select count(*) from Login
  where login = 'achromatic_color' and passMd5 = '0e40fde52d2de04e2dd398eb0b6fc44f';

-- Получить всех пользователей, родившихся в определённый месяц определённого года
select *
  from PersonalData
    join Login on Login.personalDataId = PersonalData.id
  where
    birthdate >= '1973-11-01' and birthdate <= '1973-11-30';

-- Получить данные пользователя с заданным полным именем
select *
  from PersonalData
    join Login on Login.personalDataId = PersonalData.id
  where lastName = 'Javier' and firstName = 'Sandy';

-- Получить персональные данные некоторого списка пользователей
select * from Login, PersonalData
  where PersonalData.id = Login.personalDataId
    and Login.Id in (123, 125, 256);

-- Получить все блоги, созданные некоторым пользователем
select * from Blog where ownerId = 1000;

-- Получить последние 100 созданных блогов и их создателей
select * from Blog, Login, PersonalData
  where Blog.ownerId = Login.id and PersonalData.id = Login.personalDataId
  order by creationDate limit 100;

-- Получить все блоги, название которых начинается с thum
select * from Blog
  where name like 'thum%';

-- Найти все посты некоторого пользователя, отсортированные по дате
select * from BlogPost
  where posterId = 1000
  order by creationDate;

-- Получить все посты некоторого блога, отсортированные по дате
select * from BlogPost
  where blogId = 1000
  order by creationDate;


-- Найти всех читателей блога
select readerId from BlogReader where blogId = 1000;

-- Найти все блоги, которые читает пользователь с некоторым id
select blogId from BlogReader where readerId = 1000;
