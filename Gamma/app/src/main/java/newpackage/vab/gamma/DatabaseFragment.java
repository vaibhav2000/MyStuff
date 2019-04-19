package newpackage.vab.gamma;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.L;

import java.util.ArrayList;
import java.util.TooManyListenersException;


public class DatabaseFragment extends ListFragment {

  ArrayAdapter<String> adpt=null;
    ArrayList<String> templist;

 private ProgressBar pgb;
 public static ListView lst;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


    View raw= inflater.inflate(R.layout.fragment_database,container,false);
    lst= raw.findViewById(android.R.id.list);


       templist= new ArrayList<>(); //for testing purpose


    adpt= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,templist)
    {
      @Override
      public View getView(int position, View convertView,ViewGroup parent) {
         View view= super.getView(position,convertView,parent);
        TextView temptext=(TextView)view.findViewById(android.R.id.text1);
        temptext.setTextColor(getResources().getColor(android.R.color.white));
         return view;
          }
    };

    lst.setAdapter(adpt);

    lst.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("Pos",""+position);
        }
    });

    lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            SSHManager.getSSHinstance().executeSSHCommand("cd All && cd text && rm -rf "+ templist.get(position));


            String temp = templist.get(position);
            String imagestr="";

            for(int i=0;temp.charAt(i)!='.';i++)
                imagestr+= temp.charAt(i);

            imagestr+= ".png";

            SSHManager.getSSHinstance().executeSSHCommand("cd All && cd image && rm -rf "+ imagestr);


            new AlertDialog.Builder(getContext())
                    .setTitle("Delete")
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .setMessage("Delete this database entry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            templist.remove(position);
                             adpt.notifyDataSetChanged();

                            Toast.makeText(getContext(),"Item deleted",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();

            return true;
        }
    });


      pgb= raw.findViewById(R.id.databaseuploadprogressID);
      pgb.setVisibility(View.INVISIBLE);



      return raw;

   }


   volatile boolean flag;
   public void updateDatabaseList()
   {

       flag=false;
       templist.clear();
       adpt.notifyDataSetChanged();

       pgb.setVisibility(View.VISIBLE);


         new Thread(new Runnable() {
              @Override
              public void run() {

                  SSHManager.getSSHinstance().executeSSHCommand("cd All && cd text && ls");
                  String curr = "";
                  if(SSHManager.tempret!=null)
                      for (int i = 0; i < SSHManager.tempret.length(); i++) {
                          if (SSHManager.tempret.charAt(i) == '\n') {
                              templist.add(curr);
                              curr = "";
                          } else
                              curr += SSHManager.tempret.charAt(i);
                      }


                     pgb.setVisibility(View.INVISIBLE);

                      lst.post(new Runnable() {
                          @Override
                          public void run() {
                            adpt.notifyDataSetChanged();
                          }
                      });
              }
          }).start();




   }


   private static DatabaseFragment onlyinstance=null;
   public static DatabaseFragment giveInstance()
  {
      if(onlyinstance==null)
           onlyinstance=new DatabaseFragment();

      return onlyinstance;

  }

}