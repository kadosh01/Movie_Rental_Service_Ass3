package bgu.spl181.net.impl.newsfeed;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl181.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl181.net.srv.Database;
import bgu.spl181.net.srv.MovieRentalProtocol;
import bgu.spl181.net.srv.Server;

import javax.xml.crypto.Data;

public class NewsFeedServerMain {

    public static void main(String[] args) {
        NewsFeed feed = new NewsFeed(); //one shared object

        DatabaseReadWrite dat= new DatabaseReadWrite();
        dat.DeserializeMovies();
        dat.DeserializeUsers();

// you can use any server... 
        Server.threadPerClient(
                7777, //port
                () -> new MovieRentalProtocol(dat), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
                /*
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                7777, //port
           ///     () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
              //  ObjectEncoderDecoder::new //message encoder decoder factory
        ).serve();
*/
    }

}
