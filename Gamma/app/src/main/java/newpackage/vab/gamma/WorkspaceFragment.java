package newpackage.vab.gamma;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WorkspaceFragment extends Fragment {

  private ImageButton camerabutton;
  private ImageButton gallerybutton;
  private ImageButton docsbutton;
  private ImageButton searchbutton;

  private ImageProcessingClass imageprocessor;
  private String extractedText=null;

  private static int  TAKE_PICTURE_CODE= 4324;
  private static int  GALLERY_INTENT=2654;
  private static int  PDF_REQUEST=5345;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View raw= inflater.inflate(R.layout.fragment_workspace,container,false);

   //manipulate this view here
   camerabutton= (ImageButton)raw.findViewById(R.id.camerabutton);
   gallerybutton=(ImageButton)raw.findViewById(R.id.gallerybutton);
   docsbutton= (ImageButton)raw.findViewById(R.id.docsbutton);
   searchbutton=(ImageButton)raw.findViewById(R.id.seachbutton);

   camerabutton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
       //write Camera path here Vab, it's a drag
       //i haven't programmed that path yet lol
       Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // Ensure that there's a camera activity to handle the intent
       if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
         // Create the File where the photo should go
         File photoFile = null;
         try {
           photoFile = createImageFile();
         } catch (IOException ex) {
           // Error occurred while creating the File
           ex.printStackTrace();
         }
         // Continue only if the File was successfully created
         if (photoFile != null) {
           Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
               "newpackage.vab.gamma",
               photoFile);
           takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
           startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE);
         }
       }


     }
   });

   gallerybutton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
       Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       intent.setType("image/*");
       startActivityForResult(intent,GALLERY_INTENT);
     }
   });

    docsbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent,PDF_REQUEST);
      }
    });

   return raw;
  }


  String currentPhotoPath=null;
  Bitmap bitmap;
  Boolean processing;
  String threadString;
  private Uri BitmapUri;

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

     bitmap=null;
     threadString="";

    if(resultCode!=getActivity().RESULT_OK)
       return;

    if (requestCode == GALLERY_INTENT&& data != null && data.getData() != null) {

      try {
        BitmapUri =data.getData();
        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),BitmapUri);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if(requestCode==TAKE_PICTURE_CODE)
    {
      File f = new File(currentPhotoPath);
      BitmapUri = Uri.fromFile(f);
      try {

        bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),BitmapUri);


      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if(requestCode==PDF_REQUEST)
    {
       //do the pdf bitmap stuff here, son. I'm not gonna do this one(I'll even remove that button later)

    }

    if(bitmap!=null)
    {
      AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getContext());
      dialogbuilder.setTitle("Processing");
      dialogbuilder.setMessage("Initiating");

      final AlertDialog realDialogue= dialogbuilder.create();
      realDialogue.setCanceledOnTouchOutside(false);
      realDialogue.show();

      imageprocessor= new ImageProcessingClass(getContext());
      processing= true;

      new Thread(new Runnable() {
        @Override
        public void run() {
          while(processing){
           threadString+= ".";
           realDialogue.setMessage("Applying Filters & OCR"+ threadString);

          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          if(threadString.length()>5)
             threadString="";
        }}
      }).start();

      new Thread(new Runnable() {
        @Override
        public void run() {

          bitmap= imageprocessor.doImagePreProcessing(bitmap);
          TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
          if (!textRecognizer.isOperational()) {
            Toast.makeText(getContext(), "Text Recogniser Not Ready", Toast.LENGTH_SHORT).show();
            return;
          }

          extractedText = imageprocessor.extractTextwithOCR(bitmap,textRecognizer);

          processing=false;
          Log.i("Extract",extractedText);

          Intent intent = new Intent(getContext(),LetterDisplay.class);
          intent.putExtra("EXTRACTED_TEXT",extractedText);
          intent.putExtra("BITMAP_URI",BitmapUri);
          startActivity(intent);

          realDialogue.dismiss();

          }
      }).start();

    }


  }


  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    currentPhotoPath = image.getAbsolutePath();
    return image;
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