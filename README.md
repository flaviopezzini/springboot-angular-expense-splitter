## about

This is an expenses splitter application.
It's ready to be deployed to Heroku, except for the database configuration.

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

* `$ gradle clean build`
  `$ java -jar build/libs/expense-splitter-0.0.1.jar`  
* using browser, navigate to`localhost:8080`

## license
[MIT](/LICENSE)
