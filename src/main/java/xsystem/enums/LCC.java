package xsystem.enums;

import java.util.ArrayList;
import java.util.HashMap;

import xsystem.support.Config;

/** Represents LowerCase Characters in a string.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class LCC implements CharClass {
    
	public String representation(){
        return "\\w";
    }

    public XClass toXClass(){
        return new X_LCC();
    }

    public ArrayList<Character> domain(){
        char[] lowerCaseChar = Config.lowerCaseChar;
        ArrayList<Character> charlst = new ArrayList<Character>();
        for(char ch: lowerCaseChar){
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