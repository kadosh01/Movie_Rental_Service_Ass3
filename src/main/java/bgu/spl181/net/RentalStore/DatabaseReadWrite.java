package bgu.spl181.net.RentalStore;

import bgu.spl181.net.srv.Database;
import bgu.spl181.net.srv.Users;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseReadWrite implements Database{

    private final String MOVIE_PATH="/home/kadoshy/Downloads/spl-net/Movie_Rental_Service_Ass3/Database/example_Movies.json";
    private final String USERS_PATH="C:\\Users\\Joseph\\.IntelliJIdea2017.1\\Projects\\Movie_Rental_Service_Ass3\\Database\\example_Users.json";
    private final String U="C:\\Users\\Joseph\\.IntelliJIdea2017.1\\Projects\\Movie_Rental_Service_Ass3\\Database\\Users.json";
    private Gson gson;
    private ConcurrentHashMap<String,Movie> _movies; //<id,Movie>
    private ConcurrentHashMap<String,Users> _users; //<username,User>
    private ConcurrentHashMap<Integer, Users> _loggedUsers;
    private ReadWriteLock _userLock;
    private ReadWriteLock _movieLock;
    private ConcurrentLinkedQueue<Runnable> _ReadWriteJsonQueue;

    public DatabaseReadWrite(){
        gson=new Gson();
        _movies=new ConcurrentHashMap<>();
        _users=new ConcurrentHashMap<>();
        _loggedUsers= new ConcurrentHashMap<>();
        _userLock=new ReentrantReadWriteLock();
        _movieLock= new ReentrantReadWriteLock();
        _ReadWriteJsonQueue=new ConcurrentLinkedQueue<>();
    }

    public void DeserializeMovies(){
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

    public void DeserializeUsers(){
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

    public void SerializedUser(){
        try(Writer writer=new FileWriter(U)){
            JsonObject file=new JsonObject();
            JsonArray users=new JsonArray();
            //users.add("usuers");
            for(HashMap.Entry<String,Users> user : _users.entrySet()) {
                JsonObject obj = new JsonObject();
                obj.add("username", new JsonPrimitive(user.getValue().getUsername()));
                obj.add("type", new JsonPrimitive(((User)user.getValue()).get_type()));
                obj.add("password" , new JsonPrimitive(((User)user.getValue()).getPassword()));
                obj.add("country" , new JsonPrimitive(((User)user.getValue()).get_country()));
                JsonArray movies=new JsonArray();
                for(Movie movie : ((User)(user).getValue()).get_rentedMovies()){
                    JsonObject mov=new JsonObject();
                    mov.add("id",new JsonPrimitive(movie.get_id()));
                    mov.add("name",new JsonPrimitive(movie.get_name()));
                    movies.add(mov);
                }
                obj.add("movies",movies);
                obj.add("balance" , new JsonPrimitive(((User)user.getValue()).get_balance()));
                users.add(obj);
            }
            file.add("users",users);
            gson.toJson(file,file.getClass(),writer);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SerializedMovies(){
        try(Writer writer=new FileWriter(MOVIE_PATH)){
            JsonObject file=new JsonObject();
            JsonArray movies=new JsonArray();
            for(HashMap.Entry<String,Movie> movie : _movies.entrySet()) {
                JsonObject obj = new JsonObject();
                obj.add("id", new JsonPrimitive(movie.getValue().get_id()));
                obj.add("name", new JsonPrimitive((movie.getValue()).get_name()));
                obj.add("price" , new JsonPrimitive((movie.getValue()).get_price()));
                JsonArray banned=new JsonArray();
                for(String country : (movie.getValue()).get_bannedCountries()){
                    JsonPrimitive con=new JsonPrimitive(country);
                    banned.add(con);
                }
                obj.add("bannedCountries",banned);
                obj.add("availableAmount" , new JsonPrimitive((movie.getValue()).get_availableAmount()));
                obj.add("totalAmount" , new JsonPrimitive((movie.getValue()).get_totalAmount()));
                movies.add(obj);
            }
            file.add("movies",movies);
            gson.toJson(file,file.getClass(),writer);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String,Movie> getMovies(){//add lock??
        return _movies;
    }

    public boolean updateUserFile(){return true;}

    @Override
    public ConcurrentHashMap<String, Users> getUsers() {
        return _users;
    }

    @Override
    public void addUser(Users user) {//lock function?
        //_readWriteLock.writeLock().lock();
        _users.putIfAbsent(user.getUsername(), user);
        //_readWriteLock.writeLock().unlock();

        _ReadWriteJsonQueue.add(()->{
            _userLock.readLock().lock();
            SerializedUser();
            _userLock.readLock().unlock();
        });
    }

    @Override
    public ConcurrentHashMap<Integer, Users> getLoggedUsers() {
        return _loggedUsers;
    }

    @Override
    public void addLoggedUser(Integer id, String username) {
        //_readWriteLock.writeLock().lock();
        _users.get(username).setLoggedIn(true);
        _loggedUsers.putIfAbsent(id, _users.get(username));
    }

    @Override
    public void removeLoggedUser(Integer id) {
        _users.get(_loggedUsers.get(id)).setLoggedIn(false);
        _loggedUsers.remove(id);
    }

    public User getUserByConnectionId(Integer connectionId){

        return (User)_loggedUsers.get(connectionId);
    }

    public boolean tryRentMovie(String movieName){
        _movieLock.readLock().lock();
        Movie mov= _movies.get(movieName);
        boolean canRent= mov.get_availableAmount()>0;
        if(canRent){
            mov.set_availableAmount(mov.get_availableAmount()-1);
        }
        _movieLock.readLock().unlock();
        return canRent;
    }

    public Movie getMovie(String movieName){
        _movieLock.readLock().lock();
        Movie ret= _movies.get(movieName).clone();
        _movieLock.readLock().unlock();
        return ret;
    }

    public String getMoviesNames(){
        String out="";
        _movieLock.readLock().lock();
        for(String movie: _movies.keySet()){
            out=out+movie+" ";
        }
        _movieLock.readLock().unlock();
        return out;
    }

    /*
    @Override
    public HashMap<String, String> getUsersData() {
        HashMap<String,String> ans=new HashMap<>();
        for(Map.Entry<String,User> entry : _users.entrySet()){
            ans.put(entry.getKey(),entry.getValue().get_password());
        }
        return ans;

    }


    @Override
    public boolean isLogged(String userName) {
        return _loggedUsers.get(userName).get();
    }

    @Override
    public void setLogIn(String userName) {
        while (!_loggedUsers.get(userName).compareAndSet(false,true));
    }

    @Override
    public void setLogOut(String userName) {
        while (!_loggedUsers.get(userName).compareAndSet(true,false));
    }*/
}
