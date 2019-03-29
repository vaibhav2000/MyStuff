package newpackage.vab.gamma;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements WorkspaceFragment.MessageSender{

  private ViewPager viewpager= null;

  private static int COLLECTIVE_PERMISSIONS=32423;


  Button workbutton;
  Button databutton;
  Button addData;

  TextView workbottomtext;
  TextView databottomtext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    workbutton=(Button)findViewById(R.id.workspace);
    databutton=(Button)findViewById(R.id.database);
    addData=(Button)findViewByTd(R.id.addData);
    addDataListener();
    workbottomtext=(TextView)findViewById(R.id.belowworkspace);
    databottomtext=(TextView)findViewById(R.id.belowdatabase);
    
    doInitialCustomlayout();
    AskPermissions();

    CustomAdapter pagerAdapter= new CustomAdapter(getSupportFragmentManager());
    viewpager= (ViewPager)findViewById(R.id.pager);
    viewpager.setAdapter(pagerAdapter);

    workbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
         viewpager.setCurrentItem(0);

        workbutton.setTextColor(getResources().getColor(android.R.color.white));
        workbottomtext.setBackgroundColor(getResources().getColor(android.R.color.white));
        databutton.setTextColor(getResources().getColor(R.color.palewhite));
        databottomtext.setBackgroundColor(getResources().getColor(R.color.palewhite));


      }
    });

    databutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        viewpager.setCurrentItem(1);

        databutton.setTextColor(getResources().getColor(android.R.color.white));
        databottomtext.setBackgroundColor(getResources().getColor(android.R.color.white));
        workbutton.setTextColor(getResources().getColor(R.color.palewhite));
        workbottomtext.setBackgroundColor(getResources().getColor(R.color.palewhite));



      }
    });

    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i1) {

      }

      @Override
      public void onPageSelected(int i) {
        switch (i)
        {
          case 0:  {  workbutton.setTextColor(getResources().getColor(android.R.color.white));
            workbottomtext.setBackgroundColor(getResources().getColor(android.R.color.white));
            databutton.setTextColor(getResources().getColor(R.color.palewhite));
            databottomtext.setBackgroundColor(getResources().getColor(R.color.palewhite));

            break;}
          case 1:  {
            databutton.setTextColor(getResources().getColor(android.R.color.white));
            databottomtext.setBackgroundColor(getResources().getColor(android.R.color.white));
            workbutton.setTextColor(getResources().getColor(R.color.palewhite));
            workbottomtext.setBackgroundColor(getResources().getColor(R.color.palewhite));

            break;}
        }

      }

      @Override
      public void onPageScrollStateChanged(int i) {

      }
    });
  }
  
    public void addDataListener(){
    addData.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(ctx);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInputName = (EditText) promptsView
                .findViewById(R.id.name);
        final EditText userInputPosts = (EditText) promptsView
                .findViewById(R.id.posts);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,int id) {
                            // get user input and set it to result
                            // edit text
                            pname = userInputName.getText().toString();
                            posts = userInputPosts.getText().toString();
                            addToDataBase();
                          }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                          }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

      }
    });
  }

  private void AskPermissions() {

    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE
          }, COLLECTIVE_PERMISSIONS);
    }



  }

  private void doInitialCustomlayout()
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getSupportActionBar().setElevation(0);

      workbutton.setBackgroundColor(getResources().getColor(R.color.tabcolor));
      databutton.setBackgroundColor(getResources().getColor(R.color.tabcolor));

      ActionBar bar = getSupportActionBar();
      bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
      bar.setTitle(Html.fromHtml("<font color=\"#ffffff\">" + "Gamma" + "</font>"));

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#000000"));
      }


    workbutton.setTextColor(getResources().getColor(android.R.color.white));
    workbottomtext.setBackgroundColor(getResources().getColor(android.R.color.white));
    databutton.setTextColor(getResources().getColor(R.color.palewhite));
    databottomtext.setBackgroundColor(getResources().getColor(R.color.palewhite));



    }

  }

  @Override
  public void currentMessage(String from, String to, String date, String sub, String image) {
       //do the database stuff here son
  }


  private class CustomAdapter extends FragmentPagerAdapter
  {
    public CustomAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {

      switch(i)
      {
        case 0:  return WorkspaceFragment.getInstance();

        case 1:   return DatabaseFragment.giveInstance();

      }

      return null;
    }

    @Override
    public int getCount() {
      return 2;
    }
  }


}
