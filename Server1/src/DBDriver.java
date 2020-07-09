import java.sql.*;

/**
 * @author Faustino
 * This function has all the methods needed to interact with the data base
 *
 */
public class DBDriver {
	private static Connection Conn = null;
	private static ResultSet Rs = null;

	/**
	 * -connect the server to the data base
	 * @throws SQLException
	 */
	public static Connection DBConnect(String schema) throws SQLException {
		try {
			Conn = DriverManager.getConnection("jdbc:postgresql://db.fe.up.pt:5432/up201503038?currentSchema=" + schema ,
					"up201503038" , "M4Xe42Tm9");
			System.out.println("Connection established.");
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		return Conn;
	}
	
	/**
	 * -disconnect the server from the data base
	 * @throws SQLException
	 */
	public static void DBDisconnect(Connection Conn) throws SQLException {
		 try{
			 Conn.close();
			 System.out.println("Connection closed.");
	            
	     }
		 catch (SQLException ex) {
	        System.err.print(ex.getMessage());
	     }
	 }
	
	 
	 /**
	 *-checks if the given username exists in the data base 
	 * @param user_name
	 * @return username id or 0
	 */
	public static int  UserExists(String user_name) {
		 int id = 0;
		 String sql = "SELECT \"user_id\" FROM \"user\" WHERE \"username\"= ?";
		 ResultSet Rs = null;
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setString(1, user_name);
			 Rs = p.executeQuery();
			 if(Rs.next()) {
				 id = Rs.getInt("user_id");
			 }
			 else {
				 id=0;
			 }
			 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 return id;
	 }
	
	 
	 /**
	 *-searches the data base for all the users with 
	 * the given name(username , first name or last name) 
	 * @param name
	 * @return username_ret - result of the search
	 */
	public static String[]  GetUserbyUsername(String name) {
		 String[] username_ret = new String[256];
		 int i = 0;
		 String sql = "SELECT \"username\" FROM \"user\" WHERE (\"username\" LIKE ? OR "
		 		+ "\"first_name\"= ? OR \"last_name\"= ? )";
		 ResultSet Rs = null;
		 try {
			 
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setString(1, "%"+name+"%");
			 p.setString(2, name);
			 p.setString(3, name); // executar na mesma linha
			 Rs = p.executeQuery();
			 while(Rs.next()) {
				  username_ret[i] = Rs.getString("username");
				  i++;
			 } 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 return username_ret;
	 }
	 
	 
	 /**
	 * Search using first and last name , retrives all the users with that name  
	 * @param first_name
	 * @param last_name
	 * @return username_ret - results of the search
	 */
	public static String[]  GetUserbyFullName(String first_name , String last_name) {
		 String[] username_ret = new String[256];
		 int i = 0;
		 ResultSet Rs = null;
		 String sql = "SELECT \"username\" FROM \"user\" WHERE (\"first_name\"= ? AND \"last_name\"= ?)";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setString(1, first_name);
			 p.setString(2, last_name);
			 Rs = p.executeQuery();
			 while(Rs.next()) {
				  username_ret[i] = Rs.getString("username");
				  i++;
			 } 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 return username_ret;
	 }
	 
	 /**
	  * adds each user to the respective friendlist
	 * @param self_id
	 * @param friend_id
	 */
	public static void AddFriend(int self_id, int friend_id) {
		 String sql = "INSERT INTO \"friend\"(\"user_id\", \"friend_id\") "
			 		+ "VALUES (? , ?)";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setInt(1, self_id);
			 p.setInt(2, friend_id);
			 p.executeUpdate();
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	}
	 
	 /**
	 * -Gets the user password; 
	 * @param user_id
	 * @return pass
	 */
	public static String GetPass(int user_id) {
		 String pass = null;
		 String sql = "SELECT \"pass\" FROM \"user\" WHERE \"user_id\"= ?";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setInt(1, user_id);
			 Rs = p.executeQuery();
			 if(Rs.next()) {
				 pass= Rs.getString("pass");  
			 }
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 return pass;
	 }
	
	 /**
	  * Register the user info into the data base
	 * @param user_name
	 * @param password
	 * @param first_name
	 * @param last_name
	 * @param email
	 */
	public static void UserRegister(String user_name, String password, String first_name, 
			 String last_name, String email) {
		 
		 String sql = "INSERT INTO \"user\"(\"username\", \"pass\", \"first_name\", "
		 		+ "\"last_name\", \"email\") VALUES ( ?, ?, ?, ?, ?)";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setString(1, user_name);
			 p.setString(2, password);
			 p.setString(3, first_name);
			 p.setString(4, last_name);
			 p.setString(5, email);
			 p.executeUpdate();
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	
	 /**
	  * gets the user friend list from the data base 
	 * @param id
	 * @return ids , the ids of the friends
	 */
	public static int[]  GetFriends(int id) {
         int[] ids = new int[256];
         int i=0;
         ResultSet Rs = null;
         String sql = "SELECT \"friend_id\" FROM \"friend\" WHERE (\"user_id\" = ?)";
         try {
        	 PreparedStatement p = Conn.prepareStatement(sql);
        	 p.setInt(1, id);
        	 Rs = p.executeQuery();
             while(Rs.next()) {
                  ids[i] = Rs.getInt("friend_id");
                  i++;
             }
         }
         catch(Exception e){
             e.printStackTrace();
         }
         return ids;
     }
	 
	 /**
	 * gets the username with the given id
	 * @param id
	 * @return username_ret 
	 */
	public static String  GetUsernamebyId(int id) {
         String username_ret = new String();
         ResultSet Rs = null;
         String sql = "SELECT \"username\" FROM \"user\" WHERE (\"user_id\" = ?)";
         try {
        	 PreparedStatement p = Conn.prepareStatement(sql);
        	 p.setInt(1 ,id);
        	 Rs = p.executeQuery();
             if(Rs.next()) {
                  username_ret = Rs.getString("username");
             }
         }
         catch(Exception e){
             e.printStackTrace();
         }
         return username_ret;
     }
	 
	 /**
	 * Saves the message to the respective conversation table 
	 * @param sender_id
	 * @param receiver_id
	 * @param content
	 */
	public static void PutMessage(int sender_id, int receiver_id, String content ) {
		 String sql = "INSERT INTO \"msg\"(\"sender_id\", \"receiver_id\",\"content\") "
			 		+ "VALUES ( ?, ?, ?)";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setInt(1 ,sender_id);
			 p.setInt(2 ,receiver_id);
			 p.setString(3 ,content);
			 p.executeUpdate();
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	/**
	 * gets the last msg_no messages from increment*msg_no in a conversation
	 * @param sender_id
	 * @param receiver_id
	 * @param increment
	 * @param msg_no
	 * @return conv - object 
	 */
	public static Conversation GetConversation(int sender_id, int receiver_id, int increment, int msg_no) {
		 String sql = "SELECT \"sender_id\", \"content\", \"time\" FROM \"msg\" WHERE (\"sender_id\" = ? AND \"receiver_id\" = ?) "
		 		+ "OR (\"sender_id\" = ? AND \"receiver_id\" = ?)"
		 		+ "ORDER by \"created_by\" DESC "
		 		+ "OFFSET ?"
		 		+ "LIMIT ?";
		 
		 String sender_ret = null;
		 String content =null;
		 String timestamp = null;
		 Conversation conv = new Conversation();
		 Message msg = null;
		 int id = 0, offset = increment * msg_no;
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.setInt(1 , sender_id);
			 p.setInt(2 , receiver_id);
			 p.setInt(3 , receiver_id);
			 p.setInt(4 , sender_id);
			 p.setInt(5 , offset);
			 p.setInt(6 , msg_no);
			 Rs = p.executeQuery();
			 while(Rs.next()) {
                id = Rs.getInt("sender_id");
                content = Rs.getString("content");
                timestamp = Rs.getString("time");
                sender_ret = DBDriver.GetUsernamebyId(id);
                msg = new Message(sender_ret , content, timestamp);
                conv.add_message(msg);
			 }
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 System.out.println("CONV SIZE: " + conv.msg_list.size());
		 return conv;
	 }
	 
	 /**
	 * {@summary deletes the content from the given table in the data base} 
	 * @param table
	 */
	public static void ResetMessage() {
		 String sql = "DELETE FROM \"msg\"";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			
			 p.executeUpdate();
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	public static void ResetUser() {
		 String sql = "DELETE FROM \"user\"";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.executeUpdate();
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	public static void ResetFriend() {
		 String sql = "DELETE FROM \"friend\"";
		 try {
			 PreparedStatement p = Conn.prepareStatement(sql);
			 p.executeUpdate();
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
}

