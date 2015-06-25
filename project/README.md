#Project - The Longest Path Problem
* [Back](https://github.com/johnolos/CS290B)

The project was about implementing the longest path problem to work with the exisiting cluster api.

A future plan would be make this implementation a tad more intelligent by pruning away branches that doesn't look interesting or discard them completely if possible.

##Clean, Compile, Jar
+ `ant all`
    - This command run ant clean compile jar  


## How to run
1. Space:
    - `ant runSpace` or `ant runSpace -Dserver-ip=x.x.x.x` 
2. Computer:
    - `ant runComputer` or `ant runComputer -Dserver-ip=x.x.x.x`
3. Clients:
	- `ant runLPP` or `ant runLPP -Dfile="filename"`

The IP is set to default (localhost), but it is possible to override it with your own choice of IP by attaching the -Dserver-ip command.

Initiate a Space, computer(s) and then run task(s). Make sure a task is completed before running another one.
