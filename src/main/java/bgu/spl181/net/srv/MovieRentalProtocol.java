package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.srv.Commands.ERRORCommand;

import java.util.HashMap;

public class MovieRentalProtocol extends UserServiceTextBasedProtocol<String>{

    private HashMap<String, Boolean> _loggedInUsers= new HashMap<>();
    private DatabaseReadWrite _database;

    public MovieRentalProtocol(DatabaseReadWrite database)
    {
        _database=database;
    }

    @Override
    public void process(String message) {
        java.lang.String[] split= message.toString().split(" ");
        switch (split[0]){

        }
    }
}
