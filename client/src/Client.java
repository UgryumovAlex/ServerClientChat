import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class Client {

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8189);

            BufferedReader in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            try {
                //Переводим ввод данных в консоль в отдельный поток
                new Thread(()->{
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("Введите сообщение : ");

                        while( true ) {
                            String clientMessage = reader.readLine();
                            if (!clientSocket.isClosed()) {
                                out.write(clientMessage + "\n");
                                out.flush();

                                if (clientMessage.equalsIgnoreCase("exit")) {
                                    in.close();
                                    out.close();
                                    clientSocket.close();

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

                //В основном потоке слушаем сервер
                while ( !clientSocket.isClosed() ) {

                    String serverMessage = in.readLine();
                    if (serverMessage != null) {
                        System.out.println("Сервер : " + serverMessage);

                        if (serverMessage.equalsIgnoreCase("exit")) {
                            break;
                        }
                    } else {
                        break;
                    }
                }

            } finally {
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