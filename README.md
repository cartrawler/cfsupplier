# Reference API server implementation and protocol schema test

cfsupplier API defines supplier offer and booking creation integration model with Cartrawler private transfer engine.

Contact: gtdevsupport@cartrawler.com for more information.

## api-reference-server

API server is a reference server implementation for cfsupplier API. It implements basics of the cfsupplier API protocol. Including JSON schema, expected state changes and unit tests to define expected API responses.

Server is implemented with scala 2.12 utilizing Java 8. Server can be used wither with docker file or stand alone. Simplified docker file is provided in the project.

Get Docker : https://www.docker.com/get-docker


```
Building and starting the server.

cd api-reference-server
docker-compose up

```

## api-schema-test-client

API schema test is a ruby script that can be used to verify cfsupplier API server API integrity. 

More details in : api-schema-test-client/README.md

