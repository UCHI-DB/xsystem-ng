package xsystem.enums;

import java.util.ArrayList;
import java.util.HashMap;

import xsystem.support.Config;

/** Represents Special Characters in string.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class SPC implements CharClass {
    String str;
    String realStr;

    public SPC(String str) {
        this.str = str;
        if(new String(Config.upperCaseChar).contains(str))
            this.realStr = str;
        else
            this.realStr = "*";
    }
    
    public String representation(){
        return realStr;
    }

    public XClass toXClass(){
        return new X_SPEC(realStr);
    }

    public ArrayList<Character> domain(){
        char[] splchar = realStr.toCharArray();
        ArrayList<Character> charlst = new ArrayList<Character>();
        charlst.add(splchar[0]);
        return charlst;
    }

    public Boolean isClass(){
        return false;
    }

    public HashMap<Character, Double> baseHist(){
        ArrayList<Character> chars = domain();
        HashMap<Character, Double> history = new HashMap<Character, Double>();
        chars.forEach(
            (c) -> history.put(c, 0.0)
        );
        return history;
    }
}