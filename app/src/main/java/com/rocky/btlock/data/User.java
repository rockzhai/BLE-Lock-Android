package com.rocky.btlock.data;

/**
 * Created by Rocky on 2016/6/1.
 */
public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHaveFiger() {
        return haveFiger;
    }

    public void setHaveFiger(boolean haveFiger) {
        this.haveFiger = haveFiger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id ;
    private boolean haveFiger;
    private String name;
}
