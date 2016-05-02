--!/bin/psql
-- vim: ts=2 sw=2 sts=2

-- 1
SELECT DisplayName
FROM Users;

-- 2
SELECT TOP 100
  Title,
  Body,
  OwnerDisplayName
FROM Posts
WHERE PostTypeId = 1
ORDER BY CreationDate;

-- 3
SELECT SELECT count(*)
FROM Users, 
SELECT count(*)
FROM Posts, 
WHERE PostTypeId = 1
UNION
FROM Posts
WHERE PostTypeId = 2
UNION
SELECT count(*)
FROM Tags AS TagsCount;

-- 4
SELECT
  DisplayName,
  Location
FROM Users
WHERE Age > 25 AND year(CreationDate) = 2015;

-- 5
SELECT count(*)
FROM Users
WHERE Age > 25 AND year(CreationDate) = 2015;

-- 6
SELECT
  count(*)
FROM users
WHERE Location LIKE '%Germany%';

-- 7
SELECT
  Location,
  count(*)
FROM Users
GROUP BY Location;

-- 8
SELECT
  OwnerUserId AS MyId,
  (SELECT count(*) FROM Posts WHERE OwnerUserId = MyId AND PostTypeId = 1)
FROM Posts
WHERE OwnerUserId IN
      (SELECT TOP 10 Id
       FROM Users
       ORDER BY Reputation DESC)
GROUP BY OwnerUserId;

-- 9
SELECT OwnerUserId
FROM Posts
WHERE PostTypeId = 1 AND Tags LIKE '%sql%'
GROUP BY OwnerUserId
HAVING count(*) > 3
INTERSECT
SELECT OwnerUserId
FROM posts
WHERE
  'java' IN (SELECT tagname
             FROM tags
               JOIN postTags ON tags.id = postTags.tagId
             WHERE postTags.postId = parentId)
GROUP BY OwnerUserId
HAVING count(*) > 9;

-- 10
SELECT DISTINCT
  C1.UserId,
  C2.UserId
FROM Comments C1 JOIN (SELECT
                         Comments.UserId,
                         Posts.OwnerUserId
                       FROM Comments
                         JOIN Posts ON Comments.PostId = Posts.Id) C2 ON C1.UserId = C2.OwnerUserId;

-- 11
SELECT count(*)
FROM Posts P
WHERE P.PostTypeId = 1 AND (SELECT CommentCount
                            FROM Posts
                            WHERE Id = P.AcceptedAnswerId) * 2 < (SELECT MAX(CommentCount)
                                                                  FROM Posts
                                                                  WHERE ParentId = P.Id);

-- 12
SELECT Id, (SELECT TOP 1 Id FROM Posts WHERE ParentId = P.Id ORDER BY CommentCount DESC)
FROM Posts P
WHERE P.PostTypeId = 1;

-- 13
SELECT DISTINCT U.*
FROM   Users U,
       Posts P,
       (SELECT postid
        FROM   PostTags
        WHERE  tagid = ANY (SELECT TOP 10 id
                            FROM   Tags
                            ORDER  BY Tags.count DESC)
        GROUP  BY postid
        HAVING Count (*) >= 2) AS p2
WHERE  P.posttypeid = 2
       AND P.owneruserid = U.id
       AND P2.postid = P.id
UNION
SELECT DISTINCT U.*
FROM   Users U,
       Posts P,
       Comments C,
       (SELECT postid
        FROM   posttags
        WHERE  tagid = ANY (SELECT TOP 10 id
                            FROM   tags
                            ORDER  BY tags.count DESC)
        GROUP  BY postid
        HAVING Count (*) >= 2) AS P2
WHERE  ( P.id = P2.postid
         AND P.posttypeid = 1
         AND C.postid = p2.postid
         AND C.userid = u.id )
        OR ( P2.postid = P.ParentId
             AND P.posttypeid = 2
             AND C.postid = P.id
             AND C.userid = U.id ) SELECT * FROM TopTags;
