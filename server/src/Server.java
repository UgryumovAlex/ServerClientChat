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
            System.out.println("Сервер запущен.");

            client = server.accept();

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            //Переводим ввод данных в консоль в отдельный поток
            new Thread(()->{
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("Введите сообщение : ");

                    while( true ) {
                        String serverMessage = reader.readLine();
                        if (!client.isClosed()) {
                            out.write(serverMessage + "\n");
                            out.flush();

                            if (serverMessage.equalsIgnoreCase("exit")) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            //В основном потоке случаем клиента
            while( !client.isClosed() ) {

                String clientMessage = in.readLine();
                if (clientMessage != null) {
                    System.out.println("Клиент : " + clientMessage);

                    if (clientMessage.equalsIgnoreCase("exit")) {
                        out.write(clientMessage + "\n");
                        out.flush();

                    }

                } else {
                    break;
                }
            }
        } catch (IOException e) {
                e.printStackTrace();

        } finally {
            in.close();
            out.close();
            client.close();
            server.close();
            System.out.println("Сервер был остановлен...");
        }
    }
}