import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient {

	private JFrame frame = new JFrame("Tic Tac Toe");
	private JLabel messageLabel = new JLabel("...");

	private com.company.TicTacToeClient.Square[] board = new com.company.TicTacToeClient.Square[9];
	private com.company.TicTacToeClient.Square currentSquare;

	private Socket socket;
	private Scanner in;
	private PrintWriter out;

	public TicTacToeClient(String serverAddress) throws Exception {

		socket = new Socket(serverAddress, 58901);
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream(), true);

		messageLabel.setBackground(Color.yellow);
		frame.getContentPane().add(messageLabel, BorderLayout.SOUTH);

		var boardPanel = new JPanel();
		boardPanel.setBackground(Color.red);
		boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
		for (var i = 0; i < board.length; i++) {
			final int j = i;
			board[i] = new com.company.TicTacToeClient.Square();
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




}
