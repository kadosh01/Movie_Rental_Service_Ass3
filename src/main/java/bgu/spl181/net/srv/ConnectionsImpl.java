package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> _connectionsMap= new ConcurrentHashMap<>();
    private Database _database;
    /*public ConnectionsImpl(Database database){
        _database=database;
    }*/
    @Override
    public boolean send(int connectionId, T msg) {
        if(!_connectionsMap.containsKey(connectionId)){
            return false;
        }
        _connectionsMap.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for(Map.Entry<Integer,ConnectionHandler<T>> handler : _connectionsMap.entrySet()){
            //if(handler)
            handler.getValue().send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        try {
            _connectionsMap.get(connectionId).close();
            _connectionsMap.remove(connectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void set_database(Database database){_database=database;}

}
