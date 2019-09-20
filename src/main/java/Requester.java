import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Requester {
    static Response exchange(String path,Request request) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(request.method);

        for(HashMap.Entry<String,String> entry : request.getHeaders().entrySet()){
            connection.setRequestProperty(entry.getKey(),entry.getValue());
        }
        StringBuilder responseBody = new StringBuilder();
        int statusCode = connection.getResponseCode();
        InputStreamReader is = new InputStreamReader( statusCode >= 400 ?
                connection.getErrorStream() :
                connection.getInputStream()
        );

        int ch;
        while((ch = is.read()) != -1){
            responseBody.append((char)ch);
        }
        Response response = new Response(
                statusCode,
                responseBody.toString(),
                null
        );

        return response;
    }
}
