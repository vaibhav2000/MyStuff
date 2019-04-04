package newpackage.vab.gamma;

import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

public class Person {

    private int key;
    private String name = "";
    private ArrayList<String> posts;

    public Person(){posts = new ArrayList<String>();}

    public Person(int key, String name, ArrayList<String> posts){
        this.key = key;
        this.name = name;
        this.posts = new ArrayList<>();
        this.posts = (ArrayList<String>) posts.clone();

    }

    public Person(int key, String name){
        this.name = name;
        this.key = key;
        posts = new ArrayList<String>();
    }

    public Person(String name,ArrayList<String> posts){
        this.name = name;
        this.posts = new ArrayList<>();
        this.posts = (ArrayList<String>) posts.clone();
    }

    public int getKey() {
        return key;
    }

    public void setKey(int id) {
        this.key = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    public void addPost(String post){ posts.add(post); }

    public boolean isNull(){
        Log.i("person name",name+"\n");
        if(name=="" && posts.size()==0)return true;
        else return false;
    }

}