package xsystem.enums;

import java.util.ArrayList;
import java.util.HashMap;

import xsystem.support.Config;

/** Represents UpperCase Characters in string.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class UCC implements CharClass {

	public String representation(){
        return "\\W";
    }

    public XClass toXClass(){
        return new X_UCC();
    }

    public ArrayList<Character> domain(){
        char[] upperCaseChar = Config.upperCaseChar;
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