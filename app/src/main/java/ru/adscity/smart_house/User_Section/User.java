package ru.adscity.smart_house.User_Section;


public class User {

    public String username, key, email, firstname, surname, city, img, language;

    public User(String username, String key, String email, String firstname, String surname,  String city,
                String img, String language) {
        this.firstname = firstname;
        this.surname = surname;
        this.username = username;
        this.city = city;
        this.key = key;
        this.img = img;
        this.email = email;
        this.language = language;
    }

}
