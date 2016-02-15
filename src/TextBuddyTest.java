import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TextBuddyTest {

	@Test
	public void testIsInvalidCommand() {
		assertFalse(TextBuddy.isNullCommand("clear"));
		assertFalse(TextBuddy.isNullCommand("sdfdsa"));
		assertFalse(TextBuddy.isNullCommand("add kdfkdpos"));
		assertTrue(TextBuddy.isNullCommand(""));
	}
}
