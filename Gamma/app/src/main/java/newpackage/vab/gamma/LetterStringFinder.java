package newpackage.vab.myapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.Vector;
import java.lang.*;

class LetterStringFinder
{
  String mainText,str;
  int fpos; // position of from-identifier
  int apos; // position of addressing to someone
  int dpos; //position of date
  int NOT_FOUND = -1; // if algo is not able to find fpos,dpos,apos.
  Vector<String> ps;
  
  public LetterStringFinder(String s){
    mainText=s;
    str = s.toLowerCase();
    str += '\n';
    //
    ps = new Vector<>();
        ps.add("asif ekbal");
        ps.add("suman kumar maji");
        ps.add("ashok singh sairam");
        ps.add("jimson mathew");
    //
    //findApos();
  }
  
  // finding names-list
  private int getNxtLineInd(int k){
    while(str.charAt(k) != '\n'){
      k++;
    }
    k++;
    return k;
  }
  private String getLine(int k){
    String tmp="";
    while(str.charAt(k) != '\n'){
      tmp+=str.charAt(k);k++;
    }
    return tmp;
  }

  public Vector<String> getNamesList(){
    Vector<String> ls = new Vector<>();
    int k=0;
    while(k<str.length()){
      String tmp = getLine(k);
      for(String x : ps){
        if(tmp.contains(x))ls.add(x);
      }
      k = getNxtLineInd(k);
    }
    return ls;
  }
  

  // finding to-name --------->

  String Apos[]={
    "dear ","respected ","honorable ","hon'ble ","hi "
  };

  public String getLine(int i,String sx){
    String res="";
    while(sx.charAt(i)==((char)32) || sx.charAt(i)=='\t')i++;
    while(i<sx.length()){
      if(sx.charAt(i)=='\n')break;
      res+=sx.charAt(i);i++;
    }
    return res;
  }

  public String backTrim(String sx){
    int i = sx.length();i--;
    String res="";
    while(i>0){
      if(sx.charAt(i)=='\t' || sx.charAt(i)==',' || sx.charAt(i)==(char)32){
        i--;
      }
      else break;
    }
    while(i>=0){
      res = sx.charAt(i)+res;i--;
    }
    return res;
  }

  public void findApos(){

    String alt="",ss = mainText.toLowerCase();
    int i=0;

    while(i < ss.length()){

      alt = getLine(i,ss);
      for(String AposX : Apos){
        if(alt.startsWith(AposX)){
          apos = i;return;
        }
      }

      while(i<ss.length()){
        if(ss.charAt(i)=='\n') break;
        i++;
      }

      i++;
    }

    apos = NOT_FOUND;

  }

  public String getToname(){
    String ss = mainText.toLowerCase();

    int i;
    String alt="";

    if(apos!=NOT_FOUND){
      i=apos;
      while(i<ss.length()){
        if(ss.charAt(i)=='\n')break;
        i++;
      }
      i--;
      while(i>apos){
        if(ss.charAt(i)==',' || ss.charAt(i)==(char)32 ||ss.charAt(i)=='\t')i--;
        else break;
      }
      while(i>apos){
        if(ss.charAt(i)==' ' || ss.charAt(i)=='\t')break;
        alt = ss.charAt(i)+alt;i--;
      }

      i = 0; //
      while(i<apos){
        String tmp = getLine(i,ss);
        tmp = backTrim(tmp);
        if(tmp.endsWith(alt)) return tmp;
        while(i<ss.length()){
          if(ss.charAt(i)=='\n')break;
          i++;
        }
        i++;
      }

    }
    else{
      return "null";
    }

    String ultStr="";

    ultStr+= Character.toUpperCase(alt.charAt(0));
    for(int j=1;j<alt.length();j++)
      if(alt.charAt(j-1)==' ')
        ultStr+= Character.toUpperCase(alt.charAt(j));
      else
        ultStr+= alt.charAt(j);
    return ultStr;

  }

  // finding from-Name ----------->

  String Fpos[] = {
    "yours","with regards","sincerely","cheers"
  };
      
  public String getFromname(){
    String ss = mainText.toLowerCase();
    String res = "";
        
    int l=ss.length();l-=6;
    if(l<0) return "null";
    while(l>=0){
      l=getToFirstInd(l,ss);
      if(checkAt(l,ss)){
        int x = l;
        while(ss.charAt(x)!='\n')x++;
        //1
        while(ss.charAt(x)=='\n')x++;
        while(ss.charAt(x)!='\n'){
          res+=ss.charAt(x);x++;
        }
        break;
      }
      l--;
    }        
    if(res=="")
      return "null";
    String ultBuild="";
    ultBuild+= Character.toUpperCase(res.charAt(0));
  
    for(int i=1;i<res.length();i++)
      if(res.charAt(i-1)==' ')
        ultBuild+= Character.toUpperCase(res.charAt(i));
      else
        ultBuild+= res.charAt(i);
  
    return ultBuild; 
              
  } 

  public int getToFirstInd(int i,String sx){
    i--;
    while(i>=0){
      if(sx.charAt(i)=='\n') break;
      i--;
    }
    return i+1;
  }
          
  public boolean checkAt(int i,String sx){

    while(sx.charAt(i)=='\t' || sx.charAt(i)==(char)32) i++;

    String alt = "";
    while(i<sx.length()){
      if(sx.charAt(i)=='\n')break;
      alt+=sx.charAt(i);
      i++;
    }
    i=0;
    while(i<Fpos.length){
      if(alt.startsWith(Fpos[i])){
        fpos = i;
        return true;
      }
      i++;
    }

    return false;

  }
  
  // finding Date ------------>

  String str;
  int D,M,Y,n,l;

  String mnths[]={
      "jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec",
      "january","february","march","april","may","june","july","august","september","october",
      "november","december"
  };

  public int getI(char ch){
    switch(ch){
      case '0':return 0;
      case '1':return 1;
      case '2':return 2;
      case '3':return 3;
      case '4':return 4;
      case '5':return 5;
      case '6':return 6;
      case '7':return 7;
      case '8':return 8;
      case '9':return 9;
    }
    return -1;
  }

  public int getTI(int k){
    n=0;
    if(getI(str.charAt(k))>=0){
      n=getI(str.charAt(k));
      if(getI(str.charAt(k+1))>=0){
        n*=10;
        n+=getI(str.charAt(k+1));
      }
      else{
        return 1;
      }
    }
    else return 0;
    return 2;
  }

  public int ignoreXX(int k){
    if(k>=l)return 0;
    int i=0;
    while(k+i<l){
      if(str.charAt(k+i)!=',' && str.charAt(k+i)!=(char)32)break;
      i++;
    }
    if(k+i>=l)return i;
    if(str.charAt(k+i)=='\n')return l;
    return i;
  }

  public boolean checkYY(int k){
    if(getTI(k)<2)return false;
    Y=n;
    k+=2;
    if(getTI(k)<2)return false;
    Y=(Y*100)+n;
    return true;
  }

  public boolean checkMM(int z, int k){

    int ml = mnths[z+12].length();int i=0;
    if(k+ml <= l){
      while(i<ml){
        if(str.charAt(k+i) == mnths[z+12].charAt(i))i++;
        else break;
      }
      if(i==ml){
        n=ml;
        return true;
      }
    }

    if(k+2>=l)return false;
    if(str.charAt(k) == mnths[z].charAt(0) &&
        str.charAt(k+1) == mnths[z].charAt(1)  &&
        str.charAt(k+2) == mnths[z].charAt(2)){
      n=3;return true;
    }

    return false;
  }

  public boolean checkDD(int k){
    int z = k+getTI(k);
    if(z==k)return false;
    k=z;D=n;
    if(D>31)return false;
    if(k+1>=l)return false;
    if(((D%10 == 1) && str.charAt(k)=='s' && str.charAt(k+1)=='t') ||
        ((D%10 == 2) && str.charAt(k) == 'n' && str.charAt(k+1) == 'd') ||
        ((D%10 == 3) && str.charAt(k) == 'r' && str.charAt(k+1) == 'd') ||
        ((D%10 > 3) && str.charAt(k) == 't' && str.charAt(k+1) == 'h') ){
      k+=2;
    }
    n=k;
    return true;
  }

  // for DD/MM/YYYY
  public boolean isSlash(int k){
    if(k+1>=l)return false;
    int z = k+getTI(k);
    if(z>k){
      if(n>31)return false;
      D=n;k=z;
      if(k>=l)return false;
      if(str.charAt(k) == '/'){
        k++;
        if(k+1>=l)return false;
        z=k+getTI(k);
        if(z>k){
          if(n>12)return false;
          M=n;k=z;
          if(k>=l)return false;
          if(str.charAt(k) == '/'){
            k++;
            if(k+3>=l)return false;
            return checkYY(k);
          }else return false;
        }else return false;
      }else return false;
    }else return false;
  }

  // for DD mnth, year
  public boolean isType1(int k){

    //day
    if(!checkDD(k)) return false;
    k=n;k+=ignoreXX(k);
    if(k+1>=l) return false;
    if(str.charAt(k)=='o' && str.charAt(k+1)=='f')k+=2;
    k+=ignoreXX(k);
    if(k>=l) return false;

    //month
    int z = 0;
    while(z<12){
      if(checkMM(z,k)){
        k+=n;
        M=z+1;break;
      }
      z++;
    }
    if(z==12) return false;
    k+=ignoreXX(k);
    if(k+3 >= l) return false;

    //year
    if(!checkYY(k)) return false;

    return true;
  }

  // for mnth DD, YYYY
  public boolean isType2(int k){
    int z=0;

    //month
    while(z<12){
      if(checkMM(z, k)){
        k+=n;
        M=z+1;break;
      }
      z++;
    }
    if(z==12) return false;
    k+=ignoreXX(k);
    if(k>=l) return false;

    //day
    if(!checkDD(k)) return false;
    k=n;k+=ignoreXX(k);
    if(k+1>=l) return false;
    if(str.charAt(k)=='o' && str.charAt(k+1)=='f')k+=2;
    k+=ignoreXX(k);
    if(k>=l) return false;

    //year
    if(!checkYY(k)) return false;

    return true;
  }

  public boolean isDate(int k){
    if(isType1(k))return true;
    if(isType2(k))return true;
    if(isSlash(k))return true;
    return false;
  }

  public String getDate(){
    String date="";
    str = mainText.toLowerCase();
    l = str.length();
    int i =0 ;
    while(i<l){
      if(isDate(i))break;
      i++;
    }
    if(i==l)return "null";
    date+=(D/10);date+=(D%10);
    date+=(M/10);date+=(M%10);
    date+=Y;

    if(date.length()<8)
       return "null";


    String extDate="";
    extDate+= date.substring(0,2);
    extDate+='/';
    extDate+= date.substring(2,4);
    extDate+='/';
    extDate+= date.substring(4,8);


    return extDate;
  }

boolean isAlpha(char c)
{
     if(c<='z'&&c>='a')
        return true;

     return false;
}

  String getThroughName()
  {
     String copyText= mainText.toLowerCase();
     String res="";

     int t=0;
    {
      while (t < copyText.length()-10) {
        String line = getLine(t, copyText);
        if (line.startsWith("through")) {
          String customstr="";

           int index=t+7;

           while(!isAlpha(copyText.charAt(index)))
              index++;


           while (copyText.charAt(index)!='\n')
           {customstr+= copyText.charAt(index);
             index++;}

          String ult="";
          ult+= Character.toUpperCase(customstr.charAt(0));

          for(int i=1;i<customstr.length();i++)
             if(customstr.charAt(i-1)==' ')
                ult+= Character.toUpperCase(customstr.charAt(i));
             else
               ult+= customstr.charAt(i);


          return ult;
        }

        while (copyText.charAt(t) != '\n')
          t++;
        t++;
      }
    }
    return "null";
  }


}
