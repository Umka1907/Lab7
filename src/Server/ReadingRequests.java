package Server;

import Communication.Communication;
import citis.DataCities;
import citis.Enter;
import data.SQLRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.RecursiveTask;

public class ReadingRequests extends RecursiveTask {
    DataCities dataCities = new DataCities();

    public void setDataCities(DataCities dataCities) {
        this.dataCities = dataCities;
    }

    byte[] buffer;
    //DatagramSocket sock;
    Communication communication;
    Enter enter = new Enter();
    static DatagramPacket incoming;
    static String s="";
    String answer;
    byte[] data;
    SQLRequest sql = new SQLRequest();
    private static int quantity;
    int online;
    DatagramSocket sock;
    public static LinkedList<String> str = new LinkedList();
    int localQuantity;
    public static LinkedList<InetAddress> address= new LinkedList();
    public static LinkedList<Integer> port = new LinkedList();

    public static LinkedList<InetAddress> getAddress() {
        return address;
    }

    public static LinkedList<Integer> getPort() {
        return port;
    }

    public static LinkedList<String> getStr() {
        return str;
    }

    public int getLocalQuantity() {
        return localQuantity;
    }

    public ReadingRequests(int online, DatagramSocket sock) {
        this.online = online;
        this.sock = sock;
    }

    @Override
    protected LinkedList compute() {
        quantity = online;
        try {
            sql.fromSQLToCity(dataCities);
            localQuantity = sql.number_Of_Online();
            if (quantity==localQuantity){
                buffer = new byte[65536];
                incoming = new DatagramPacket(buffer, buffer.length);


                //Получаем данные
                sock.receive(incoming);
                data = incoming.getData();
                s = new String(data, 0, incoming.getLength());
                Thread.sleep(100);
                str.add(s);

                address.add(incoming.getAddress());
                port.add(incoming.getPort());




            }else if (quantity<localQuantity){
                for (quantity = online;quantity<localQuantity;quantity++ ){
                    ReadingRequests readingRequests = new ReadingRequests(localQuantity,sock);

                    invokeAll(readingRequests);

                }

            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str;
    }
}
