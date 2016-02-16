import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * This class is used to add, display, delete and clear text that the user
 * inputs into the system. If the user enters an invalid command, the message
 * "invalid command format" will be shown to the user. The TextBuddy program
 * will save the final user input into a text file which will be created upon
 * exiting the program.
 * 
 * Assumptions:
 * 1. Command(s) will be the first single word.
 * 2. Adding of empty line will not be permitted.
 * 3. Deleting non-valid line number will not be permitted.
 * 
 * Commands:
 * 1. add [CONTENT TO BE ADDED]
 * 2. display
 * 3. delete [LINE NUMBER TO BE DELETED]
 * 4. clear
 * 5. sort
 * 6. exit
 * 
 * @author Rebekah Low Jin Yan
 */

public class TextBuddy {
	
	/* 
	 * ==============================
	 * Constants and Variables 
	 * ==============================
	 */
	
	private static final String MESSAGE_EMPTY = "%1s is empty";
	private static final String MESSAGE_ADDED = "added to %1s: %2$s";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1s is ready for use";
	private static final String MESSAGE_NULL_COMMAND_TYPE = "command type string cannot be null!";
	private static final String MESSAGE_COMMAND_PROMPT = "command: ";
	private static final String MESSAGE_DELETE = "deleted from %1$s : %2$s";
	private static final String MESSAGE_DISPLAY = "%1$s. %2$s";
	private static final String MESSAGE_SEARCH_FAILED = "search text not found";
	private static final String MESSAGE_SORT = "lines successfully sorted alphabetically";
	
	/**Regular expression used for splitting command*/
	private final static String REGEX_COMMAND_DELIMITER = "\\s+";
	
	private static final int ARRAY_OFFSET = 1;
	
	private static Scanner scanner = new Scanner(System.in);
	private static ArrayList<String> commandList = new ArrayList<String>();
	private static String fileName;
	
	/** Enumeration of command types */
	enum CommandType {
		ADD_TEXT, DISPLAY_CONTENT, DELETE_TEXT, CLEAR_CONTENT, INVALID, SEARCH, SORT, EXIT
	};
	
	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		readGivenFileName(args);
		print(formatWelcomeMessage());
		beginCommandLoop();
	}
	
	/*
	* ===========================================
	* Class Methods
	* ===========================================
	*/

	public static void beginCommandLoop() {
		while (true) {
			String userInput = parseUserInput();
			executeCommand(userInput);
		}
	}

	protected static void executeCommand(String userInput) {
		if (isInputNull(userInput)){
			print(MESSAGE_NULL_COMMAND_TYPE);
		} else {
			CommandType commandType = getCommandType(userInput);
			executeCommandType(userInput, commandType);
		}
	}
	
	private static CommandType getCommandType(String userInput) {
		String commandTypeString = getFirstWord(userInput);
		return getCommandTypeFromString(commandTypeString);
	}
	
	protected static void executeCommandType(String userInput, CommandType commandType) {
		switch (commandType) {
			case ADD_TEXT:
				addText(userInput);
				break;
			case DISPLAY_CONTENT:
				displayContent();
				break;
			case DELETE_TEXT:
				deleteText(userInput);
				break;
			case CLEAR_CONTENT:
				clearContent(userInput);
				break;
			case SEARCH:
				searchText(userInput);
				break;
			case SORT:
				sortLines();
				break;
			case EXIT:
				saveFile();
				System.exit(0);
			case INVALID:
			default:
				print(formatInvalidFormatMessage(userInput));
		}
	}
	
	/**
	* Add text to be written into text file.
	*
	* @param userInput			User input to be added to text file.
	*/
	protected static void addText(String userInput) {
		String textToAdd = removeFirstWord(userInput);
		commandList.add(textToAdd);
		print(formatAddedMessage(textToAdd));
	}
	
	/** Display content to be written into text file. */
	protected static void displayContent() {
		if (commandList.isEmpty()) {
			print(formatFileEmptyMessage());
		} else {
			for (int i = 0; i < commandList.size(); i++) {
				int textIndex = (i + ARRAY_OFFSET);
				String textLine = commandList.get(i);
				print(formatTextLine(textIndex, textLine));
			}
		}
	}
	
	/**
	* Delete text to be written into text file.
	*
	* @param userInput			User input to be added to text file.
	*/
	protected static void deleteText(String userInput) {
		int lineNumberToDelete = Integer.parseInt(removeFirstWord(userInput));
		int arrayIndexToDelete = (lineNumberToDelete - ARRAY_OFFSET);
		String removedCommand = commandList.remove(arrayIndexToDelete);
		print(formatDeleteMessage(removedCommand));
	}
	
	/** Clear content to be written into text file. */
	protected static void clearContent(String userCommand) {
		commandList.clear();
		print(formatClearedTextMessage());
	}
	
	/** Search for a word in the file and return the lines containing that word. */
	protected static void searchText (String userInput) {
		ArrayList<String> foundList = new ArrayList<String>();
		String searchText = removeFirstWord(userInput);
		for (String currentString : commandList){
			if (currentString.contains(searchText)){
				int textIndex = (commandList.indexOf(currentString) + ARRAY_OFFSET);
				String textLine = currentString;
				foundList.add(formatTextLine(textIndex, textLine));
			}
		}
		if (foundList.isEmpty()){
			print(MESSAGE_SEARCH_FAILED);
		} else for (int i = 0; i < foundList.size(); i++){
			String foundLine = foundList.get(i);
			print(foundLine);
		}
	}
	
	/** Sort lines alphabetically and change the order lines are arranged. */
	protected static void sortLines () {
		Collections.sort(commandList, String.CASE_INSENSITIVE_ORDER);
		print(MESSAGE_SORT);
	}
	
	/** Save content modified by user command into text file. */
	protected static void saveFile() {
		try {
			FileOutputStream input = new FileOutputStream(fileName);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(input);
			BufferedWriter writer = new BufferedWriter(outputStreamWriter);
			for (int i = 0; i < commandList.size(); i++) {
				int textIndex = (i + ARRAY_OFFSET);
				String textLine = commandList.get(i);
				writer.write(formatTextLine(textIndex, textLine));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Converts String to its CommandType equivalent.
	*
	* @param commandTypeString	String to be converted to CommandType.
	* @return 					CommandType equivalent
	*/

	protected static CommandType getCommandTypeFromString(String commandTypeString) {
		String lowerCaseCommandTypeString = commandTypeString.toLowerCase();
		switch (lowerCaseCommandTypeString) {
			case "add":
				return CommandType.ADD_TEXT;
			case "display":
				return CommandType.DISPLAY_CONTENT;
			case "delete":
				return CommandType.DELETE_TEXT;
			case "clear":
				return CommandType.CLEAR_CONTENT;
			case "search":
				return CommandType.SEARCH;
			case "sort":
				return CommandType.SORT;
			case "exit":
				return CommandType.EXIT;
			default: 
				return CommandType.INVALID;
		}
	}

	
	protected static boolean isInputNull(String userCommand) {
		if (userCommand.trim().isEmpty()){
			return true;
		} else {
			return false;
		}
	}
	
	protected static String parseUserInput() {
		printCommandPromptMessage();
		return scanner.nextLine();
	}
	
	protected static void readGivenFileName(String[] args) {
		fileName = (args[0]);
	}
	
	protected static void print(String message){
		System.out.println(message);
	}
	
	private static void printCommandPromptMessage() {
		System.out.print(MESSAGE_COMMAND_PROMPT);
	}

	protected static String formatWelcomeMessage() {
		return String.format(MESSAGE_WELCOME, fileName);
	}
	
	private static String formatAddedMessage(String textToAdd) {
		return String.format(MESSAGE_ADDED, fileName, textToAdd);
	}
	
	private static String formatInvalidFormatMessage(String userInput) {
		return String.format(MESSAGE_INVALID_FORMAT, userInput);
	}
	
	private static String formatFileEmptyMessage() {
		return String.format(MESSAGE_EMPTY, fileName);
	}
	
	private static String formatClearedTextMessage() {
		return String.format(MESSAGE_CLEAR, fileName);
	}
	
	private static String formatDeleteMessage(String removedCommand) {
		return String.format(MESSAGE_DELETE, fileName, removedCommand);
	}
	
	protected static String formatTextLine(int textIndex, String textLine){
		return String.format(MESSAGE_DISPLAY, textIndex, textLine);
	}
	
	protected static String removeFirstWord(String userInput) {
		return userInput.replace(getFirstWord(userInput), "").trim();
	}
	
	protected static String getFirstWord(String userInput) {
		return userInput.trim().split(REGEX_COMMAND_DELIMITER)[0];
	}
}
