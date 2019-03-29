package newpackage.vab.gamma;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TooManyListenersException;


public class DatabaseFragment extends ListFragment {

  ArrayList<LetterContent> datacollection= new ArrayList<LetterContent>();
  ArrayAdapter<String> adpt=null;
  DatabaseTransactionsClass dbtrans;
    ArrayList<String> templist;

 final static DatabaseFragment dbfragobj= new DatabaseFragment();


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    dbtrans=new DatabaseTransactionsClass(getContext());

    View raw= inflater.inflate(R.layout.fragment_database,container,false);
    final ListView lst= raw.findViewById(android.R.id.list);

    datacollection=dbtrans.givedataList();

       templist= new ArrayList<>(); //for testing purpose

    for(int i=0;i<datacollection.size();i++)
       templist.add(datacollection.get(i).bitmapStr);



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


   public void updateDatabaseList(LetterContent obj)
   {

       templist.add(obj.bitmapStr.toString());
       adpt.notifyDataSetChanged();

   }



  public static DatabaseFragment giveInstance()
  {
   return dbfragobj;

  }

}