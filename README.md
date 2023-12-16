# SwiftSend Application

SwiftSend is a Java-based middleware platform developed using the Spring Boot framework. It serves as a bridge between courier services and users in Canada, allowing users to create accounts, book orders, and enabling clients to provide and manage services. The application uses MySQL as its database (via Hibernate), React for the front end, and includes email authentication features.

## Final Year Capstone Project

This project was developed as a part of the final year capstone project. It represents the culmination of our academic journey, showcasing our skills in Java Spring Boot, React, and various technologies. We aimed to create a robust middleware platform that facilitates communication between courier services, clients, and users in Canada.

## Features

- **User Authentication:**
  - Users can create accounts and verify their emails for secure authentication.
  - JWT (JSON Web Token) is used for enhanced security.

- **Service Booking:**
  - Users can view available services provided by different clients.
  - Book orders seamlessly through the application.

- **Client Management:**
  - Clients can sign up, verify their emails, and manage their services.
  - Admins receive notifications about new clients and can approve or reject client accounts.

- **Service Management:**
  - Approved clients can upload and manage services.
  - Uploaded services are visible to users for booking.

- **Email Authentication:**
  - Email verification is implemented for both user and client registration.
  - Java Mail Sender is utilized to send verification emails.

## Technologies Used

- **Backend:**
  - Java Spring Boot
  - Spring Security
  - Hibernate
  - JWT (JSON Web Token)
  - Java Mail Sender
  - MySQL (Database)

- **Frontend:**
  - React
  - JavaScript



## Getting Started

### Prerequisites

- Java Development Kit (JDK)
- Node.js and npm
- MySQL Server
- Maven

### Configuration

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/swiftsend.git
   cd swiftsend
# Navigate to the project root directory
cd swiftsend

# Build and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
## Future Developments

As part of our commitment to continuous improvement, we plan to keep updating SwiftSend with new features and enhancements. We welcome contributions from the community and aim to evolve this project to meet the ever-changing needs of the courier services industry.

## Contributing

We welcome contributions to SwiftSend! If you'd like to contribute, please follow these guidelines:

### How to Contribute

 Fork the repository to your GitHub account.
   Create a new branch for your feature or bug fix:
   git checkout -b feature/your-feature-name

Make your changes and commit them with descriptive commit messages.
Push your changes to your fork
Open a pull request on the original repository.
