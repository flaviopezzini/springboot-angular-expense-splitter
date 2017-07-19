# 

Using the spring-boot-angular2 starter kit from https://github.com/borysn/spring-boot-angular2
spring boot backend, angular2 frontend with webpack, typescript, sass, bootstrap4, karma, jasmine

## about

This is an expenses splitter application.

## pre-install

1. install jdk8
    - set `JAVA_HOME` environment variable
1. (optional) install latest gradle
    - otherwise just use `$ ./gradlew`
1. (optional) install python 2.7.x
    - set `python` environment variable
    - `$ gradle npmInstall` may complain otherwise

## install

We're using a MySQL server community edition version 5.7.18
During the build it will create the necessary tables and insert data
via a data.sql file available at src/main/resources folder.

It will require a MySQL user called expensesplitter, check the password in application.properties
with the following privileges:
GRANT SELECT, INSERT, UPDATE, DELETE ON expensesplitter.* TO 'expensesplitter'@'%';

There's a file called ddl.sql with the creation queries.

## build & run

* `$ gradle clean build runAll`
    - server side will finish building first
    - give npm/webpack a little time to finish up
* using browser, navigate to`localhost:3000`

## Notes
I have used the following guide to implement security
http://www.svlada.com/jwt-token-authentication-with-spring-boot/.

## license
[MIT](/LICENSE)
