package bgu.spl181.net.srv.Commands;

public class ACKCommand {

    private String _msg;

    public ACKCommand(String msg){
        _msg=msg;
    }

    public String getACK(){return "ACK "+_msg;}
}
