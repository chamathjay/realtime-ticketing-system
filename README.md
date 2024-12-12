# Real-Time Event Ticketing Simulation

## Overview

This project is a simulation of a real-time event ticketing system developed as part of an Object-Oriented Programming (OOP) University Module Coursework. The system simulates the ticketing process using the Producer-Consumer pattern, showcasing real-time updates and seamless synchronization across the frontend, backend, and CLI components.

## Features

- Real-time updates when Vendors add Tickets and Customers remove Tickets from the designated Ticket Pool.
- Producer-Consumer pattern implementation to handle ticket generation and consumption.
- Multi-threaded backend for managing ticket pools and status updates.
- Responsive user interface developed with ReactJS.
- Comprehensive API layer developed using Spring Boot.
- Command-Line Interface (CLI) for testing, debugging and easy access to a miniature version of the finished product.

## Technology Stack

- **Programming Language**: Java v21.0.5
- **Backend Framework**: Spring Boot
- **Frontend Framework**: React
- **Node.js Version**: v22.12.0

## Project Structure

```
realtime-ticketing/
|
├── backend/
│   ├── .mvn/
│   ├── src/
│   │   ├── main/java/com/chamathjay/realtime_ticketing/
│   │   │   ├── cli/
│   │   │   │   ├── Config.java
│   │   │   │   ├── ConfigLoader.java
│   │   │   │   ├── Customer.java
│   │   │   │   ├── MainCLI.java
│   │   │   │   ├── TicketPool.java
│   │   │   │   └── Vendor.java
│   │   │   ├── Controllers/
│   │   │   │   └── TicketController.java
│   │   │   ├── Models/
│   │   │   │   └── TicketStatus.java
│   │   │   ├── Services/
│   │   │   │   └── TicketService.java
│   │   │   └── RealtimeTicketingBackendApplication.java
│   │   └── resources/
│   └── test/
│   ├── pom.xml
│   ├── config.json
│   └── tickets_log.txt
|
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   │   ├── ConfigPanel.tsx
│   │   │   └── Logs.tsx
│   │   ├── api.ts
│   │   ├── App.css
│   │   ├── App.tsx
│   │   ├── index.css
│   │   ├── main.tsx
│   │   └── vite-env.d.ts
│   ├── index.html
│   ├── package.json
│   ├── package-lock.json
│   ├── tsconfig.app.json
│   ├── tsconfig.json
│   ├── tsconfig.node.json
│   └── vite.config.ts
└── README.md
```

## Installation and Setup

### Prerequisites

Ensure the following are installed on your system:

- Java v21.0.5
- Node.js v22.12.0
- npm (comes with Node.js)
- Spring Boot

### Backend Setup

1. Navigate to the `backend/` directory.
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```

### Frontend Setup

1. Navigate to the `frontend/` directory.
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the React development server:
   ```sh
   npm run dev
   ```

### CLI Setup

1. Compile the Java CLI application:
   ```sh
   javac -d out src/*.java
   ```
2. Run the CLI application:
   ```sh
   java -cp out Main
   ```

## Usage

1. Start the backend server.
2. Launch the frontend React application.
3. Optionally, use the CLI to simulate ticket production and consumption processes.
4. Use the frontend interface to view real-time ticket updates and perform booking operations.

## Key OOP Concepts

- **Encapsulation**: Modular design of ticket operations and data handling.
- **Polymorphism**: Flexible handling of ticket operations in different components.
- **Inheritance**: Shared logic across similar ticket-related classes.
- **Abstraction**: Clear interfaces for producer and consumer interactions.

Feel free to reach out for any queries or suggestions!

