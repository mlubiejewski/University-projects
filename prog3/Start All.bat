javac BattagliaNavale.java
javac ServerImpl.java

start cmd /k java -Djava.rmi.server.logCalls=true -Djava.rmi.server.codebase=http://127.0.0.1:8080/ -Djava.security.policy=server.policy ServerImpl

start cmd /k java -Djava.rmi.server.codebase=http://127.0.0.1:8081/ -Djava.security.policy=client.policy BattagliaNavale 127.0.0.1 2100

start cmd /k java -Djava.rmi.server.codebase=http://127.0.0.1:8082/ -Djava.security.policy=client.policy BattagliaNavale 127.0.0.1 2200