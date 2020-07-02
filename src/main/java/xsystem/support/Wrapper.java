package xsystem.support;

/**Represents the Wrapper class.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class Wrapper {
    String string;

    public Wrapper(String string){
        this.string = string;
    }

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}