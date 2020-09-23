import Server.Answer;
import Server.ReadingRequests;
import Server.Server;
import citis.Enter;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        System.out.println("Укажите порт для прослушивания:");
        String port = Enter.writeString();
        DatagramSocket sock = new DatagramSocket(Integer.parseInt(port));
        ForkJoinPool pool = ForkJoinPool.commonPool();
        Server server = new Server(1, sock);



        while (true){
            ReadingRequests readingRequests = new ReadingRequests(1,sock);
            pool.invoke(readingRequests);
            ExecutorService executorService = Executors.newFixedThreadPool(readingRequests.getLocalQuantity());
            executorService.submit(server);
            Thread.sleep(1000);

            for (int i =0; i<server.getAnswer().size(); i++){
                Answer answer = new Answer(sock, server.getAnswer(),readingRequests.getAddress(), readingRequests.getPort() );
                answer.start();
            }

        }

    }
}
