package bgu.spl181.net.srv;

import bgu.spl181.net.RentalStore.User;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface Database {
    /**
     * return Hashmap of user data
     *
     *
     *
     * @return a Hashmap where the key is user username and the value is user password.
     */
    public ConcurrentHashMap<String, Users> getUsers();

    public void addUser(Users user);

    public ConcurrentHashMap<Integer, Users> getLoggedUsers();

    public void addLoggedUser(Integer id, String username);

    public void removeLoggedUser(Integer id);

}
