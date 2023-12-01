import message.CalculationMessage;
import message.Message;
import message.MessageInputStream;
import message.MessageOutputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;


public class Server {
    /**
     * The client welcoming socket
     */
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException {
        var port = 6789;
        var server = new Server(port);
        server.start();
    }

    public void start() throws IOException {
        System.out.println("Server listening on port " + serverSocket.getLocalPort());
        //noinspection InfiniteLoopStatement
        while (true) new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class ClientHandler extends Thread {
        /**
         * The client socket
         */
        private final Socket clientSocket;
        /**
         * The output stream to the client
         */
        private final MessageOutputStream out;
        /**
         * The input stream from the client
         */
        private final MessageInputStream in;

        private String name;

        public ClientHandler(Socket clientSocket) throws IOException {
            // set up the client socket
            this.clientSocket = clientSocket;
            this.out = new MessageOutputStream(clientSocket.getOutputStream());
            this.in = new MessageInputStream(clientSocket.getInputStream());
        }

        public void run() {
            try {
                log("Client connection accepted: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                while (true) {
                    var message = in.readMessage();
                    // break if the message is null
                    if (message == null) {
                        return;
                    }
                    switch (message.type()) {
                        case "CONN" -> {
                            // set the client name
                            name = message.body();
                        }
                        case "CALC" -> {
                            // calculate the result
                            var calculator = new Calculation();
                            var result = calculator.calculate(message.body());
                            // reply with result of calculation
                            out.write(new CalculationMessage(result));
                            out.flush();
                        }
                        case "TERM" -> {
                            // break from the loop and close the socket
                            return;
                        }
                        default -> throw new RuntimeException("Illegal message type " + message.type());
                    }
                    log(message);
                }
            } catch (IOException e) {
                // something went wrong, so print the stack trace, and close the socket
                e.printStackTrace();
            } finally {
                // close the socket (runs when the "TERM" case returns, or something fails)
                try {
                    clientSocket.close();
                    log("Socket closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void log(String logString) {
            var timestamp = "[" + Instant.now() + "] ";
            var client = "[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "] ";
            System.out.println(timestamp + client + logString);
        }

        public void log(Message message) {
            var logString = switch (message.type()) {
                case "CONN" -> "Client connected: " + message.body();
                case "CALC" -> "Calculation: " + message.body();
                case "TERM" -> "Client " + name + " disconnected";
                default -> throw new RuntimeException("Illegal message type " + message.type());
            };
            log(logString);
        }
    }
}