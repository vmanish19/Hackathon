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

                // Server ask user what he wants
                dos.writeUTF("What do you want?[Hi | Math | count]..\n"
                        + "Type Exit to terminate connection.");

                // getting the answer from client
                received = dis.readUTF();
                String inputReceived = received;

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // Checking Math operation input
                if (received.contains("*") || received.contains("/") || received.contains("-") || received.contains("+")
                        || received.contains("%")) {
                    inputReceived = "Math";
                }

                //  Based on the answer from the client
                //  write on output stream
                switch (inputReceived) {

                    case "Hi":
                        dos.writeUTF("515OK");
                        break;

                    case "Math":
                        Stack<Integer> stack = new Stack<Integer>();
                        int number = 0;
                        boolean flag = false;
                        int len = received.length();
                        char sign = '+';
                        for (int i = 0; i < received.length(); i++) {
                            if (Character.isDigit(received.charAt(i))) {
                                number = number * 10 + received.charAt(i) - '0';
                            }
                            if ((!Character.isDigit(received.charAt(i)) && ' ' != received.charAt(i)) || i == len - 1) {
                                if (sign == '-') {
                                    stack.push(-number);
                                }
                                if (sign == '+') {
                                    stack.push(number);
                                }
                                if (sign == '*') {
                                    stack.push(stack.pop() * number);
                                }
                                if (sign == '/') {
                                    if (number == 0) {
                                        flag = true;
                                        break;
                                    } else {
                                        stack.push(stack.pop() / number);
                                    }
                                }
                                sign = received.charAt(i);
                                number = 0;
                            }
                        }
                        if (!flag) {
                            int re = 0;
                            for (int i : stack) {
                                re += i;
                            }
                            dos.writeUTF(String.valueOf(re));
                        } else {
                            dos.writeUTF("Can't divide by 0");
                        }

                        break;

                    case "count":
                        dos.writeUTF(String.valueOf(count));
                        break;
                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
