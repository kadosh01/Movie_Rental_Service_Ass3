package bgu.spl181.net.RentalStore;

import bgu.spl181.net.srv.Users;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable, Users {

    private String _username;
    private String _password;
    private String _type;
    private String _country;
    private List<Movie> _rentedMovies;
    private AtomicInteger _balance;
    private AtomicBoolean _loggedIn;

    public User(String username, String type, String password, String country, List<Movie> rentedMovies, int balance){
        _username= username;
        _type=type;
        _password= password;
        _country= country;
        _rentedMovies= rentedMovies;
        _balance= new AtomicInteger(balance);
        _loggedIn= new AtomicBoolean(false);
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
        return _balance.get();
    }

    public void set_balance(int balance) {
        while(!_balance.compareAndSet(_balance.get(), balance));
    }

    @Override
    public boolean isLoggedIn() {
        return _loggedIn.get();
    }

    @Override
    public void setLoggedIn(boolean bool) {
        while(!_loggedIn.compareAndSet(_loggedIn.get(), bool));
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
