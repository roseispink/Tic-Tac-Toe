package com.src;

import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(10)) {
            System.out.println("Tic Tac Toe Server is Running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) {
                com.src.Game game = new Game();
                pool.execute(game.new Player(listener.accept(), 'X'));
                System.out.println("Player one has join");
                pool.execute(game.new Player(listener.accept(), 'O'));
                System.out.println("Player two has join");
            }
        }
    }
}