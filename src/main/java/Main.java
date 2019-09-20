import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import convertors.CsvConvertor;
import model.Human;
import model.RouteResponse;
import model.Token;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    static ObjectMapper mapper = new ObjectMapper();
    static XmlMapper xmlMapper = new XmlMapper();
    static YAMLMapper yamlMapper = new YAMLMapper();
    static volatile int amountOfWorkingThreads = 0;
    static Connection connection;
    static Statement statement;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\skeez\\Desktop\\hackaton\\server_java\\lab1_network\\src\\main\\resources/networkDB.s3db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static String url = "http://localhost:5000";
    static ArrayList<Human> records = new ArrayList<>();

    static String printPretty(Object obj) throws JsonProcessingException {
        String res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        System.out.println(res);
        return res;
    }

    static String executeSqlQuery(String command) throws SQLException {

        StringBuilder sb = new StringBuilder();

        if (command.equals("create")) {
            for (Human h : records) {
                statement.execute("INSERT INTO HUMANS (" +
                        "id, " +
                        "first_name, " +
                        "last_name, " +
                        "bitcoin_address, " +
                        "username, " +
                        "email, " +
                        "gender, " +
                        "card_number, " +
                        "ip_address, " +
                        "card_balance, " +
                        "employee_id, " +
                        "card_currency, " +
                        "organization, " +
                        "created_account_data" +
                        ")" +
                        "VALUES ('" +
                        h.id + "','" +
                        (h.first_name != null ? h.first_name.replace("'","''") : null) + "','" +
                        (h.last_name != null ? h.last_name.replace("'","''") : null )+ "','" +
                        h.bitcoin_address + "','" +
                        h.username + "','" +
                        h.email + "','" +
                        h.gender + "','" +
                        h.card_number + "','" +
                        h.ip_address + "','" +
                        h.card_balance + "','" +
                        h.employee_id + "','" +
                        h.card_currency + "','" +
                        h.organization + "','" +
                        h.created_account_data + "'" +
                        ");"
                );
            }
        } else {
            ResultSet resultSet = statement.executeQuery(command);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                     sb.append(rsmd.getColumnName(i) + " : " + columnValue);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException, SQLException, ClassNotFoundException {

        Response responseToken = Requester.exchange(url  + "/register",new Request("GET",null));

        Token token = mapper.readValue(responseToken.getBody(), Token.class);
        Request request = new Request("GET",null);
        request.addHeader("X-Access-Token",token.getAccessToken());
        System.out.println(token.getAccessToken());

        Class.forName("org.sqlite.JDBC");
        statement.execute("DROP TABLE if EXISTS  'HUMANS'");
        statement.execute("CREATE TABLE if NOT EXISTS 'HUMANS' (" +
                "'id' INTEGER, " +
                "'first_name' varchar (20), " +
                "'last_name' varchar (20), " +
                "'bitcoin_address' varchar (20), " +
                "'username' varchar (20), " +
                "'email' varchar (20), " +
                "'gender' varchar (20), " +
                "'card_number' varchar (20), " +
                "'ip_address' varchar (20), " +
                "'card_balance' varchar (20), " +
                "'employee_id' varchar (20), " +
                "'card_currency' varchar (20), " +
                "'organization' varchar (20), " +
                "'created_account_data' varchar (20) " +
                ");");


        Response responseHome = Requester.exchange("http://localhost:5000/home",request);

        new ThreadRequest("/home",request).start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Collecting data");
        while(amountOfWorkingThreads > 0){ }
        System.out.println("Finsihed collecting data");
        
        ServerSocket server = new ServerSocket(4000);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String) ois.readObject();
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Received command: " + message);
            String response = executeSqlQuery(message);
            System.out.println("Out to client : \n");
            System.out.println();
            //write object to Socket
            oos.writeObject(response);
            //close resources
            ois.close();
            oos.close();
            socket.close();
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();

    }



    static class ThreadRequest extends Thread{
        String route;
        Request request;
        @Override
        public void run(){
            System.out.println("Starting processing url: " + url + route );
            try {
                Response response = Requester.exchange(url + route,request);
                RouteResponse routeResponse = mapper.readValue(response.getBody(), RouteResponse.class);

                if(routeResponse.getMimeType() != null){
                    switch (routeResponse.getMimeType()) {
                        case "application/xml":
                            records.addAll(xmlMapper.readValue(routeResponse.getData(), new TypeReference<ArrayList<Human>>() {
                            }));
                            break;
                        case "application/json":
                            records.addAll(mapper.readValue(routeResponse.getData(), new TypeReference<ArrayList<Human>>() {
                            }));
                            break;
                        case "text/csv":
                            records.addAll(new ArrayList<Human>(
                                    CsvConvertor.convertCsv(routeResponse.getData(), Human.class)
                                            .stream()
                                            .map(e -> (Human) e)
                                            .collect(Collectors.toList()))
                            );
                            break;
                        case "application/x-yaml":
                            records.addAll(yamlMapper.readValue(routeResponse.getData(), new TypeReference<ArrayList<Human>>() {
                            }));
                            break;
                    }
                }
                Map<String, String> links = routeResponse.getLink();
                if(links != null){
                    for(String link : links.values()){
                        new ThreadRequest(link,request).start();
                    }
                }
                amountOfWorkingThreads--;
            } catch (IOException | InstantiationException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        public ThreadRequest(String route,Request request){
            amountOfWorkingThreads++;
            this.route = route;
            this.request = request;
        }

    }
}
