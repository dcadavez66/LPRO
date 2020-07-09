import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;

class DBdriverTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Connection Conn=null;
		Conn = DBDriver.DBConnect("faustinotest");
		DBDriver.UserRegister("test", "testpass", "test", "test", "test");
		DBDriver.UserRegister("test1", "test1", "test1", "test1", "test1");
		DBDriver.DBDisconnect(Conn);
	}

	@AfterEach
	void tearDown() throws Exception {
		Connection Conn=null;
		Conn = DBDriver.DBConnect("faustinotest");
		DBDriver.ResetUser();
		DBDriver.DBDisconnect(Conn);
	}

	@Test
	void testDBConnectAndDBDisconnect() throws SQLException {
		Connection Conn = null;
		Conn = DBDriver.DBConnect("faustinotest");
		assertNotNull(Conn);
	}

	@Test
	void testUserExistsAndGetUserbyIdAndUserRegister () throws SQLException {
		int id;
		String name;
		DBDriver.DBConnect("faustinotest");
		id=DBDriver.UserExists("test");
		name=DBDriver.GetUsernamebyId(id);
		assertTrue(name.equals("test"));
	}

	@Test
	void testGetUserbyUsername() throws SQLException {
		Connection Conn = null;
		String[] name = new String[256];
		Conn = DBDriver.DBConnect("faustinotest");
		name=DBDriver.GetUserbyUsername("test");
		System.out.println(name[0]);
		System.out.println(name[1]);
		assertTrue(name[0].contentEquals("test"));
		assertTrue(name[1].contentEquals("test1"));
		DBDriver.DBDisconnect(Conn);
	}

	@Test
	void testGetUserbyFullName() throws SQLException {
		Connection Conn = null;
		String[] name = new String[256];
		Conn = DBDriver.DBConnect("faustinotest");
		name=DBDriver.GetUserbyFullName("test" ,"test");
		System.out.println(name[0]);
		System.out.println(name[1]);
		assertTrue(name[0].contentEquals("test"));
		DBDriver.DBDisconnect(Conn);
	}

	@Test
	void testAddFriendAndGetFriends() throws SQLException {
		Connection Conn = null;
		int id=0, id1=0;
		int[] ids = new int[256]; 
		Conn = DBDriver.DBConnect("faustinotest");
		id = DBDriver.UserExists("test");
		id1 = DBDriver.UserExists("test1");
		DBDriver.AddFriend(id, id1);
		ids = DBDriver.GetFriends(id);
		assertTrue(ids[0]==id1);
		DBDriver.DBDisconnect(Conn);
	}

	@Test
	void testGetPass() throws SQLException {
		Connection Conn = null;
		String pass = null;
		int id=0;
		Conn = DBDriver.DBConnect("faustinotest");
		id = DBDriver.UserExists("test");
		pass = DBDriver.GetPass(id);
		assertTrue(pass.contentEquals("testpass"));
		DBDriver.DBDisconnect(Conn);
	}

	@Test
	void testPutMessageAndGetConversation() throws SQLException {
		Connection Conn = null;
		int id=0, id1=0, msg_no=2, inc=0;
		Conversation conv = new Conversation();
		Conn = DBDriver.DBConnect("faustinotest");
		id = DBDriver.UserExists("test");
		id1 = DBDriver.UserExists("test1");
		DBDriver.PutMessage(id, id1, "testmsg");
		DBDriver.PutMessage(id1, id, "testmsg1");
		conv=DBDriver.GetConversation(id, id1, inc, msg_no);
		assertTrue(conv.msg_list.get(0).msg.contentEquals("testmsg1"));
		assertTrue(conv.msg_list.get(1).msg.contentEquals("testmsg"));
		
	}
}
