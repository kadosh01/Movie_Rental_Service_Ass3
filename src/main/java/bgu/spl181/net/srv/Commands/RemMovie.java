package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.api.bidi.Connections;

/**
 * Created by Joseph on 07/01/2018.
 */
public class RemMovie extends Request {

    private String _movie;

    public RemMovie(Connections connections, DatabaseReadWrite database, int connectionId, String movie) {
        super(connections, database, connectionId);
        _movie=movie;
    }

    @Override
    public void execute() {
        if(!(((DatabaseReadWrite)_database).getUserByConnectionId(_connectionId).get_type().equals("user")))
        {
            ERRORCommand error=new ERRORCommand("User is not an administrator");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(!((DatabaseReadWrite)_database).getMoviesNames().contains(_movie)){
            ERRORCommand error=new ERRORCommand("Movie does not exists in the system ");
            _connections.send(_connectionId,error.getError());
            return;
        }
        Movie mov=((DatabaseReadWrite)_database).getMovie(_movie);
        if(mov.get_availableAmount()!=mov.get_totalAmount())
        {
            ERRORCommand error=new ERRORCommand("Price or Amount are smaller than or equal to 0 (there are no free movies)");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(_database.tryToRemove(_movie)) {
            ACKCommand success = new ACKCommand(String.format("remmovie ”movie name” success ", _movie));
            _connections.send(_connectionId,success.getACK());
            BroadcastCommand brd= new BroadcastCommand("movie "+_movie+" removed");
            _connections.broadcast(brd.broadcast());
        }
        else{
            ERRORCommand error=new ERRORCommand("There is (at least one) a copy of the movie that is currently rented by a user");
            _connections.send(_connectionId,error.getError());
        }
    }
}
