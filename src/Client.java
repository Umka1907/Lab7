import Commands.ExecuteScriptCommand;
import Communication.ClientArgument;
import Communication.Communication;
import citis.Enter;
import citis.Inputting;
import data.Users;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {

    public static void main(String[] arg) throws JAXBException {
        try {

            System.out.println("Укажите имя хоста:");
            String host = Enter.writeString();
            System.out.println("Укажите порт:");
            String port = Enter.writeString();

            Users users = new Users();
            users.creatOrExit();



            ClientArgument clientArgument = new ClientArgument();
            Communication communication = null;
            String and = new String();
            String type;
            ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
            String execute_script= new String();;


            try (DatagramChannel channel = DatagramChannel.open()) {

                ByteBuffer buffer = null;
                SocketAddress address = new InetSocketAddress(InetAddress.getByName(host), Integer.parseInt(port));
                channel.bind(null);
                channel.socket().setSoTimeout(5000);


                while (true) {


                        //Ожидаем ввод сообщения серверу
                        clientArgument.setUsername(users.getUsername());
                        clientArgument.param();

                        type = clientArgument.getType();


                        //Отправляем сообщение
                    do {

                        communication = new Communication(clientArgument.getType(), clientArgument.getParams(), clientArgument.getCityArgument(),clientArgument.getUsername());
                        buffer = ByteBuffer.wrap(communication.serializeToString().getBytes());
                        channel.send(buffer, address);
                        if (clientArgument.getType().equals("exit")) {
                            System.out.println("Работа заверщена.");
                            System.exit(0);
                        }


                        { //получение входящих данных
                            byte[] buf = new byte[65536];
                            DatagramPacket packet = new DatagramPacket(buf, buf.length);
                            boolean successfullAttempt = false;

                            try {
                                channel.socket().receive(packet);
                                successfullAttempt = true;

                            } catch (SocketTimeoutException e) {
                                System.out.println("Server.Server is unavailable...");
                                continue;
                            }
                            String received;
                            if (successfullAttempt) {
                                received = new String(packet.getData(), 0, packet.getLength());
                            } else {
                                received = "Can't connect to server";
                            }
                            buffer.flip();


                            String s = new String(buf, 0, buf.length);
                            System.out.println('\n' + "Ответ сервера: " + '\n' + s + '\n');

                            if ((type.equals("update")) && (s.trim().equals("В коллекции есть элемент с указанным id, введите изменения:"))) {

                                clientArgument.setType("update_data");
                                clientArgument.getCityArg();

                                communication = new Communication(clientArgument.getType(), clientArgument.getParams(), clientArgument.getCityArgument(), clientArgument.getUsername());
                                buffer = ByteBuffer.wrap(communication.serializeToString().getBytes());
                                channel.send(buffer, address);

                                packet = new DatagramPacket(buf, buf.length);
                                successfullAttempt = false;

                                try {
                                    channel.socket().receive(packet);
                                    successfullAttempt = true;

                                } catch (SocketTimeoutException e) {
                                    System.out.println("Server.Server is unavailable...");
                                    continue;
                                }
                                if (successfullAttempt) {
                                    received = new String(packet.getData(), 0, packet.getLength());
                                } else {
                                    received = "Can't connect to server";
                                }
                                buffer.flip();


                                s = new String(buf, 0, buf.length);
                                System.out.println('\n' + "Ответ сервера: " + '\n' + s + '\n');

                            }

                            if (type.equals("execute_script")||(execute_script.equals("execute_script"))){
                                execute_script = "execute_script";
                                if (clientArgument.getParams() != null) {
                                    try {
                                        if (Inputting.runningScriptNames.contains(clientArgument.getParams())&&(clientArgument.getType().equals("execute_script"))) {
                                            System.out.println("Этот сценарий уже был вызван. Избегайте бесконечной рекурсии.");
                                            clientArgument.setType("null");
                                        }
                                        if (clientArgument.getType().equals("execute_script")) {
                                            Inputting.parseScript(clientArgument.getParams());
                                        }

                                            String commandFromAFile = Inputting.readLine();

                                            if (commandFromAFile.split(" ")[0].equals("ENDFILE")){

                                                System.out.println ("Выполнение команд из файла "+ commandFromAFile.split(" ")[1]+" закончино\n");

                                                if (Inputting.getListCommands().peekLast()==null){
                                                    and = "ENDFILE";
                                                    execute_script = "null";
                                                    break;
                                                }else {
                                                    commandFromAFile = Inputting.readLine();
                                                    Enter.history(commandFromAFile);
                                                    clientArgument.bagging(commandFromAFile);

                                                }


                                            }else{
                                                Enter.history(commandFromAFile);
                                                clientArgument.bagging(commandFromAFile);


                                            }


                                    } catch (IOException e) {
                                        System.out.println ("Error while reading script file " + clientArgument.getParams());
                                    }
                                } else {
                                    System.out.println("Неправильный формат команд. ");
                                }
                            }
                            }

                    }while ((execute_script.equals("execute_script")&&(and != "ENDFILE") ));
                }

            } catch (IOException e) {
                System.err.println("IOException " + e);
            }

        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

}


