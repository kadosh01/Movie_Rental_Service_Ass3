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
            if(_database.getLoggedUsers().containsKey(handler.getKey())) {
                handler.getValue().send(msg);
            }
        }
    }

    @Override
    public void disconnect(int connectionId) {
        _connectionsMap.remove(connectionId);
    }

    public void set_database(Database database){_database=database;}

}
