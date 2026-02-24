# StudyFlow API Documentation

## Swagger UI Access

This API is documented using **OpenAPI 3.0** and **Swagger UI**. Once the application is running, you can access the interactive API documentation by visiting:

### URLs

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs (JSON)**: `http://localhost:8080/v3/api-docs`
- **API Docs (YAML)**: `http://localhost:8080/v3/api-docs.yaml`

## Features

### API Groups

The API is organized into the following logical groups:

1. **Authentication** - User registration, login, and email verification
2. **Subjects** - Create, read, update, and delete study subjects
3. **Tasks** - Manage tasks associated with subjects
4. **Summaries** - Create and manage study summaries
5. **Account** - User account management

### Authentication

Most endpoints require JWT (JSON Web Token) authentication. To use authenticated endpoints:

1. First, register a new user using the `/auth/register` endpoint
2. Verify your email using the `/auth/verify` endpoint
3. Login using the `/auth/login` endpoint to obtain a JWT token
4. In Swagger UI, click the **Authorize** button and paste your token in the format: `Bearer <your-token>`
5. All subsequent requests will include the authentication token

### Response Codes

- **200 OK** - Successful request
- **201 Created** - Resource created successfully
- **204 No Content** - Successful request with no response body
- **400 Bad Request** - Invalid input data
- **401 Unauthorized** - Authentication required or invalid token
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

## Testing with Swagger UI Examples

### Example: Register a New User

1. Open Swagger UI
2. Navigate to the **Authentication** section
3. Click on `POST /auth/register`
4. Click **Try it out**
5. Fill in the request body:
   ```json
   {
     "username": "john_doe",
     "email": "john@example.com",
     "password": "SecurePass123!"
   }
   ```
6. Click **Execute**

### Example: Login and Get Token

1. Navigate to `POST /auth/login`
2. Use the same email and password from registration:
   ```json
   {
     "email": "john@example.com",
     "password": "SecurePass123!"
   }
   ```
3. Copy the token from the response
4. Click the **Authorize** button at the top and paste the token with `Bearer ` prefix

### Example: Create a Subject

1. With authorization configured, navigate to **Subjects** section
2. Click `POST /subjects`
3. Click **Try it out**
4. Fill in the request body:
   ```json
   {
     "title": "Advanced Mathematics"
   }
   ```
5. Click **Execute**

## API Specification Details

- **Language**: English
- **Format**: OpenAPI 3.0
- **Framework**: Spring Boot 3.5.4
- **Security**: JWT Bearer Token
- **Database**: PostgreSQL

## Documentation Standards

Each endpoint includes:

- Clear operation summary
- Detailed description
- Request parameters with examples
- Response schemas with examples
- Possible error responses
- Security requirements (where applicable)

## Troubleshooting

### Swagger UI Not Loading

- Ensure the application is running on `http://localhost:8080`
- Check that Spring Boot started successfully
- Verify no port conflicts

### Unauthorized Errors (401)

- You need to authenticate first using `/auth/login` or `/auth/register`
- Verify your JWT token is valid by clicking **Authorize** in Swagger UI
- Ensure the token format is correct: `Bearer <token>`

### Database Connection Issues

- Verify PostgreSQL is running
- Check database credentials in `application.properties`
- Ensure the database exists and is accessible

## Additional Resources

- [OpenAPI Specification](https://spec.openapis.org/oas/v3.0.3)
- [Springdoc-OpenAPI Documentation](https://springdoc.org/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)

---

**Last Updated**: February 2025
**API Version**: 1.0.0
