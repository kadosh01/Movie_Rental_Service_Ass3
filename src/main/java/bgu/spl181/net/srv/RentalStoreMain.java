package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;

public class RentalStoreMain {

        public static void main(String[] args) {

            DatabaseReadWrite dat= new DatabaseReadWrite();
            dat.DeserializeMovies();
            dat.DeserializeUsers();
/*
// you can use any server...
            /*
            Server.threadPerClient(
                    7777, //port
                    () -> new MovieRentalProtocol(dat), //protocol factory
                    LineMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();
*/
// you can use any server...


            Server.reactor(
                    Runtime.getRuntime().availableProcessors(),
                    7777, //port
                    () ->  new MovieRentalProtocol(dat), //protocol factory
                    LineMessageEncoderDecoder::new //message encoder decoder factory
            ).serve();

        }
    }
