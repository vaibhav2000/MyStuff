package newpackage.vab.gamma;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WorkspaceFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View raw= inflater.inflate(R.layout.fragment_workspace,container,false);

   //manipulate this view here
    


   return raw;
     }

   interface MessageSender {
     public void currentMessage(String from,String to,String date,String sub,String image);
   }

   MessageSender interObject;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      interObject = (MessageSender) activity;
    }catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  public static WorkspaceFragment getInstance()
   {
     return new WorkspaceFragment();
   }


}