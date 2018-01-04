package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.User;

import java.util.HashMap;

public interface Database {
    /**
     * return Hashmap of user data
     *
     *
     *
     * @return a Hashmap where the key is user username and the value is user password.
     */
    public HashMap<String, String> getUsersData();

    public boolean isLogged(String userName);
    public void setLogIn(String userName);
    public void setLogOut(String userName);
}
