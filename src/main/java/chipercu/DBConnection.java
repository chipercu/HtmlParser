package chipercu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DBConnection {
    private static DBConnection dbIsntance;
    private static Connection connection;


    private DBConnection() throws ClassNotFoundException {
        Class.forName(Config.DRIVER);
    }

    public static DBConnection getInstance() throws ClassNotFoundException {
        if (dbIsntance == null) {
            dbIsntance = new DBConnection();
        }
        return dbIsntance;
    }

    public Connection getConnection() {

        String url = "jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DB_NAME;
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, Config.DB_LOGIN, Config.DB_PASSWORD);
            } catch (SQLException ex) {
                BufferedReader sb = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Не удалось подключится к базе даных, проверьте файл конфигурации или введите \"y\" что бы создать базу");
                try {
                    String str = sb.readLine();
                    if (str.equalsIgnoreCase("y")) {
                        newDataBase();
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return connection;
    }

    private void newDataBase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + Config.HOST + ":" + Config.PORT, Config.DB_LOGIN, Config.DB_PASSWORD);
        connection.createStatement().execute("CREATE DATABASE " + Config.DB_NAME);
        connection = DriverManager.getConnection("jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DB_NAME, Config.DB_LOGIN, Config.DB_PASSWORD);
        String accaunt = "CREATE TABLE " + Config.ACCAUNT_TABLE +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " login VARCHAR(255), " +
                " password VARCHAR(255), " +
                " name VARCHAR(255), " +
                " PRIMARY KEY ( id ))";
        connection.createStatement().execute(accaunt);
        String logger = "CREATE TABLE " + Config.LOGGER_TABLE +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " name VARCHAR(255), " +
                " site VARCHAR(255), " +
                " result VARCHAR(255), " +
                " data VARCHAR(255), " +
                " PRIMARY KEY ( id ))";
        connection.createStatement().execute(logger);
    }

    public static void addNewUser(User user) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DB_NAME, Config.DB_LOGIN, Config.DB_PASSWORD);
            String sql = "INSERT INTO " + Config.ACCAUNT_TABLE + " (login, password, name) " +
                    "VALUES ('" + user.getLogin() +
                    "', '" + user.getPassword() +
                    "', '" + user.getName() + "')";
            connection.createStatement().executeUpdate(sql);
            System.out.println("Пользователь был успешно зарегестрирован!");
        } catch (SQLException e) {
            System.out.println("Не удалос добавить пользователя в базу, проверте есть ли подключение с базой!");
        }
    }

    public static void addLog(User user, String address, String result) {
        String months[] = {"Ian", "Fev", "Mart", "Apr", "May", "Iun", "Jul", "Aug", "Sept",
                "Oct", "Nov", "Dec"};
        GregorianCalendar gcalendar = new GregorianCalendar();

        String data = "Date: " + months[gcalendar.get(Calendar.MONTH)] + " " + gcalendar.get(Calendar.DATE) + " " + "Time: " + gcalendar.get(Calendar.HOUR) + ":" + gcalendar.get(Calendar.MINUTE) + ":" + gcalendar.get(Calendar.SECOND);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DB_NAME, Config.DB_LOGIN, Config.DB_PASSWORD);
            String log = "INSERT INTO " + Config.LOGGER_TABLE + " (name, site, result, data) " +
                    "VALUES ('" + user.getName() +
                    "', '" + address +
                    "', '" + result +
                    "', '" + data + "')";
            connection.createStatement().executeUpdate(log);
            System.out.println("Событье было успешно записоно в базе даных");
        } catch (SQLException e) {

            System.out.println("Не удалос добавить новую запись в базу, проверте есть ли подключение с базой!");
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
