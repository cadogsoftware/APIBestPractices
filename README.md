# API Best Practices

This repository contains code that demonstrates API Best Practices when creating REST
based services with Java and Spring Boot.

It contains an application that allows CRUD operations on Books. As well
as the source code there are comprehensive unit tests and integration tests.

## Useful commands for this project

### To start up:
Clone the repository:

```
git clone https://github.com/cadogsoftware/APIBestPractices.git
```

Run the project:
```
./mvnw clean spring-boot:run
```

### To manually test:
```
curl -v -X GET localhost:8080/books
curl -v -X GET localhost:8080/books/1-2-3
curl -X DELETE localhost:8080/books/1-2-3
curl -X POST localhost:8080/books -H 'Content-type:application/json' -d '{"isbn" : "9-9-9", "title": "Test Book", "author": "Sandy Else"}'
```
or using a modified structure for a Book (to mimic a required but non-breaking change):

```
curl -X POST localhost:8080/books -H 'Content-type:application/json' -d '{"isbn" : "8-8-8", "title": "Test Book 2", "authorFirstName": "Sandy", "authorLastName": "Else"}'
```

### Running the integration tests
Start up the application as detailed above, then open the file called 'API_Best_Practices.postman_collection.json' from the 'integration' folder
in the Postman application. Run the collection from there.

## Helpful Guides

### If you are just starting off
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)

### More detailed guides
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)


### Other really useful resources
* [REST APIs must be hyper-text driven](https://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven)
* [RedHat - What is a REST API?](https://www.redhat.com/en/topics/api/what-is-a-rest-api)
* [Best Practices for Designing a Pragmatic RESTful API](https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api#advanced-queries)

For Bean validation:
* [A useful bean validation question on Stack Overflow](https://stackoverflow.com/questions/72456958/spring-custom-validator-with-dependencies-on-other-fields)
* [Baledung Bean Validation](https://www.baeldung.com/spring-mvc-custom-validator#custom-class-level-validation)