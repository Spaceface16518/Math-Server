import message.CalculationMessage;
import message.Message;
import message.MessageInputStream;
import message.MessageOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;


public class Server {
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException {
        var port = 8000;
        var server = new Server(port);
        server.start();
    }

    public void start() throws IOException {
        //noinspection InfiniteLoopStatement
        while (true) new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final MessageOutputStream out;
        private final MessageInputStream in;

        private String name;

        public ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.out = new MessageOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            this.in = new MessageInputStream(clientSocket.getInputStream());
        }

        public void run() {
            try {
                while (true) {
                    var message = in.readMessage();
                    switch (message.type()) {
                        case "CONN" -> {
                            name = message.body();
                        }
                        case "CALC" -> {
                            // reply with result of calculation
                            var calculator = new Calculation();
                            var result = calculator.calculate(message.body());
                            out.write(new CalculationMessage(result));
                            out.flush();
                        }
                        case "TERM" -> {
                            return;
                        }
                        default -> throw new RuntimeException("Illegal message type " + message.type());
                    }
                    log(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // close the socket (runs when the "TERM" case returns, or something fails)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void log(Message message) {
            var timestamp = "[" + Instant.now() + "] ";
            var client = "[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] ";
            var logString = switch (message.type()) {
                case "CONN" -> "Client connected: " + message.body();
                case "CALC" -> "Calculation: " + message.body();
                case "TERM" -> "Client " + name + " disconnected";
                default -> throw new RuntimeException("Illegal message type " + message.type());
            };
            System.out.println(timestamp + client + logString);
        }
    }
}