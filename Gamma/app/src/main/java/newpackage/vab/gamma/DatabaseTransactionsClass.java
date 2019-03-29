package newpackage.vab.gamma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

public class DatabaseTransactionsClass {

    //
    //

    public final static String EXTRACTDATA= "extractedTextCol";
    public final static String BITMAPDATA= "bitmapCol";
    public final static String TABLEDATA ="Datatable";

    private DatabaseHelper dbhelp;
    private SQLiteDatabase univwritedatabase=null;
    private SQLiteDatabase univreaddatabase=null;

    public DatabaseTransactionsClass(Context context)
    {
        dbhelp= new DatabaseHelper(context);
        univwritedatabase= dbhelp.getWritableDatabase();
        univreaddatabase= dbhelp.getReadableDatabase();
    }

    public void insertData(String extractstr,String bitmpstr)
    {
        ContentValues currContent= new ContentValues();
        currContent.put(EXTRACTDATA,extractstr);
        currContent.put(BITMAPDATA,bitmpstr);
        univwritedatabase.insert(TABLEDATA,null,currContent);

    }

    public ArrayList<LetterContent> givedataList()
    {

        ArrayList<LetterContent> temp= new ArrayList<LetterContent>();

        Cursor c= univreaddatabase.query(TABLEDATA,null,null,null,null,null,null);
        while (c.moveToNext())
        {
            Log.v("***","Sno:"+c.getString(c.getColumnIndex("Sno"))+" |Bitmap:"+c.getString(c.getColumnIndex("bitmapCol"))+ " |Extract:"+c.getString(c.getColumnIndex("extractedTextCol")));
            LetterContent currentLet= new LetterContent();

            currentLet.rank= Integer.parseInt(c.getString(c.getColumnIndex("Sno")));
            currentLet.bitmapStr= c.getString(c.getColumnIndex("bitmapCol"));
            currentLet.extractStr= c.getString(c.getColumnIndex("extractedTextCol"));

            temp.add(currentLet);
        }
        return temp;
    }

    //
    //
    //

    public final static String PERSON_TABLE = "personTable";
    public final static String POSTS_TABLE = "postsTable";
    public final static String KEY = "id";
    public final static String NAME = "name";
    public final static String POST = "post";

    private int getUniKey(){
        int ret =0 ;
        Cursor c = univreaddatabase.rawQuery("SELECT MAX(id) AS mx FROM "+PERSON_TABLE,null);
        if(c.moveToFirst()){
            ret = c.getInt(c.getColumnIndex("mx"));
        }

        ret++;

        return ret;

    }

    public void insertPerson(Person person){

        int key;
        ContentValues currContent= new ContentValues();
        key = getUniKey();
        currContent.put(KEY,key);
        currContent.put(NAME,person.getName());
        univwritedatabase.insert(PERSON_TABLE,null,currContent);
        //
        ArrayList<String> posts = person.getPosts();
        for(String post : posts){
            currContent = new ContentValues();
            currContent.put(KEY,key);
            currContent.put(POST,post);
            univwritedatabase.insert(POSTS_TABLE,null,currContent);
        }

        //

    }

    public void removePerson(int key){
        univwritedatabase.delete(PERSON_TABLE,"id = ?",new String[]{key+""});
        univwritedatabase.delete(POSTS_TABLE,"id = ?",new String[]{key+""});
        //
    }

    public ArrayList<Person> getPersons(){
        ArrayList<Person> temp= new ArrayList<Person>();
        ArrayList<Integer> key_ind = new ArrayList<Integer>();

        Cursor c_name = univreaddatabase.rawQuery("SELECT * FROM "+PERSON_TABLE,null);
        if(c_name.moveToFirst())
         do {

            Person person = new Person(c_name.getInt(c_name.getColumnIndex(KEY)), c_name.getString(c_name.getColumnIndex(NAME)));
            temp.add(person);key_ind.add(person.getKey());

        } while (c_name.moveToNext());
        c_name.close();
        //
        Cursor c_post = univreaddatabase.rawQuery("SELECT * FROM "+POSTS_TABLE,null);
        if(c_post.moveToFirst())
        do {
            Person person = temp.get(key_ind.indexOf(c_post.getInt(c_post.getColumnIndex(KEY))));
            int ind = temp.indexOf(person);
            person.addPost(c_post.getString(c_post.getColumnIndex(POST)));
            temp.add(ind , person);
        } while(c_post.moveToNext());
        c_post.close();
        return temp;
    }


}

