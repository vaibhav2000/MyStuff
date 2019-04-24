package newpackage.vab.gamma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TextDisplayer extends AppCompatActivity {

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_displayer);

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
