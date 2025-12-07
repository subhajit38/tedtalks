# tedtalks - Please Read me before use

## Prerequisite
Java SDK 17+

Maven 3.8+

Git latest

## verify installation
java -version

mvn -version

git --version


## How to Run
Clone git:

git clone https://github.com/subhajit38/tedtalks.git


Move Inside Directory:

cd tedtalks

Build Code :

mvn clean install

java -jar target/tedtalks-1.0-SNAPSHOT.jar

## Swagger UI
http://localhost:8080/webjars/swagger-ui/index.html#/

## Open Api Spec Contract
http://localhost:8080/v3/api-docs

## Postman collection is added inside postman dir

Import collection and use.

## Steps of API execution

1. Load CSV file Inside 

http://localhost:8080/tedtalksApi/import-csv

2. You are ready to use all apis 


## Stop server

CTRL + C

## Considerations :

1. Use non-blocking reactor framework to handle any amount of load.
2. While loading initial data from CSV check invalid data and ignore.
3. Application is using H2 Database in reactive mode (R2DBC driver)
4. Database will hold till server is running. Once server will be down, you need to load csv data via api.
5. Influence determination logic as likes are more important than views:: views + likes * 3 + likes / views * 1000;
6. Exception handling is done for basic scenarios.

## Improvement :

1. For expanded business in the future, convert the application to microservice service pattern.
2. Managed services could be used for DB or robust deployment.
3. Mapper like mapstruct can be used for bean mapping.
4. Response of getAll tedtalks api should needs to be paginated.
5. More script validation.