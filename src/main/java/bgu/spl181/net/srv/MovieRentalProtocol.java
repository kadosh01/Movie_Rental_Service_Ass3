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
       // String[] split= message.split(" ");
        List<String> split= splitString(message, ' ');
        String command="";
        if(!split.isEmpty()) {
            command = split.get(0); //out of bounds
        }
        switch (command){
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
                    ERRORCommand err= new ERRORCommand("request "+split.get(1)+" failed");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if(split.size()<2){
                    ERRORCommand err= new ERRORCommand("request "+split.get(1)+" failed");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                String requests= split.get(1);
                switch (requests){
                    case "balance":{
                        //String request= split[1]+" "+split[2];
                        if(split.size()<3){
                            ERRORCommand err= new ERRORCommand("request balance failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        if(split.get(2).equals("info")){
                            req= new BalanceInfo(_connections,_database,_connectionId);
                            req.execute();
                        }
                        else if(split.get(2).equals("add")){
                            if(split.size()<4){//amount missing
                                ERRORCommand err= new ERRORCommand("request addbalance failed");
                                _connections.send(_connectionId, err.getError());
                                return;
                            }
                            int amount= Integer.parseInt(split.get(3));
                            req= new BalanceAdd(_connections, _database, _connectionId, amount);
                            req.execute();
                        }
                        break;
                    }
                    case "info":{
                        if(split.size()>2){//asking info about a specific movie
                            String msg="";
                            for(int i=2; i<split.size(); i++)
                                msg+=split.get(i)+" ";
                            //String movie= msg.replace('"'+"","");//.split('"'+"");
                            //movie=movie.substring(0,movie.length()-1);
                            List<String> apSplit= splitString(msg, '\"');
                            if(apSplit.size()>1){
                                ERRORCommand err= new ERRORCommand("request info failed");
                                _connections.send(_connectionId, err.getError());
                                return;
                            }
                            String movie= apSplit.get(0);
                            req= new MoviesInfo(_connections, _database, _connectionId, movie);
                            req.execute();
                        }
                        else{//asking info about all the movies in the database
                            req= new MoviesInfo(_connections, _database, _connectionId);
                            req.execute();
                        }
                        break;
                    }
                    case "rent":{
                        if(split.size()<3){//movie name missing
                            ERRORCommand err= new ERRORCommand("request rent failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.size(); i++)
                            msg+=split.get(i)+" ";
                        List<String> apSplit= splitString(msg, '\"');
                        if(apSplit.size()>1){
                            ERRORCommand err= new ERRORCommand("request rent failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String movie= apSplit.get(0);
                        req= new RentMovie(_connections, _database, _connectionId, movie);
                        req.execute();
                        break;
                    }
                    case "return":{
                        if(split.size()<3){//movie name missing
                            ERRORCommand err= new ERRORCommand("request return failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.size(); i++)
                            msg+=split.get(i)+" ";
                        //String[] movie= msg.split('"'+"");
                        List<String> apSplit= splitString(msg, '\"');
                        if(apSplit.size()>1){
                            ERRORCommand err= new ERRORCommand("request return failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String movie= apSplit.get(0);
                        req= new ReturnMovie(_connections, _database, _connectionId, movie);
                        req.execute();
                        break;
                    }
                    case "addmovie":{
                        if(split.size()<5){
                            ERRORCommand err= new ERRORCommand("request addmovie failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }

                        List<String> apSplit= splitString(message, '\"');
                        //String[] apSplit= message.split("\"");
                        String movie= apSplit.get(1);
                        //String[] numbers= apSplit[2].split(" ");
                        List<String> numbers= splitString(apSplit.get(2), ' ');
                        if(numbers.size()<2){
                            ERRORCommand err= new ERRORCommand("request addmovie failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        int availableAmount= Integer.parseInt(numbers.get(0));
                        int price= Integer.parseInt(numbers.get(1));

                        List<String> bannedCountries= new LinkedList<>();
                        for(int i=3; i<apSplit.size(); i++){
                            bannedCountries.add(apSplit.get(i));
                        }
                        Movie mov= new Movie(movie, price, "", bannedCountries, availableAmount, availableAmount);
                        req= new AddMovie(_connections, _database, _connectionId, mov);
                        req.execute();
                        break;
                    }
                    case "remmovie":{
                        if(split.size()<3){
                            ERRORCommand err= new ERRORCommand("request remmovie failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.size(); i++)
                            msg+=split.get(i)+" ";
                        //String[] apSplit= msg.split('"'+"");
                        //String movie= apSplit[0];
                        List<String> apSplit= splitString(msg, '\"');
                        if(apSplit.size()>1){
                            ERRORCommand err= new ERRORCommand("request remmovie failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String movie= apSplit.get(0);
                        req= new RemMovie(_connections, _database, _connectionId, movie);
                        req.execute();
                        break;
                    }
                    case "changeprice":{
                        if(split.size()<4){
                            ERRORCommand err= new ERRORCommand("request changeprice failed");
                            _connections.send(_connectionId, err.getError());
                            return;
                        }
                        String msg="";
                        for(int i=2; i<split.size(); i++)
                            msg+=split.get(i)+" ";
                        List<String> apSlit= splitString(msg, '\"');
                        String movie= apSlit.get(0);
                        String price= apSlit.get(1).trim();
                        req= new ChangePrice(_connections, _database, _connectionId, movie, price);
                        req.execute();
                        break;
                    }
                }
                break;

            }
            default:{_connections.send(_connectionId,"Command does not exist");}
        }
    }

    public List<String> splitString(String s, char[] del){
        List<String> ret= new LinkedList<>();
        int index=0;
        while(index<s.length()){
            String curr="";
            while(index<s.length() && !contains(del, s.charAt(index))){
                curr+=s.charAt(index);
                index++;
            }
            if(curr.trim().length()>0)
                ret.add(curr);
            index++;
        }
        return ret;
    }

    public List<String> splitString(String s, char c){
        List<String> ret= new LinkedList<>();
        int index=0;
        while(index<s.length()){
            String curr="";
            while(index<s.length() && s.charAt(index)!=c){
                curr+=s.charAt(index);
                index++;
            }
            if(curr.trim().length()>0)
                ret.add(curr);
            index++;
        }
        return ret;
    }

    public boolean contains(char[] delimiters, char c){
        for(int i=0; i<delimiters.length; i++){
            if(delimiters[i]==c)
                return true;
        }
        return false;
    }

    @Override
    protected void Register(String msg) {
        String[] split= msg.split(" ");
        if (_clientLoggedIn) {
            ERRORCommand err = new ERRORCommand("registration failed");
            _connections.send(_connectionId, err.getError());
            return;
        }
        if (split.length < 3) {
            ERRORCommand err = new ERRORCommand("registration failed");
            _connections.send(_connectionId, err.getError());
            return;
        }
        String username = split[1];
        String password = split[2];

        if(_database.getUsers().containsKey(username)){
            ERRORCommand err = new ERRORCommand("registration failed");
            _connections.send(_connectionId, err.getError());
            return;
        }

        if(split.length<4){
            ERRORCommand err = new ERRORCommand("registration failed");
            _connections.send(_connectionId, err.getError());
            return;
        }
        String[] split2= split[3].split("=");
        String country= split2[1].substring(1, split2[1].length()-1);
        Users user= new User(username, "normal", password, country, new LinkedList<>(), 0);
        _database.addUser(user);
        ACKCommand ack= new ACKCommand("registration succeeded");
        _connections.send(_connectionId, ack.getACK());

    }
}
