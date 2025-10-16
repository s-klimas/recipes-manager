# Recipes Manager
## Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![AI](https://img.shields.io/badge/AI-%230080FF?style=for-the-badge&logo=openai&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-5C2D91?style=for-the-badge)
![Lombok](https://img.shields.io/badge/Lombok-A50?style=for-the-badge&logo=lombok&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Liquibase](https://img.shields.io/badge/Liquibase-003BCE?style=for-the-badge&logo=liquibase&logoColor=white)

![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=java&logoColor=white)
![AssertJ](https://img.shields.io/badge/AssertJ-0069C0?style=for-the-badge)
![Mockito](https://img.shields.io/badge/Mockito-46B6AC?style=for-the-badge&logo=java&logoColor=white)

## Description

Recipes Manager is a backend service built with Java and Spring Boot that provides a secure, scalable API for managing user accounts and recipes. It supports user registration, authentication with JWT, role-based access control, and recipe CRUD operations. The project integrates AI-powered chat and image processing features to assist users with recipe management.

The API is documented with [Swagger/OpenAPI](src/main/java/pl/sebastianklimas/recipesmenager/swagger/SwaggerConfig.java) for easy exploration and integration.

## Interesting Techniques

- **JWT Authentication and Authorization**: The project uses JSON Web Tokens for stateless authentication, with refresh tokens managed via secure HTTP-only cookies. This approach is implemented in [AuthController.java](src/main/java/pl/sebastianklimas/recipesmenager/auth/AuthController.java) and [JwtAuthenticationFilter.java](src/main/java/pl/sebastianklimas/recipesmenager/config/jwt/JwtAuthenticationFilter.java).
- **Spring Security Customization**: Custom security rules are defined via the `SecurityRules` interface and implemented in feature-specific classes like [AuthSecurityRules.java](src/main/java/pl/sebastianklimas/recipesmenager/auth/AuthSecurityRules.java) and [SwaggerSecurityRules.java](src/main/java/pl/sebastianklimas/recipesmenager/swagger/SwaggerSecurityRules.java), allowing modular and clear access control.
- **AI Integration with Spring AI**: The project integrates [Spring AI](https://spring.io/projects/spring-ai) to provide chat and image-to-recipe conversion features, leveraging large language models and chat memory. See [ChatService.java](src/main/java/pl/sebastianklimas/recipesmenager/ai/chat/ChatService.java) and [ChatController.java](src/main/java/pl/sebastianklimas/recipesmenager/ai/chat/ChatController.java).
- **Image Processing with Imgscalr**: The [ImageService.java](src/main/java/pl/sebastianklimas/recipesmenager/ai/image/ImageService.java) uses the lightweight [Imgscalr](https://github.com/rkalla/imgscalr) library for efficient image resizing before processing.
- **MapStruct for DTO Mapping**: The project uses [MapStruct](https://mapstruct.org/) to generate type-safe mappers between entities and DTOs, improving maintainability and reducing boilerplate. Examples include [UserMapper.java](src/main/java/pl/sebastianklimas/recipesmenager/users/UserMapper.java) and [RecipeMapper.java](src/main/java/pl/sebastianklimas/recipesmenager/recipes/RecipeMapper.java).
- **Lombok for Boilerplate Reduction**: Lombok annotations like `@Getter`, `@Setter`, `@AllArgsConstructor`, and `@Data` are used extensively to reduce boilerplate code in entities and DTOs.
- **JPA and Hibernate for ORM**: Entities like [User.java](src/main/java/pl/sebastianklimas/recipesmenager/users/User.java) and [Recipe.java](src/main/java/pl/sebastianklimas/recipesmenager/recipes/Recipe.java) use JPA annotations for ORM mapping, with lifecycle callbacks (`@PrePersist`, `@PreUpdate`) to manage timestamps.
- **Exception Handling with Spring MVC**: Controllers handle domain-specific exceptions like `UserNotFoundException` and `RecipeNotFoundException` to return appropriate HTTP status codes.
- **Unit Testing with JUnit 5, Mockito, and AssertJ**: Comprehensive unit tests cover service layers, using [Mockito](https://site.mockito.org/) for mocking and [AssertJ](https://assertj.github.io/doc/) for fluent assertions.

## Notable Technologies and Libraries

- **Spring AI**: Provides integration with large language models and chat memory for AI-powered features.
- **Imgscalr**: A simple and efficient image-scaling library for Java.
- **MapStruct**: Compile-time code generator for mapping between Java bean types.
- **Lombok**: Reduces boilerplate code with annotations.
- **Swagger/OpenAPI**: API documentation and exploration.
- **PostgreSQL**: The primary database, managed via JPA/Hibernate.
- **Liquibase**: Database migration tool (implied by `src/main/resources/db/data/0001_user_and_roles.sql`).
- **JUnit 5, Mockito, AssertJ**: Testing frameworks for unit and integration tests.

## Project Structure

```
/src
  /main
    /java
      /pl/sebastianklimas/recipesmenager
        /ai
          /chat
          /image
          /tools
        /auth
        /config
          /ai
          /jwt
          /security
        /recipes
          /ingredients
            /dtos
        /swagger
        /users
          /dto
          /exceptions
          /roles
    /resources
      /db
        /data
  /test
    /java
      /pl/sebastianklimas/recipesmenager
        /ai
          /image
        /auth
        /recipes
        /users
```

- **/ai**: Contains AI-related services including chat, image processing, and AI tools integration.
- **/auth**: Authentication and authorization logic, including controllers and services.
- **/config**: Configuration classes for security, JWT, AI chat memory, and Swagger.
- **/recipes**: Core domain logic for recipes and ingredients, including DTOs and exceptions.
- **/swagger**: Swagger/OpenAPI configuration and security rules.
- **/users**: User management, roles, DTOs, exceptions, and services.
- **/resources/db/data**: SQL scripts for initial data seeding.
- **/test**: Unit tests organized by feature.
