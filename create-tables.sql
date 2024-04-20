BEGIN;

CREATE TABLE books (
    id serial not null primary key,
    title text not null,
    author text not null,
    isbn varchar(255) default null,
    publication_year varchar(50) default null,
    created_at timestamp not null default now(),
    updated_at timestamp null default now()
);

CREATE TABLE patrons (
    id serial not null primary key,
    name text not null,
    contact varchar(50) default null,
    created_at timestamp not null default now(),
    updated_at timestamp null default now()
);

CREATE TABLE librarians (
    id serial not null primary key,
    email varchar(100) not null,
    name text not null,
    password varchar(255) not null,
    created_at timestamp not null default now(),
    updated_at timestamp null default now()
);

CREATE TABLE borrow_records (
    id serial not null primary key,
    book_id integer not null references books (id),
    patron_id integer not null references patrons (id),
    borrow_date timestamp not null default now(),
    return_date timestamp null default null
);

COMMIT;