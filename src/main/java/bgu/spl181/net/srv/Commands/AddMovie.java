package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.api.bidi.Connections;

/**
 * Created by Joseph on 05/01/2018.
 */
public class AddMovie extends Request {
    public AddMovie(Connections connections, DatabaseReadWrite database, int connectionId) {
        super(connections, database, connectionId);
    }

    @Override
    public void execute() {

    }
}
