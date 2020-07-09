
import java.net.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * @author Faustino
 * This class creates the server , to which clients will connect;
 *
 */
public class Server{
	
	private ServerSocket serversocket;
	static Conversation new_conversation ;
	static Message new_message;
    
	//guardar o ip de cada utilizador conectado com o servidor
    //static ArrayList<User> users_on = new ArrayList<User>();
    
    //static ArrayList<Conversation> conv_list = new ArrayList<Conversation>();
    

	public void start_server(int port) throws IOException {

		try {
			serversocket = new ServerSocket(port);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		while(true)
		{
			try {
				new ClientHandler(serversocket.accept()).start();
				System.out.println("New client\n");
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
	}


	public void stop() throws IOException{
		try {
			serversocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @Faustino
	 * This class creates the client thread
	 *  when it connects to the server
	 *
	 */
	private static  class ClientHandler extends Thread {

		private Socket clientsocket;
		private PrintWriter out;
		private BufferedReader in;
		protected String register = "reg";
		protected String signin = "sin";
		protected String friend = "frd";
	    protected String ack = "ack";
	    protected String nack = "nack";
	    protected String send = "snd";
	    protected String logout = "out";
	    protected String friend_add = "fad";
	    protected String update = "upd";
	    protected String getMsg = "gms";
	    
	   
		

		/**
		 * sets the clientsocket
		 * @param socket
		 */
		public ClientHandler(Socket socket)
		{
			this.clientsocket = socket;
		}

		
		/**
		 *-runs the client thread
		 */
		public void run() {

			try {
				out = new PrintWriter(clientsocket.getOutputStream(), true);
			} catch (IOException e2) {
				
				e2.printStackTrace();
			}
        	try {
				in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
			} catch (IOException e2) {
				
				e2.printStackTrace();
			}
        	String receivedmsg;
        	String ctrl_string;
        	String real_message;
        	String server_reply;

        	try {
        		/*
        		 	-recebe as mensagens da app;
        		 */
				while((receivedmsg = in.readLine()) != null ){
					
					ServerLogic serverlogic = new ServerLogic(receivedmsg);
					System.out.println(receivedmsg);
					
					ctrl_string = serverlogic.getControlMessage();
					real_message = serverlogic.getRealMessage();
					System.out.println(ctrl_string);
					
					
					/*
					  - user sends "reg" control message;
					 */
					if(ctrl_string.equals(register)) {
						
						server_reply = serverlogic.Register(real_message);
						out.println(server_reply);
						
					}
					
					
					/*
					   - user sends "sin" control message;
					 */
					else if(ctrl_string.equals(signin)) {
						
						server_reply = serverlogic.SignIn(real_message);
						out.println(server_reply);
						
					}
					
					/*
					 -Receives the "out" control message;
					 - stop this client thread;
					 */
					else if(ctrl_string.equals(logout)) {
						
					
					}

					else if( ctrl_string.equals(getMsg)) {

						JSONObject json = new JSONObject();
						Conversation con = new Conversation();

						con = serverlogic.GetMsg(real_message);
						System.out.println("***********************");
						System.out.println("\n" + con +"\n");
						System.out.println("***********************");

						System.out.println("Sending messages to the ServerSocket\n" + con);

						if(con.msg_list.size() == 0) {
							Map<String, String> m = new HashMap<String, String>(2);
							m.put("user_send" , con.user_send);
							m.put("user_rcv", con.user_rcv);

							json.put("user_info", m);

							out.println(json.toString());
							System.out.println("ZEEEEEEE"+json.toString());
						}
						else {
							Map<String, String> m = new LinkedHashMap<String, String>(2);
							m.put("user_send" , con.user_send);
							m.put("user_rcv", con.user_rcv);
							json.put("user_info", m);

							JSONArray array = new JSONArray();
						      for(int i = 0; i < con.msg_list.size(); i++) {
						         array.put(con.msg_list.get(i));
						      }

						      json.put("messages" , array);

						      out.println(json.toString());
						      System.out.println("else"+json.toString());

						}

				        //out.println(con);

						//outputStream.writeObject(con);

						System.out.println("******DONE******" + con.user_send.toUpperCase() + "******DONE******");
					}


					/*
					  -Receives the "snd" control messages;
					 */
					else if(ctrl_string.equals(send)) {
						
						server_reply = serverlogic.Send(real_message);
						out.println(server_reply);
					}
						
					/*
					  -Receives the "fad" control message
					 */
					else if(ctrl_string.equals(friend_add)) {
						
						 serverlogic.FriendAdd(real_message);
						
					}
					/*
					  -Receives the "frd" control message;
					 */
					else if(ctrl_string.equals(friend)) {
						
						server_reply = serverlogic.SearchFriend(real_message);
						out.println(server_reply);	
					}
					
					/*
					  -Receives the "upd" control message;
					 */
					else if (ctrl_string.equals(update)) {
						
						server_reply = serverlogic.UpdateFriends(real_message);
						out.println(server_reply);
						
					}
				

				
				}
			} catch (IOException e2) {
				
				e2.printStackTrace();
			}

        	try {
				in.close();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
            out.close();
            try {
				clientsocket.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

		}
		
		
	}

	 public static void main(String[] args) throws IOException, SQLException {
		 
        Server server=new Server();
        DBDriver.DBConnect("faustino");
        //DBDriver.ResetTable("friend");  //friend, user, txt_msg
        server.start_server(6666);
		 
    }
	
}