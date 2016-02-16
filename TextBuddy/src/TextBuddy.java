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
		showToUser(formatWelcomeMessage());
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
		if (isInputCommandNull(userInput)){
			showToUser(MESSAGE_NULL_COMMAND_TYPE);
		} else {
			CommandType commandType = getCommandType(userInput);
			Object feedback = executeCommandType(userInput, commandType);
			if(feedback instanceof String){
				String feedbackMessage = (String) feedback;
				if(feedbackMessage != null){
					showToUser(feedbackMessage);
				}
			}
		}
	}
	
	private static CommandType getCommandType(String userInput) {
		String commandTypeString = getFirstWord(userInput);
		return getCommandTypeFromString(commandTypeString);
	}
	
	protected static Object executeCommandType(String userInput, CommandType commandType) {
		switch (commandType) {
			case ADD_TEXT:
				return addText(userInput);
			case DISPLAY_CONTENT:
				return displayContent();
			case DELETE_TEXT:
				return deleteText(userInput);
			case CLEAR_CONTENT:
				return clearContent();
			case SEARCH:
				return searchText(userInput);
			case SORT:
				return sortLines();
			case EXIT:
				saveFile();
				System.exit(0);
				return null;
			case INVALID:
			default:
				return formatInvalidFormatMessage(userInput);
		}
	}
	
	/**
	* Add text to be written into text file.
	*
	* @param userInput			String to add to text file.
	*/
	protected static String addText(String userInput) {
		String textToAdd = removeFirstWord(userInput);
		commandList.add(textToAdd);
		return formatAddedMessage(textToAdd);
	}
	
	/** Display content to be written into text file. 
	 * 
	 * @return emptyMessage		Empty file message to be shown to user.
	 * @return textDisplay		Text lines to be shown to user. 
	 */
	protected static String displayContent() {
		if (commandList.isEmpty()) {
			String emptyMessage = formatFileEmptyMessage();
			return emptyMessage;
		} else {
			for (int i = 0; i < commandList.size(); i++) {
				int textIndex = (i + ARRAY_OFFSET);
				String textLine = commandList.get(i);
				String textDisplay = formatTextLine(textIndex, textLine);
				showToUser(textDisplay);
			}
		}
		return null;
	}

	/**
	* Delete text to be written into text file.
	*
	* @param userInput			String line number to deleted in text file.
	* @return deleteMessage		Delete message to be shown to user.
	*/
	protected static String deleteText(String userInput) {
		int lineNumberToDelete = Integer.parseInt(removeFirstWord(userInput));
		int arrayIndexToDelete = (lineNumberToDelete - ARRAY_OFFSET);
		String removedCommand = commandList.remove(arrayIndexToDelete);
		String deleteMessage = formatDeleteMessage(removedCommand);
		return deleteMessage;
	}
	
	/** Clear content to be written into text file. 
	 * 
	 * @return clearMessage		Cleared message to be shown to user.
	 */
	protected static String clearContent() {
		commandList.clear();
		String clearMessage = formatClearedTextMessage();
		return clearMessage;
	}
	
	/** Search for a word in the file and return the lines containing that word. 
	 * 
	 * @param userInput			String to search in text file.
	 * @return foundList		ArrayList containing the lines containing search word.
	 */
	protected static ArrayList <String> searchText (String userInput) {
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
			showToUser(MESSAGE_SEARCH_FAILED);
		} else for (int i = 0; i < foundList.size(); i++){
			String foundLine = foundList.get(i);
			showToUser(foundLine);
		}
		return foundList;
	}
	
	/** Sort lines alphabetically and change the order lines are arranged. 
	 * 
	 * @return MESSAGE_SORT		Sorted message to be shown to user. 
	 */
	protected static String sortLines () {
		Collections.sort(commandList, String.CASE_INSENSITIVE_ORDER);
		return MESSAGE_SORT;
	}
	
	/** Save content modified by user command into text file. */
	protected static String saveFile() {
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
		return null;
	}
	
	/**
	* Converts String to its CommandType equivalent.
	*
	* @param commandTypeString	String to be converted to CommandType.
	* @return 					CommandType equivalent.
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

	protected static void showToUser(String feedback){
		System.out.println(feedback);
	}
	
	protected static boolean isInputCommandNull(String userCommand) {
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
	
	protected static String getGivenFileName() {
		return fileName;
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