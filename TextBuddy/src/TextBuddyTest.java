import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextBuddyTest {

	private static final String MESSAGE_ADDED = "added to %1s: %2$s";
	private static final String MESSAGE_DELETE = "deleted from %1$s : %2$s";
	private static final String MESSAGE_DELETE_ERROR = "cannot delete number that does not exist";
	
	private static ArrayList<String> testList = new ArrayList<String>();
	
	/** Tests for CE2 Code */
	@Test
	public void testSort() {
		testList.clear();
		testList.add("BANANA");
		testList.add("COCONUT");
		testList.add("APPLE");
		
		ArrayList<String> expectedList = new ArrayList<String>(Arrays.asList("APPLE", "BANANA", "COCONUT"));
		
		assertEquals(expectedList, TextBuddy.sortLines(testList));
	}
	
	@Test
	public void testSearch() {
		testList.clear();
		testList.add("BANANA");
		testList.add("COCONUT");
		testList.add("APPLE");
		testList.add("NAANANA");
		testList.add("ANANAS");
		
		ArrayList<String> expectedList = new ArrayList<String>(Arrays.asList("1. BANANA", "4. NAANANA", "5. ANANAS"));
		
		assertEquals(expectedList, TextBuddy.searchText("search NANA", testList));
	}
	
	/** Tests for CE1 Code */
	@Test
	public void testAddText() {
		testList.clear();
		assertEquals(String.format(MESSAGE_ADDED, TextBuddy.getGivenFileName(), "Hello World"), 
					 TextBuddy.addText("add Hello World", testList));
		assertEquals(String.format(MESSAGE_ADDED, TextBuddy.getGivenFileName(), "CS2103 is Fun~"), 
					 TextBuddy.addText("add CS2103 is Fun~", testList));	
	}
	
	@Test
	public void testDeleteText() {
		testList.clear();
		testList.add("BANANA");
		testList.add("COCONUT");
		testList.add("APPLE");
		testList.add("NAANANA");
		testList.add("ANANAS");
		
		assertEquals(String.format(MESSAGE_DELETE, "notepad.txt", "APPLE"), 
					 TextBuddy.deleteText("delete 3", testList));
		assertEquals(String.format(MESSAGE_DELETE, "notepad.txt", "NAANANA"), 
					 TextBuddy.deleteText("delete 3", testList));
		
		testList.clear();
		testList.add("HEY");
		testList.add("THERE");
		
		assertEquals((MESSAGE_DELETE_ERROR), TextBuddy.deleteText("delete 3", testList));
		assertEquals((MESSAGE_DELETE_ERROR), TextBuddy.deleteText("delete -2", testList));
	}

	@Test
	public void testIsInputCommandNull() {
		assertFalse(TextBuddy.isInputCommandNull("clear"));
		assertFalse(TextBuddy.isInputCommandNull("sdfdsa"));
		assertFalse(TextBuddy.isInputCommandNull("add kdfkdpos"));
		assertTrue(TextBuddy.isInputCommandNull(""));
	}

}
