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
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PINK = "\033[95m";

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
			while ((inMessage = in.readLine()) != null) {
			    if (inMessage.startsWith("/upload")) {
			        String totalName = "";
			        StringBuilder fileContent = new StringBuilder();
                    // String temp = inMessage;
                    String upperLine = inMessage.substring("/upload".length()).trim();
                    fileContent.append(upperLine).append("\n");

			        while (true) {
			            String line = in.readLine();
			            if (line == null || line.isEmpty()) {
			                // Exit the loop if the line is null or empty
			                break;
			            }

			            // Remove the "/upload " prefix from each line
			            line = line.replace("/upload ", "");

			            if (line.contains(".")) {
			                // If the line contains ".", set totalName and exit the loop
			                totalName = line;
			                break;
			            }

			            // Append the line to fileContent
			            fileContent.append(line.trim()).append("\n"); // Trim leading and trailing whitespace
			        }

                        // while (!(line = in.readLine()).isEmpty()) {
                            
                        //     line = line.replace("/send ","");

                        //     if(line.contains(".")){
                        //         totalName = line;
                        //     }else{
                        //     fileContent.append(line).append("\n");
                        //     // broadcast(line);
                        // }

                        // }



                    
                    // Print file content for each upload
                    // System.out.println("File content:");
                    // System.out.println(fileContent.toString());
                    // System.out.println("Total name: " + totalName);

                     String currentDirectory = System.getProperty("user.dir");

                     String[] chaArr = totalName.split("\\.");
                     String extension = chaArr[1];
                     String fileName = chaArr[0];


                     File file = new File(currentDirectory, fileName+"."+extension);

                      try {

                      if (file.createNewFile()) {
                      // broadcast(ANSI_GREEN + "File successfully uploaded to server!" + ANSI_RESET);
                      FileWriter fileWriter = new FileWriter(file);
                      fileWriter.write(fileContent.toString());
                      fileWriter.close();


                        int inter = 50;
                        int delay = 30;
                        System.out.print(ANSI_PINK + "Loading-> [");

                        for(int i =0;i<=inter;i++){
                            
                            System.out.print("==");

                            System.out.flush();

                            try{
                                if(i==20 || i==30){
                                    delay = 500;
                                }
                                Thread.sleep(delay);
                                delay = 30;
                            }catch(InterruptedException e){
                                e.printStackTrace();
                                System.out.println("Error!!!");
                                Thread.currentThread().interrupt();
                            }
                        }

                      System.out.print("]" + ANSI_RESET);
                      System.out.println();
                      System.out.flush();
                      System.out.println(ANSI_GREEN + "You have successfully downloaded the file" + ANSI_RESET);

                       } else {
                                System.out.println(ANSI_RED + "File cant be downloaded, ERROR!!." + ANSI_RESET);
                       }



                        } catch (IOException e) {
                            e.printStackTrace();
                        }








                    
                } else {
                    System.out.println(inMessage);
                }
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


                        int inter = 50;
                        int delay = 30;
                        System.out.print("Loading-> [");

                        for(int i =0;i<=inter;i++){
                            
                            System.out.print("==");

                            System.out.flush();

                            try{
                                if(i==20 || i==30){
                                    delay = 500;
                                }
                                Thread.sleep(delay);
                                delay = 30;
                            }catch(InterruptedException e){
                                e.printStackTrace();
                                System.out.println("Error!!!");
                                Thread.currentThread().interrupt();
                            }
                        }


                        System.out.print("]");
                        System.out.println();
                        System.out.flush();













                        



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



