#Homework 3 - A Basic Compute Farm
* [Back](https://github.com/johnolos/CS290B)


##How to run:
* ant all (clean, compile, jar)
	- ant clean
	- ant compile (depends clean)
	- ant jar (depends compile)
* ant runSpace*
* ant runComputer*
* ant runFib**
* ant runTSP*

(*)	Optional parameter: -Dserver-ip=x.x.x.x
Default value: "localhost"
(**)	Optional parameter: -Dnum=x
Default value:16

Initaite a Space, computer(s) and then run task(s). Make sure a task is completed before running another one.


