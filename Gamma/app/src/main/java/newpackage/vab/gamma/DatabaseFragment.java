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

import java.util.ArrayList;


public class DatabaseFragment extends ListFragment {

  ArrayList<String> datacollection= new ArrayList<>();
  ArrayAdapter<String> adpt=null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View raw= inflater.inflate(R.layout.fragment_database,container,false);

    ListView lst= raw.findViewById(android.R.id.list);

    datacollection.add(getArguments().getString("FROM"));
    datacollection.add(getArguments().getString("TO"));

    adpt= new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,datacollection);
    lst.setAdapter(adpt);

    return raw;

   }

  public static DatabaseFragment giveInstance(String from,String to,String date,String subject,String image)
  {
    DatabaseFragment dbfrg= new DatabaseFragment();

    Bundle bundle = new Bundle();
    bundle.putString("FROM",from);
    bundle.putString("TO",to);
    bundle.putString("DATE",date);
    bundle.putString("SUBJECT",subject);
    bundle.putString("IMAGE",image);

    dbfrg.setArguments(bundle);
    return dbfrg;

  }

}