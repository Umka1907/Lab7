package data;

import citis.*;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SQLRequest {
    public Connection conn;

    private String insertSQL;
    String stringSQL;

    public String getStringSQL() {
        return stringSQL;
    }


    public void contact() throws ClassNotFoundException {

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://pg/studs", "s285682", "tnf822");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String addSQL(City city, String us) throws ClassNotFoundException, SQLException {
        try {
            contact();
            insertSQL = "INSERT INTO CITIES" +
                    "(ID,"+
                    "USERNAME,"+
                    " NAME," +
                    " COORDINATE_X," +
                    " COORDINATE_Y," +
                    " CREATION_DATE," +
                    " AREA," +
                    " POPULATION," +
                    " METERS_ABOVE_SEA_LEVEL," +
                    " TELEPHONE_CODE," +
                    " AGGLOMERATION," +
                    " GOVERMENT," +
                    " GOVERNOR_AGE)"+
                    "Values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement stat = conn.prepareStatement(insertSQL);
            LocalDateAdapter localDateAdapter = new LocalDateAdapter();

            stat.setInt(1,city.getId());
            stat.setString(2,us);
            stat.setString(3,city.getName());
            stat.setFloat(4,city.getCoordinates().getX());
            stat.setLong(5,city.getCoordinates().getY());
            stat.setString(6, localDateAdapter.marshal(city.getCreationDate()));
            stat.setFloat(7,city.getArea());
            stat.setLong(8,city.getPopulation());
            stat.setLong(9,city.getMetersAboveSeaLevel());
            stat.setInt(10,city.getTelephoneCode());
            stat.setFloat(11,city.getAgglomeration());
            stat.setString(12,city.getGovernment().getGerontocracy());
            stat.setLong(13,city.getGovernor().getAge());

            int rows = stat.executeUpdate();




            stat.close();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "add";

    }

    public void offline(String us) throws ClassNotFoundException {

        try {
            contact();
            insertSQL = "UPDATE USERS SET OPERATING_MODE = ? WHERE USERNAME = ?";

            PreparedStatement stat = conn.prepareStatement(insertSQL);

            stat.setString(1, "offline");
            stat.setString(2, us);

            int rows = stat.executeUpdate();
            stat.close();
            conn.commit();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fromSQLToCity(DataCities dataCities) throws ClassNotFoundException {

        try {
            contact();
            LocalDateAdapter localDateAdapter = new LocalDateAdapter();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CITIES;");
            while (rs.next()) {
                City city = new City();
                Coordinates coordinates = new Coordinates();
                Human human = new Human();
                city.setId(rs.getInt("ID"));
                city.setName(rs.getString("NAME"));

                coordinates.setX((rs.getFloat("COORDINATE_X")));
                coordinates.setY(rs.getLong("COORDINATE_Y"));
                city.setCoordinates(coordinates);

                city.setCreationDate(localDateAdapter.unmarshal(rs.getString("CREATION_DATE")) );
                city.setArea(rs.getLong("AREA"));
                city.setPopulation(rs.getLong("POPULATION"));
                city.setMetersAboveSeaLevel(rs.getLong("METERS_ABOVE_SEA_LEVEL"));
                city.setTelephoneCode(rs.getInt("TELEPHONE_CODE"));
                city.setAgglomeration(rs.getInt("AGGLOMERATION"));

                city.setGovernment(Governmet.valueOf(rs.getString("GOVERMENT")));

                human.setAge(rs.getInt("GOVERNOR_AGE"));
                city.setGovernor(human);

                dataCities.cities.add(city);


            }

            stmt.close();
            rs.close();
            conn.commit();
        } catch(SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int number_Of_Online(){
        int quantity = 0;
        try {
            contact();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM USERS WHERE OPERATING_MODE='online'");
            rs.next();
            quantity = rs.getInt(1);


            stmt.close();
            rs.close();
            conn.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;

    }

    public boolean  userElements(String us, int id){
        Set<Integer> ids = new HashSet<Integer>();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CITIES;");
            while (rs.next()) {
                if (us.equals(rs.getString("USERNAME"))){
                    ids.add(rs.getInt("ID"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(ids.contains(id)){
            return true;
        }
        return false;
    }

    public void update(City city, int id) throws ClassNotFoundException, SQLException {
        try {
            contact();
            //"UPDATE USERS SET OPERATING_MODE = ? WHERE USERNAME = ?";
            insertSQL = "UPDATE CITIES SET" +
                    " NAME= ?," +
                    " COORDINATE_X= ?," +
                    " COORDINATE_Y= ?," +
                    " CREATION_DATE= ?," +
                    " AREA= ?," +
                    " POPULATION= ?," +
                    " METERS_ABOVE_SEA_LEVEL= ?," +
                    " TELEPHONE_CODE= ?," +
                    " AGGLOMERATION= ?," +
                    " GOVERMENT= ?," +
                    " GOVERNOR_AGE= ? "+
                    "WHERE ID = ?";

            PreparedStatement stat = conn.prepareStatement(insertSQL);
            LocalDateAdapter localDateAdapter = new LocalDateAdapter();

            stat.setString(1,city.getName());
            stat.setFloat(2,city.getCoordinates().getX());
            stat.setLong(3,city.getCoordinates().getY());
            stat.setString(4, localDateAdapter.marshal(city.getCreationDate()));
            stat.setFloat(5,city.getArea());
            stat.setLong(6,city.getPopulation());
            stat.setLong(7,city.getMetersAboveSeaLevel());
            stat.setInt(8,city.getTelephoneCode());
            stat.setFloat(9,city.getAgglomeration());
            stat.setString(10,city.getGovernment().getGerontocracy());
            stat.setLong(11,city.getGovernor().getAge());
            stat.setInt(12, id);

            int rows = stat.executeUpdate();


            stat.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void remove(int id, String us) throws ClassNotFoundException {
        try {
            contact();
            insertSQL = "DELETE FROM CITIES WHERE ID = ? AND USERNAME = ?";

            PreparedStatement stat = conn.prepareStatement(insertSQL);

            stat.setInt(1, id);
            stat.setString(2,us);

            stat.executeUpdate();
            stat.close();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void  userElementsClear(String us){
        try {
            contact();
            insertSQL = "DELETE FROM CITIES WHERE USERNAME = ?";

            PreparedStatement stat = conn.prepareStatement(insertSQL);

            stat.setString(1, us);

            stat.executeUpdate();
            stat.close();
            conn.commit();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public LinkedList<Integer>   userElementsCollection(String us){
        LinkedList<Integer> ids = new LinkedList<Integer>();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CITIES;");
            while (rs.next()) {
                if (us.equals(rs.getString("USERNAME"))){
                    ids.add(rs.getInt("ID"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }


}
