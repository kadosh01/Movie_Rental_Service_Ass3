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
        if(!(_database.getUserByConnectionId(_connectionId).get_type().equals("admin")))
        {
            ERRORCommand error=new ERRORCommand("User is not an administrator");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(_database.getMoviesNames().contains(_movie.get_name())){
            ERRORCommand error=new ERRORCommand("Movie name already exists in the system ");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(_movie.get_price()<=0 || _movie.get_availableAmount()<=0)
        {
            ERRORCommand error=new ERRORCommand("Price or Amount are smaller than or equal to 0 (there are no free movies)");
            _connections.send(_connectionId,error.getError());
            return;
        }
        _database.addMovie(_movie);
        ACKCommand success=new ACKCommand("addmovie "+"\""+_movie.get_name()+"\""+" success");
        _connections.send(_connectionId,success.getACK());
        BroadcastCommand brd= new BroadcastCommand("movie "+_movie.get_name()+" "+_movie.get_availableAmount()+" "+_movie.get_price());
        _connections.broadcast(brd.broadcast());

    }
}
