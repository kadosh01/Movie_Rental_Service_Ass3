package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Database;

public class BalanceAdd extends Request{

    private int _connectionId;
    private int _amount;

    public BalanceAdd(Connections connections, Database database, int connectionId, int amount) {
        super(connections, database);
        _connectionId= connectionId;
        _amount= amount;
    }

    @Override
    protected void execute() {

    }
}
