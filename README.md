# RESTful API for Customer and Certificate Management
## Overview
This project provides a robust HTTP-based RESTful API designed for managing Customers and their Certificates efficiently and effectively. Tailored to handle large-scale data, the system can support millions of certificates, ensuring scalability and reliability.

## Features
Customer Management: Create and delete customer profiles. Each customer has a name, an email address, and a password.

Certificate Management: Create certificates for customers. Each certificate is associated with one customer and has a status (active/inactive), a private key, and a certificate body.

Active Certificates Listing: Retrieve a list of all active certificates belonging to a specific customer.

Certificate Activation/Deactivation: Change the status of a certificate. Notifications are sent to an external system (http://requestb.in) upon activation or deactivation.

Persistence: Ensure data durability and integrity, surviving computer restarts and system failures.

## Getting Started
### Prerequisites
Java JDK 21

### Installation
Clone the repository:
```git clone git@github.com:Julie17XI/assignment_project.git```

Navigate to the project directory:
```cd your-repository```

Build the project using Gradle:
```./gradlew build```

Configure your database settings in application.yml.

Run the application:
```java -Dspring.datasource.password=PASSWORD -jar build/libs/cloudflare-assignment-0.0.1-SNAPSHOT.jar```

Contact
Xi Tang - xi.tang717@gmail.com

Project Link: git@github.com:Julie17XI/assignment_project.git