import java.util.HashMap;

public class Response {
    int code;
    HashMap<String,String> headers;
    String body;

    public Response(int code,String body, HashMap<String, String> headers) {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
