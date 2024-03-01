import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.sql.SQLOutput;

public class Client implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {

        try{
            Socket client = new Socket("127.0.0.1",9999); //if your in the same net, man can then give his own IP
            out = new PrintWriter(client.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            InputHandler inHandler = new InputHandler();

            //using the runnable functionality instead of directly using a thread
            //this is done normally if man wants to inject Threads
            
            Thread t = new Thread(inHandler);
            t.start();

            String inMessage;
            while((inMessage = in.readLine()) != null) {
                System.out.println(inMessage);
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("error is occuring");
            shutdown();

        }

    }

    public void shutdown(){

        done = true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch(IOException e){
            //man will handle shutdown

        }
    }

    class InputHandler implements Runnable {

        @Override
        public void run() {
            try{
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while(!done){
                    String message = inReader.readLine();
                    if(message.equals("/quit")){
                        inReader.close();
                        shutdown();
                    }else if(message.startsWith("/send")){    

                        



                        // out.println(message);
                        // int index1 = message.indexOf("name:")+"name:".length();
                        // int index2 = message.indexOf("/name");
                        // String totalName = message.substring(index1,index2);

                        String fileName = message.substring("/send".length()).trim();

                        // sendMessage("name of the file is " + filename2); 

                        // String[] chaArr = totalName.split("\\.");
                        // String extension = chaArr[1];
                        // String fileName = chaArr[0];

                        StringBuilder content = new StringBuilder();
                        try{
                            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                            String line;
                            // while((line = fileReader.readLine())!=null){
                            //     content.append(line).append("\n").append("/send ");
                            //     // out.print("/send ");
                            //     // out.print(line);
                            //     // out.flush();
                            // }
                            boolean firstLine = true;
                            while ((line = fileReader.readLine()) != null) {
                                if (!firstLine) {
                                    content.append("\n"); // Add newline character after the first line
                                }
                                content.append("/send ").append(line); // Append "/send" and the line wtf is actually going on
                                firstLine = false; // Set the flag to false after the first line
                            }


                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        out.print("/send ");
                        out.print(content.toString());
                        out.flush();
                        out.println();
                        out.println(fileName);
                        out.flush();
                        out.println();
                        out.println(); //will have to correct this eventually

                        // System.out.println(fileName);

                    



                    }
                    else{
                        out.println(message); //this is then sent to the server
                    }
                }
            }catch(IOException e){
                shutdown();
            }
        }

//        public void shutdown(){
//
//
//        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}


// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.Socket;
// import java.io.*;
// //import java.sql.SQLOutput;

// public class Client implements Runnable{

//     private Socket client;
//     private BufferedReader in;
//     private PrintWriter out;
//     private boolean done;

//     @Override
//     public void run() {

//         try{
//             Socket client = new Socket("loopback",9999); //if your in the same net, man can then give his own IP
//             out = new PrintWriter(client.getOutputStream(),true);
//             in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//             InputHandler inHandler = new InputHandler();

//             //using the runnable functionality instead of directly using a thread
//             //this is done normally if man wants to inject Threads
            
//             Thread t = new Thread(inHandler);
//             t.start();

//             String inMessage;
//             while((inMessage = in.readLine()) != null) {
//                 System.out.println(inMessage);
//             }
//         }catch(IOException e){
//             e.printStackTrace();
//             System.out.println("error is occuring");
//             shutdown();

//         }

//     }

//     public void shutdown(){

//         done = true;
//         try{
//             in.close();
//             out.close();
//             if(!client.isClosed()){
//                 client.close();
//             }
//         }catch(IOException e){
//             //man will handle shutdown

//         }
//     }

//     class InputHandler implements Runnable {

//         @Override
//         public void run() {
//             try{
//                 BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
//                 while(!done){
//                     String message = inReader.readLine();
//                     if(message.equals("/quit")){
//                         inReader.close();
//                         shutdown();
//                     }else if(message.startsWith("/send")){    

                        



//                         // out.println(message);
//                         // int index1 = message.indexOf("name:")+"name:".length();
//                         // int index2 = message.indexOf("/name");
//                         // String totalName = message.substring(index1,index2);

//                         String fileName = message.substring("/send".length()).trim();

//                         // sendMessage("name of the file is " + filename2); 

//                         // String[] chaArr = totalName.split("\\.");
//                         // String extension = chaArr[1];
//                         // String fileName = chaArr[0];

//                         StringBuilder content = new StringBuilder();
//                         try{
//                             BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
//                             String line;
//                             while((line = fileReader.readLine())!=null){
//                                 content.append(line).append("\n");
//                             }
//                         }catch(IOException e){
//                             e.printStackTrace();
//                         }

//                         out.print("/send ");
//                         out.print(content.toString());
//                         // out.flush();
//                         // System.out.println(fileName);

                    



//                     }

//                     else{
//                         out.println(message); //this is then sent to the server
//                     }
//                 }
//             }catch(IOException e){
//                 shutdown();
//             }
//         }

// //        public void shutdown(){
// //
// //
// //        }
//     }

//     public static void main(String[] args) {
//         Client client = new Client();
//         client.run();
//     }
// }




// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.Socket;
// //import java.sql.SQLOutput;

// public class Client implements Runnable{

//     private Socket client;
//     private BufferedReader in;
//     private PrintWriter out;
//     private boolean done;

//     @Override
//     public void run() {

//         try{
//             Socket client = new Socket("loopback",9999); //if your in the same net, man can then give his own IP
//             out = new PrintWriter(client.getOutputStream(),true);
//             in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//             InputHandler inHandler = new InputHandler();

//             //using the runnable functionality instead of directly using a thread
//             //this is done normally if man wants to inject Threads
            
//             Thread t = new Thread(inHandler);
//             t.start();

//             String inMessage;
//             while((inMessage = in.readLine()) != null) {
//                 System.out.println(inMessage);
//             }
//         }catch(IOException e){
//             e.printStackTrace();
//             System.out.println("error is occuring");
//             shutdown();

//         }

//     }

//     public void shutdown(){

//         done = true;
//         try{
//             in.close();
//             out.close();
//             if(!client.isClosed()){
//                 client.close();
//             }
//         }catch(IOException e){
//             //man will handle shutdown

//         }
//     }

//     class InputHandler implements Runnable {

//         @Override
//         public void run() {
//             try{
//                 BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
//                 while(!done){
//                     String message = inReader.readLine();
//                     if(message.equals("/quit")){
//                         inReader.close();
//                         shutdown();
//                     }else{
//                         out.println(message); //this is then sent to the server
//                     }
//                 }
//             }catch(IOException e){
//                 shutdown();
//             }
//         }

// //        public void shutdown(){
// //
// //
// //        }
//     }

//     public static void main(String[] args) {
//         Client client = new Client();
//         client.run();
//     }
// }
