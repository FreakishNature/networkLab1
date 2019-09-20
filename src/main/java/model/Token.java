package model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

    @JsonProperty(value="access_token")
    String accessToken;
    String link;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Token(String access_token, String link) {
        this.accessToken = access_token;
        this.link = link;
    }

    public Token(){

    }
}
