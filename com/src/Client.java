package com.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private com.src.Client.Square[] board = new com.src.Client.Square[9];
    private com.src.Client.Square currentSquare;
    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel label = new JLabel("...");

    public Client(String serverAddress) throws Exception {

        socket = new Socket(serverAddress, 10);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        frame.getContentPane().add(label,BorderLayout.SOUTH);

        var boardPanel = new JPanel();
        boardPanel.setBackground(Color.red);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (var i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new com.src.Client.Square();
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
            this.setBackground(Color.white);
            this.setLayout(new GridBagLayout());
            this.label.setFont(new Font("Arial", 1, 40));
            this.add(this.label);
        }

        public void setText(char text) {
            this.label.setForeground(text == 'X' ? Color.BLUE : Color.RED);
            this.label.setText(text + "");
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Enter IP address of host: ");
        String ipAddress = scanner.nextLine();
        com.src.Client client = new com.src.Client(ipAddress);

    }
}