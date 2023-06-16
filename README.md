# Сoursework: Security system
API, which allows you to receive and transmit information about the object under protection (the object consists of rooms and corridors), connected sensors, notifications from sensors, setting sensors, setting notifications and notifications from the system

# Functions
  - Create, update and delete room/hallway/sensor/notification
  - Read from CSV files
  - Write to CSV files
  - Returns information about room/hallway/sensor/notification.

# Installation and Usage

To run this project locally, follow these steps:

- Clone the repository: git clone https://github.com/KeykoLi/CourseProject.git
- Open project in your IDE
- Build project with command : mvn clean install
- Run the application with command : mvn spring-boot: run
- The application will start running on http://localhost:8080.
- Use GET/rooms|hallways|sensors|notifications to retrieve a list of all entities
- Use GET /rooms|hallways|sensors|notifications/{id} to retrieve a entiti by ID.
- Use POST /rooms|hallways|sensors|notifications to create entiti.
- Use PUT /rooms|hallways|sensors|notificationss/{id} to update an existing entiti.
- Use DELETE /rooms|hallways|sensors|notifications/{id} to delete entiti by ID.

# DateStorage

The application stores data in CSV files located in the src/main/storage.File named with the format ClassName-yyyy-mm-dd.csv
