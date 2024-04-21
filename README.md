# Library Management System 

This project is a Library Management System and it was built using Docker and the Java SpringBoot Framework. It allows 
Librarians to manage books, patrons and borrowing records of a Library.

## Basic Steps to run and test the Project
### 1. Download or Clone the Project from Github [here](https://github.com/princejohnnie/libmgt-backend)
- Inside any folder or directory in your computer, download and unzip the git file containing the project.
- Alternatively, you can clone the project from Github by running the following command `git clone https://github.com/princejohnnie/libmgt-backend.git` in any terminal on your computer.

### 2. Run the Application
**NOTE:** You must have Docker installed on your computer in order to test this project
- If you do not have Docker installed on your computer, you can download Docker from the official Docker website [here](https://www.docker.com/get-started/)
  - Download the file for your Operating System and confirm that Docker was installed successfully by running the command `docker --version ` on your termianl to see the verion of docker that was installed.
- Open your terminal from the directory where you downloaded or cloned the project and enter into the folder named `libmgt-backend`.
- In the above terminal run the following command: `docker compose up --build`.
**NOTE:** Please ensure you are connected to the internet while running this command to enable Docker download the reuired docker image and build the Dockerfile in the project.
- After Docker builds successfully, we should have the Database service listening on `localhost:5432` and the Backend(Springboot) service listening on `localhost:8081`.

### 3. Test the Application
- You can test the application by sending requests to the endpoints on Postman or using the curl command on your terminal.

## API Endpoints
This project comprises of three major entities; Librarian, Book and Patron
- A Librarian manages Books and Patrons in the library and also borrows books to Patrons.
- Patrons are students or individuals that use the Library.
- A Book is a single book instance that can be borrowed

### 1. Authentication
> **POST:** `http://localhost:8081/api/register`
 - Registers a new librarian in the system and returns a bearer token used to authenticate the Librarian.
```
Sample Request
{
    "email": "johnny@gmail.com",
    "name": "John Prince",
    "password": "password"
}
```
```
Sample Response
eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MTM3Mzg4MDEsImV4cCI6MTcxMzgyNTIwMSwic3ViIjoiMiJ9.sCY7A5ZYCwuJftZjvUS-JtzQF2LkrXEHaccrvPn7ObqwdTJB7adfm31mJjM-QKyWvE1abmNWPhm2dihupu9PnQ
```
> **POST:** `http://localhost:8081/api/login`
 - Login an existing Librarian to the system and returns a Bearer token used to authenticate the Librarian.
```
Sample Request
{
    "email": "johnny@gmail.com",
    "password": "password"
}
```
```
Sample Response
eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MTM3Mzg5NDIsImV4cCI6MTcxMzgyNTM0Miwic3ViIjoiMiJ9.Q7cfBxyKAU2-x42uHCL9HIT0cX1jxrFKX1GeEUUgiBfXeA2ko0S8ezo35bce18RVfNVmIJdaRQF3A4k7UoWgeQ
```
> **GET:** `http://localhost:8081/api/auth`
 - Returns the current authenticated librarian using the Bearer token
```
Sample Response
{
    "id": 2,
    "email": "johnny@gmail.com",
    "name": "John Prince"
}
```

### 2. Librarian
> **GET:** `http://localhost:8081/api/librarians`
 - Returns all Librarians in the Library. **NOTE:** This is a paginated endpoint
```
Sample Response
{
    "_embedded": {
        "items": [
            {
                "id": 1,
                "email": "johnny5@gmail.com",
                "name": "John Uzo"
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8081/api/librarians?page=0&size=2000&sort=email,asc"
        }
    },
    "page": {
        "size": 2000,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```
> **GET:** `http://localhost:8081/api/librarians/{id}`
 - Returns a single Librarian with the specified librarian id.
```
Sample Response
{
    "id": 1,
    "email": "johnny5@gmail.com",
    "name": "John Uzo"
}
```
> **PUT:** `http://localhost:8081/api/librarians/{id}`
 - Updates the details of the Librarian with the specified id. **NOTE:** This is an authenticated endpoint
```
Sample Request
{
    "email": "charlie123@gmail.com",
    "name": "Mbonu Ibeabuchi"
}
```
```
Sample Response
{
    "id": 1,
    "email": "charlie123@gmail.com",
    "name": "Mbonu Ibeabuchi"
}
```
> **DELETE:** `http://localhost:8081/api/librarians/{id}`
 - Deletes a Librarian profile from the system. **NOTE:** This is an authenticated endpoint and can only be deleted by the owner of the profile.

### 3. Borrow Records
> **POST:** `http://localhost:8081/api/borrow/{bookId}/patron/{patronId}`
 - Borrows a book specified by the bookId to a particular Patron specified by the patronId. **NOTE:** This can only be done by an authenticated Librarian and a particular Patron cannot borrow the same book twice.
```
Sample Response
{
    "id": 1,
    "borrowDate": "2024-04-21",
    "returnDate": null,
    "book": {
        "id": 1,
        "title": "Things fall",
        "author": "Chinua Achebe",
        "isbn": "8900-2920-2729-2828",
        "publicationYear": "2016"
    },
    "patron": {
        "id": 1,
        "name": "Mbonu Charlie",
        "contact": "08132468741"
    },
    "librarian": {
        "id": 3,
        "email": "johnny2@gmail.com",
        "name": "John Prince"
    }
}
```
> **PUT:** `http://localhost:8081/api/return/{bookId}/patron/{patronId}`
 - Records the return of a borrowed book by a particular Patron specified by the patronId. **NOTE:** This can only be done by the authenticated Librarian that borrowed the book to the Patron.
```
Sample Response
{
    "id": 1,
    "borrowDate": "2024-04-21",
    "returnDate": "2024-04-21",
    "book": {
        "id": 1,
        "title": "Things fall",
        "author": "Chinua Achebe",
        "isbn": "8900-2920-2729-2828",
        "publicationYear": "2016"
    },
    "patron": {
        "id": 1,
        "name": "Mbonu Charlie",
        "contact": "08132468741"
    },
    "librarian": {
        "id": 3,
        "email": "johnny2@gmail.com",
        "name": "John Prince"
    }
}
```

### 4. Book
> **GET:** `http://localhost:8081/api/books`
 - Returns the list of books in the Library. **NOTE:** This is a paginated endpoint.
```
Sample Response
{
    "_embedded": {
        "items": [
            {
                "id": 1,
                "title": "Things fall",
                "author": "Chinua Achebe",
                "isbn": "8900-2920-2729-2828",
                "publicationYear": "2016"
            },
            {
                "id": 2,
                "title": "Purple Hibiscus",
                "author": "Amanda Achebe",
                "isbn": "8900-2920-2729-2828",
                "publicationYear": "2016"
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8081/api/books?page=0&size=2000&sort=publicationYear,asc"
        }
    },
    "page": {
        "size": 2000,
        "totalElements": 2,
        "totalPages": 1,
        "number": 0
    }
}
```
> **GET:** `http://localhost:8081/api/books/{id}`
 - Returns a single Book with the specified book id.
```
Sample Response
{
    "id": 2,
    "title": "Purple Hibiscus",
    "author": "Amanda Achebe",
    "isbn": "8900-2920-2729-2828",
    "publicationYear": "2016"
}
```
> **POST:** `http://localhost:8081/api/books`
 - Adds a new Book to the Library. **NOTE:** This is an authenticated endpoint and can only be done by a Librarian.
```
Sample Request
{
    "title": "Purple Hibiscus",
    "author": "Amanda Achebe",
    "isbn": "8900-2920-2729-2828",
    "publicationYear": "2016"
}
```
```
Sample Response
{
    "id": 2,
    "title": "Purple Hibiscus",
    "author": "Amanda Achebe",
    "isbn": "8900-2920-2729-2828",
    "publicationYear": "2016"
}
```
> **PUT:** `http://localhost:8081/api/books/{id}`
 - Updates the details of a Book with the specified id. **NOTE:** This can only be done by an authenticated Librarian.
```
Sample Request
{
    "title": "Half a Yellow Sun",
    "author": "Amanda Achebe",
    "isbn": "8900-2920-2729-2828",
    "publicationYear": "2016"
}
```
```
Sample Response
{
    "id": 2,
    "title": "Half a Yellow Sun",
    "author": "Amanda Achebe",
    "isbn": "8900-2920-2729-2828",
    "publicationYear": "2016"
}
```
> **DELETE:** `http://localhost:8081/api/books/{id}`
 - Deletes a Book with the specified id from the Library. **NOTE:** This can only be done by an authenticated Librarian.

### 5. Patron
> **GET:** `http://localhost:8081/api/patrons`
 - Returns the list of patrons that have access to the Library. **NOTE:** This is a paginated endpoint.
```
Sample Response
{
    "_embedded": {
        "items": [
            {
                "id": 1,
                "name": "Mbonu Charlie",
                "contact": "08132468741"
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8081/api/patrons?page=0&size=2000&sort=name,asc"
        }
    },
    "page": {
        "size": 2000,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```
> **GET:** `http://localhost:8081/api/patrons/{id}`
 - Returns a single Patron with the specified patron id.
```
Sample Response
{
    "id": 1,
    "name": "Mbonu Charlie",
    "contact": "08132468741"
}
```
> **POST:** `http://localhost:8081/api/patrons`
- Gives a new Patron access to the Library. **NOTE:** This can only be done by an authenticated Librarian.
```
Sample Request
{
    "name": "Mbonu Charlie",
    "contact": "08132468741"
}
```
```
Sample Response
{
    "id": 1,
    "name": "Mbonu Charlie",
    "contact": "08132468741"
}
```
> **PUT:** `http://localhost:8081/api/patrons/{id}`
- Updates the details of a Patron with the specified id. **NOTE:** This can only be done by an authenticated Librarian.
```
Sample Request
{
    "name": "John Prince",
    "contact": "08132468741"
}
```
```
Sample Response
{
    "id": 1,
    "name": "John Prince",
    "contact": "08132468741"
}
```
> **DELETE:** `http://localhost:8081/api/patrons/{id}`
- Removes access of a Patron with the specified id from the Library. **NOTE:** This can only be done by an authenticated Librarian.
