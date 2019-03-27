package newpackage.vab.gamma;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

public class LetterDisplay extends AppCompatActivity {

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


    String ext=  getIntent().getStringExtra("EXTRACTED_TEXT");
    TextView txt= (TextView)findViewById(R.id.text);
    txt.setText(ext);

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

      //save file here
      finish(); }

    if(item.getItemId()==R.id.cancel_actionbarbutton)
      finish();


    return super.onOptionsItemSelected(item);
  }
}
