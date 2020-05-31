package xsystem.ng.Enums;

import java.util.ArrayList;
import java.util.HashMap;

import xsystem.ng.Support.Config;

public class SPC implements CharClass {
    String str;
    String realStr;
    {
        Config config = new Config();
        if(new String(config.upperCaseChar).contains(str))
            realStr = str;
        else
            realStr = "*";
    }

    public SPC(String str) {
		this.str = str;
    }
    
    public String rep(){
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