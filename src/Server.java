//CAUTION: code looks too clean (props to IJ)
//community edition ofcourse
//used a basic template to get started (server temp)

import com.sun.xml.internal.bind.v2.TODO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run(){

        try{
            server = new ServerSocket(9999);
            System.out.println("Server listening on port 9999..."); //make port dynamic
            pool = Executors.newCachedThreadPool();
            while(!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler); //this will run the run method in ConnectionHandler
            }
        } catch(Exception e){
//            e.printStackTrace();
            shutdown();
        }

    }

    public void shutdown(){
        done = true;
        try {
            if (!server.isClosed()) {
                server.close();
            }
            for(ConnectionHandler ch : connections){
                ch.shutdown();
            }
        }catch(IOException e){
            //ignore
        }
    }

    public void broadcast(String message){
        for(ConnectionHandler ch : connections){
            if(ch != null){
                ch.sendMessage(message);
            }
        }
    }

    class ConnectionHandler implements Runnable{

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String nickName;

        ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run(){
            try{
                out = new PrintWriter(client.getOutputStream(), true); //setting auto flush for cleaner code
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println("Please enter a nickname--> ");
                nickName = in.readLine();

                //add if statements for checking exceptions
                System.out.println(nickName+" has connected!");  //for server backend
                broadcast(nickName+" has joined the chat!");

                String message;
                while((message = in.readLine()) != null){
                    if(message.startsWith("/nick ")){
                        String[] messageSplit = message.split(" ",2);

                        if(messageSplit.length == 2){

                            broadcast(nickName + " renamed themselves to " + messageSplit[1]);
                            System.out.println(nickName + " renamed themselves to " + messageSplit[1]);
//                          sendMessage("you have now renamed yourself to "+ messageSplit[1]);
                            nickName = messageSplit[1];
                            out.println("you have now renamed yourself to "+ messageSplit[1]);
                        }else{
                            out.println("No nickname provided");
                            out.println("Please have a look at the format");
                        }
                    }else if(message.startsWith("/quit ")){
                        broadcast(nickName + " has left the chat");
                        shutdown();
                    }else {
                        broadcast(nickName + ": " + message);
                    }
                }

            }catch(IOException e){
                shutdown(); //Exception instead of IOException so that server shuts down on any exception
            }

        }

        public void sendMessage(String message){
            out.println(message);
        }

        public void shutdown(){
            //closing all the streams
            try {
                in.close();
                out.close();
                if (!client.isConnected()) {
                    client.close();
                }
            }catch(IOException e){
                //ignore
            }
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run(); //connection handler
    }
}
