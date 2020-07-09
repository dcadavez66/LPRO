import java.util.ArrayList;
public class User {

	protected String user_name = null;
	protected String password = null;
	protected String email = null;
	protected String first_name = null;
	protected String last_name = null;
	
	ArrayList<User> friends_list = new ArrayList<User>();
	ArrayList<Conversation> my_conversations = new ArrayList<Conversation>();
	
	public User (String user_name)
	{
		this.user_name = user_name;
	}
	
	public User(String user_name , String first_name , String  last_name, String email  , String password) 
	{
		this.user_name = user_name;
		this.password = password;    
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		//this.age = age;
		//this.birth_date = birth_date;
	}
	
	public void add_friend(User new_friend)
	{
		friends_list.add(new_friend);
	}
	
	public void remove_friend(User remove_friend)
	{
		User aux ;
		for(int i = 0 ; i < friends_list.size() ; i++)
		{
			aux = friends_list.get(i);
			if(remove_friend.equals(aux))
			{
				friends_list.remove(i);
			}
		}
	}
	
	/*
	public Conversation check_conv(User u1) //returns conversation_ID if exists
    {
       
        for ( int i = 0; i < my_conversations.size(); i++) {
            for( int j = 0; j <  my_conversations.get(i).users_allowed.size(); j++)
            if (u1 == my_conversations.get(i).users_allowed.get(j))
                return my_conversations.get(i);
        }
       
        return null;
    }
    */
	
}
