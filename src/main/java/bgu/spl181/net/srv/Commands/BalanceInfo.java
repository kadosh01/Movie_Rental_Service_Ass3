package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Database;

/**
 * Created by Joseph on 05/01/2018.
 */
public class BalanceInfo extends Request {

    private int _connectionid;
    public BalanceInfo(Connections connections, DatabaseReadWrite database, int connectionId) {
        super(connections, database);
        _connectionid=connectionId;
    }

    @Override
    protected void execute() {
        //_database.getUserByConnectionId()
    }
}
