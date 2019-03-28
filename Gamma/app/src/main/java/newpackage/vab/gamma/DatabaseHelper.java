package newpackage.vab.gamma;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME= "scannedLetters";
    public static final int DATABASE_VERSION=2;

    public DatabaseHelper( Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS "+DatabaseTransactionsClass.TABLEDATA+
                "(Sno INTEGER PRIMARY KEY AUTOINCREMENT,extractedTextCol LONGTEXT,bitmapCol LONGTEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

         db.execSQL("DROP TABLE IF EXISTS letterDatatable");
         onCreate(db);

    }
}