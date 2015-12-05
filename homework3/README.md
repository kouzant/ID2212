 1. Requirements
--------------------
 * Java 1.8 [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
 * Apache Maven [here](https://maven.apache.org/)
 * Glassfish 4 Application Server [here](https://glassfish.java.net/)
 * Apache Derby database [here](https://db.apache.org/derby/)

 2. Configuration
--------------------
* Create a connection pool to Glassfish

> $GLASSFISH_HOME/bin/asadmin create-jdbc-connection-pool \
> --datasourceclassname=org.apache.derby.jdbc.ClientDataSource \
> --restype=javax.sql.DataSource \
> --property portNumber=1527:password=APP:user=APP:serverName=localhost 
> :databaseName=id2212_3:connectionAttributes='create\=true' Id2212_3Pool

* Ping the connection pool
> $GLASSFISH_HOME/bin/asadmin ping-connection-pool Id2212_3Pool

* Create JDBC resource
> $GLASSFISH_HOME/bin/asadmin create-jdbc-resource --connectionpoolid Id2212_3Pool jdbc/id2212_3

* List available resources
> $GLASSFISH_HOME/bin/asadmin list-jdbc-resources

 3. Package
-------------
> mvn package -DskipTests=true

 4. Deploy
------------
Deploy either from the Glassfish web UI or
> $GLASSFISH_HOME/bin/asadmin deploy - -force-true target/homework3.war

5. Miscellaneous
--------------------
* Connect to Derby db
> $DERBY_HOME/bin/ij
> connect 'jdbc:derby://localhost:1527/id2212_3';

* Run Java main class from the Application Server
> $GLASSFISH_HOME/glassfish/bin/appclient -client target/SomeClient.jar
