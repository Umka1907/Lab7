package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

public class Answer extends Thread {
    DatagramSocket sock;
    LinkedList<String> answer;
    LinkedList<InetAddress> address;
    LinkedList<Integer> ports;




    public Answer(DatagramSocket sock, LinkedList<String> answer, LinkedList<InetAddress> address, LinkedList<Integer> ports) {
        this.sock = sock;
        this.answer = answer;
        this.address = address;
        this.ports = ports;
    }


    @Override
    public void run() {
        try {
        String s = answer.pollFirst();
        InetAddress ad = address.pollFirst();
        int port = ports.pollFirst();
        DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, ad,port);


            sock.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
