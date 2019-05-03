package newpackage.vab.gamma;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class TextDisplayer extends AppCompatActivity {

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_displayer);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
        bar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Gamma" + "</font>"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }


        SSHManager.getSSHinstance().executeSSHCommand("cd All/text && ls");
        DatabaseFragment.lst.postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseFragment.giveInstance().updateDatabaseList();
            }
        },200);


        txt= findViewById(R.id.textdisplay);
        txt.setText(getIntent().getStringExtra("passedString"));


    }


}
