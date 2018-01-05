package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Database;

public class BalanceAdd extends Request{


    public BalanceAdd(Connections connections, Database database, int connectionId, int amount) {
        super(connections, database);
    }

    @Override
    protected void execute() {

    }
}
