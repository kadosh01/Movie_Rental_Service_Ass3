package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Commands.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MovieRentalProtocol extends UserServiceTextBasedProtocol{

    public DatabaseReadWrite _database;

    public MovieRentalProtocol(DatabaseReadWrite database) {
        super(database);
        _database=database;
    }
    @Override
    public void start(int connectionId, Connections<String> connections){
        super.start(connectionId,connections);
        ((ConnectionsImpl)_connections).set_database(_database);
    }

    public void process(String message) {
        String[] split= message.split(" ");
        switch (split[0]){
            case "REGISTER":{
                Register(message);
                break;
            }
            case "LOGIN":{
                super.process(message);
                break;
            }
            case "SIGNOUT":{
                super.process(message);
                break;
            }
            case "REQUEST":{
                Request req;
                if(!_clientLoggedIn){
                    ERRORCommand err= new ERRORCommand("client isn't logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if(split.length<2){
                    ERRORCommand err= new ERRORCommand("request doesn't exist");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                switch (split[1]){
                    case "balance":{
                        String request= split[1]+split[2];
                        if(request.equals("balance info")){
                            req= new BalanceInfo(_connections,_database,_connectionId);
                            req.execute();
                        }
                        else if(request.equals("balance add")){
                            if(split.length<4){//amount missing
                                ERRORCommand err= new ERRORCommand("REQUEST failed, amount required");
                                _connections.send(_connectionId, err.getError());
                                return;
                            }
                            int amount= Integer.parseInt(split[3]);
                            req= new BalanceAdd(_connections, _database, _connectionId, amount);
                            req.execute();
                        }
                        break;
                    }
                    case "info":{
                        if(split.length>2){//asking info about a specific movie
                            String msg="";
                            for(int i=2; i<split.length; i++)
                                msg+=split[i]+" ";
                            String[] movie= msg.split('"'+"");
                            if(movie.length>1){
                                ERRORCommand err= new ERRORCommand("REQUEST failed, movie doesn't exist");
                                _connections.send(_connectionId, err.getError());
                                return;
                            }
                            req= new MoviesInfo(_connections, _database, _connectionId, movie[0]);
                            req.execute();
                        }
                        else{//asking info about all the movies in the database
                            req= new MoviesInfo(_connections, _database, _connectionId);
                            req.execute();
                        }
                        break;
                    }
                    case "rent":{
                        if(split.length<3){//movie name missing
                            ERRORCommand err= new ERRORCommand("REQUEST failed, movie name required");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.length; i++)
                            msg+=split[i]+" ";
                        String[] movie= msg.split('"'+"");
                        if(movie.length>1){
                            ERRORCommand err= new ERRORCommand("REQUEST failed, movie doesn't exist");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        req= new RentMovie(_connections, _database, _connectionId, movie[0]);
                        req.execute();
                        break;
                    }
                    case "return":{
                        if(split.length<3){//movie name missing
                            ERRORCommand err= new ERRORCommand("REQUEST failed, movie name required");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.length; i++)
                            msg+=split[i]+" ";
                        String[] movie= msg.split('"'+"");
                        if(movie.length>1){
                            ERRORCommand err= new ERRORCommand("REQUEST failed, movie doesn't exist");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        req= new ReturnMovie(_connections, _database, _connectionId, movie[0]);
                        req.execute();
                        break;
                    }
                    case "addmovie":{
                        String msg="";
                        if(split.length<5){
                            ERRORCommand err= new ERRORCommand("REQUEST failed, information missing");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        for(int i=2;i<split.length; i++)
                            msg+=split[i]+" ";
                        String[] apSplit= msg.split('"'+"");
                        String movie= apSplit[0];
                        String[] numbers= apSplit[1].split(" ");
                        if(numbers.length<2){
                            ERRORCommand err= new ERRORCommand("REQUEST failed, information missing");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        int availableAmount= Integer.parseInt(numbers[0]);
                        int price= Integer.parseInt(numbers[1]);
                        List<String> bannedCountries= new LinkedList<>();
                        for(int i=2; i<apSplit.length; i++){
                            bannedCountries.add(apSplit[i]);
                        }
                        Movie mov= new Movie(movie, price, "", bannedCountries, availableAmount, availableAmount);
                        req= new AddMovie(_connections, _database, _connectionId, mov);
                        req.execute();

                    }
                    case "remmovie":{
                        if(split.length<3){
                            ERRORCommand err= new ERRORCommand("REQUEST failed, information missing");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.length; i++)
                            msg+=split[i]+" ";
                        String[] apSplit= msg.split('"'+"");
                        String movie= apSplit[0];
                        req= new RemMovie(_connections, _database, _connectionId, movie);
                        req.execute();
                    }
                    case "changeprice":{
                        if(split.length<4){
                            ERRORCommand err= new ERRORCommand("REQUEST failed, information missing");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.length; i++)
                            msg+=split[i]+" ";
                        String[] apSplit= msg.split('"'+"");
                        String movie= apSplit[0];
                        String price= apSplit[1];
                        req= new ChangePrice(_connections, _database, _connectionId, movie, price);
                        req.execute();
                    }
                }

            }
            default:
        }
    }

    @Override
    protected void Register(String msg) {
        String[] split= msg.split(" ");
        if (_clientLoggedIn) {
            ERRORCommand err = new ERRORCommand("REGISTER failed, client already logged in");
            _connections.send(_connectionId, err.getError());
            return;
        }
        if (split.length < 3) {
            ERRORCommand err = new ERRORCommand("REGISTER failed, username or password missing");
            _connections.send(_connectionId, err.getError());
            return;
        }
        String username = split[1];
        String password = split[2];

        if(_database.getUsers().containsKey(username)){
            ERRORCommand err = new ERRORCommand("REGISTER failed, username already exists");
            _connections.send(_connectionId, err.getError());
            return;
        }

        if(split.length<4){
            ERRORCommand err = new ERRORCommand("REGISTER failed, country required");
            _connections.send(_connectionId, err.getError());
            return;
        }
        String[] split2= split[3].split("=");
        String country= split2[1].substring(1, split2[1].length()-1);
        Users user= new User(username, "normal", password, country, new LinkedList<>(), 0);
        _database.addUser(user);
        ACKCommand ack= new ACKCommand("REGISTER succeeded");
        _connections.send(_connectionId, ack.getACK());

    }
}
