import java.io.*;
import java.net.Socket;

public class Client {

    private static Socket clientSocket;
    //private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 8189);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                //Пробуем перенести ввод данных в консоль в отдельный поток


                while (true) {
                    System.out.println("Сообщение на сервер:");

                    String clientMessage = reader.readLine(); // ждём пока соощение пишем в консоль
                    out.write(clientMessage + "\n"); // отправляем сообщение на сервер
                    out.flush();

                    String serverMessage = in.readLine(); // ждём, что скажет сервер
                    System.out.println(serverMessage); // получив - выводим на экран

                    if (clientMessage.equalsIgnoreCase("exit")) {
                        break;
                    }
                }

            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}