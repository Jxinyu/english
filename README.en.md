# English Word Learning System

English is a Spring Boot web application for vocabulary learning, daily check-ins, word review, and personal learning progress tracking.

## Features

- User login and session management with Apache Shiro
- Vocabulary learning flow with next-word recommendations
- Daily word review by date
- Marked-word review for important or difficult words
- Personal learning statistics and study records
- Email notification support
- Server-rendered pages with Thymeleaf

## Tech Stack

- Java 8
- Spring Boot 2.7.10
- Thymeleaf
- Apache Shiro
- MyBatis-Plus
- PageHelper
- MySQL
- Druid connection pool
- Maven

## Project Structure

```text
english/
+-- src/main/java/com/ljb/english
|   +-- config/        Application, Shiro, and MyBatis configuration
|   +-- controller/    Page routes and REST APIs
|   +-- mapper/        MyBatis mapper interfaces
|   +-- pojo/          Entity classes
|   +-- service/       Business services
|   +-- utils/         Shared utility classes
+-- src/main/resources
|   +-- mapper/        MyBatis XML mappings
|   +-- static/        Static assets
|   +-- templates/     Thymeleaf pages
|   +-- application.yaml
+-- pom.xml
```

## Configuration

The application reads sensitive values from environment variables. Configure these before running the project:

| Variable | Description | Default |
| --- | --- | --- |
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | empty |
| `DB_URL` | JDBC connection URL | `jdbc:mysql://localhost:3306/english?...` |
| `MAIL_USERNAME` | SMTP account username | empty |
| `MAIL_PASSWORD` | SMTP account password or authorization code | empty |

PowerShell example:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:MAIL_USERNAME="your_email@example.com"
$env:MAIL_PASSWORD="your_email_auth_code"
```

## Run Locally

1. Create a MySQL database named `english`.
2. Configure the required environment variables.
3. Start the application:

```powershell
.\mvnw spring-boot:run
```

4. Open the application in your browser:

```text
http://localhost:8089/index
```

## Main Pages

- `/index`: Home page
- `/toLogin`: Login page
- `/toStart`: Start vocabulary learning
- `/toReview/today`: Review today's learned words
- `/toReview/sign`: Review marked words
- `/english/self`: Personal learning profile

## API Overview

Most vocabulary APIs are under `/english`.

- `POST /english/login`
- `GET /english/getNextInfo`
- `GET /english/reviewStudiedWordsByDate`
- `GET /english/updateSign/{wid}/{choice}`
- `GET /english/queryWord`
- `GET /english/signWordReview/{choice}`
- `POST /english/self/update`
- `GET /english/sendEmail`
- `GET /english/queryAllCordData`

## Notes

- Runtime secrets are intentionally not committed to the repository.
- The database schema and seed data must be prepared separately before the application can be used fully.
- The application uses server-rendered templates, so no separate frontend build step is required.
