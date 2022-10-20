# Challenge with Java BE

This is a simple project with a challenge for Backend developrs.

## In this project you will learn:

* Consuming API (Feign)
* Creating API REST (Spring Web)
* Create a database (Spring Data)
* Create a CircuitBreak (Resilience4J)
* Different Roles (Spring Security)
* Cache with Hazelcast
* Swagger
* Sonar
* CI/CD
* Docker
* Kubernetes
* Deploy in Cloud

## Tasks:

### LVL 1 

**Goals:** 
* Create the project
* Create the database using H2
* Create the user entity, and Roles
* Include JWT authentication
* Create the login/registration/upgrade-permissions endpoints

**Expected time:** 16 | 8 | 4 hours

#### Create your project

- [X] Create the database using [H2](https://www.h2database.com/html/main.html). You should include the user table, the user can have multiple roles.
- [X] Create an endpoint to create a user. (The user should have a username and a password, be careful when saving the user password, and the roles)
- [X] Create an endpoint to update the permission to this user to ADMIN and/or USER role. This API could be called just by the ADMIN user, see @Secured and Roles in Spring Security.

#### Create the authentication for your API

- [X] Create the authentication using JWT
- [X] Create two roles ADMIN, USER
- [X] Create an endpoint to return the JWT (5 minutes of timeout), receive the username and password

* Book: REST API - Design Rulebook : Mark Masse: https://www.oreilly.com/library/view/rest-api-design/9781449317904/
* Spring Data: https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa
* Spring REST - Full Tutorial: https://www.baeldung.com/rest-with-spring-series
* Spring REST: https://spring.io/guides/tutorials/rest/
* Spring Security: https://www.toptal.com/spring/spring-security-tutorial
* Spring Security JWT: https://www.bezkoder.com/spring-boot-jwt-authentication/

### LVL 2

**Expected time:** 16 | 9 | 5 hours

#### Upgrade your API REST

- [X] Create an endpoint to consume an API from IMDB (https://imdb-api.com/) and populate your database (you can use WebClient or Feign to consume the API). This API could be called just by the ADMIN user, see @Secured and Roles in Spring Security.
- [X] Create an endpoint to list all the movies.
- [ ] Create an endpoint to include a movie to the user (favorite list)
- [ ] Create an endpoint to exclude the movie from the favorite list
- [ ] Each time the user includes the movie in the favorite list add one "star" to the movie
- [ ] Create an endpoint to list the top 10 movies, the movies with more stars.
- [ ] Create an endpoint to list the favorite movies per user.
- [ ] Don't forget to include Swagger/OpenAPI, and the test.

#### Content to help:

* Spring Test: https://www.baeldung.com/integration-testing-in-spring
* Open API: https://www.baeldung.com/spring-rest-openapi-documentation
* WebClient: https://www.baeldung.com/spring-5-webclient
* Feign: https://www.baeldung.com/intro-to-feign

----------

### LVL 3

**Expected time:** 18 | 11 | 7 hours

#### Include Hazelcast, design pattern, Resilience4J

- [ ] Include this rank top movies in the cache (Hazelcast), and get from it using RateLimiter (https://resilience4j.readme.io/docs/ratelimiter) as fallback.
- [ ] Find other API to get Movies, and update the first endpoint to use template method design pattern to be able to get the movies from bove APIs. Use a CircuitBreak for that. If you have any problem with one API you should get from the other API as a fallback. (You can try that changing the API Key)
- [ ] Create a new endpoint to send a random movie to the user.
This endpoint should do this: find another user who likes the same movies as the current user and upload a random movie from that favorites list.
If this condition does not exist, just send a random movie.

#### Content to help:

* Guide to Resilience4j: https://www.baeldung.com/resilience4j
* RateLimiter: https://resilience4j.readme.io/docs/ratelimiter
* Caching with Spring Boot and Hazelcast: https://hazelcast.com/blog/spring-boot/
* Get Started with Hazelcast using Spring Boot: https://docs.hazelcast.com/tutorials/hazelcast-embedded-springboot
* Template Method: https://refactoring.guru/design-patterns/template-method
* Failover: https://medium.com/lydtech-consulting/failover-and-circuit-breaker-with-resilience4j-14a57a43c0da

----------

### LVL 4

#### Docker, Kubernetes, Deploy in the cloud

- [ ] Run your application using Docker, create a docker file.
- [ ] Include Spring Actuator.
- [ ] Create the files to deploy the application using kubernetes (2 replicas).
- [ ] Include the probes from actuator in your deployment.yml
- [ ] Update your probes in case of hazelcast go down the application should restart
- [ ] Do the deployment into sandbox Openshift (https://developers.redhat.com/developer-sandbox)

#### Content to help:

* Spring Boot Actuator: https://www.baeldung.com/spring-boot-actuators
* Spring Boot with Docker: https://spring.io/guides/gs/spring-boot-docker/
* Template Method: https://refactoring.guru/design-patterns/template-method
* Kubernetes Tutorial: https://medium.com/@javatechie/kubernetes-tutorial-run-deploy-spring-boot-application-in-k8s-cluster-using-yaml-configuration-3b079154d232
* Book: Modernizing Enterprise Java: https://developers.redhat.com/e-books/modernizing-enterprise-java

----------

### LVL 5

#### Pipeline, CI/CD, Sonar

- [ ] Include SonarCloud in your project, make sure the coverage is above 70% and you don't have a loud code smell.
- [ ] Include a pipeline, you can use gitHub Actions, or Travis CI, use what you prefer.
- [ ] Your CI pipeline should include [build, test, sonar]
- [ ] Include also CD in your pipeline, it should be able to deploy in your PaaS (Heroku) or in your Openshift.

#### Content to help:

* SonarCloud Integration with SpringBoot-Maven: https://medium.com/@bethecodewithyou/sonarcloud-integration-with-springboot-maven-5820180ef764
* How to build a CI/CD pipeline with GitHub Actions in four simple steps: https://github.blog/2022-02-02-build-ci-cd-pipeline-github-actions-four-steps/
* The twelve-factor: https://12factor.net/
* Travis CI: https://www.travis-ci.org/


