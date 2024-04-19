BEGIN;

CREATE TABLE books (
    id serial not null primary key,
    title text not null,
    author text not null,
    isbn varchar(255) default null,
    publication_year date not null,
    CONSTRAINT date_must_be_year CHECK ( date_trunc('year', publication_year) = publication_year ),
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

COMMIT;