package data;

import citis.Enter;

import java.sql.*;
import java.util.ArrayList;

public class Users {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public void enterUsername() {
        System.out.println("Введите логин:");
        username = Enter.writeString();

    }

    public void enterPassword(){
        Password pas = new Password();
        System.out.println("Введите пароль:");
        String input = Enter.writeString();
        password = pas.encryptThisString(input);
    }

    public void creatOrExit() throws ClassNotFoundException {

        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();

        Connection conn;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://pg/studs", "s285682", "tnf822");
            conn.setAutoCommit(false);
            String insertSQL;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS;");
            while (rs.next()) {
                usernames.add(rs.getString("USERNAME"));
                passwords.add(rs.getString("PASSWORD"));
            }

            stmt.close();
            rs.close();
            conn.commit();

            Boolean flag = false;

            while (flag==false) {

                System.out.println("Если хотите войти в систему нажмите 1;\nЕсли хотите аторизоваться назмите 2;");
                String command = Enter.writeString();

                if (command.equals("1")) {
                    enterUsername();
                    if (usernames.contains(username)) {
                        enterPassword();
                        if (passwords.contains(password)) {
                            System.out.println("Успешных вход в систему");
                            insertSQL = "UPDATE USERS SET OPERATING_MODE = ? WHERE USERNAME = ?";

                            PreparedStatement stat = conn.prepareStatement(insertSQL);

                            stat.setString(1, "online");
                            stat.setString(2, username);

                            int rows = stat.executeUpdate();
                            stat.close();
                            conn.commit();
                            flag = true;
                        } else {
                            System.out.println("Неправельный пороль.");
                        }

                    } else {
                        System.out.println("Пользователя с таким логином не существует. Проверьте написание своего логина или авторизуйтесь");
                    }


                } else if (command.equals("2")) {
                    enterUsername();
                    if (usernames.contains(username)) {
                        System.out.println("Данный логин уже занят другим пользователем, пожалуйста введите другой логин.");
                    } else {
                        enterPassword();

                        insertSQL = "INSERT INTO USERS" +
                                "( USERNAME," +
                                " PASSWORD," +
                                "OPERATING_MODE)" +
                                "Values (?,?,?)";

                        PreparedStatement stat = conn.prepareStatement(insertSQL);

                        stat.setString(1, getUsername());
                        stat.setString(2, getPassword());
                        stat.setString(3, "online");

                        int rows = stat.executeUpdate();
                        stat.close();
                        conn.commit();
                        System.out.println("Успешная авторизация.");
                        flag = true;
                    }
                } else {
                    System.out.println("Неизвестная команда.");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
