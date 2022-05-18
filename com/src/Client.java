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

    private Square[] board = new Square[9];
    private Square currentSquare;
    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("...");

    public Client(String serverAddress) {

        try{
            socket = new Socket(serverAddress, 10);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch(Exception e){
            System.out.println("Server is off or doesn't exist");
            System.exit(-1);
        }
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, BorderLayout.SOUTH);

        var boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (var i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
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

    static class Square extends JPanel {
        JLabel label = new JLabel();

        public Square() {
            setBackground(Color.white);
            setLayout(new GridBagLayout());
            label.setFont(new Font("Arial", Font.BOLD, 40));
            add(label);
        }

        public void setText(char text) {
            label.setForeground(text == 'X' ? Color.BLUE : Color.RED);
            label.setText(text + "");
        }
    }




    public static void main(String[] args){
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

        Client client = new Client(ipAddress);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(320, 320);
        client.frame.setVisible(true);
        client.frame.setResizable(false);

    }
}