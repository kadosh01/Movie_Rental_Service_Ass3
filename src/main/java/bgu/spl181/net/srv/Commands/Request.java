package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Database;

public abstract class Request {

    protected Connections _connections;
    protected DatabaseReadWrite _database;

    public Request(Connections connections, DatabaseReadWrite database){
        _connections= connections;
        _database= database;
    }

    protected abstract void execute();
}
