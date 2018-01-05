package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.User;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Database;

public class BalanceAdd extends Request{

    private int _connectionId;
    private int _amount;

    public BalanceAdd(Connections connections, DatabaseReadWrite database, int connectionId, int amount) {
        super(connections, database);
        _connectionId= connectionId;
        _amount= amount;
    }

    @Override
    protected void execute() {
        User user= _database.getUserByConnectionId(_connectionId);
        int balance= user.get_balance();
        user.set_balance(balance+_amount);
        ACKCommand ack= new ACKCommand("balance "+user.get_balance()+" added "+_amount);
        _connections.send(_connectionId, ack.getACK());
    }
}
