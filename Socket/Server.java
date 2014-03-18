package tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

    public static void main(String[] args) throws IOException {
        new Server().begin(4444);
    }
    ServerSocket serverSocket;

    public void begin(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Waiting for clients to connect on port " + port + "...");
            new ProtocolThread(serverSocket.accept()).start();
            //Thread.start() calls Thread.run()
        }
    }

    class ProtocolThread extends Thread {

        Socket socket;
        PrintWriter out_socket;
        BufferedReader in_socket;

        public ProtocolThread(Socket socket) {
            System.out.println("Accepting connection from " + socket.getInetAddress() + "...");
            this.socket = socket;
            try {
                out_socket = new PrintWriter(socket.getOutputStream(), true);
                in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            int r = random();
            try {
                System.out.println("Expecting Hello from client...");
                //sleep(5000);
                            try {

                                int y = Integer.parseInt(in_socket.readLine());
                                check(y);
                            } catch (Exception e) {
                                e.printStackTrace();
                            };

               
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Closing connection.");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int random() {
        int rand = 0;
        long seed = System.currentTimeMillis();

        Random r = new Random();

        r.setSeed(seed);  /*tirage aléatoire de 0 à 20 
         avec graine aléatoire en fonction du temps de millis*/

        rand = r.nextInt(5);// tirage aléatoire entre 0 et 5
        return rand;
    }

    public void check(int userTry) {
        int a = random();
        if (userTry > a) {
            System.out.println("- , TRY AGAIN");
        } else if (userTry < a) {
            System.out.println("+ , TRY AGAIN");
        } else if (userTry == a) {
            //gg c'est win
        } else {
            //petit bug 
        }
    }
}
