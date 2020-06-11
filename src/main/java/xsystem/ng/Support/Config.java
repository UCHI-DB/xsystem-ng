package xsystem.ng.Support;

import java.util.ArrayList;

public class Config {
    public static int maxBranches = 7;
    public static double branchingSeed = 0.1;
    public static char[] splChars;
    public static char[] upperCaseChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static char[] lowerCaseChar = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static char[] numbers = "0123456789".toCharArray();
    public static int inc = 100;
    public static Boolean tts = true;
    public static Double capturePct = 0.8;
    

    public Config(){
        Config.splChars = getsplchars();
    }
    
    private char[] getsplchars(){

        if(splChars != null)
            return splChars;

        ArrayList<Character> r = new ArrayList<Character>();
        for(int i = 0; i<=47; i++){
            char c = (char)i;
            r.add(c);
        }
        for(int i = 58; i<=64; i++){
            char c = (char)i;
            r.add(c);
        }
        for(int i = 91; i<=96; i++){
            char c = (char)i;
            r.add(c);
        }
        for(int i = 123; i<=255; i++){
            char c = (char)i;
            r.add(c);
        }
        char[] res = new char[r.size()];
        for(int i = 0; i<r.size(); i++){
            res[i] = r.get(i).charValue();
        }
        return res;

    }

    public static double neededSampleSize(double std){
        return Math.pow(1.96*std/0.5, 2.0);
    }

}
