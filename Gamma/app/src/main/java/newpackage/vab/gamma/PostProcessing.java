package newpackage.vab.gamma;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostProcessing {

     public void  writeDownAstxt(String filename,String text,String path)
    {
        try
        {
            Log.d("Text Storage",path);
            File newfile= new File(path,filename);

            FileWriter fileWriter= new FileWriter(newfile);
            fileWriter.append(text);
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

     public void compactText(String str)
    {
        String temp="";

        for(int i=0;i<str.length();i++)
             if(Character.isLetterOrDigit(str.charAt(i)))
                  temp+= Character.toLowerCase(str.charAt(i));


    }

     public String getIdentifier()
    {
         Date date= Calendar.getInstance(Locale.ROOT).getTime();
         String temp="LTR_"+date.toString();

         temp=temp.replaceAll(" ","_");
         temp= temp.replace(":","");

         Log.d("TAG",temp);

         temp+=".txt";
         return temp;

    }


}
