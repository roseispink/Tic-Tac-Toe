package com.src;

import java.net.Socket;

public class Game {

    class  Player implements Runnable{
        char mark;
        Socket socket;
        public Player(Socket socket, char mark)  {
            this.socket = socket;
            this.mark = mark;
        }

        @Override
        public void run() {


        }
    }
}