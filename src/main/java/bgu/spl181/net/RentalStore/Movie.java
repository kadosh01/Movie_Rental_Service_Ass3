package bgu.spl181.net.RentalStore;

import java.util.List;

public class Movie {

    private String _name,_id;
    private int _price,_availableAmount,_totalAmount;
    private List<String> _bannedCategories;

    public Movie(String name, int price , String id, List<String> bannedCategories ,int availableAmount,int totalAmount){
        _availableAmount=availableAmount;
        _bannedCategories=bannedCategories;
        _id=id;
        _name=name;
        _price=price;
        _totalAmount=totalAmount;
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

    public List<String> get_bannedCategories() {
        return _bannedCategories;
    }

    public void set_bannedCategories(List<String> _bannedCategories) {
        this._bannedCategories = _bannedCategories;
    }
}
