package com.example;

/**
 *
 * @author Manish Vishwakarma
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        DataInputStream dataInputStream = null;
        try {

            Scanner input = new Scanner(System.in);
            SimpleAudioPlayer simple = new SimpleAudioPlayer();
            // getting ip address of local host
            InetAddress localIp = InetAddress.getByName("localhost");

            // make connection with server port 3000
            Socket clientSocket = new Socket(localIp, 3000);

            // obtaining input and out streams
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
//            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Loop will  exchange information between client and client handler
            while (true) {
//                System.out.println(dataInputStream.readUTF());
//                String tosend = input.nextLine();
//                dataOutputStream.writeUTF(tosend);

                // If exit is send then close this connection 
                // and comeout from while loop
//                if (tosend.equals("Exit")) {
//                    System.out.println("Closing this connection : " + clientSocket);
//                    clientSocket.close();
//                    System.out.println("Connection closed");
//                    break;
//                }
                // printing the message as requested by client
                String received = dataInputStream.readUTF();
                simple.startMusic(received);
                System.out.println(received);

//                dataOutputStream.close();
            }

            // closing resources
//            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataInputStream.close();
    }
}
