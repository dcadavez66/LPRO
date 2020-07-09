import static org.junit.Assert.*;
 
 
import org.junit.jupiter.api.*;
 
public class ServerLogicTest {
 
    @BeforeAll
    public static void setUpBeforeAll() throws Exception {
        DBDriver.DBConnect("faustinoservertest");
        DBDriver.ResetFriend();  //friend, user, txt_msg
        DBDriver.ResetUser();
        DBDriver.ResetMessage();
        String input1 = "regola Ola Alves ola@hotmail.com 123";
        ServerLogic serverlogic = new ServerLogic(input1);
        serverlogic.Register(serverlogic.getRealMessage());
         String input2 = "regadeus Adeus Alves adeus@hotmail.com 123";
         serverlogic.SetMessageIn(input2);
        serverlogic.Register(serverlogic.getRealMessage());
           
    }
 
    @AfterAll
    public static void tearDownAfterAll() throws Exception {
    }
 
    @BeforeEach
    public void setUp() throws Exception {
       
    }
 
    @AfterEach
    public void tearDown() throws Exception {
    }
 
    @Test
    @Order(1)
    public void testServerLogic() {
        String input = "mensagem recebida";
        ServerLogic serverlogic = new ServerLogic(input);
        assertTrue(serverlogic.message_in.equals(input));
    }
 
    @Test
    @Order(2)
    public void testGetControlMessage() {
        String input = "sinola123";
        ServerLogic serverlogic = new ServerLogic(input);
        assertTrue(serverlogic.getControlMessage().equals("sin"));
       
    }
 
    @Test
    @Order(3)
    public void testGetRealMessage() {
        String input = "sinola123";
        ServerLogic serverlogic = new ServerLogic(input);
        assertTrue(serverlogic.getRealMessage().equals("ola123"));
    }
 
    @Test
    @Order(4)
    public void testRegister() {
        String input1 = "regjohn123 João Alves joao@hotmail.com 123";
        ServerLogic serverlogic = new ServerLogic(input1);
        assertTrue(serverlogic.Register(serverlogic.getRealMessage()).equals("ack"));
       
        String input2 = "regjohn123 João Martins martins@hotmail.com 123";
        serverlogic.SetMessageIn(input2);
        assertTrue(serverlogic.Register(serverlogic.getRealMessage()).equals("nack"));
       
        String input3 = "regjoazinho24 João Martins omeuemail@hotmail.com 123";
        serverlogic.SetMessageIn(input3);
        assertTrue(serverlogic.Register(serverlogic.getRealMessage()).equals("ack"));
       
        String input5 = "regjorginho Jorge Novo jorge_novo@hotmail.com 123";
        serverlogic.SetMessageIn(input5);
        assertTrue(serverlogic.Register(serverlogic.getRealMessage()).equals("ack"));
       
       
       
    }
   
    @Test
    @Order(5)
    public void testFriendAdd() {
        String input1= "fadola adeus";
        ServerLogic serverlogic = new ServerLogic(input1);
        serverlogic.FriendAdd(serverlogic.getRealMessage());
       
    }
   
    @Test
    @Order(6)
    public void testSignIn() {
       
        String input1 = "sinnemtenhoconta 123";
        ServerLogic serverlogic = new ServerLogic(input1);
        assertTrue(serverlogic.SignIn(serverlogic.getRealMessage()).equals("nack"));
       
        String input2 = "sinjohn123 12345";
        serverlogic.SetMessageIn(input2);
        assertTrue(serverlogic.SignIn(serverlogic.getRealMessage()).equals("nack"));
       
        String input3 = "sinjohn123 123";
        serverlogic.SetMessageIn(input3);
        assertTrue(serverlogic.SignIn(serverlogic.getRealMessage()).equals(" "));
       
       
       
    }
   
   
 
    @Test
    @Order(7)
    public void testSend() {
        String input1 = "sndola adeus ola";
        ServerLogic serverlogic = new ServerLogic(input1);
        assertTrue(serverlogic.Send(serverlogic.getRealMessage()).equals("ack"));
    }
 
   
 
    @Test
    public void testSearchFriend() {
        String input1 = "frdOla Alves";
        ServerLogic serverlogic = new ServerLogic(input1);
        assertTrue(serverlogic.SearchFriend(serverlogic.getRealMessage()).equals("ola "));
       
        String input2 = "frdOla";
        serverlogic.SetMessageIn(input2);
        assertTrue(serverlogic.SearchFriend(serverlogic.getRealMessage()).equals("ola "));
       
        String input3 = "frdola";
        serverlogic.SetMessageIn(input3);
        assertTrue(serverlogic.SearchFriend(serverlogic.getRealMessage()).equals("ola "));
       
        String input4 = "frdcouves";
        serverlogic.SetMessageIn(input4);
        assertTrue(serverlogic.SearchFriend(serverlogic.getRealMessage()).equals("null"));
       
    }
 
    @Test
    public void testUpdateFriends() {
        fail("Not yet implemented");
    }
 
}
