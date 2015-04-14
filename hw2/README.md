#Homework 2 - A Basic Compute Farm
* [Back](https://github.com/johnolos/CS290B)


##How to run:
* ant clean compile jar
* ant -Dserver-ip=169.231.121.188 runSpace
* ant -Dserver-ip=169.231.121.188 runComputer

First line cleans, compiles and jars the project.
Second line initiates the Space (RMI server) along with RMI-registery.
Third line spawns multiple computers that register themselves at Space.


