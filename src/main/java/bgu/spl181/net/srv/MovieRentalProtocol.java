package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.srv.Commands.ACKCommand;
import bgu.spl181.net.srv.Commands.ERRORCommand;

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
