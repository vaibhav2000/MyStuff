package newpackage.vab.gamma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTransactionsClass {
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


}


