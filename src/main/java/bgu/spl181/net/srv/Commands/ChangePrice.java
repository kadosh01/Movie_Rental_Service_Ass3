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
        if(!(_database.getUserByConnectionId(_connectionId).get_type().equals("admin")))
        {
            ERRORCommand error=new ERRORCommand("User is not an administrator");
            _connections.send(_connectionId,error.getError());
            return;
        }
        if(!_database.containsMovie(_movieName)){
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
        _database.changePrice(_movieName,Integer.parseInt(_price));
        ACKCommand success=new ACKCommand("REQUEST changeprice "+_movieName + " success");
        _connections.send(_connectionId,success.getACK());
        BroadcastCommand brd= new BroadcastCommand("movie "+_movieName+" "+_database.getMovies().get(_movieName).get_availableAmount()+" "+_database.getMovies().get(_movieName).get_price());
        _connections.broadcast(brd.broadcast());
    }
}
