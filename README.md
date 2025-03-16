# Dental Appointment Management System

## Description
This is a Java application for managing appointments in a dental clinic. It allows patients to book appointments while ensuring that no two appointments overlap. The project includes a SQL-based repository for persistent data storage and a JavaFX graphical user interface for easy interaction. Additionally, a NoSQL repository implementation is included as an alternative storage option.

## Features

### **Core Functionality:**
- Manage patients with attributes: `ID`, `Name`, `Surname`, `Age`.
- Manage appointments with attributes: `ID`, `Patient`, `Date`, `Purpose`.
- Ensure that no two appointments overlap (each appointment lasts 60 minutes).

### **Data Storage:**
- Implement a SQL-based repository for storing patients and appointments.
- Store 100 randomly generated patients and appointments in the database using Java Faker.
- Choose between file-based and SQL-based storage through `settings.properties`.
- Implement a NoSQL repository for storing entities.

### **User Interface:**
- Provide both a **command-line interface (CLI)** and a **JavaFX graphical user interface (GUI)**.
- Ability to switch between CLI and GUI with minimal code changes.

  ![image](https://github.com/user-attachments/assets/52a017c8-aa91-4288-800d-10277bb5176f)
  ![image](https://github.com/user-attachments/assets/a0c9c2bc-de5d-4894-83df-2c679c3d8e47)


### **Reports using Java 8 Streams:**
- **Appointments per patient:** Display each patient's details along with their total number of appointments, sorted in descending order.
- **Monthly appointment count:** Display the number of appointments for each month, sorted in descending order.
- **Days since last appointment:** Show the last appointment date for each patient and the number of days passed, sorted in descending order.
- **Busiest months:** Display the months with the highest number of appointments, sorted in descending order.

## Technologies Used
- **Java** (Java 8+)
- **JavaFX** for GUI
- **JDBC** for SQL database connectivity
- **NoSQL database integration**
- **Java Streams** for functional data processing
- **Java Faker** for generating test data

## How to Run
1. Clone this repository.
2. Configure the database connection in `settings.properties`.
3. Run the application:
   - **CLI Mode:** Execute the main class from the terminal.
   - **GUI Mode:** Run the JavaFX-based user interface.

## Future Enhancements
- Extend the NoSQL database support.
- Implement additional statistics and analytics for appointment management.
- Improve the UI with enhanced interactivity and styling.

---
This project is part of an academic assignment and follows specific requirements defined for the coursework.

