package xsystem.learning;

import xsystem.XStructure;

public class XStructType {
    
    public String type;

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