package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionsImpl;

public class RentMovie extends Request{

    private String _movieName;

    public RentMovie(Connections connections, DatabaseReadWrite database, int connectionId, String movieName) {
        super(connections, database, connectionId);
        _movieName= movieName;
    }

    @Override
    public void execute() {
        if(!_database.getMovies().containsKey(_movieName)){//synchronize???
            ERRORCommand err= new ERRORCommand("movie doesn't exist");
            _connections.send(_connectionId, err.getError());
            return;
        }
        Movie mov= _database.getMovie(_movieName);
        User user= _database.getUserByConnectionId(_connectionId);
        if(user.get_rentedMovies().contains(mov)){//make sure all movies are the same instances
            ERRORCommand err= new ERRORCommand("user is already renting the movie");
            _connections.send(_connectionId, err.getError());
            return;
        }
        if(user.get_balance()<mov.get_price()){
            ERRORCommand err= new ERRORCommand("user doesn't have enough money");
            _connections.send(_connectionId, err.getError());
            return;
        }
        for(String country: mov.get_bannedCountries()){
            if(country.equalsIgnoreCase(user.get_country())){
                ERRORCommand err= new ERRORCommand("movie is banned in user's country");
                _connections.send(_connectionId, err.getError());
                return;
            }
        }
        if(_database.tryRentMovie(_movieName)){
            user.get_rentedMovies().add(mov);
            user.set_balance(user.get_balance()-mov.get_price());
            ACKCommand ack= new ACKCommand("rent "+_movieName+" success");
            _connections.send(_connectionId, ack.getACK());
            BroadcastCommand brd= new BroadcastCommand("movie "+_movieName+" "+_database.getMovie(_movieName).get_availableAmount()+" "+_database.getMovie(_movieName).get_price());
            _connections.broadcast(brd.broadcast());
            //synchronize???

        }
        else{
            ERRORCommand err= new ERRORCommand("no more copies available");
            _connections.send(_connectionId, err.getError());
            return;
        }

    }
}
