package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Database;

/**
 * Created by Joseph on 05/01/2018.
 */
public class BalanceInfo extends Request {

    public BalanceInfo(Connections connections, DatabaseReadWrite database, int connectionId) {
        super(connections, database, connectionId);
    }

    @Override
    public void execute() {
        int balance=_database.getUserByConnectionId(_connectionId).get_balance();
        ACKCommand ACK=new ACKCommand("balance " +balance);
        _connections.send(_connectionId,ACK.getACK());
    }
}
