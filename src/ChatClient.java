import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void chatClient() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Сервер подключен");

            // Создаем поток для чтения сообщений от сервера
            Thread readThread = new Thread(new ReadThread(socket));
            readThread.start();

            // Создаем поток для отправки сообщений серверу
            Thread writeThread = new Thread(new WriteThread(socket));
            writeThread.start();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}


class ReadThread implements Runnable {

    private Socket socket;

    public ReadThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Читаем сообщение от сервера
                byte[] buffer = new byte[1024];
                int bytesCount = socket.getInputStream().read(buffer);
                String message = new String(buffer, 0, bytesCount);

                // Выводим сообщение в консоль
                System.out.println("Сервер: " + message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class WriteThread implements Runnable {

    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Читаем сообщение из консоли
                byte[] buffer = new byte[1024];
                int bytesCount = System.in.read(buffer);
                String message = new String(buffer, 0, bytesCount);

                // Отправляем сообщение серверу
                socket.getOutputStream().write(message.getBytes());
                socket.getOutputStream().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}