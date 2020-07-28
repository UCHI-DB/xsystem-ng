package xsystem;

import java.util.Iterator;
import java.util.regex.Pattern;

public interface XSystem {

    XStructure build(Iterator<String> data);

    boolean match(XStructure pattern, String value);

    boolean match(XStructure pattern1, XStructure pattern2);

    boolean match(XStructure pattern1, Pattern regex);

}
