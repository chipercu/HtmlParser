package chipercu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApp {
    private static Statement statement;
    private static User user;
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println("HtmlParser is started");
        System.out.println("Максимальная доступная оперативная паметь: " + (Runtime.getRuntime().maxMemory() / 1000000) + " мегабайт");
        Config.loadConfigFile("HtmlParser");
        try {
            statement = DBConnection.getInstance().getConnection().createStatement();

        } catch (SQLException e) {
            System.out.println("Не удалось подключится к базе даных");
        }
        System.out.println("Для доступа к програме Вам неабходима авторизоватся! Если вы еще не зарегестрированы Введите \"R\" .");
        System.out.println("Введите свой логин:");
        start();
        HtmlParser.startParser(user);
    }
    public static void start() throws IOException {
        user = new User();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String login = br.readLine();
        String password = null;

        if (login.equalsIgnoreCase("R")){
            System.out.println("Для регистрации нового пользовотеля введите логин: ");
            user.setLogin(br.readLine());
            System.out.println("Введите пароль: ");
            user.setPassword(br.readLine());
            System.out.println("Введите свое имя: ");
            user.setName(br.readLine());
            DBConnection.addNewUser(user);
            System.out.println("Для доступа к програме Вам неабходима авторизоватся! Если вы еще не зарегестрированы Введите \"R\" .");
            System.out.println("Введите свой логин:");
            start();
        }else //if(!login.isEmpty() || !login.equalsIgnoreCase(" "))
        {
            try {
                String user_login = null;
                String user_password = null;
                String user_name = null;
                ResultSet rs = statement.executeQuery("SELECT * FROM "+ Config.ACCAUNT_TABLE + " WHERE login='" + login + "'");
                while (rs.next()){
                    user_login  = rs.getString("login");
                    user_password = rs.getString("password");
                    user_name = rs.getString("name");
                    user.setName(user_name);
                    user.setLogin(user_login);
                    user.setPassword(user_password);
                }
                if (!login.equalsIgnoreCase(user_login)){
                    System.out.println("Пользователя с даным логин не существует! Попробуйте еще раз или введите \"R\" для регистрации нового пользователя");
                    start();
                }else {
                    System.out.println("Введите пароль: ");
                    password = br.readLine();
                }
                if (password == null)
                    password = " ";
                if (!password.equalsIgnoreCase(user_password)){
                    System.out.println("Пороль не подходит к даному логину! Попробуйте еще раз или введите \"R\" для регистрации нового пользователя");
                    start();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }




}
