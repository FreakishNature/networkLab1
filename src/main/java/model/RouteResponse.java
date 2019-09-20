package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteResponse {
    String data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String,String> link;

    @JsonProperty(value="mime_type")
    String mimeType;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, String> getLink() {
        return link;
    }

    public void setLink(Map<String, String> link) {
        this.link = link;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "RouteResponse{" +
                "data='" + data + '\'' +
                ", link=" + link +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
