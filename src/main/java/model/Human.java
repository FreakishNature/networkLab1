package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Human {
    public int id;
    public String first_name;
    public String last_name;
    public String bitcoin_address;
    public String username;
    public String email;
    public String gender;
    public String ip_address;
    public String card_number;
    public String card_balance;
    public String card_currency;
    public String organization;
    public String employee_id;
    public String created_account_data;

    @Override
    public String toString() {
        return "Human{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", bitcoin_address='" + bitcoin_address + '\'' +
                ", user_name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", ip_address='" + ip_address + '\'' +
                ", card_number='" + card_number + '\'' +
                ", card_balance='" + card_balance + '\'' +
                ", card_currency='" + card_currency + '\'' +
                ", organization='" + organization + '\'' +
                ", employee_id='" + employee_id + '\'' +
                ", created_account_data='" + created_account_data + '\'' +
                '}';
    }
}
