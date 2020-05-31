package xsystem.ng.Enums;

import java.util.ArrayList;
import java.util.HashMap;

import xsystem.ng.Support.Config;

public class UCC implements CharClass {

	public String rep(){
        return "\\W";
    }

    public XClass toXClass(){
        return new X_UCC();
    }

    public ArrayList<Character> domain(){
        char[] upperCaseChar = new Config().upperCaseChar;
        ArrayList<Character> charlst = new ArrayList<Character>();
        for(char ch: upperCaseChar){
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