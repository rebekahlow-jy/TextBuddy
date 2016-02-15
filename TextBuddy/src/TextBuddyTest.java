import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TextBuddyTest {

	@Test
	public void testIsNullCommand() {
		assertFalse(TextBuddy.isInputNull("clear"));
		assertFalse(TextBuddy.isInputNull("sdfdsa"));
		assertFalse(TextBuddy.isInputNull("add kdfkdpos"));
		assertTrue(TextBuddy.isInputNull(""));
	}
	
}
