package bgu.spl181.net.srv;
import java.io.File;
import java.lang.Throwable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl181.net.RentalStore.DatabaseReadWrite;
import bgu.spl181.net.RentalStore.Movie;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.rci.ObjectEncoderDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.xml.internal.ws.wsdl.writer.document.http.Address;


public class RentalStoreMain {

        public static void main(String[] args) {

            DatabaseReadWrite d=new DatabaseReadWrite();
            d.DeserializeUsers();
            d.DeserializeMovies();


// you can use any server...
        Server.threadPerClient(
                7777, //port
                () -> new MovieRentalProtocol(d), //protocol factory
                LineMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();

           // Server.reactor(
          //          Runtime.getRuntime().availableProcessors(),
          //          7777, //port
          //          () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
          //          ObjectEncoderDecoder::new //message encoder decoder factory
          //  ).serve();

        }
    }
