package bgu.spl181.net.RentalStore;

import com.fasterxml.jackson.databind.util.Converter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseReadWrite {

    private final String MOVIE_PATH="/home/kadoshy/Downloads/spl-net/Movie_Rental_Service_Ass3/Database/example_Movies.json";
    private final String USERS_PATH="/home/kadoshy/Downloads/spl-net/Movie_Rental_Service_Ass3/Database/example_Users.json";
    private Gson gson;
    private HashMap<String,Movie> _movies;
    private HashMap<String,User> _users;

    public DatabaseReadWrite(){
        gson=new Gson();
        _movies=new HashMap<>();
        _users=new HashMap<>();
    }

    public void DesrializeMovies(){
        List<JsonObject> list=new ArrayList<>();
        try(Reader reader=new FileReader(MOVIE_PATH)){
            JsonObject obj=gson.fromJson(reader,JsonObject.class);
            list=gson.fromJson(obj.getAsJsonArray("movies"),new TypeToken<ArrayList<JsonObject>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(JsonObject obj : list)
        {
            String id=obj.get("id").getAsString();
            String name=obj.get("name").getAsString();
            String price=obj.get("price").getAsString();
            List<String> bannedCountries= gson.fromJson(obj.get("bannedCountries").getAsJsonArray(),new TypeToken<List<String>>(){}.getType()) ;
            String availableAmount=obj.get("availableAmount").getAsString();
            String totalAmount=obj.get("totalAmount").getAsString();
            _movies.put(id,new Movie(name,Integer.parseInt(price),id,bannedCountries,Integer.parseInt(availableAmount),Integer.parseInt(totalAmount)));
        }
    }

    public void DesrializeUsers(){
        List<JsonObject> list=new ArrayList<>();
        try(Reader reader=new FileReader(USERS_PATH)){
            JsonObject obj=gson.fromJson(reader,JsonObject.class);
            list=gson.fromJson(obj.getAsJsonArray("users"),new TypeToken<ArrayList<JsonObject>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(JsonObject obj : list)
        {
            String username=obj.get("username").getAsString();
            String type=obj.get("type").getAsString();
            String password=obj.get("password").getAsString();
            List<JsonObject> movies= gson.fromJson(obj.getAsJsonArray("movies"),new TypeToken<ArrayList<JsonObject>>(){}.getType());
            List<Movie> rentalMovies=new ArrayList<>();
            for(JsonObject s:movies){
                String id=s.get("id").getAsString();
                String name=s.get("name").getAsString();
                rentalMovies.add(new Movie(name,id));
            }
            String country=obj.get("country").getAsString();
            String balance=obj.get("balance").getAsString();
            _users.put(username,new User(username,type,password,country,rentalMovies,Integer.parseInt(balance)));
        }
    }

    public boolean UpdateMoviesFile(){return true;}
}
