package bgu.spl181.net.srv;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    @Override
    public void start(int connectionId, Connections<T> connections) {

    }

    @Override
    public void process(T message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
