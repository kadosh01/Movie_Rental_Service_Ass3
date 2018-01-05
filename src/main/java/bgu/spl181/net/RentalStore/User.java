package bgu.spl181.net.RentalStore;

import bgu.spl181.net.srv.Users;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable, Users {

    private String _username;
    private String _password;
    private String _type;
    private String _country;
    private List<Movie> _rentedMovies;
    private int _balance;
    private boolean _loggedIn;

    public User(String username, String type, String password, String country, List<Movie> rentedMovies, int balance){
        _username= username;
        _type=type;
        _password= password;
        _country= country;
        _rentedMovies= rentedMovies;
        _balance= balance;
        _loggedIn= false;

    }


    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_country() {
        return _country;
    }

    public void set_country(String _country) {
        this._country = _country;
    }

    public List<Movie> get_rentedMovies() {
        return _rentedMovies;
    }

    public void set_rentedMovies(List<Movie> _rentedMovies) {
        this._rentedMovies = _rentedMovies;
    }

    public int get_balance() {
        return _balance;
    }

    public void set_balance(int _balance) {
        this._balance = _balance;
    }

    @Override
    public boolean isLoggedIn() {
        return _loggedIn;
    }

    @Override
    public void setLoggedIn(boolean bool) {
        _loggedIn= bool;
    }

    @Override
    public String getUsername() {
        return _username;
    }

    @Override
    public String getPassword() {
        return _password;
    }
}
