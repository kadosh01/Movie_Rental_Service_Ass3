package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.api.bidi.Connections;

public class MoviesInfo extends Request{

    private String _movieName;

    public MoviesInfo(Connections connections, DatabaseReadWrite database) {
        super(connections, database);
    }

    public MoviesInfo(Connections connections, DatabaseReadWrite database, String movieName) {
        super(connections, database);
    }

    @Override
    protected void execute() {

    }
}
