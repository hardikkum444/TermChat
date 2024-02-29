import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.sql.SQLOutput;

public class Client implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {

        try{
            Socket client = new Socket("172.22.35.51",9999); //if your in the same net, man can then give his own IP
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
                    }else{
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
