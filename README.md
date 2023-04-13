
## Useful commands for this project

### To start up:
./mvnw clean spring-boot:run

### To manually test:
curl -v -X GET localhost:8080/books
curl -v -X GET localhost:8080/books/1-2-3
curl -X DELETE localhost:8080/books/1-2-3
curl -X POST localhost:8080/books -H 'Content-type:application/json' -d '{"isbn" : 9-9-9, "title": "Test Book", "author": "Sandy Orchid Else"}'

## Helpful Guides

### If you are just starting off
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)

### More detailed guides
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)


### Other really useful resources
* [REST APIs must be hyper-text driven](https://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven)
* [RedHat - What is a REST API?](https://www.redhat.com/en/topics/api/what-is-a-rest-api)
* [Best Practices for Designing a Pragmatic RESTful API](https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api#advanced-queries)


### TODO

Validate input to Controllers
HATEOS
Postman pack - add more
CI/CD ?
