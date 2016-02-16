import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class TextBuddyTest {
	
	private static final String MESSAGE_ADDED = "added to %1s: %2$s";
	
	//private static String[] randomLines = new String[] {"CS2103", "Text", "Buddy", "Plus", "Plus"};
	private static ArrayList<String> expectedList = new ArrayList<String>();
	
	private void populateList(int numberOfItems){
		for (int i = 0; i < numberOfItems; i++) {
			expectedList.add(String.valueOf(i+1));
		}
	}
	
	@Test
	public void testAddText() {
		expectedList.clear();
		populateList(1);
		assertEquals(String.format(MESSAGE_ADDED, TextBuddy.getGivenFileName(), "1"), TextBuddy.addText("add 1"));
	}
	
	@Test
	public void testIsInputCommandNull() {
		assertFalse(TextBuddy.isInputCommandNull("clear"));
		assertFalse(TextBuddy.isInputCommandNull("sdfdsa"));
		assertFalse(TextBuddy.isInputCommandNull("add kdfkdpos"));
		assertTrue(TextBuddy.isInputCommandNull(""));
	}

}
