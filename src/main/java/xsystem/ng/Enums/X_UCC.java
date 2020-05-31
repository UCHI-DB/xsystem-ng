package xsystem.ng.Enums;

import java.util.ArrayList;
import java.util.Random;

import xsystem.ng.Support.*;

public class X_UCC implements XClass {

	public String rep(){
        return "\\W";
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

    public String randomRep(){
        ArrayList<Character> chars = domain();
        int random = new Random().nextInt(chars.size());
        String randomChar = Character.toString(chars.get(random));
        return randomChar;
    }

    public ArrayList<String> lshDomain(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(rep());
        return list;
    }
    public String toString(){
        return rep();
    }

}