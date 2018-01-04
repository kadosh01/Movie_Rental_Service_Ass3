package bgu.spl181.net.RentalStore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Store implements Serializable {

    private HashMap<String, Movie> _movies;
    private HashMap<String, User> _clients;

    public Store(HashMap<String, Movie> movies, HashMap<String, User> clients){
        _movies= movies;
        _clients= clients;
    }
}
