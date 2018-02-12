package com.example.EchoServer;

/**
 *
 * @author Manish Vishwakarma
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class EchoServer {

    private static int count;
    private static int clientNum;

    public static void main(String[] args) throws IOException {
        // server is listening on port 3000
        ServerSocket ss = new ServerSocket(3000);

        // infinite loop is running for getting client request
        while (true) {
            Socket serverSocket = null;

            try {
                // server socket object receive incoming client requests
                serverSocket = ss.accept();
                count++;

                System.out.println("A new client" + clientNum++ + " is connected : " + serverSocket);

                // getting input and out streams
                DataInputStream dataInputStream = new DataInputStream(serverSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());

                System.out.println("Allocating new thread for this client" + clientNum);

                // create a new thread object
                Thread newThread = new ClientHandler(serverSocket, dataInputStream, dataOutputStream, count);

                // Invoking the start() method
                newThread.start();

            } catch (Exception e) {
                serverSocket.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private static int count;

    // Constructor
    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos, int count) {
        this.s = socket;
        this.dis = dis;
        this.dos = dos;
        this.count = count;

    }

    @Override
    public void run() {
        String received;
        while (true) {
            try {

                Scanner input = new Scanner(System.in);
                // Server ask user what he wants
                dos.writeUTF(input.nextLine());

                // getting the answer from client
//                received = dis.readUTF();
//                String inputReceived = received;
//
//                if (received.equals("Exit")) {
//                    System.out.println("Client " + this.s + " sends exit...");
//                    System.out.println("Closing this connection.");
//                    this.s.close();
//                    System.out.println("Connection closed");
//                    break;
//                }
//
//                // Checking Math operation input
//                if (received.contains("*") || received.contains("/") || received.contains("-") || received.contains("+")
//                        || received.contains("%")) {
//                    inputReceived = "Math";
//                }
                //  Based on the answer from the client
                //  write on output stream
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
