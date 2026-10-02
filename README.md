# Botanical Garden Management System

A web application for managing a botanical garden's plant catalog, individual specimens, users, and reports. Built with Spring Boot microservices and a React frontend, the system provides JWT authentication, four user roles, and a multilingual interface.

## Features

- **Plant catalog:** browse plants with images, search by name or species, and filter by type, species, garden area, or carnivorous status.
- **Plant and specimen management:** create, update, and delete plants and individual specimens; associate specimens with garden areas and upload images.
- **Data export:** export the plant catalog to CSV, JSON, XML, and Word, and export user lists to CSV.
- **Statistics and reporting:** view plant distribution by type, carnivorous versus non-carnivorous plants, and the most represented species; export statistics and charts to Word.
- **User administration:** manage staff accounts and notify users when their account information changes.
- **Internationalization:** switch between Romanian, English, and Hungarian without reloading the page.
<img width="845" height="427" alt="Screenshot 2026-10-02 190617" src="https://github.com/user-attachments/assets/c7cf59f0-4334-4a59-8bdd-b44d8027da13" />

## User Roles

| Role | Access |
| --- | --- |
| Visitor | Browse, search, and filter the catalog without signing in. |
| Employee | Visitor features, plant and specimen management, and catalog exports. |
| Manager | Employee features, statistics dashboard, and Word reports. |
| Administrator | Visitor features, user management, user CSV export, and account change notifications. |

Staff accounts are created by an administrator.

## Architecture

The React frontend sends requests through a Spring Cloud Gateway, which routes them to four backend microservices. Services communicate synchronously through REST APIs using RestTemplate, and each service uses a separate MySQL database.

| Component | Port | Responsibility |
| --- | --- | --- |
| API Gateway | 8080 | Request routing, CORS configuration, and JWT validation. |
| Plant Service | 8081 | Plant catalog, validation, and statistics. |
| Exemplar Service | 8082 | Individual specimens and associated images. |
| User Service | 8083 | Authentication and user management. |
| Notification & Export Service | 8084 | File exports, reports, notifications, and operation history. |

Relationships across service databases are managed at the application level. Specimens and their images share a database with a one-to-many relationship and cascading image deletion.

## Technology Stack

- **Backend:** Java 21, Spring Boot, Spring Security, JJWT, Spring Data JPA / Hibernate, Spring Cloud Gateway, Maven.
- **Database:** MySQL 8.
- **Frontend:** React, Axios, react-i18next, Chart.js.
- **Reports:** Apache POI and JFreeChart.
- **Notifications:** Spring Boot Mail for email and a Twilio-based WhatsApp integration. WhatsApp notifications are simulated when API credentials are unavailable.

## Design Patterns

- **Factory and Strategy:** select and implement the requested export format.
- **State:** manage the lifecycle of export jobs.
- **Observer:** trigger notifications and audit handling when user information changes.
- **Template Method:** define the validation sequence for plant data.

## Local Setup

1. Install Java 21, Maven, MySQL 8, and Node.js with npm.
2. Configure each backend service's database connection and the gateway's service routes using the configuration files included in the repository.
3. Configure the JWT settings and, for email delivery, SMTP credentials. Configure Twilio credentials if using live WhatsApp notifications.
4. Start the four backend services and the API Gateway.
5. Install the frontend dependencies and start the React application using the scripts defined in its `package.json`.

## Documentation

See [the project documentation](docs/Documentatie.pdf) for the application description, interface examples, UML diagrams, database relationships, and design decisions. The documentation is written in Romanian.
