package newpackage.vab.gamma;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.widget.ListViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class ShowItemsInList extends AppCompatActivity {


    ArrayList<String> customList;
    ArrayAdapter<String> adpt= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_in_list);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
        bar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Gamma" + "</font>"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }


        customList= new ArrayList<String>();
        adpt= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,customList)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view= super.getView(position,convertView,parent);
                TextView temptext=(TextView)view.findViewById(android.R.id.text1);
                temptext.setTextColor(getResources().getColor(android.R.color.white));
                return view;
            }
        };


        String passedStr= getIntent().getStringExtra("passedString");


        String temp="";
        for(int i=0;i<passedStr.length();i++)
           if(passedStr.charAt(i)=='\n')
           {
                customList.add(temp);
                temp="";
           }
            else
             temp+=  passedStr.charAt(i);


        ListView lst= (ListView)findViewById(android.R.id.list);
        lst.setAdapter(adpt);
        adpt.notifyDataSetChanged();

        lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {



                new AlertDialog.Builder(ShowItemsInList.this)
                        .setTitle("Actions")
                        .setItems(new CharSequence[]
                                        {"Show Text", "Show Image", "Delete Entry"},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                            {
                                                Log.i("Str",customList.get(position)+".txt");

                                                Toast.makeText(getApplicationContext(), "Showing Text...", Toast.LENGTH_SHORT).show();
                                                SSHManager.getSSHinstance().executeSSHCommand("cd All/text/ && cat "+customList.get(position)+".txt");

                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Thread.sleep(800);

                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }

                                                        Log.e("Err",SSHManager.tempret);
                                                        Intent intent = new Intent(getApplicationContext(),TextDisplayer.class);
                                                        intent.putExtra("passedString",SSHManager.tempret);
                                                        startActivity(intent);

                                                    }
                                                }).start();
                                                break;
                                            }
                                            case 1:
                                            {Toast.makeText(getApplicationContext(), "Showing Image...", Toast.LENGTH_SHORT).show();
                                                /////
                                                String idf = customList.get(position);
                                                String iid ="";int i=0;
                                                while(i<idf.length()){
                                                    if(idf.charAt(i) == '.')break;
                                                    else iid += idf.charAt(i);
                                                    i++;
                                                }
                                                SSHManager.getSSHinstance().imageDownload(iid);
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        while(SSHManager.dwnlded != 0 ){
                                                            Log.i("herecont.","e\n");
                                                        }
                                                        Log.i("heredwnlded","yes!!\n");
                                                        //
                                                        File img = new File("data/data/newpackage.vab.gamma/files/temp.png");
                                                        if (!img.exists()) {
                                                            Log.i("hereimg","notfound\n");
                                                        }
                                                        else{
                                                            Log.i("hereimg","exists\n");
                                                            //Bitmap bmp = BitmapFactory.decodeFile(img.getAbsolutePath());

                                                            //
//                                                try {
//                                                    Thread.sleep(300);
//
//                                                } catch (InterruptedException e) {
//                                                    e.printStackTrace();
//                                                }
                                                            Intent intent = new Intent(getApplicationContext(),imgDisplayer.class);
                                                            //intent.putExtra("imggbitmap",bmp);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }).start();

                                                break;}
                                            case 2:
                                            {new AlertDialog.Builder(ShowItemsInList.this)
                                                    .setTitle("Delete")
                                                    .setIcon(android.R.drawable.ic_menu_delete)
                                                    .setMessage("Delete this database entry?")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {


                                                            SSHManager.getSSHinstance().executeSSHCommand("cd All/text && rm -rf "+ customList.get(position)+".txt");


                                                            SSHManager.getSSHinstance().executeSSHCommand("cd iitpatna/abhishekdixit && rm -rf "+ customList.get(position)+".txt");
                                                            SSHManager.getSSHinstance().executeSSHCommand("cd iitpatna/jimsonmathew && rm -rf "+ customList.get(position)+".txt");
                                                            SSHManager.getSSHinstance().executeSSHCommand("cd iitpatna/mayankagarwal && rm -rf "+ customList.get(position)+".txt");
                                                            SSHManager.getSSHinstance().executeSSHCommand("cd iitpatna/somanathtripathy && rm -rf "+ customList.get(position)+".txt");

                                                            String curr= customList.get(position);
                                                            SSHManager.getSSHinstance().executeSSHCommand("./delete "+curr);

                                                            String imagestr= curr+".png";


                                                            SSHManager.getSSHinstance().executeSSHCommand("cd All && cd image && rm -rf "+ imagestr);
                                                            customList.remove(position);
                                                            adpt.notifyDataSetChanged();
                                                            Toast.makeText(getApplicationContext(),"Item deleted",Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .setNegativeButton("No",null)
                                                    .show();

                                                break;}

                                        }
                                    }
                                })
                        .create()
                        .show();


                return true;
            }
        });



    }
}
