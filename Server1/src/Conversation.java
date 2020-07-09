import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Faustino
 * This class is used to store the messages of given conversation
 *
 */
public class Conversation implements Serializable {

	ArrayList<Message> msg_list = new ArrayList<Message>();
	protected String user_send;
	protected String user_rcv;
	protected boolean messages_changed = false;
	
	
	/**
	 * -checks if new messages where added
	 * @return true or false
	 */
	public boolean check_messages() {
		
		if(messages_changed) {
			messages_changed = false;
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * -Add a new object Message to the ArrayList
	 * @param msg
	 */
	public void add_message(Message msg) {
		
		//Message new_msg = new Message(user , message);
		msg_list.add(msg);
		messages_changed = true;
		
	}
	
}
