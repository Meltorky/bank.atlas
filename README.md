# 🏦 Atlas Bank — Banking Management System

**A modular banking backend built with Java and Spring Boot, focusing on secure financial operations, maintainable architecture, and practical software design patterns.**

[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-6DB33F?logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-4169E1?logo=postgresql)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Status](https://img.shields.io/badge/Version-V1-blue)]()

---

## 📌 Overview

Atlas Bank is a backend banking management system designed to simulate essential banking operations while applying software engineering principles used in real-world enterprise applications.

The project focuses on more than implementing CRUD operations. It demonstrates how to organize business logic, protect financial transactions, maintain account history, manage different financial products, and build an extensible system using established design patterns.

The system follows a modular approach, keeping responsibilities separated while allowing new banking features to be introduced without unnecessarily complicating existing functionality.

**Main objectives:**
- Build secure and maintainable RESTful APIs.
- Implement financial operations with transaction integrity.
- Apply SOLID principles and practical design patterns.
- Separate customer information from online banking authentication.
- Design an extensible foundation for future banking services.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Core programming language |
| Spring Boot | Application framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access and persistence |
| Hibernate | ORM implementation |
| PostgreSQL | Relational database |
| Maven | Dependency management and build |
| Lombok | Boilerplate reduction |
| Jakarta Validation | Request validation |
| AWS / Azure adapters | External image storage integration |

---

## ✨ Core Features — V1

### 1. Customer & Identity Management

The system separates customer information from online banking authentication.

- Customer management and customer-related information.
- Separate `User` entity for online banking access.
- One-to-one relationship between `User` and `Customer`.
- Spring Security integration for securing application access.
- Employee identities are kept separate from customer online banking users.

**Why separate User and Customer?**

A customer can exist in the bank without having online banking access. The `User` entity represents the credentials and identity required to access the digital banking system, while `Customer` represents the actual banking customer.

This separation avoids mixing authentication responsibilities with banking domain information.

### 2. Account Management

- Checking accounts.
- Savings accounts.
- Account balance management.
- Account status management:
  - `ACTIVE`
  - `SUSPENDED`
  - `CLOSED`
- Account closure instead of physical deletion.
- Multiple accounts associated with one customer.
- Currency support through an enum-based currency model.

### 3. Financial Transactions

The system supports different transaction types:

- Deposits.
- Withdrawals.
- Internal transfers between bank accounts.
- External transfers.
- Savings profit payments.
- Certificate profit payments.
- Debit card purchases.
- Credit card purchases.

Financial operations are designed around transactional consistency.

**Important implementation decisions:**
- `BigDecimal` is used for monetary values instead of floating-point types.
- `@Transactional` manages database transaction boundaries.
- Pessimistic locking is applied where concurrent financial operations could cause inconsistent balances.
- Completed transactions are retained as financial history.

### 4. Ledger System

Atlas Bank uses a ledger-based approach to record account movements.

Each ledger entry contains:
- Account reference.
- Transaction reference.
- Amount.
- Entry type (`DEBIT` or `CREDIT`).

Instead of treating the account balance as the only financial record, ledger entries provide a history of money entering and leaving accounts.

For example:

| Operation | Account balance | Ledger entry |
|---|---|---|
| Deposit | Increases | CREDIT |
| Withdrawal | Decreases | DEBIT |
| Internal transfer — sender | Decreases | DEBIT |
| Internal transfer — receiver | Increases | CREDIT |
| Debit card purchase | Decreases | DEBIT |

Internal transfers are represented through corresponding ledger entries, making both sides of the movement traceable.

### 5. Savings Accounts & Certificates

The system supports different profit calculation rules.

- Savings account profit calculations.
- Fixed-term certificates.
- Monthly certificate profit payments.
- Monthly profit processing.
- Profit payments recorded through transactions and ledger entries.

Profit calculations are separated from the services that execute the payments, allowing different financial products to use different calculation rules.

### 6. Debit & Credit Cards

The card module uses a shared abstract `Card` entity with specialized implementations.

**Debit Cards**
- Linked to checking accounts.
- Validate card status and expiry.
- Validate the linked account status.
- Check available account balance.
- Deduct the purchase amount from the account.
- Record the purchase in the ledger.

**Credit Cards**
- Maintain an assigned credit limit.
- Track outstanding balances.
- Calculate available credit.
- Reject purchases that exceed the available credit.
- Increase outstanding balance after successful purchases.

Both card types generate transaction records and publish purchase-completed events.

---

## 🧠 Design Patterns & Engineering Decisions

Design patterns are applied where they solve an actual design problem, rather than being introduced simply to increase the number of patterns in the codebase.

### 1. Adapter Pattern — External Image Storage

**Problem:**

Different cloud providers expose different APIs and implementation details for uploading images. Directly coupling business services to a specific provider makes switching providers difficult.

**Implementation:**

An `ImageStorage` interface defines a common contract. Separate adapters implement that contract for AWS and Azure.

```text
CustomerService
      |
      v
ImageStorage
      |
      +---- AwsImageAdapter
      |
      +---- AzureImageAdapter
```

**Benefits:**
- Decouples business logic from external providers.
- Makes switching storage providers easier.
- Allows multiple implementations through dependency injection.
- Simplifies testing by allowing alternative implementations.

### 2. Strategy Pattern — Profit Calculation

**Problem:**

Different banking products have different profit calculation rules. Putting every calculation inside one service would eventually create large conditional blocks and make new financial products harder to maintain.

**Implementation:**

A common strategy contract allows each product to provide its own calculation algorithm.

Examples:
- `SavingsProfitStrategy`
- `CertificateProfitStrategy`

The service delegates the calculation to the appropriate strategy instead of containing all calculation rules itself.

**Benefits:**
- Separates financial algorithms from execution logic.
- Makes adding new calculation rules easier.
- Reduces conditional complexity.
- Keeps each calculation independently maintainable and testable.

### 3. Factory Pattern — Card Creation

**Problem:**

Debit and credit cards share common information but require different initialization and configuration. Scattering card creation logic throughout services would tightly couple card creation to business operations.

**Implementation:**

`CardFactory` centralizes the creation of card objects.

It creates the appropriate implementation based on the requested card type:
- `DebitCard`
- `CreditCard`

Common initialization, such as assigning the customer, card reference, status, and expiry date, is handled in one place.

**Benefits:**
- Centralizes object creation.
- Prevents duplicated initialization logic.
- Keeps card creation separate from service responsibilities.
- Makes introducing additional card types easier.

### 4. Observer Pattern — Transaction Events

**Problem:**

After a successful card purchase, the system may need to perform several independent actions, such as auditing, customer notifications, and eventually fraud detection.

Putting all these responsibilities inside the purchase service would tightly couple unrelated operations.

**Implementation:**

The purchase service publishes a `CardPurchaseCompletedEvent`.

Independent listeners react to the event:

| Listener | Responsibility |
|---|---|
| `PurchaseAuditListener` | Records purchase information for internal auditing |
| `PurchaseNotificationListener` | Handles customer notification preparation |

The listeners use Spring's `@TransactionalEventListener` with `AFTER_COMMIT`.

This ensures the listeners react only after the database transaction successfully commits.

**Benefits:**
- Decouples transaction processing from follow-up actions.
- Makes adding new event listeners easier.
- Prevents notifications for rolled-back purchases.
- Keeps the purchase service focused on financial business logic.

---

## 🏗️ Other Important Engineering Practices

### Repository Pattern & Spring Data JPA

The project uses Spring Data JPA repositories to separate persistence operations from business logic.

**Benefits:**
- Keeps database access out of service classes.
- Reduces repetitive persistence code.
- Makes services easier to maintain and test.
- Uses derived repository methods instead of writing unnecessary SQL queries.

### Dependency Injection

Spring's IoC container manages application components and their dependencies.

Constructor injection is used to make dependencies explicit and improve testability.

`@Qualifier` is used when multiple implementations of the same interface exist, such as the AWS and Azure image storage adapters.

### Transaction Management & Concurrency Protection

Financial operations require more than simply updating a balance.

The project uses:
- `@Transactional` for atomic database operations.
- Pessimistic locking where concurrent operations could otherwise modify the same financial resource.
- Validation before executing financial changes.

For example, two simultaneous withdrawals should not both succeed using the same outdated account balance.

### DTOs & Validation

The API uses Data Transfer Objects to control incoming requests and outgoing responses.

Jakarta Validation is used to validate request data before business operations are executed.

This keeps persistence entities separate from API contracts.

### Separation of Responsibilities

The application separates responsibilities across controllers, services, repositories, entities, DTOs, factories, events, and listeners.

Controllers handle HTTP requests, services implement business rules, and repositories handle persistence.

This keeps the codebase easier to understand, maintain, and extend.

---

## 🔐 Security & Financial Integrity

Security and financial consistency are important parts of the system.

The current implementation includes:
- Spring Security integration.
- Separation between customer banking profiles and online banking users.
- Request validation.
- Account and card status validation.
- Expiry validation for cards.
- Balance and credit-limit checks.
- Transaction boundaries.
- Pessimistic locking for relevant financial operations.
- Financial transaction and ledger history.

**Note:** Atlas Bank is an educational portfolio project, not a certified production banking platform or a real payment-processing system.

---

## 🚀 Roadmap — V2

The next phase focuses on expanding the banking domain and introducing additional infrastructure where it provides real value.

### 1. Loan Management
- Customer loan applications.
- Loan eligibility assessment.
- Loan approval and rejection workflows.
- Interest calculation strategies.
- Installment schedules.
- Loan repayment tracking.

### 2. NoSQL Notification System
Introduce a dedicated notification module backed by a NoSQL database.

Planned notification events:
- Deposits.
- Withdrawals.
- Internal and external transfers.
- Debit card purchases.
- Credit card purchases.
- Credit card outstanding balance changes.
- Loan approvals and repayment reminders.

The goal is to keep notification history and delivery-related data separate from the core relational banking model.

### 3. Employee Management System
- Dedicated employee management.
- Employee authentication and authorization.
- Role-based access control.
- Employee-specific permissions.
- Administrative banking operations.

Employee identities will remain separate from customer online banking users.

### 4. Investment Accounts
- Investment account management.
- Investment products.
- Investment profit and return calculations.
- Investment transaction history.

### 5. Business Banking
- Business customer profiles.
- Business accounts.
- Business account management.
- Business-oriented financial operations.

---

## 🎯 Project Goals

Atlas Bank is designed to demonstrate practical backend engineering skills, including:

- Object-Oriented Programming.
- SOLID principles.
- RESTful API development.
- Spring Boot application development.
- Spring Security.
- Spring Data JPA and Hibernate.
- Relational database design.
- Financial transaction management.
- Concurrency protection.
- Design Patterns.
- Dependency Injection.
- Event-driven application design.
- Maintainable and extensible backend architecture.

---

## ⚙️ Getting Started

### Prerequisites

- Java 25
- Maven
- PostgreSQL
- Git

### Clone the repository

```bash
git clone https://github.com/Meltorky/bank.atlas.git
cd bank.atlas
```

### Configure the database

Create a PostgreSQL database and configure your database connection in the application's configuration.

Keep credentials outside version control. Use environment variables or another secure configuration mechanism.

### Run the application

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

---

## 👨‍💻 Author

**Mohamed Eltorky**

Backend Developer | Java | Spring Boot | .NET

GitHub: [@Meltorky](https://github.com/Meltorky)

Project Repository: [Atlas Bank](https://github.com/Meltorky/bank.atlas)

---

*Atlas Bank — Building a maintainable banking backend through practical engineering principles.*
