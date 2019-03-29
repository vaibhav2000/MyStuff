package newpackage.vab.gamma;

    /*
    *   three public methods
    *
    *   1. Vector<String> getNamesList(); returns the vector of names of persons mentioned.
    *   2. String getSubject(); returns the subject of letter if mentioned clearly.
    *   3. String getDate(); returns the date(DD_MM_YYYY) mentioned in letter.
    *
    */

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

public class LetterStringFinder {

    private String mainStr;     // main string
    private String copiedStr;   // copy of main string with all letter in small.

    public LetterStringFinder(String str, Context context){
        //
        mainStr = str;
        mainStr += '\n';
        //
        copiedStr = mainStr.toLowerCase();
        DatabaseTransactionsClass db = new DatabaseTransactionsClass(context);
        Log.i("lsf","here\n");
        people = db.getPersons();

        Log.i("lsf",people.get(0).getName());
    }

    //

    private int getNxtLineInd(int k , String str){
        while(str.charAt(k) != '\n'){
            k++;
        }
        k++;
        return k;
    }
    private String getLine(int k , String str){
        String tmp="";
        while(str.charAt(k) != '\n'){
            tmp+=str.charAt(k);k++;
        }
        return tmp;
    }


    // finding names-list--------->

    private ArrayList<Person> people;

    public Vector<String> getNamesList(){
        Vector<String> ls = new Vector<>();
        for (Person p : people){
            String x = p.getName();
            int t = 0;
            while(t<copiedStr.length()){
                String tmp = getLine(t,copiedStr);
                if(tmp.contains(x.toLowerCase())){
                    ls.add(x);break;
                }
                boolean ps = false;
                ArrayList<String> posts = p.getPosts();
                for(String pst : posts){
                    if(tmp.contains(pst.toLowerCase())){ls.add(x);ps=true;break;}
                }
                if(ps)break;
                t = getNxtLineInd(t,copiedStr);
            }
        }

        return ls;
    }


    // finding subject ----------->

    private boolean isXtra(char ch){
        if(ch == ':')return true;
        if(ch == '-')return true;
        if(ch == (char)32)return true;
        if(ch == '.')return true;
        else return false;
    }

    private String trimForSub(int k){

        while(copiedStr.charAt(k) != 't')k++;
        k++;
        while(!isXtra(copiedStr.charAt(k))) k++;
        return getLine(k,mainStr);
    }

    public String getSubject(){
        String sub="";
        int k = 0;
        while(k<copiedStr.length()){
            sub = getLine(k,copiedStr);
            if(sub.startsWith("subject"))break;
            k = getNxtLineInd(k,copiedStr);
        }

        if(k>=copiedStr.length()) return "null";

        sub = trimForSub(k);

        return sub;
    }


    //finding Date ---------->

    private int D,M,Y; // for final day/month/year
    private int n,l;   //  l = length of string. n = for internal use.
    private String str;// for internal use and is equal to copiedStr.

    private String mnths[]={
            "jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec",
            "january","february","march","april","may","june","july","august","september","october",
            "november","december"
    };

    private int getI(char ch){
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

    // return : the length of decimal form of int . max=2.
    private int getTI(int k){
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

    // return : min. int, such that (k+int)th char is something which should not be ignored.
    private int ignoreXX(int k){
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

    // check if year int has valid length.
    private boolean checkYY(int k){
        if(getTI(k)<2)return false;
        Y=n;
        k+=2;
        if(getTI(k)<2)return false;
        Y=(Y*100)+n;
        return true;
    }

    // check month
    private boolean checkMM(int z, int k){

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

    // check day
    private boolean checkDD(int k){
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

    // for DD(/or-)MM(/or-)YYYY
    private boolean isSlash(int k){
        if(k+1>=l)return false;
        int z = k+getTI(k);
        if(z>k){
            if(n>31)return false;
            D=n;k=z;
            if(k>=l)return false;
            if(str.charAt(k)=='/' || str.charAt(k)=='-'){
                k++;
                if(k+1>=l)return false;
                z=k+getTI(k);
                if(z>k){
                    if(n>12)return false;
                    M=n;k=z;
                    if(k>=l)return false;
                    if(str.charAt(k)=='/' || str.charAt(k)=='-'){
                        k++;
                        if(k+3>=l)return false;
                        return checkYY(k);
                    }else return false;
                }else return false;
            }else return false;
        }else return false;
    }

    // for DD mnth, year
    private boolean isType1(int k){

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
    private boolean isType2(int k){
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

    private boolean isDate(int k){
        if(isType1(k))return true;
        if(isType2(k))return true;
        if(isSlash(k))return true;
        return false;
    }

    public String getDate(){
        String date="";
        str = copiedStr;
        l = str.length();
        int i =0 ;
        while(i<l){
            if(isDate(i))break;
            i++;
        }
        if(i==l)return "null";
        date+=(D/10);date+=(D%10);date+="/";
        date+=(M/10);date+=(M%10);date+="/";
        date+=Y;

        if(date.length()<8)
            return "null";

        return date;
    }

    // end -->

}
