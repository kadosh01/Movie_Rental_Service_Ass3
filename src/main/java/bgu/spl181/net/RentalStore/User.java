package bgu.spl181.net.RentalStore;

import java.util.List;

public class User {

    private String _username;
    private String _password;
    private String _type;
    private String _country;
    private List<Movie> _rentedMovies;
    private int _balance;

    public User(String username, String type, String password, String country, List<Movie> rentedMovies, int balance){
        _username= username;
        _type=type;
        _password= password;
        _country= country;
        _rentedMovies= rentedMovies;
        _balance= balance;

    }

}
