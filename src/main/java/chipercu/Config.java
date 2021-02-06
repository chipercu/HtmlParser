package chipercu;

import java.io.*;

public class Config {

    /*************************** FILE CONFIG ***************************/
    public static String DEFAULT_URL_ADDRESS;
    public static String symbol = "[\",.\\-—–?!}{\\[\\]()\n\r\t\f\b;:%0-9";
    public static String CONFIG_FILE_PATH;
    /*************************** DATA BASE CONFIG **********************/
    protected static final String HOST = "localhost";
    protected static final String PORT = "3306";
    protected static final String DB_NAME = "HtmlParser";
    protected static final String ACCAUNT_TABLE = "USERS";
    protected static final String LOGGER_TABLE = "LOG";
    protected static final String DB_LOGIN = "root";
    protected static final String DB_PASSWORD = "68464846l2";
    protected static final String DRIVER = "com.mysql.jdbc.Driver";


    public static void loadConfigFile(String conf) throws IOException {

        File file = new File("C://", conf + ".config");
        if (file.exists()) {
            CONFIG_FILE_PATH = file.getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line = br.readLine();
                while (line != null ) {
                    if (!line.startsWith("#")){
                        if (line.startsWith("DEFAULT_URL_ADDRESS")) {
                            line = line.replace("DEFAULT_URL_ADDRESS=", "");
                            if (!line.equalsIgnoreCase("")){
                                DEFAULT_URL_ADDRESS = line;
                            }
                        }else if (line.startsWith("SYMBOL=")){
                            line = line.replace("SYMBOL=", "");
                            symbol = symbol.concat(line) + "]";
                        }
                    }
                    line = br.readLine();
                }
            } catch (IOException ex){
                System.out.println(ex.getMessage());
            }
        } else {
            file.createNewFile();
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
            {
                bw.write("#**************************************************************************\n");
                bw.write("#Страничка котороя будет обробатыватся по умолчанию. \nDEFAULT_URL_ADDRESS=simbirsoft.com\n");
                bw.write("#**************************************************************************\n");
                bw.write("#Символы которые будут удалины из обробатываемой странички. \nSYMBOL=»«©\n");
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
            System.out.println("Файл конфигурации был успешно создан в директорию: " + file.getAbsolutePath());
            loadConfigFile(conf);
        }
    }


}
