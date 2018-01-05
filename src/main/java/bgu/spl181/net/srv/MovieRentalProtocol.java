package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.srv.Commands.ACKCommand;
import bgu.spl181.net.srv.Commands.ERRORCommand;
import bgu.spl181.net.srv.Commands.Request;

import java.util.HashMap;
import java.util.LinkedList;

public class MovieRentalProtocol extends UserServiceTextBasedProtocol{

    private HashMap<String, Boolean> _loggedInUsers= new HashMap<>();
    private DatabaseReadWrite _database;

    public MovieRentalProtocol(DatabaseReadWrite database) {
        super(database);
        _database=database;
    }

    public void process(String message) {
        String[] split= message.split(" ");
        switch (split[0]){
            case "REGISTER":{
                super.process(message);
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
                        }
                        else if(request.equals("balance add")){
                            if(split.length<4){//amount missing
                                ERRORCommand err= new ERRORCommand("REQUEST failed, amount required");
                                _connections.send(_connectionId, err.getError());
                                return;
                            }
                            int amount= Integer.parseInt(split[3]);
                            //create request
                        }
                    }
                    case "info":{
                        if(split.length>2){//asking info about a specific movie
                            String movie= split[2];
                            //create movie info request
                        }
                        else{//asking info about all the movies in the database
                            //create all movies info request
                        }
                    }
                    case "rent":{
                        if(split.length<3){//movie name missing
                            ERRORCommand err= new ERRORCommand("REQUEST failed, movie name required");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String movie= split[2];
                        //create rent movie request
                    }
                    case "return":{
                        if(split.length<3){//movie name missing
                            ERRORCommand err= new ERRORCommand("REQUEST failed, movie name required");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String movie= split[2];
                        //create return movie request
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
        ACKCommand ack= new ACKCommand("RGISTER succeeded");
        _connections.send(_connectionId, ack.getACK());

    }
}
