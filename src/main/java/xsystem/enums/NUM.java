package xsystem.enums;

import java.util.ArrayList;
import java.util.HashMap;

import xsystem.support.Config;

public class NUM implements CharClass {
    
	public String representation(){
        return "\\d";
    }

    public XClass toXClass(){
        return new X_NUM();
    }

    public ArrayList<Character> domain(){
        char[] numerics = Config.numbers;
        ArrayList<Character> charlst = new ArrayList<Character>();
        for(char ch: numerics){
            charlst.add(ch);
        }
        return charlst;
    }

    public Boolean isClass(){
        return true;
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