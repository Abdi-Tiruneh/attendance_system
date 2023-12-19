## Table of Contents
- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
- [Database Configuration](#database-configuration)
- [Accessing the API Documentation](#accessing-the-api-documentation)
- [Setting the Conditional Property for Seeding the Database](#setting-the-conditional-property-for-seeding-the-database)
- [Building the Application](#building-the-application)

## Introduction
The Attendance Monitoring System is a web-based application designed to track and manage attendance records for teams and users.
This README provides instructions for setting up and running the Spring Boot application. To use this application effectively, please follow the guidelines provided in this document.

## Prerequisites
Before you begin, ensure you have the following prerequisites in place:

- **Java 17 or Higher:** This application requires Java 17 or a higher version to run. Ensure that you have the correct Java version installed on your system.

## Running the Application
To run the application, follow these steps:

1. Clone or download the application codebase to your local environment.
2. Open a terminal and navigate to the root directory of the application where the `mvnw` file exists.

   ```
   cd /path/to/your/application-directory
   ```

3. Run the following command to start the application:

   ```
   ./mvnw spring-boot:run
   ```

   This will start the Spring Boot application, and it will be accessible at `http://localhost:9191`.

4. If you wish to change the port, you can do so by modifying the `application.properties` file or providing a different port as a command-line argument when running the application.

## Database Configuration
The application uses a PostgreSQL database hosted in the cloud. You can test the application with the provided database configuration. If you want to use your own database, follow these steps:

1. Update the following properties in the `application.properties` file with your own database information:

   ```properties
   spring.datasource.url=jdbc:postgresql://your-database-url:your-port/your-database-name
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

2. Replace `your-database-url`, `your-port`, `your-database-name`, `your-username`, and `your-password` with your own database connection details.

## Accessing the API Documentation
You can access the list of available APIs and their documentation through Swagger UI. The application has been deployed, and the documentation is available at the following URL:

[Swagger Documentation](https://attendance-system-fwt8.onrender.com/swagger-ui/index.html)

This documentation provides information about the available endpoints, request/response formats, and allows you to interact with the API directly.

## Setting the Conditional Property for Seeding the Database
The application allows you to seed the database with initial data. This can be useful when you are starting the app for the first time. Follow these steps to control the seeding process:

1. Open the `application.properties` file in your application directory.
2. Locate the section for `database` properties:

   ```properties
   database:
     seed: false
   ```

3. The `seed` property is set to `false` by default, which means the database will not be seeded with initial data when you start the application. If you want to seed the database, change the value to `true`.

   ```properties
   database:
     seed: true
   ```

4. Save the changes to the `application.properties` file.

Remember to set the `seed` property based on your specific needs. It's recommended to set it to `true only if you are starting the app for the first time to populate the database with initial data. Once the database is seeded, you can set it back to `false` to prevent re-seeding on subsequent application starts.

## Building the Application
If you need to build the application for deployment or other purposes, execute the following command in the root directory of your application where the `mvnw` file exists:

```
./mvnw clean package
```

This will package the application into a JAR file that you can use for deployment. The JAR file can be found in the `target` directory of your application.

Please note that the provided information is based on the current state of the application and its environment. Ensure that you have the necessary configurations and access permissions in place to run and modify the application according to your requirements.