//CAUTION: code looks too clean (props to IJ)
//community edition ofcourse
//used a basic template to get started (server temp)

// import com.sun.xml.internal.bind.v2.TODO;

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
import java.io.*;

public class Server implements Runnable{

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private int port = 9999;

    public Server(){
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run(){

        try{
            server = new ServerSocket(port);
            System.out.println("Server listening on port "+port+"..."); //man has made his port dynamic
            pool = Executors.newCachedThreadPool();
            while(!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler); //this will run the run method in ConnectionHandler (something new i learnt)
            }
        } catch(Exception e){
//            e.printStackTrace(); not necessary but whatever
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
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_BLACK = "\u001B[30m";


        public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
        // public static final String bold = "\033[1m";
        // public static final String 



        ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run(){
            try{
                out = new PrintWriter(client.getOutputStream(), true); //setting auto flush for cleaner code (heheh)
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                out.println("Please enter a nickname --> " );
                nickName = in.readLine();

                //add if statements for checking exceptions 
                System.out.println(ANSI_GREEN_BACKGROUND + nickName + " has connected!" + ANSI_RESET);  //for server backend
                broadcast( ANSI_GREEN_BACKGROUND + nickName + " has joined the chat!" + ANSI_RESET);
                out.println();

                String message;
                while((message = in.readLine()) != null){
                    if(message.startsWith("/rename ")){
                        String[] messageSplit = message.split(" ",2);

                        if(messageSplit.length == 2 && !messageSplit[1].trim().isEmpty()){

                            broadcast( ANSI_RED + nickName + " renamed themselves to " + messageSplit[1] + ANSI_RESET );
                            out.println();
                            System.out.println( ANSI_RED + nickName + " renamed themselves to " + messageSplit[1] + ANSI_RESET );
//                          sendMessage("you have now renamed yourself to "+ messageSplit[1]);
                            nickName = messageSplit[1];
                            out.println( ANSI_GREEN + "you have now renamed yourself to "+ messageSplit[1] + ANSI_RESET);
                            out.println();
                        }else{
                            out.println( ANSI_RED + "No nickname provided" + ANSI_RESET);
                            out.println("Please have a look at the format");
                        }
                    }else if(message.startsWith("/quit")){
                        broadcast(ANSI_RED_BACKGROUND + nickName + " has left the chat"+ANSI_RESET);
                        System.out.print(ANSI_RED_BACKGROUND+nickName+" has left the chat!"+ANSI_RESET);
                        sendMessage("you have now left the chat!");
                        shutdown();
                    }else if(message.startsWith("/send")){


                        //dont be a mad man like me and forget to sourround following code with try and catch
                        String currentDirectory = System.getProperty("user.dir");

                        int index1 = message.indexOf("name:")+"name:".length();
                        int index2 = message.indexOf("/name");
                        String totalName = message.substring(index1,index2);

                        // sendMessage("name of the file is " + filename2); 

                        String[] chaArr = totalName.split("\\.");
                        String extension = chaArr[1];
                        String fileName = chaArr[0];


                        File file = new File(currentDirectory, fileName+"."+extension);

                        try {

                            if (file.createNewFile()) {
                                broadcast("New file has been created!");
                            } else {
                                broadcast("File already exists.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                    else {
                        out.println();
                        broadcast(ANSI_PURPLE_BACKGROUND + nickName + ANSI_RESET + ": " + message);
                        out.println();
                        // System.out.println(ANSI_YELLOW + "This text is colored" + ANSI_RESET);
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

    //adding the functionality of file uploading
    public void sendFileServe(String path){

        //set the path
        //create the file
        //with the data 
        //figure out how to write data to the file 
        //figure out how to download data from the server

    }
}
