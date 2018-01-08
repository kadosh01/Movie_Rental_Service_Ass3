package bgu.spl181.net.srv;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.Commands.ACKCommand;
import bgu.spl181.net.srv.Commands.ERRORCommand;

public abstract class UserServiceTextBasedProtocol implements BidiMessagingProtocol<String> {
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
        String[] split= message.split(" ");
        switch (split[0]){
            case "REGISTER": {
                Register(message);
                break;
            }
            case "LOGIN": {
                if (_clientLoggedIn) {
                    ERRORCommand err = new ERRORCommand("LOGIN failed, client is already logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if (split.length < 3) {
                    ERRORCommand err = new ERRORCommand("LOGIN failed, username or password doesn't exist");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                String username = split[1];
                String password = split[2];

                if (!_database.getUsers().containsKey(username) || !_database.getUsers().get(username).getPassword().equals(password)) {
                    ERRORCommand err = new ERRORCommand("LOGIN failed, username or password doesn't exist");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if (_database.getUsers().get(username).isLoggedIn()) {
                    ERRORCommand err = new ERRORCommand("LOGIN failed, user is already logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                _clientLoggedIn = true;
                _database.addLoggedUser(_connectionId, username);
                ACKCommand ack= new ACKCommand("LOGIN succeeded");
                _connections.send(_connectionId, ack.getACK());
                break;
            }
            case "SIGNOUT": {
                if(!_clientLoggedIn){
                    ERRORCommand err = new ERRORCommand("SIGNOUT failed, client not logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                _clientLoggedIn = false;
                _database.removeLoggedUser(_connectionId);
                _shouldTerminate= true;
                ACKCommand ack= new ACKCommand("SIGNOUT succeeded");
                _connections.send(_connectionId, ack.getACK());
                _connections.disconnect(_connectionId); //added disconnect
                break;
            }
        }
    }

    protected abstract void Register(String msg);

    @Override
    public boolean shouldTerminate() {//when does it change to true??
        return _shouldTerminate;
    }
}
