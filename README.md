# Set of simple Spring Boot projects for learning purposes.

### **SpringJPA**
A simple Spring Boot project that uses Spring Data JPA, Kafka producer and consumer, PostgreSQL database, Docker and Kubernetes for containerization.
The project includes OAuth2 authentication for security and requires external OAuth2 authorization server for user authentication and authorization. The default address of the OAuth2 authentication server is `https://localhost:9000`, but it can be changed in the `application.properties` file or by using environment variables.

### **Chat**
A simple Spring Boot project that uses Spring AI framework for building a chat application with AI capabilities.

## **How to run the projects**
The applications can be run using IDE (IntelliJ IDEA, Eclipse, etc.) or by using the command line with Maven. 
Make sure to have the required dependencies and configurations set up before running the applications.

There is a Docker `compose.yml` file provided for running the applications in Docker containers.
There are also several Kubernetes deployment files provided for deploying the applications in a Kubernetes cluster.

