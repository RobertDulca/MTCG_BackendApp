package fhtechnikum.robert.server;

import fhtechnikum.robert.system.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private final int PORT = 10001;
    private final Router router;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Server(Router router) {
        this.router = router;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server listening on port: " + PORT + "\n");

            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("Accepted connection from: " + socket.getInetAddress() + "\n");

                RequestHandler request = new RequestHandler(socket, router);

                new Thread(request).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
