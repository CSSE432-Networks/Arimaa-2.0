package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Jesse Shellabarger on 5/11/2017.
 */
public class ObserverListener extends Thread {

    private final ServerSocket serverSocket;
    private final List<Socket> observers;

    public ObserverListener(ServerSocket serverSocket, List<Socket> observers) {

        this.serverSocket = serverSocket;
        this.observers = observers;
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                observers.add(socket);
                System.out.println("added observer");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
