package bgu.spl181.net.srv.Commands;

public class ERRORCommand {
    private String _msg;

    public ERRORCommand(String msg){
        _msg=msg;
    }
    public String getError(){return "ERROR "+_msg;}
}
