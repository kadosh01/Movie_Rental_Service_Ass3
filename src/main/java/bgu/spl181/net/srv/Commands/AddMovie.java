package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.api.bidi.Connections;

/**
 * Created by Joseph on 05/01/2018.
 */
public class AddMovie extends Request {
    private Movie _movie;
    public AddMovie(Connections connections, DatabaseReadWrite database, int connectionId, Movie newmovie) {
        super(connections, database, connectionId);
        _movie=newmovie;
    }

    @Override
    public void execute() {
        if(!(((DatabaseReadWrite)_database).getUserByConnectionId(_connectionId).get_type().equals("user")))
        {
            ERRORCommand error=new ERRORCommand("User is not an administrator");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(((DatabaseReadWrite)_database).getMoviesNames().contains(_movie.get_name())){
            ERRORCommand error=new ERRORCommand("Movie name already exists in the system ");
            _connections.send(_connectionId,error.getError());
            return;
        }

    }
}
