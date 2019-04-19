package newpackage.vab.gamma;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class LetterDisplay extends AppCompatActivity {

  private Bitmap bitmap;
  private String extractedText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_letter_display);

    ActionBar bar = getSupportActionBar();
    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
    bar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Gamma" + "</font>"));

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.parseColor("#000000"));
    }

    extractedText=  getIntent().getStringExtra("EXTRACTED_TEXT");

    Uri BitmapUri= getIntent().getParcelableExtra("BITMAP_URI");
    try {
      bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),BitmapUri);
    } catch (IOException e) {
      e.printStackTrace();
    }

    TextView txt= (TextView)findViewById(R.id.text);
    txt.setText(extractedText);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

     getMenuInflater().inflate(R.menu.custommenu,menu);
     return super.onCreateOptionsMenu(menu);
  }



  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if(item.getItemId()==R.id.save_actionbarbutton)
    {
      Toast.makeText(getApplicationContext(),"Saving File...",Toast.LENGTH_SHORT).show();

       //Update ssh remote server here

      PostProcessing postprep= new PostProcessing();

      String uniqueID=postprep.getIdentifier();
      postprep.writeDownAstxt(uniqueID+".txt",extractedText,getExternalCacheDir().getPath());
      postprep.writeDownasBitmap(uniqueID+".png",bitmap,getExternalCacheDir().getPath());

      String uploadtxt ="";
      uploadtxt+="##"+ uniqueID+ "\n";
      uploadtxt+= postprep.compactText(extractedText);

      postprep.writeDownAstxt("upload.txt",uploadtxt,getExternalCacheDir().getPath());

      Log.i("Loc",getExternalCacheDir().getPath()+ "/"+uniqueID+".txt");
      SSHManager.getSSHinstance().fileUpload(getExternalCacheDir().getAbsolutePath()+ "/"+uniqueID+".txt","All/text/");
      SSHManager.getSSHinstance().fileUpload(getExternalCacheDir().getAbsolutePath()+ "/"+uniqueID+".png","All/image/");

      SSHManager.getSSHinstance().fileUpload(getExternalCacheDir().getAbsolutePath()+ "/"+"upload.txt","upload.txt");
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          SSHManager.getSSHinstance().executeSSHCommand("./append");
        }
      }).start();


      //save file here
      finish(); }

    if(item.getItemId()==R.id.cancel_actionbarbutton)
      finish();

    return super.onOptionsItemSelected(item);
  }
}
