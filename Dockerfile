FROM maven
MAINTAINER "callharshraj@gmail.com"

## Install maven
#RUN apt-get update
#RUN apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "package", "-DskipTests"]

EXPOSE 8080
CMD ["java", "-jar", "target/bank-account-management-0.0.1-SNAPSHOT.jar"]