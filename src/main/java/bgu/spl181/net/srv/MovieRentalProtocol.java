package bgu.spl181.net.srv;

import bgu.spl181.net.srv.Commands.ERRORCommand;

public class MovieRentalProtocol extends UserServiceTextBasedProtocol<String>{


    @Override
    public void process(String message) {
        java.lang.String[] splitted= message.toString().split(" ");
        switch (splitted[0]){
            case "REGISTER": {
                if (_loggedIn) {
                    ERRORCommand err = new ERRORCommand("REGISTER failed, client already logged in");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                if (splitted.length < 3) {
                    ERRORCommand err = new ERRORCommand("REGISTER failed, username or password missing");
                    _connections.send(_connectionId, err.getError());
                    return;
                }
                String username = (String) splitted[1];
                String password = (String) splitted[2];
                break;
            }
            case "LOGIN":
                if(_loggedIn){
                    ERRORCommand err= new ERRORCommand("LOGIN failed, user already logged in");
                    _connections.send(_connectionId, err.getError());
                }
                else {
                    if (splitted.length < 3) {
                        ERRORCommand err= new ERRORCommand("LOGIN failed, username or password doesn't exist");
                        _connections.send(_connectionId, err.getError());
                    }
                    else{
                        String username= (String) splitted[1];
                        String password= (String) splitted[2];

                    }
                }
                _loggedIn= true;
                break;
            case "SIGNOUT":
                _loggedIn= false;
                break;
        }
    }
}
