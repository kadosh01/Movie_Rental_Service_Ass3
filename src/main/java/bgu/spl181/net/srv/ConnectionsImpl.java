package bgu.spl181.net.srv;

import bgu.spl181.net.api.bidi.Connections;

import java.util.HashMap;


public class ConnectionsImpl<T> implements Connections<T> {
    private HashMap<Integer, ConnectionHandler<T>> _connectionsMap;
    @Override
    public boolean send(int connectionId, T msg) {
        if(!_connectionsMap.containsKey(connectionId)){return false;}
        //_connectionsMap.get(connectionId).
        return false;
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
