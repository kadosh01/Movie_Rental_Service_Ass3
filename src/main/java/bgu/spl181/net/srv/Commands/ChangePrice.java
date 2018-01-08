package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.api.bidi.Connections;

/**
 * Created by Joseph on 07/01/2018.
 */
public class ChangePrice extends Request {
    private String _movieName;
    private String _price;
    public ChangePrice(Connections connections, DatabaseReadWrite database, int connectionId,String movieNmae,String price) {
        super(connections, database, connectionId);
        _movieName=movieNmae;
        _price=price;
    }

    @Override
    public void execute() {
        if(!(((DatabaseReadWrite)_database).getUserByConnectionId(_connectionId).get_type().equals("admin")))
        {
            ERRORCommand error=new ERRORCommand("User is not an administrator");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(!((DatabaseReadWrite)_database).getMoviesNames().contains(_movieName)){
            ERRORCommand error=new ERRORCommand("Movie does not exists in the system ");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(Integer.parseInt(_price)<=0)
        {
            ERRORCommand error=new ERRORCommand("Price is smaller than or equal to 0");
            _connections.send(_connectionId,error.getError());
            return;
        }
        //_database.addMovie(_movie);
        //ACKCommand success=new ACKCommand(String.format("addmovie ”{0}” success",_movie.get_name()));
        //_connections.send(_connectionId,success.getACK());
        //BroadcastCommand brd= new BroadcastCommand("movie "+_movie.get_name()+" "+_movie.get_availableAmount()+" "+_movie.get_price());
        //_connections.broadcast(brd.broadcast());
    }
}
