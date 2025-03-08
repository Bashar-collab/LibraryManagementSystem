# Library Management System

This is a Library Management System built with **Spring Boot** and **PostgreSQL**. It provides various APIs to manage books, patrons, borrowing, and returning of books in a library. It also includes authentication for accessing protected endpoints.

## Prerequisites

Before running the application, ensure you have the following:

- **Java 8+** installed.
- **Spring Boot** setup (or an IDE like IntelliJ IDEA or Eclipse for development).
- **Maven** installed for dependency management.
- **PostgreSQL (or any configured database)** running.
  - Ensure that you have a database ready with necessary configurations.
  - The application uses **JPA** to connect to a PostgreSQL database by default, but this can be changed.

## Getting Started

### 1. Clone the Repository

Clone the repository of the Library Management System:

```bash
git clone https://github.com/Bashar-collab/LibraryManagementSystem.git
cd LibraryManagementSystem
```

### 2. Set Up Database Configuration

Before running the application, you need to configure the database connection. This project uses **PostgreSQL** as the default database. Follow the steps below to update the database configuration in the `application.properties` file:

#### Update `application.properties`

1. Navigate to the `src/main/resources/application.properties` file.
2. Update the following properties with your database details:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Run the Application

Once the database configuration is done, you can run the application either using **Maven** or an IDE.

#### Using Maven

1. Open a terminal and navigate to the project directory.
2. Run the following command:

```bash
mvn spring-boot:run
```

### 4. Interact with API Endpoints

Once the application is running, the API is available at `http://localhost:8080`. Below are the main API endpoints you can interact with:

#### Books API

1. **Get All Books**
   - **Endpoint**: `GET /api/books`
   - **Description**: Fetches all books in the library.
   - **Response**: A list of books in JSON format.

2. **Get Book by ID**
   - **Endpoint**: `GET /api/books/{id}`
   - **Description**: Fetch a book by its ID.
   - **Response**: The book details if found, otherwise an error message.

3. **Add Book**
   - **Endpoint**: `POST /api/books`
   - **Description**: Adds a new book to the library.
   - **Request Body**: A `BooksDTO` object with details like `isbn`, `title`, `author`, etc.
   - **Response**: A confirmation message along with the created book details.

4. **Update Book**
   - **Endpoint**: `PUT /api/books/{id}`
   - **Description**: Updates an existing book in the library.
   - **Request Body**: A `BooksDTO` object with updated book details.
   - **Response**: A confirmation message along with the updated book details.

5. **Delete Book**
   - **Endpoint**: `DELETE /api/books/{id}`
   - **Description**: Deletes a book by its ID.
   - **Response**: A confirmation message.

#### Patrons API

1. **Get All Patrons**
   - **Endpoint**: `GET /api/patrons`
   - **Description**: Fetches all patrons of the library.
   - **Response**: A list of patrons in JSON format.

2. **Get Patron by ID**
   - **Endpoint**: `GET /api/patrons/{id}`
   - **Description**: Fetch patron details by their ID.
   - **Response**: The patron details if found, otherwise an error message.

3. **Add Patron**
   - **Endpoint**: `POST /api/patrons`
   - **Description**: Adds a new patron to the system.
   - **Request Body**: A `PatronsDTO` object with patron details like `name`, `contactInfo`, etc.
   - **Response**: A confirmation message along with the created patron details.

4. **Update Patron**
   - **Endpoint**: `PUT /api/patrons/{id}`
   - **Description**: Updates an existing patron's details.
   - **Request Body**: A `PatronsDTO` object with updated patron details.
   - **Response**: A confirmation message along with the updated patron details.

5. **Delete Patron**
   - **Endpoint**: `DELETE /api/patrons/{id}`
   - **Description**: Deletes a patron from the system by their ID.
   - **Response**: A confirmation message.

#### Borrow/Return API

1. **Borrow Book**
   - **Endpoint**: `POST /api/borrow/{bookId}/patron/{patronId}`
   - **Description**: Allows a patron to borrow a book.
   - **Response**: A confirmation message and borrowing record details.

2. **Return Book**
   - **Endpoint**: `PUT /api/return/{bookId}/patron/{patronId}`
   - **Description**: Allows a patron to return a borrowed book.
   - **Response**: A confirmation message and updated borrowing record details.
