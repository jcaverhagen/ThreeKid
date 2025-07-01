# Three-Kid Family Challenge

ThreeKid Family Challenge is a Spring Boot application designed to manage family relationships and data. It provides RESTful APIs to create, retrieve, update, and delete family-related information.

---

## Features

✅ Accepts POST requests to `/api/v1/people` to insert or update person records  
✅ Checks if any person matches the pattern:
- Has a partner
- Has exactly 3 children
- All children list that partner as parent
- At least one child is under 18

✅ Returns `200 OK` with matching persons if a match is found  
✅ Returns `444 No Response` if no match is found  
✅ Bidirectional relationship integrity: ensures parents/children/partner references are consistent  
✅ Unit tests, integration tests, and end-to-end tests  
✅ In-memory repository for simplicity

---

## Technologies

- Java 21
- Spring Boot
- JUnit 5
- MockMvc
- ConcurrentHashMap (in-memory fake database)

---

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/janverhagencrv/ThreeKid.git
   cd ThreeKid
    ```
2. Build the project:
    ```
   mvn clean install
    ```
   
3. Run the application:
    ```bash
   mvn spring-boot:run
   ```


## 📝 Time spent

⏱ Approximate time spent on the challenge: **4 hours**

It was difficult to concentrate with the temperature inside the house being 30 degrees Celsius.


## Future improvements

- Replace in-memory database with real database like PostgreSQL or MySQL
- Improve API validation / error handling

## Author
Jan Verhagen