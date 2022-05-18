package com.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private com.src.Square[] board = new com.src.Square[9];
    private com.src.Square currentSquare;
    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel label = new JLabel("...");

    public Client(String serverAddress) throws Exception {

        try{
            socket = new Socket(serverAddress, 10);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch(Exception e){
            System.out.println("Server is off or doesn't exist");
            System.exit(-1);
        }


        frame.getContentPane().add(label,BorderLayout.SOUTH);

        var boardPanel = new JPanel();
        boardPanel.setBackground(Color.red);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (var i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new com.src.Square();
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    out.println("MOVE " + j);
                }
            });
            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);

    }


    public static void main(String[] args) throws Exception {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Enter IP address of host: ");
        boolean flag = true;
        String ipAddress;
        while(true){
            ipAddress = scanner.nextLine();
            if(!ipAddress.equalsIgnoreCase("localhost")){
                String[] parts = ipAddress.split("\\.");
                if(parts.length!=4){
                    System.out.println("Wrong ip address, try again");
                    continue;
                }
                for(int i = 0; i<4; i++){
                    try{
                        int temp = Integer.parseInt(parts[i]);
                    }catch(Exception e) {
                        System.out.println("Wrong ip address, try again");
                        flag = false;
                        break;
                    }

                }
                if(flag) {
                    break;
                }
            }
            else
                break;

        }

        com.src.Client client = new com.src.Client(ipAddress);

    }
}