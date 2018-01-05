package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.api.bidi.Connections;

public class MoviesInfo extends Request{

    private String _movieName="";

    public MoviesInfo(Connections connections, DatabaseReadWrite database, int connectionId) {
        super(connections, database, connectionId);
    }

    public MoviesInfo(Connections connections, DatabaseReadWrite database, int connectionId, String movieName) {
        super(connections, database, connectionId);
        _movieName= movieName;
    }

    @Override
    public void execute() {
        String msg="";
        if(_movieName.length()>0){
            if(!_database.getMovies().containsKey(_movieName)){
                ERRORCommand err= new ERRORCommand("movie doesn't exist");
                _connections.send(_connectionId, err.getError());
                return;
            }
            Movie mov= _database.getMovies().get(_movieName);
            msg= "info "+_movieName+" "+mov.get_availableAmount()+" "+mov.get_price();
            for(String country: mov.get_bannedCountries()){
                msg= msg+" "+country;
            }
        }
        else{
            for(String mov: _database.getMovies().keySet()){
                msg= msg+ mov+" ";
            }
        }
        ACKCommand ack= new ACKCommand(msg);
        _connections.send(_connectionId, ack.getACK());
    }
}
