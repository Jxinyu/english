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
|   +-- config/
|   +-- controller/
|   +-- mapper/
|   +-- pojo/
|   +-- service/
|   +-- utils/
+-- src/main/resources
|   +-- mapper/
|   +-- static/
|   +-- templates/
|   +-- application.yaml
+-- pom.xml
```

## Configuration

Sensitive values are read from environment variables.

| Variable | Description | Default |
| --- | --- | --- |
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | empty |
| `DB_URL` | JDBC connection URL | local `english` database |
| `MAIL_USERNAME` | SMTP username | empty |
| `MAIL_PASSWORD` | SMTP password or authorization code | empty |

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

4. Open:

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

## Notes

- Runtime secrets are intentionally not committed to the repository.
- The database schema and seed data must be prepared separately.
- This is a server-rendered application, so no separate frontend build step is required.
