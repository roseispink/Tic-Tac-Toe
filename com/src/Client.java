package com.src;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public Client(String serverAddress) throws Exception {

        socket = new Socket(serverAddress, 10);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

    }


    public static void main(String[] args) throws Exception {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Enter IP address of host: ");
        String ipAddress = scanner.nextLine();
        com.src.Client client = new com.src.Client(ipAddress);

    }
}