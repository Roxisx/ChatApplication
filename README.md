# ChatApplication

* CSC 3002 F - Assignment 1
* Client with ClientThread to contact server
* Server visa-versa
* Currently limited to multiple users on same PC OR 'localhost'

# To Run Server OR Client in terminal use the following commands 
First move to director with either client OR server EXAMPLE ==> cd "\ChatServer\src\ChatServer\server"
## For Server
*  javac -classpath . ChatServer.java ServerThread.java
*  java ChatServer
## For Clients
* javac -classpath . Client.java ClientThread.java
* java Client

# Server 
* to run simply run ChatServer.java at folder location: ChatServer\src\ChatServer\server
* Manual port number to input but leave blank and press enter to use default [6666]  
* it will run ServerThread to listern for clients 

# Client 
* Run Client at location/path: ChatServer\src\ChatServer\client
* Can run multiple on same PC
* Manual input for Username followed by portnumber or press enter to use default [6666]
* From there simply type messages to send to server 
* Once done type Exit or 'exit' to end client

