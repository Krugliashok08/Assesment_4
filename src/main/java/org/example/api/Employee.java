package org.example.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    private Integer id;
    private String name;
    private String surname;
    private String position;
    private String city;

    public Employee(String name, String surname, String position, String city) {
        this.name = name;
        this.surname = surname;
        this.position = position;
        this.city = city;
    }

    @JsonProperty("id")
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty("surname")
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    @JsonProperty("position")
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    @JsonProperty("city")
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
