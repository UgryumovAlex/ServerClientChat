import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket server;
    private static Socket client;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) throws IOException {

        try {
            server = new ServerSocket(8189);
            System.out.println("Server started.");

            client = server.accept();

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            while (true) {

                String clientMessage = in.readLine();
                System.out.println("Client : " + clientMessage);

                if (clientMessage.equalsIgnoreCase("exit")) {
                    out.write("Server echo : " + clientMessage + ". Bye!\n");
                    out.flush();
                    break;
                }

                out.write("Server echo : " + clientMessage + "\n");
                out.flush();
            }
        } catch (IOException e) {
                e.printStackTrace();

        } finally {
            in.close();
            out.close();
            client.close();
            server.close();
        }
    }
}