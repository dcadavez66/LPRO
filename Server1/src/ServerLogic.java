
/**
 * @author Faustino
 * This class is used to deal with all 
 * the actions the server has to do
 * depending on the control message
 *
 */


public class ServerLogic {
	
	String message_in;
	String control_message;
	String real_message;
	
	
	/**
	 * constructor , sets the message received from the socket;
	 * @param msg_in
	 */
	public ServerLogic(String msg_in) {
		this.message_in = msg_in;
		
	}
	
	public void SetMessageIn(String message_in) {
		this.message_in = message_in;
	}
	
	/**
	 * -gets the control message
	 * @return control_message
	 */
	public String getControlMessage() {
		
		char[] ctrl_aux = new char[3] ;
		char[] msgin = message_in.toCharArray();
		for(int i = 0 ; i < 3 ; i++) {
			
			ctrl_aux[i] = msgin[i];
		}
		control_message = new String(ctrl_aux); 
		return control_message;
	}
	
	/**
	 * -gets the message without the control message
	 * @return real_message
	 */
	public String getRealMessage() {
		
		char[] msgin = message_in.toCharArray();
		char[] realmsg = new char[msgin.length-3];
		int j = 0;
		for(int i = 3 ; i < msgin.length ; i++) {
			realmsg[j] = msgin[i];
			j++;
		}
		
		real_message = new String(realmsg);
		
		return real_message;
		
	}
	
	/**
	 * -registers the user information in the data base;
	 * - user information:
	 *	-username
	 *	-password
	 *	-First Name
	 *	-Last Name 					 					
	 *	-email
	 * @param user_information
	 * @return ack or nack
	 */
	public String Register(String user_information) {
		int user_id = 0;
		
		String[] user_info;
		System.out.println(user_information);
		user_info = user_information.split(" ");
		for(int i = 0 ; i < user_info.length ; i++) {
			System.out.println(user_info[i]);
		}
		user_id = DBDriver.UserExists(user_info[0]);
		if(user_id != 0) {
			
			System.out.println("this username already exists!\n");
			return "nack";
			
		}
		
		else {
			
			DBDriver.UserRegister(user_info[0], user_info[4], user_info[1], user_info[2], user_info[3]); 
			return "ack";
			
		}
	}
	
	/**
	 * - receives one or more usernames , add to the user friends list;
	 * @param friend_usernames
	 */
	public void FriendAdd(String friend_usernames) {

		String[] friend_list; //[0] username
		int l = 1;

		int self_id;
		System.out.println("********************");
		System.out.println(friend_usernames);
		friend_list = friend_usernames.split(" ");
		int[] ids = new int[friend_list.length];
		System.out.println("********** \n"+friend_list[0]);
		self_id= DBDriver.UserExists(friend_list[0]);
		System.out.println("self id :" + self_id);

		System.out.println(self_id);
		int [] fr = DBDriver.GetFriends(self_id);

		for(int i=0 ; i< friend_list.length - 1 ;i++) {
			int k = 0;

			ids[i]= DBDriver.UserExists(friend_list[l]);
			for ( int j = 0; j < fr.length; j++) {
				if(ids[i] == fr[j]) {
					k = 1;
				}
			}
			if (k == 1) 
				continue;
			System.out.println(ids[i]);
			DBDriver.AddFriend(self_id, ids[i]);
			DBDriver.AddFriend(ids[i], self_id);
			l++;
		}
	}



	/*
	 * - Checks in data base if username and password are valid 
	 * @param username_password
	 * @return ack or nack 
	 */
	public String SignIn(String username_password) {
		
		
		int user_id;
		String[] user_info ;
		
		int self_id;
		int id[];
		String send_back = "";
		
		System.out.println(username_password);
		user_info = username_password.split(" ");
		for(int i = 0 ; i < user_info.length ; i++) {
			System.out.println(user_info[i]);
		}
	
		user_id=DBDriver.UserExists(user_info[0]);
		if(user_id==0) {
			System.out.println("this username isn't valid!\n");
			return "nack";
		}
		else {
			
			
			
			if(DBDriver.GetPass(user_id).equals(user_info[1])) {
				System.out.println("The password is correct , user can enter!\n");
				
				self_id = DBDriver.UserExists(user_info[0]);					
				id =  DBDriver.GetFriends(self_id);
				System.out.println(id[0]);
				if(id[0] == 0) {
					return " ";
				}
				else {
					for(int i = 0 ; i < id.length; i++) {
						String aux = DBDriver.GetUsernamebyId(id[i]);
						System.out.println(aux);
						send_back += aux + " ";
					}
					System.out.println("***********" + send_back+ "***********");
					System.out.println(send_back);
					return send_back;
				}

				
			}
			else {
				System.out.println("The password is incorrect, please try again!");
				return "nack";
			}
			
		}
	}
	
	

	
	/**
	 * -stores the message in the data base; 
	 * @param receiver_sender_msg
	 * @return ack 
	 */
	public String Send (String receiver_sender_msg) {
		
		int sender_id, receiver_id;
		String[] user_info;
		String message;
		System.out.println(receiver_sender_msg);
		user_info = receiver_sender_msg.split(" ", 3);
		System.out.println("***********************");
		receiver_id=DBDriver.UserExists(user_info[0]);
		sender_id =DBDriver.UserExists(user_info[1]); 
		//System.out.println(receiver_id);	
		//System.out.println(sender_id);
		message = user_info[2];				
		//System.out.println(message);
		DBDriver.PutMessage(sender_id, receiver_id, message);
		System.out.println("***********************");

		return "ack";
		
	}
	
	
	

	
	
	/**
	 * -search in data base for the given user name(username , First Name,
	 *  Last name or First Name and Last name);
	 * @param friend_name
	 * @return out_message , contains the results of the search
	 */
	public String SearchFriend(String friend_name) {
		
	
		String out_message = "null";
		String[] user_info;
		String[] username_sendback = new String[256];
		int  k = 0;
		
		System.out.println(friend_name);
		user_info = friend_name.split(" ");
		
		if(user_info.length == 1) {
			
			k = 0;
			username_sendback = DBDriver.GetUserbyUsername(user_info[0]);
			
		}
		
		else if(user_info.length == 2) {
			System.out.println("entrei aqui dude" + "\n" + user_info[0] + "\n" + user_info[1]);
			
			username_sendback = DBDriver.GetUserbyFullName(user_info[0], user_info[1]);
		}
		
		
		
		int n = 0 ;
		
		for(int i = 0; i < username_sendback.length; i++) {
			
			if(username_sendback[i] == null) {
				
				return out_message;
				
			}
			if(n == 0 ) {
				out_message = username_sendback[i] + " ";
				n=1;
			}
			else {
				out_message = out_message + username_sendback[i] + " " ;
			}
			
			
			System.out.println(username_sendback[i]);
		}
		
		return null;
		
	}
	
	/**
	 * search in data base for the user friendlist;
	 * @param self_username
	 * @return send_back - contains the friendlist
	 */
	public String UpdateFriends(String self_username) {
		
		
		int self_id;
		int id[];
		String send_back = "";
		
		self_id = DBDriver.UserExists(self_username);					
		id =  DBDriver.GetFriends(self_id);
		
		for(int i = 0 ; i < id.length; i++) {
			String aux = DBDriver.GetUsernamebyId(id[i]);
			send_back += aux + " "; 
		}
		
		return send_back;
	
	}

	/**
	 * Gets the conversation between the users
	 * @param usernames - users in the conversation
	 * @return aux - Conversation that contains the messages 
	 */
	public Conversation GetMsg(String usernames ){

		Conversation aux = new Conversation();

		int sender_id, receiver_id;
		int msg_no = 20;
		int increment = 0;
		String[] user_info;
		user_info = usernames.split(" ");

		System.out.println("***********************");
		System.out.println("\n" + user_info[0] + "\n" + user_info[1] + "\n");
		System.out.println("***********************");

		receiver_id=DBDriver.UserExists(user_info[1]);
		sender_id =DBDriver.UserExists(user_info[0]);
		System.out.println(receiver_id);
		System.out.println(sender_id);
		aux = DBDriver.GetConversation(sender_id, receiver_id, increment, msg_no);


		aux.user_send = user_info[0];
		aux.user_rcv = user_info[1];


		return aux;
	}

}
