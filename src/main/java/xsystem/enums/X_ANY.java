package xsystem.enums;

import java.util.ArrayList;
import java.util.Random;

import xsystem.support.Config;

public class X_ANY implements XClass {

	public String representation(){
        return "*";
    }

    public ArrayList<Character> domain(){
        String any = String.valueOf(Config.upperCaseChar) + String.valueOf(Config.lowerCaseChar)
                + String.valueOf(Config.numbers);
        char[] anyType = any.toCharArray();
        ArrayList<Character> charlst = new ArrayList<Character>();
        for(char ch: anyType){
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