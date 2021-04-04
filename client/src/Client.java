import java.io.*;
import java.net.Socket;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args)  throws IOException {
        try {
              clientSocket = new Socket("localhost", 8189);

              in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
              out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

              //Переводим ввод данных в консоль в отдельный поток
              Thread consoleInput = new Thread(()->{
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("Введите сообщение : ");

                        while( true ) {
                            String clientMessage = reader.readLine();
                            if (!clientSocket.isClosed()) {
                                out.write(clientMessage + "\n");
                                out.flush();

                            } else {
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
              consoleInput.setDaemon(true);
              consoleInput.start();

              //В основном потоке слушаем сервер
              while ( !clientSocket.isClosed() ) {

                    String serverMessage = in.readLine();
                    if (serverMessage != null) {
                        System.out.println("Сервер : " + serverMessage);

                        if (serverMessage.equalsIgnoreCase("exit")) {
                            //При приходе от сервера "exit", отключаемся
                            break;
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
            clientSocket.close();
            System.out.println("Клиент был закрыт...");
        }
    }
}