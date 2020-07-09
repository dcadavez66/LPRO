import java.io.Serializable;

/**
 * @author Faustino
 * This class is used to store the information of a single message
 *
 */
public class Message implements Serializable {
	
	protected String msg;
	protected String user_name;
	protected String time_stamp;
	
	/**
	 * sets the owner of the message username and it's message
	 * @param user_name
	 * @param msg
	 * @param time_stamp
	 */
	public Message(String user_name , String msg , String time_stamp )
	{
		this.user_name = user_name;
		this.msg = msg;
		this.time_stamp = time_stamp;
	}

}
