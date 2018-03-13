// place this file the path such ends with: ChatServer/server/ChatServer.java

package ChatServer.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {

    private static int portNumber = 6666;
    private static final int minPortNumber = 1023;

    private int serverPort;
    private List<ServerThread> clients; // or "protected static List<ClientThread> clients;"
    private static Scanner scanner;
    private PrintStream serverOutputStream;
    
    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }
    
    public int updatePort(){
        String input;
        int port = portNumber;
        scanner = new Scanner(System.in);
        System.out.println("Please input port number greater than 1023 or Enter to use default (6666):");
        while(true){
            input = scanner.nextLine();
            if(isInteger(input.trim())){
                port = Integer.parseInt(input); break;
            }else if(input.trim().equals("")){  
                break;
            }else{
                System.out.println("Please use digits more than 1023 or Enter for default.");
            }
        }
        return port;
    }
    
    public static boolean isInteger( String input ) {
        try {
            int value = Integer.parseInt( input );
            if(value > minPortNumber){
                return true;
            }else{
                return false;
            }
        }
        catch( Exception e ) {
            return false;
        }
    }

    public ChatServer(int portNumber){
        serverOutputStream = new PrintStream(System.out);
        this.serverPort = updatePort();
    }

    public List<ServerThread> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<ServerThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

    private void acceptClients(ServerSocket serverSocket){
        System.out.println("Server Started on port: " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("New Client loged into chat: " + socket.getRemoteSocketAddress());
                ServerThread client = new ServerThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
        //exitServer(serverSocket);
    }
    
    //Still to do: Implement exit Server using input 
    private void exitServer(ServerSocket serverSocket){
        try {
            for(ServerThread client: clients){
                client.closeThread();
            }
            scanner.close();
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Error Exiting Server");
        }
        System.exit(1);
    }
}
