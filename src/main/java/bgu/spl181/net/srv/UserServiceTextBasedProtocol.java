package bgu.spl181.net.srv;

import java.lang.String;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Commands.ERRORCommand;

public abstract class UserServiceTextBasedProtocol<String> implements BidiMessagingProtocol<String> {
    protected Connections _connections;
    protected boolean _shouldTerminate= false;
    protected boolean _loggedIn= false;
    protected int _connectionId;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        _connections= connections;
        _connectionId= connectionId;
    }

    @Override
    public abstract void process(String message);

    @Override
    public boolean shouldTerminate() {
        return _shouldTerminate;
    }
}
