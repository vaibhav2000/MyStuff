package newpackage.vab.gamma;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.L;

import java.util.ArrayList;
import java.util.TooManyListenersException;


public class DatabaseFragment extends ListFragment {

  ArrayAdapter<String> adpt=null;
    ArrayList<String> templist;



  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


    View raw= inflater.inflate(R.layout.fragment_database,container,false);
    final ListView lst= raw.findViewById(android.R.id.list);


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

    lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            SSHManager.getSSHinstance().executeSSHCommand("cd All && cd text && rm -rf "+ templist.get(position));

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

            return false;
        }
    });


      return raw;

   }




   public void updateDatabaseList()
   {

       templist.clear();
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

       adpt.notifyDataSetChanged();
   }


   private static DatabaseFragment onlyinstance=null;
   public static DatabaseFragment giveInstance()
  {
      if(onlyinstance==null)
           onlyinstance=new DatabaseFragment();

      return onlyinstance;

  }

}