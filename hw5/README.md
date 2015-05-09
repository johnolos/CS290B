#Homework 5 - A Distributed Branch & Bound Framework
* [Back](https://github.com/johnolos/CS290B)


##How to run:
* ant all (clean, compile, jar)
	- ant clean
	- ant compile (depends clean)
	- ant jar (depends compile)
* ant runSpace(1)
* ant runComputer(1,3)
* ant runFib(1,2)
* ant runTSP(1)

(1)	Optional parameter: 
* -Dserver-ip=x.x.x.x
    - Default value: "localhost"

(2)	Optional parameter: 
* -Dnum=x
    - Default value:16

(3) Optional parameter: 
* -Dmulticore="true"/"false"
    - Default value: "true"
* -DamelioriteLatency="true"/"false"
    - Default value: "true"


Initaite a Space, computer(s) and then run task(s). Make sure a task is completed before running another one.


