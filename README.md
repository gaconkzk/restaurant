# Restaurant REST service

This service is a RESTful web service for a restaurant in order to support
menu and bill order management.

## Technologies:
- Java 8+
- PostgresSQL Database (detail in `docker/docker-compose.yml`)
  
  - We can use docker-compose to start sample database (configured `yml` file above).
  - Or we can install and the database by ourselves (The database must be started
  before this server).
  
  PS: If our database configuration are different with the ones in docker-compose, 
  we should update all related properties when start the application. Check the
  [Spring User Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)
  for more details.
  ![Web UI sample](https://raw.githubusercontent.com/gaconkzk/restaurant/cotry/assets/rest-ui-01.png)

## Database models:
![Database models](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/gaconkzk/restaurant/cotry/db_schema.puml)

## Service features:

All supported REST operation are documents in simple format and served by swagger ui
We plan to add more details into the API when improve it. For reading the REST Api, 
please use browser to browse to url `http://localhost:8088/docs` after build and start
the service. We can also get the rest-api json using url `http://localhost:8088/docs/json`
(generated using open-api v3).
fix

## Build and Start:
This project is configured by using Gradle, so we can build/test/run the project easily
using those commands after `cd` into the project folder:


```shell
# build
./gradlew bootJar
# start the web at port 8288
java -jar build/libs/restaurant-1.0.1.jar --restaurant.port=8288
# testing - this is not finished yet due time constrain
./gradlew test
```

PS: 
- We need internet connection for gradle to download itself, and all 3rd parties libraries.
- We might need `chmod +x` for gradlew
