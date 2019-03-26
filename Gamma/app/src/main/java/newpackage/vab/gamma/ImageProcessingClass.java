package newpackage.vab.gamma;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.TtsSpan;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

public class ImageProcessingClass {

  Context currentContext;

  ImageProcessingClass(Context passedContext) {
    currentContext = passedContext;
  }

    Bitmap doImagePreProcessing(Bitmap bitmap) {
    GPUImage gpuImage = new GPUImage(currentContext);
    gpuImage.setFilter(new GPUImageGrayscaleFilter());
    bitmap = gpuImage.getBitmapWithFilterApplied(bitmap);

    gpuImage.setFilter(new GPUImageBrightnessFilter((float) 0.01));
    bitmap = gpuImage.getBitmapWithFilterApplied(bitmap);

    return bitmap;
  }

  String extractTextwithOCR(Bitmap bitmap,TextRecognizer textRecognizer)
  {

    Frame customframe = new Frame.Builder().setBitmap(bitmap).build();
    SparseArray<TextBlock> obtainedTextBlocks = textRecognizer.detect(customframe);
    List<Text> textLines = new ArrayList<>();

    for (int i = 0; i < obtainedTextBlocks.size(); i++) {
      TextBlock currentBlock = obtainedTextBlocks.valueAt(i);
      List<? extends Text> finalist = currentBlock.getComponents();

      for (int t = 0; t < finalist.size(); t++)
        textLines.add(finalist.get(t));
    }

    Collections.sort(textLines, new Comparator<Text>() {
      @Override
      public int compare(Text t1, Text t2) {
        int diffOfTops = t1.getBoundingBox().top - t2.getBoundingBox().top;
        int diffOfLefts = t1.getBoundingBox().left - t2.getBoundingBox().left;

        if (diffOfTops != 0) {
          return diffOfTops;
        }
        return diffOfLefts;
      }
    });

    final StringBuilder textBuilder = new StringBuilder();
    for (int i = 0; i < textLines.size(); i++) {
      Text text = textLines.get(i);
      if (text != null && text.getValue() != null) {
        textBuilder.append(text.getValue() + "\n");
      }
    }
    return textBuilder.toString();
  }
}