package xsystem.enums;

import java.util.ArrayList;
import java.util.Random;

import xsystem.support.Config;

/** Represents UpperCase Character in a XStructure.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class X_UCC implements XClass {

	public String representation(){
        return "\\W";
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

    public String randomRep(){
        ArrayList<Character> chars = domain();
        int random = new Random().nextInt(chars.size());
        String randomChar = Character.toString(chars.get(random));
        return randomChar;
    }

    public ArrayList<String> lshDomain(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(representation());
        return list;
    }
    public String toString(){
        return representation();
    }

}