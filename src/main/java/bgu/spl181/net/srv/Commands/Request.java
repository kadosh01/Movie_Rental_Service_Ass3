package bgu.spl181.net.srv.Commands;

import bgu.spl181.net.api.bidi.Connections;

public abstract class Request {

    protected Connections _connections;

    public Request(Connections connections){
        _connections= connections;
    }
}
