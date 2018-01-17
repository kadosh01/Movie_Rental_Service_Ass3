package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.MovieRentalProtocol;
import bgu.spl181.net.srv.Server;

public class TPCMain {

        public static void main(String[] args) {

            DatabaseReadWrite dat= new DatabaseReadWrite();
            dat.DeserializeMovies();
            dat.DeserializeUsers();

// you can use any server...

            Server.threadPerClient(
                    Integer.parseInt(args[0]), //port
                    () -> new MovieRentalProtocol(dat), //protocol factory
                    LineMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();

// you can use any server...

/*
            Server.reactor(
                    Runtime.getRuntime().availableProcessors(),
                    Integer.parseInt(args[0]), //port
                    () ->  new MovieRentalProtocol(dat), //protocol factory
                    LineMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();
*/
        }
    }
