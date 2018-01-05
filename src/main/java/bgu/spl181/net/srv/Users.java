package bgu.spl181.net.srv;

public interface Users {

    public boolean isLoggedIn();

    public void setLoggedIn(boolean bool);

    public String getUsername();

    public String getPassword();
}
