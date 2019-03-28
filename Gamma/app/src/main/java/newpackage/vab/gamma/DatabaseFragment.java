package newpackage.vab.gamma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DatabaseFragment extends ListFragment {

  ArrayList<LetterContent> datacollection= new ArrayList<LetterContent>();
  ArrayAdapter<String> adpt=null;
  DatabaseTransactionsClass dbtrans;

  ArrayList<String> templist;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    dbtrans=new DatabaseTransactionsClass(getContext());

    View raw= inflater.inflate(R.layout.fragment_database,container,false);
    ListView lst= raw.findViewById(android.R.id.list);


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
    return raw;

   }



  public static DatabaseFragment giveInstance()
  {
    DatabaseFragment dbfrg= new DatabaseFragment();
    return dbfrg;

  }

}