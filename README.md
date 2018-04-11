### salaryman

- `wget http://maven.ibiblio.org/maven2/cglib/cglib-nodep/3.2.4/cglib-nodep-3.2.4.jar`

- `mvn install:install-file -Dfile=cglib-nodep-3.2.4.jar -DgroupId=cglib -DartifactId=cglib-nodep -Dversion=3.2.4 -Dpackaging=jar`

- `mvn clean install`

- `java -jar bot-1.0-SNAPSHOT.war`


##

https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html