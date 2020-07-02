package xsystem.enums;

import java.util.ArrayList;

/** Represents the class of character in XStructures.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public interface XClass {
    /**
   * @return the representation of character in XStructures.
   */
    public String representation();

    /**
   * @return the domain of a specific XStructure Class.
   */
    public ArrayList<Character> domain();

    /**
   * @return false if special characters, true otherwise.
   */
    public Boolean isClass();

    /**
   * @return random character from a learned XClass.
   */
    public String randomRep();

    public ArrayList<String> lshDomain();
    
    /**
   * @return string representation.
   */
    public String toString();

}