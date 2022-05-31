package com.src;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    static int win = 0;

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private final Square[] board = new Square[9];
    private Square currentSquare;
    private final JFrame frame = new JFrame("Tic Tac Toe");
    private final JLabel messageLabel = new JLabel("...");

    public Client() {
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

    public void connect(String serverAddress) {
        try {
            socket = new Socket(serverAddress, 10);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Server is off or doesn't exist");
            System.exit(-1);
        }
    }

    public void disconnect() {
        System.out.println("QUIT bye bye");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean repeat() {
        JFrame f = new JFrame();
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(f, "Wanna play again?", "Your result " + win, dialogButton);
        return dialogResult == 0;
    }

    public void play() {
        try {
            var response = in.nextLine();
            var mark = response.charAt(8);
            var opponentMark = mark == 'X' ? 'O' : 'X';
            frame.setTitle("Tic Tac Toe: Player " + mark);
            while (in.hasNextLine()) {
                response = in.nextLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentSquare.setText(mark);
                    currentSquare.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    var loc = Integer.parseInt(response.substring(15));
                    board[loc].setText(opponentMark);
                    board[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                } else if (response.startsWith("VICTORY")) {
                    win++;
                    JOptionPane.showMessageDialog(frame, "Winner Winner");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    JOptionPane.showMessageDialog(frame, "Sorry you lost");
                    break;
                } else if (response.startsWith("TIE")) {
                    JOptionPane.showMessageDialog(frame, "Tie");
                    break;
                } else if (response.startsWith("OTHER_PLAYER_LEFT")) {
                    JOptionPane.showMessageDialog(frame, "Other player left");
                    break;
                }
            }
            out.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
            frame.dispose();
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter IP address of host: ");
        boolean flag = true;
        String ipAddress;
        while (true) {
            ipAddress = scanner.nextLine();
            if (!ipAddress.equalsIgnoreCase("localhost")) {
                String[] parts = ipAddress.split("\\.");
                if (parts.length != 4) {
                    System.out.println("Wrong ip address, try again");
                    continue;
                }
                for (int i = 0; i < 4; i++) {
                    try {
                        Integer.parseInt(parts[i]);
                    } catch (Exception e) {
                        System.out.println("Wrong ip address, try again");
                        flag = false;
                        break;
                    }

                }
                if (flag) {
                    break;
                }
            } else
                break;

        }

        Client client;
        do {
            client = new Client();
            client.connect(ipAddress);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(320, 320);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.play();
        } while (client.repeat());
        System.exit(0);
    }
}