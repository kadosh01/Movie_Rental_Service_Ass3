package bgu.spl181.net.RentalStore;

import com.google.gson.annotations.SerializedName;
import com.sun.xml.internal.ws.developer.Serialization;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private String _name,_id;
    private int _price,_availableAmount,_totalAmount;
    private List<String> _bannedCountries;

    public Movie(String name, int price , String id, List<String> bannedCountries ,int availableAmount,int totalAmount){
        _availableAmount=availableAmount;
        _bannedCountries=bannedCountries;
        _id=id;
        _name=name;
        _price=price;
        _totalAmount=totalAmount;
    }
    public Movie(String name, String id){
        _id=id;
        _name=name;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }

    public int get_availableAmount() {
        return _availableAmount;
    }

    public void set_availableAmount(int _availableAmount) {
        this._availableAmount = _availableAmount;
    }

    public int get_totalAmount() {
        return _totalAmount;
    }

    public void set_totalAmount(int _totalAmount) {
        this._totalAmount = _totalAmount;
    }

    public List<String> get_bannedCountries() {
        return _bannedCountries;
    }

    public void set_bannedCountries(List<String> _bannedCountries) {
        this._bannedCountries = _bannedCountries;
    }

    public Movie clone(){
        return new Movie(_name, _price, _id, _bannedCountries, _availableAmount, _totalAmount);
    }

    /*
    @Override
    public String toString() {
        String ret= _name+" "+_availableAmount+" "+_price;
        for(String country: _bannedCountries){
            ret= ret+" "+country;
        }
        return ret;
    }*/
}
