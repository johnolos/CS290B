#Project - The Longest Path Problem
* [Back](https://github.com/johnolos/CS290B)

The project was about implementing the longest path problem to work with the exisiting cluster api.

A future plan would be make this implementation a tad more intelligent by pruning away branches that doesn't look interesting or discard them completely if possible.

## Clean, Compile, Jar
+ `ant all`  

    This command will clean, compile and jar the project files.

## How to run
1. Space:
    - `ant runSpace` or `ant runSpace -Dserver-ip=x.x.x.x`  
    
    This will initiate a Space. The Space keeps track of all the computers and assign tasks to them and receives the result back.  

2. Computer:
    - `ant runComputer` or `ant runComputer -Dserver-ip=x.x.x.x`  
    
    This will initiate one computer. The computer will connect to space running on `localhost` if ip isn't specifically specified.  

3. Clients:
    - `ant runLPP` or `ant runLPP -Dfile="filename"`  
	
	This will run our `Longest Path Problem` implementation. If no specific file is specified, the default graph file will be loaded.  


The IP is set to default (localhost), but it is possible to override it with your own choice of IP by attaching the -Dserver-ip command.

Initiate a Space, computer(s) and then run task(s). Make sure a task is completed before running another one.
