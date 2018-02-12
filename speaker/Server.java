import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {

        // server is listening on port 3000
        ServerSocket ss = new ServerSocket(3004);
        DataInputStream dataInputStream = null;
        // infinite loop is running for getting client request
        while (true) {
            Socket serverSocket = null;

            try {
                // server socket object receive incoming client requests
                serverSocket = ss.accept();

		System.out.println("A new client is connected : " + serverSocket);
                // getting input and out streams
                dataInputStream = new DataInputStream(serverSocket.getInputStream());
//                    System.out.println(dataInputStream.readLine());
		   String received = dataInputStream.readLine();
		DataOutputStream dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());
                dataOutputStream.writeUTF("Hi");
		System.out.println("REsponse:"+dataInputStream);
		    //System.out.println(dataInputStream.readUTF());
                SimpleAudioPlayer simple = new SimpleAudioPlayer();
                // getting ip address of local host

                // Loop will  exchange information between client and client handler
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
  //              String received = dataInputStream.readLine();
		System.out.println("Input:"+received);
                simple.startMusic(received);
                System.out.println(received);

//                dataOutputStream.close();
                // closing resources
//            input.close();
            } catch (Exception e) {
                e.printStackTrace();
                serverSocket.close();

            }
            dataInputStream.close();
        }
    }
}
