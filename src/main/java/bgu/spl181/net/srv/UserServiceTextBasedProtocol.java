package bgu.spl181.net.srv;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Commands.ERRORCommand;

public abstract class UserServiceTextBasedProtocol<String> implements BidiMessagingProtocol<String> {
    protected Connections _connections;
    protected boolean _shouldTerminate= false;
    protected boolean _clientLoggedIn= false;
    protected int _connectionId;
    protected Database _database;

    public UserServiceTextBasedProtocol(Database database){
        _database=database;
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        _connections= connections;
        _connectionId= connectionId;
    }

    @Override
    public void process(String message){
        java.lang.String[] split= ((java.lang.String)message).split(" ");
        switch (split[0]){
            case "REGISTER": {
                /*
                if (_loggedIn) {
                    ERRORCommand err = new ERRORCommand("REGISTER failed, client already logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if (split.length < 3) {
                    ERRORCommand err = new ERRORCommand("REGISTER failed, username or password missing");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                String username = (String) split[1];
                String password = (String) split[2];
                */
                Register();
                break;
            }
            case "LOGIN":
                if(_clientLoggedIn){
                    ERRORCommand err= new ERRORCommand("LOGIN failed, user already logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if (split.length < 3) {
                    ERRORCommand err= new ERRORCommand("LOGIN failed, username or password doesn't exist");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                String username= (String) split[1];
                String password= (String) split[2];

              //  if(_database.isLogged(username)){

                }


                _clientLoggedIn= true;
                break;
            case "SIGNOUT":
                _clientLoggedIn= false;
                break;
        }
    }

    protected abstract void Register();
    @Override
    public boolean shouldTerminate() {
        return _shouldTerminate;
    }
}
