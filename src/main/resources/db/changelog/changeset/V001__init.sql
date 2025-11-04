create sequence authors_id_seq
    as integer;

alter sequence authors_id_seq owner to postgres;

create sequence genres_id_seq
    as integer;

alter sequence genres_id_seq owner to postgres;

create sequence books_id_seq
    as integer;

alter sequence books_id_seq owner to postgres;

create sequence readers_id_seq
    as integer;

alter sequence readers_id_seq owner to postgres;

create sequence borrowings_id_seq
    as integer;

alter sequence borrowings_id_seq owner to postgres;

create type borrowing_status as enum ('BORROWED', 'RETURNED', 'OVERDUE');

alter type borrowing_status owner to postgres;

create table authors
(
    id         bigint default nextval('authors_id_seq'::regclass) not null
        primary key,
    name       varchar(100)                                       not null,
    birth_date date,
    country    varchar(50)
);

alter table authors
    owner to postgres;

alter sequence authors_id_seq owned by authors.id;

create table genres
(
    id   bigint default nextval('genres_id_seq'::regclass) not null
        primary key,
    name varchar(50)                                       not null
        unique
);

alter table genres
    owner to postgres;

alter sequence genres_id_seq owned by genres.id;

create table books
(
    id               bigint  default nextval('books_id_seq'::regclass) not null
        primary key,
    title            varchar(500)                                      not null,
    author_id        bigint                                            not null
        references authors
            on delete cascade,
    genre_id         bigint
                                                                       references genres
                                                                           on delete set null,
    published_year   integer
        constraint books_published_year_check
            check (published_year > 0),
    available_copies integer default 1
        constraint books_available_copies_check
            check (available_copies >= 0),
    total_copies     integer default 1
        constraint books_total_copies_check
            check (total_copies >= 1)
);

alter table books
    owner to postgres;

alter sequence books_id_seq owned by books.id;

create index idx_books_author
    on books (author_id);

create index idx_books_genre
    on books (genre_id);

create table readers
(
    id              bigint  default nextval('readers_id_seq'::regclass) not null
        primary key,
    name            varchar(100)                                        not null,
    email           varchar(100)                                        not null
        unique,
    phone           varchar(20),
    membership_date date    default CURRENT_DATE,
    is_active       boolean default true
);

alter table readers
    owner to postgres;

alter sequence readers_id_seq owned by readers.id;

create index idx_readers_email
    on readers (email);

create table borrowings
(
    id            bigint           default nextval('borrowings_id_seq'::regclass) not null
        primary key,
    book_id       bigint                                                          not null
        references books
            on delete cascade,
    reader_id     bigint                                                          not null
        references readers
            on delete cascade,
    borrowed_date date             default CURRENT_DATE                           not null,
    due_date      date                                                            not null,
    returned_date date,
    status        borrowing_status default 'BORROWED'::borrowing_status           not null
);

alter table borrowings
    owner to postgres;

alter sequence borrowings_id_seq owned by borrowings.id;

create index idx_borrowings_status
    on borrowings (status);

create view active_borrowings(borrowing_id, reader_name, book_title, borrowed_date, days_borrowed) as
SELECT b.id                           AS borrowing_id,
       r.name                         AS reader_name,
       bk.title                       AS book_title,
       b.borrowed_date,
       CURRENT_DATE - b.borrowed_date AS days_borrowed
FROM borrowings b
         JOIN readers r ON b.reader_id = r.id
         JOIN books bk ON b.book_id = bk.id
WHERE b.returned_date IS NULL;

alter table active_borrowings
    owner to postgres;

create function update_available_copies() returns trigger
    language plpgsql
as
$$
BEGIN
    IF
TG_OP = 'INSERT' THEN
UPDATE books
SET available_copies = available_copies - 1
WHERE id = NEW.book_id;

ELSIF
TG_OP = 'UPDATE' AND NEW.return_date IS NOT NULL AND OLD.return_date IS NULL THEN
UPDATE books
SET available_copies = available_copies + 1
WHERE id = NEW.book_id;
END IF;

RETURN NEW;
END;
$$;

alter function update_available_copies() owner to postgres;

create trigger trg_update_available_copies
    after insert or
update
    on borrowings
    for each row
    execute procedure update_available_copies();