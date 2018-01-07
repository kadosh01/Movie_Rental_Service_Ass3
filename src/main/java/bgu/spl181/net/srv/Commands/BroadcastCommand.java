package bgu.spl181.net.srv.Commands;

public class BroadcastCommand {

    private String _msg;

    public BroadcastCommand(String msg){
        _msg=msg;
    }

    public String broadcast(){
        return "BROADCAST "+_msg;
    }

}
