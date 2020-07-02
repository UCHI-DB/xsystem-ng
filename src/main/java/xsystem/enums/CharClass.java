package xsystem.enums;

import java.util.ArrayList;
import java.util.HashMap;

/** Represents the class of character in string.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public interface CharClass {
    /**
   * @return the representation of character.
   */
    public String representation();

    /**
   * @return the class of character in XStructures.
   */
    public XClass toXClass();

    /**
   * @return the domain of a specific Character Class.
   */
    public ArrayList<Character> domain();

    /**
   * @return false if special characters, true otherwise.
   */
    public Boolean isClass();
    
    /**
   * @return basic history of learning character by character.
   */
    public HashMap<Character, Double> baseHist();
    
}
