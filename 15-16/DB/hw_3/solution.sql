-- vim: ts=2 sw=2 sts=2
DROP TABLE IF EXISTS AudioTrackStoreNF1;

-- Выкинем повторяющеяся строки получим нф1
SELECT DISTINCT *
INTO AudioTrackStoreNF1
FROM AudioTrackStore;
ALTER TABLE AudioTrackStoreNF1 ADD PRIMARY KEY (TrackId, PlaylistId, OrderId);

-- Приведем к нф2
DROP TABLE IF EXISTS NF2Quantity;
DROP TABLE IF EXISTS NF2Track;
DROP TABLE IF EXISTS NF2Order;
DROP TABLE IF EXISTS NF2Playlist;

SELECT DISTINCT
  TrackId,
  OrderId,
  PlaylistId,
  OrderTrackQuantity
INTO NF2Quantity
FROM AudioTrackStoreNF1;
ALTER TABLE NF2Quantity ADD PRIMARY KEY (TrackId, PlaylistId, OrderId);

SELECT DISTINCT
  TrackId,
  ArtistId,
  ArtistName,
  AlbumId,
  AlbumTitle,
  TrackName,
  TrackLength,
  TrackGenre,
  TrackPrice
INTO NF2Track
FROM AudioTrackStoreNF1;
ALTER TABLE NF2Track ADD PRIMARY KEY (TrackId);

SELECT DISTINCT
  PlaylistId,
  PlaylistName
INTO NF2Playlist
FROM AudioTrackStoreNF1;
ALTER TABLE NF2Playlist ADD PRIMARY KEY (PlaylistId);

SELECT DISTINCT
  OrderId,
  CustomerId,
  CustomerName,
  CustomerEmail,
  OrderDate,
  DeliveryAddress
INTO NF2Order
FROM AudioTrackStoreNF1;
ALTER TABLE NF2Order ADD PRIMARY KEY (OrderId);


ALTER TABLE NF2Quantity ADD FOREIGN KEY (TrackId)
REFERENCES NF2Track (TrackId);
ALTER TABLE NF2Quantity ADD FOREIGN KEY (PlaylistId)
REFERENCES NF2Playlist (PlaylistId);
ALTER TABLE NF2Quantity ADD FOREIGN KEY (OrderId)
REFERENCES NF2Order (OrderId);

-- Назад к 1нф
-- тут и далее оставлены генерируемые названия столбцов и дубликаты тех столбцов
-- по которым происходит JOIN для повышения читаемости кода
DROP TABLE IF EXISTS FromNF2ToNF1;

SELECT *
INTO FromNF2ToNF1
FROM (
  (SELECT *
   FROM (NF2Quantity
     JOIN NF2Order ON NF2Quantity.OrderId = NF2Order.OrderId)
     JOIN NF2Track ON NF2Quantity.TrackId = NF2Track.TrackId
     JOIN NF2Playlist ON NF2Quantity.PlaylistId = NF2Playlist.PlaylistId)
);

-- Привидем к 3нф она же нфбк
DROP TABLE IF EXISTS NF3Quantity CASCADE;
DROP TABLE IF EXISTS NF3Track CASCADE;
DROP TABLE IF EXISTS NF3Album CASCADE;
DROP TABLE IF EXISTS NF3Artist CASCADE;
DROP TABLE IF EXISTS NF3Playlist CASCADE;
DROP TABLE IF EXISTS NF3Order CASCADE;
DROP TABLE IF EXISTS NF3Customer CASCADE;

SELECT *
INTO NF3Quantity
FROM NF2Quantity;
ALTER TABLE NF3Quantity ADD PRIMARY KEY (TrackId, PlaylistId, OrderId);

SELECT DISTINCT
  ArtistId,
  ArtistName
INTO NF3Artist
FROM NF2Track;
ALTER TABLE NF3Artist ADD PRIMARY KEY (ArtistId);

SELECT DISTINCT
  AlbumId,
  AlbumTitle,
  ArtistId
INTO NF3Album
FROM NF2Track;
ALTER TABLE NF3Album ADD PRIMARY KEY (AlbumId);
ALTER TABLE NF3Album ADD FOREIGN KEY (ArtistId)
REFERENCES NF3Artist (ArtistId);

SELECT DISTINCT
  TrackId,
  TrackName,
  TrackGenre,
  TrackPrice,
  TrackLength,
  ArtistId,
  AlbumId
INTO NF3Track
FROM NF2Track;
ALTER TABLE NF3Track ADD PRIMARY KEY (TrackId);
ALTER TABLE NF3Track ADD FOREIGN KEY (ArtistId)
REFERENCES NF3Artist (ArtistId);
ALTER TABLE NF3Track ADD FOREIGN KEY (AlbumId)
REFERENCES NF3Album (AlbumId);

SELECT *
INTO NF3Playlist
FROM NF2Playlist;
ALTER TABLE NF3Playlist ADD PRIMARY KEY (PlaylistId);

SELECT DISTINCT
  CustomerId,
  CustomerName,
  CustomerEmail
INTO NF3Customer
FROM NF2Order;
ALTER TABLE NF3Customer ADD PRIMARY KEY (CustomerId);

SELECT DISTINCT
  OrderId,
  CustomerId,
  DeliveryAddress,
  OrderDate
INTO NF3Order
FROM NF2Order;
ALTER TABLE NF3Order ADD PRIMARY KEY (OrderId);
ALTER TABLE NF3Order ADD FOREIGN KEY (CustomerId)
REFERENCES NF3Customer (CustomerId);

ALTER TABLE NF3Quantity ADD FOREIGN KEY (TrackId)
REFERENCES NF3Track (TrackId);
ALTER TABLE NF3Quantity ADD FOREIGN KEY (PlaylistId)
REFERENCES NF3Playlist (PlaylistId);
ALTER TABLE NF3Quantity ADD FOREIGN KEY (OrderId)
REFERENCES NF3Order (OrderId);

-- Назад к 1нф
DROP TABLE IF EXISTS FromNF3ToNF1;

SELECT *
INTO FromNF3ToNF1
FROM (
    (SELECT *
     FROM (NF3Quantity
       JOIN NF3Playlist ON NF3Quantity.PlaylistId = NF3Playlist.PlaylistId)
       JOIN (
         SELECT *
         FROM (NF3Track
           JOIN NF3Album ON NF3Track.AlbumId = NF3Album.AlbumId)
           JOIN NF3Artist ON NF3Track.ArtistId = NF3Artist.ArtistId
         ) ON NF3Quantity.TrackId = NF3Track.TrackId)
    JOIN (
      NF3Order
      JOIN NF3Customer ON NF3Order.CustomerId = NF3Customer.CustomerId
    ) ON NF3Quantity.OrderId = NF3Order.OrderId
);

-- 4нф
-- Изменения коснутся только NF3Quantity все остальные таблицы уже в 4нф
DROP TABLE IF EXISTS NF4TrackQuantity;
DROP TABLE IF EXISTS NF4TrackInPlaylist;

SELECT DISTINCT
  TrackId,
  OrderTrackQuantity
INTO NF4TrackQuantity
FROM NF3Quantity;
ALTER TABLE NF4TrackQuantity ADD PRIMARY KEY (TrackId);
ALTER TABLE NF4TrackQuantity ADD FOREIGN KEY (TrackId) REFERENCES NF3Track (TrackId);

SELECT DISTINCT
  TrackId,
  PlaylistId
INTO NF4TrackInPlaylist
FROM NF3Quantity;
ALTER TABLE NF4TrackInPlaylist ADD PRIMARY KEY (TrackId, PlaylistId);
ALTER TABLE NF4TrackInPlaylist ADD FOREIGN KEY (TrackId) REFERENCES NF3Track (TrackId);








