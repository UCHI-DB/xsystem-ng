package xsystem.ng.Support;

import java.util.ArrayList;

public class Config {
    public int maxBranches = 7;
    public double branchingSeed = 0.1;
    
    private char[] getsplchars(){
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

    public char[] splChars = getsplchars();
    public char[] upperCaseChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public char[] lowerCaseChar = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public char[] numbers = "0123456789".toCharArray();
    public int inc = 100;
    public Boolean tts = true;
    public Double capturePct = 0.8;

}