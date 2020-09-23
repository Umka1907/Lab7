package Server;

import Commands.*;
import Communication.Communication;
import citis.City;
import citis.DataCities;
import citis.Enter;
import data.SQLRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Server  extends Thread implements Runnable{
    CommandExecutor commandExecutor = new CommandExecutor();
    DataCities dataCities = new DataCities();

    public void setDataCities(DataCities dataCities) {
        this.dataCities = dataCities;
    }

    Communication communication;
    Enter enter = new Enter();
    static DatagramPacket incoming;
    static String s="";
    byte[] data;
    SQLRequest sql = new SQLRequest();
    private static int quantity;
    int online;
    DatagramSocket sock;

    LinkedList<String> answer= new LinkedList();

    public LinkedList<String> getAnswer() {
        return answer;
    }


    public Server(int online, DatagramSocket sock) {
        this.online = online;
        this.sock = sock;
    }


    public  void TransactionProcessing(LinkedList<String> linkedList) throws IOException, ClassNotFoundException {
        sql.fromSQLToCity(dataCities);

        communication = Communication.deserializeFromString(linkedList.	pollFirst());
        System.out.println("Сервер получил: " + communication.getType());
        enter.history(communication.getType());
        if ((communication.getType().equals("update"))) {
            if ((!communication.getParams().isEmpty()) && (dataCities.arrayListId().contains(Integer.parseInt(communication.getParams())))) {
                if(sql.userElements(communication.getUsername(),Integer.parseInt(communication.getParams()))){
                    String sms = "В коллекции есть элемент с указанным id, введите изменения:";
                    answer.add(sms);
                }else {
                    String sms = "В коллекции есть элемент с указанным id, но он принадлежит другому пользователю, к сожалению, вы не можете внести изменения.";
                    answer.add(sms);
                }
            } else {
                String sms = "Элемента с таким id нет в коллекции или id не был указан";
                answer.add(sms);


            }
        } else {
            //Ответ
            s = commandExecute(communication, dataCities);
            answer.add(s);


        }
    }


    public  String commandExecute(Communication com, DataCities dataCities) throws ClassNotFoundException, IOException {

            switch (com.getType()) {
                case "add":
                    return add(com, dataCities);
                case "show":
                    return commandRan(com, dataCities);
                case "info":
                    return commandRan(com, dataCities);
                case "help":
                    return commandRan(com, dataCities);
                case "clear":
                    return clear(com, dataCities);
                case "max_by_meters_above_sea_level":
                    return commandRan(com, dataCities);
                case "remove_by_id":
                    return removeEl(com, dataCities);
                case "group_counting_by_government":
                    return commandRan(com, dataCities);
                case "add_if_min":
                    return addIfMin(com, dataCities);
                case "execute_script":
                    return executeScript(com, dataCities);
                case "filter_contains_name":
                    return commandRan(com, dataCities);
                case "update_data":
                    return update(com, dataCities);
                case "remove_greater":
                    return removeGreaterCommand(com, dataCities);
                case "history":
                    return commandRan(com, dataCities);
                case "exit":
                    return exit(com);


            }

            return "НЕИЗВЕСТНЫЙ ТИП КОМАНДЫ";
    }



     String commandRan(Communication communication, DataCities dataCities){
        InfoCommand infoCommand = new InfoCommand();
        HelpCommand helpCommand = new HelpCommand();
        ShowCommand showCommand = new ShowCommand();
        FilterContainsNameCommand filterContainsNameCommand = new FilterContainsNameCommand();
        GroupCountingByGovernmentCommand groupCountingByGovernmentCommand = new GroupCountingByGovernmentCommand();
        MaxByMetersAboveSeaLevelCommand maxByMetersAboveSeaLevelCommand = new MaxByMetersAboveSeaLevelCommand();

        HistoryCommand historyCommand = new HistoryCommand();

        
        return commandExecutor.execute(communication.getType()+" "+ communication.getParams(), dataCities);
    }

    String removeGreaterCommand(Communication communication, DataCities dataCities) throws ClassNotFoundException, IOException {
        LinkedHashSet<City> removeElement = dataCities.removeGreater(communication.deserializeCity(communication.getCityArgument()));
        for (City city1: removeElement ){
           sql.remove(city1.getId(), communication.getUsername());
        }
        dataCities.clearCollection();
        sql.fromSQLToCity(dataCities);

        return "Элементы принадлежащие вам и превышающие заданный, были удалены.";
    }

    String removeEl(Communication communication, DataCities dataCities) throws ClassNotFoundException {
        RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand();
        sql.remove(Integer.parseInt(communication.getParams()),communication.getUsername());
        return commandExecutor.execute(communication.getType()+" "+ communication.getParams(), dataCities);
    }

    String clear(Communication communication, DataCities dataCities) throws ClassNotFoundException {
        LinkedList<Integer>  ids = sql.userElementsCollection(communication.getUsername());
        System.out.println(ids.pollFirst());
        sql.userElementsClear(communication.getUsername());
        dataCities.clearCollection();
        sql.fromSQLToCity(dataCities);

        return "Элементы, принадлежащие Вам, были успешно удаленны. ";
    }


     String add(Communication communication, DataCities dataCities) {
       try {

           City city = communication.deserializeCity(communication.getCityArgument());
           dataCities.addId(city);
           dataCities.addLocalDate(city);
           sql.addSQL(city,communication.getUsername());
           dataCities.clearCollection();
           sql.fromSQLToCity(dataCities);


       } catch (IOException | ClassNotFoundException | SQLException e ){
           e.getMessage();
       }

        return "Новый элемент был добавлен в колекцию ";
    }



     String addIfMin(Communication communication, DataCities dataCities){
        try {
            City city = communication.deserializeCity(communication.getCityArgument());
            if(dataCities.ifMinValues(city) == true){
                dataCities.addId(city);
                dataCities.addLocalDate(city);
                sql.addSQL(city,communication.getUsername());
                dataCities.clearCollection();
                sql.fromSQLToCity(dataCities);
                return("Ваш объект добавлен в коллекцию." );
            } else {
                return("Значение вашего элемента не меньше чем минимальное у значений из коллекции");
            }

        } catch (IOException | ClassNotFoundException | SQLException e ){
            e.getMessage();
        }
        return "Что то пошло не так";
    }


     String update(Communication communication, DataCities dataCities) {
        try {

            City updateCity = communication.deserializeCity(communication.getCityArgument());

            dataCities.addLocalDate(updateCity);
            updateCity.setId(Integer.parseInt(communication.getParams()));

            sql.update(updateCity, Integer.parseInt(communication.getParams()));
            Thread.sleep(100);

            dataCities.clearCollection();
            sql.fromSQLToCity(dataCities);
            return ("Элемент с id  " + Integer.parseInt(communication.getParams()) + " был изменен на: "+ updateCity);
        } catch (IOException | ClassNotFoundException | SQLException | InterruptedException e ){
            e.getMessage();
        }
        return "Что то пошло не так";
    }


    static String executeScript(Communication communication, DataCities dataCities){
        return "Ожидание команд из файла.";
    }

    String exit(Communication communication) throws ClassNotFoundException {
        sql.offline(communication.getUsername());
        System.out.println("Пользователь "+communication.getUsername()+" завершил работу");
        return "Пользователь "+communication.getUsername()+" завершил работу";
    }

    @Override
    public void run() {
        try {
            TransactionProcessing(ReadingRequests.getStr());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

