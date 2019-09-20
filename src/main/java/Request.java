import java.util.HashMap;


public class Request {
    String method;
    String body;
    HashMap<String,String> headers = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key,String header) {
        this.headers.put(key,header);
    }

    public Request(String method, String body) {
        this.method = method;
        this.body = body;
    }
}
