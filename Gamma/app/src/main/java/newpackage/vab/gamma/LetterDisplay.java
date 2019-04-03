package newpackage.vab.gamma;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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

import java.io.IOException;

public class LetterDisplay extends AppCompatActivity {

  private Bitmap bitmap;
  private String extractedText;
  private DatabaseTransactionsClass dbtrans;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_letter_display);

    dbtrans= new DatabaseTransactionsClass(getApplicationContext());
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

    TextView date= findViewById(R.id.dateDescriptionID);
    date.setText(new LetterStringFinder(extractedText).getDate());

    TextView from = findViewById(R.id.fromDescriptionID);
    from.setText(new LetterStringFinder(extractedText).getFromname());

    TextView to = findViewById(R.id.toDescriptionID);
    to.setText(new LetterStringFinder(extractedText).getToname());

    TextView through = findViewById(R.id.throughDescriptionID);
    through.setText(new LetterStringFinder(extractedText).getThroughName());

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

      dbtrans.insertData(extractedText,bitmap.toString());

      LetterContent tempobj= new LetterContent();
      tempobj.rank=5;  //Change it
      tempobj.bitmapStr= bitmap.toString();
      tempobj.extractStr=extractedText;

      DatabaseFragment dtbobj= DatabaseFragment.giveInstance();
      dtbobj.updateDatabaseList(tempobj);
      //save file here
      finish(); }

    if(item.getItemId()==R.id.cancel_actionbarbutton)
      finish();

    return super.onOptionsItemSelected(item);
  }
}
