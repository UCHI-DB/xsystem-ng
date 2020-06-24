package xsystem.enums;

import java.util.ArrayList;
import java.util.Random;

public class X_SPEC implements XClass {
    String str;

    public X_SPEC(String str) {
		this.str = str;
	}

	public String representation(){
        return str.toString();
    }

    public ArrayList<Character> domain(){
        char[] splchar = str.toCharArray();
        ArrayList<Character> charlst = new ArrayList<Character>();
        charlst.add(splchar[0]);
        return charlst;
    }

    public Boolean isClass(){
        return false;
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