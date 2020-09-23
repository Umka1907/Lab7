package Communication;

import Commands.CommandExecutor;
import citis.City;
import citis.DataNewCity;
import citis.Enter;
import citis.FileCity;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.LinkedList;

public class ClientArgument {
    Enter enterCommand = new Enter();
    CommandExecutor commandExecutor = new CommandExecutor();

    FileCity fileCity = new FileCity();

    private String nameFile = new String();
    public LinkedList<String> com = new LinkedList();

    private static String username;

    private InetAddress address;

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ClientArgument.username = username;
    }

    private static String type;

    private String cityArgument;

    public String getCityArgument() {
        return cityArgument;
    }

    public void setCityArgument(String cityArgument) {
        this.cityArgument = cityArgument;
    }

    private String params = new String() ;

    public  String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


    public void param() throws IOException, JAXBException {


        String enter = enterCommand.enterCommand();
        bagging(enter);
        getCityArg();

    }

    public void getCityArg() throws IOException, JAXBException {
        com.add("add");
        com.add("help");
        com.add("show");
        com.add("history");
        com.add("update");
        com.add("filter_contains_name");
        com.add("info");
        com.add("remove_by_id");
        com.add("clear");
        com.add("execute_script");
        com.add("exit");
        com.add("add_if_min");
        com.add("remove_greater");
        com.add("max_by_meters_above_sea_level");
        com.add("group_counting_by_government");
        com.add("update_data");
        if (com.contains(type.trim())!=true){
            System.out.println("НЕИЗВЕСТНЫЙ ТИП КОМАНДЫ. Повторите запрос");
            param();
        }


        if ( (type.equals("add"))  ||  (type.equals("add_if_min")) || (type.trim().equals("update_data")) | (type.trim().equals("remove_greater")) ) {
            City cityArg = DataNewCity.newCity();
            cityArgument = serializeArg(cityArg);
        }}




    private String serializeArg(City city) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(city);
        so.flush();
        return Base64.encode(bo.toByteArray());
    }

    public void bagging(String bag) throws IOException, JAXBException {

        String[] actionParts = bag.split(" ");
        if (actionParts.length == 1){
            type = actionParts[0];
        }
        else if (actionParts.length == 2){
            type = actionParts[0];
            params = actionParts[1];
        }
        else {
            System.out.println("Необрабатываемый запрос. Ввидите коректный запрос (Запрос может содержать: команду или команду + аргумент) ");
            param();
        }
    }

}
