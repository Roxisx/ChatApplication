package ChatServer.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String host = "localhost"; //Still to do: Change to PC-Name for cross network communication
    private static final int minPortNumber = 1023;
    private static int portNumber = 6666;
    private static String userName;
    private static String serverHost;
    private static int serverPort;
    
    //Input and output streams
    private Scanner clientInputScanner;
    private PrintStream clientOutputStream;

    public static void main(String[] args){
        Client client = new Client(userName, host, portNumber);
        client.startClient();
    }

    //Constructor manual input for username then port number
    private Client(String username, String host, int portnumber){
        this.clientInputScanner = new Scanner(System.in);
        this.clientOutputStream = new PrintStream(System.out);
        this.serverHost = host;
        this.userName = updateUsername();
        this.serverPort = updatePort();
    }
           
    public PrintStream getOutPutStream(){
        return clientOutputStream;
    }
    
    public Scanner getInputScanner(){
        return clientInputScanner;
    }
    
    public final String updateUsername(){
        String input = null;
        while(input == null || input.trim().equals("")){
            clientOutputStream.print("Please input username: ");
            input = clientInputScanner.nextLine();
            if(input.trim().equals("")) {                                           // null, empty, whitespace(s) not allowed.
                System.out.println("Invalid. Please enter again:");
            }
        }
        return input;
    }
    
    public int updatePort(){
        String input;
        int port = portNumber;
        System.out.println("Please input port number greater than 1023 or Enter to use default (6666):");
        while(true){
            input = clientInputScanner.nextLine();
            if(input.equals("Exit") || input.equals("exit")){port = 0;break;}
            if(isInteger(input.trim())){
                port = Integer.parseInt(input); break;
            }else if(input.trim().equals("")){  
                break;
            }else{
                System.out.println("Please use digits more than 1023 or Enter for default. Type 'Exit' to leave.");
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

    private void startClient(){
        try{
            // Loop to allow user to retry port numbers until one works or Exit to end Client
            int port = updatePort();
            if(port == 0){ endClient(); }
            else{this.portNumber = port; this.serverPort = port;}

            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.

            ClientThread server = new ClientThread(socket, userName);
            Thread thread = new Thread(server);

            clientOutputStream.println("Welcome :" + userName);
            clientOutputStream.println("Local Port Number:" + socket.getLocalPort());
            clientOutputStream.println("Server Address: " + socket.getRemoteSocketAddress());
            clientOutputStream.println("Type 'Exit' to end the Client");

            thread.start();
            while(thread.isAlive()){
                if(clientInputScanner.hasNextLine()){
                    String message = clientInputScanner.nextLine();
                    if(message.equals("Exit") || message.equals("exit")){//Type "Exit" to end the client allow time to send final message to server
                        break;
                    }
                    server.addNextMessage(message);
                }
                // NOTE: scan.hasNextLine waits input (in the other words block this thread's process).
                // NOTE: I recommend waiting short time like the following.
                // else {
                //    Thread.sleep(200);
                // }
            }       
            server.addNextMessage(userName + " is Logging off ...");
            Thread.sleep(1000);
            endClient();
        }catch(IOException ex){
            clientOutputStream.println("Connection error! Check if correct Port number used? Error description below: \n" + ex.getMessage() + "\n");
        }catch(InterruptedException ex){
            clientOutputStream.println("Interruption Occured! Check if correct Port number used? Error Description below: \n" + ex.getMessage() + "\n");
        }
    }
    
    public void endClient(){
        clientInputScanner.close();
        clientOutputStream.close();
        System.exit(1);
    }
}
