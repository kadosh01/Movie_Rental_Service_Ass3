package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.api.bidi.Connections;

public class ReturnMovie extends Request{

    private String _movieName;

    public ReturnMovie(Connections connections, DatabaseReadWrite database, int connectionId, String movieName) {
        super(connections, database, connectionId);
        _movieName= movieName;
    }

    @Override
    public void execute() {
        if(!_database.containsMovie(_movieName)){
            ERRORCommand err= new ERRORCommand("movie doesn't exist");
            _connections.send(_connectionId, err.getError());
            return;
        }
        User user= _database.getUserByConnectionId(_connectionId);
        Movie mov= _database.getMovie(_movieName);
        if(!user.get_rentedMovies().contains(mov)){
            ERRORCommand err= new ERRORCommand("user is currently not renting the movie");
            _connections.send(_connectionId, err.getError());
            return;
        }
        user.get_rentedMovies().remove(_movieName);
        _database.increaseAvailableCopies(_movieName);
        _database.updateUserFile();
        ACKCommand ack= new ACKCommand("return "+_movieName+" success");
        _connections.send(_connectionId, ack.getACK());
        BroadcastCommand brd= new BroadcastCommand("movie "+_movieName+" "+_database.getMovie(_movieName).get_availableAmount()+" "+_database.getMovie(_movieName).get_price());
        _connections.broadcast(brd.broadcast());

    }
}
