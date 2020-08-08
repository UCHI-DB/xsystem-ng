package xsystem.learning;

import xsystem.layers.XStructure;

/**Represents the XStructType class, which acts as a Wrapper class for a XStructure and the type of data it represents
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class XStructType {
	
	/** Type data of XStructure represents*/
    public String type;

	/** The XStructre */
    public XStructure xStructure;

    public XStructType(String _type, XStructure x) {
    	this.type = _type;
    	this.xStructure = x;
	}
    public XStructType(){}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public XStructure getxStructure() {
		return xStructure;
	}

	public void setxStructure(XStructure xStructure) {
		this.xStructure = xStructure;
	}
    
}