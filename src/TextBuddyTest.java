import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class TextBuddyTest {
	
	@Test
	public void testAddCommand() {
		//check if the “clear” command returns the right status message
		testCommand("all content deleted from mytextfile.txt", "clear");
	}
	
	private void testCommand(String expected, String userCommand){
		assertEquals(expected, TextBuddy.executeCommand(userCommand));
	}
}
