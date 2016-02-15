import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * This class is used to add, display, delete and clear text that the user
 * inputs into the system. If the user enters an invalid command, the message
 * "invalid command format" will be shown to the user. The TextBuddy program
 * will save the final user input into a text file which will be created upon
 * exiting the program.
 * 
 * Assumption(s):
 * 1. Command(s) will be the first single word.
 * 2. Adding of empty line will not be permitted.
 * 3. Deleting non-valid line number will not be permitted.
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
	
	/**Regular expression used for splitting command*/
	private final static String REGEX_COMMAND_DELIMITER = "\\s+";
	
	private static final int ARRAY_OFFSET = 1;
	
	private static Scanner scanner = new Scanner(System.in);
	private static ArrayList<String> commandList = new ArrayList<String>();
	private static String fileName;
	
	/** Enumeration of command types */
	enum CommandType {
		ADD_TEXT, DISPLAY_CONTENT, DELETE_TEXT, CLEAR_CONTENT, INVALID, EXIT
	};
	
	/*
	* ===========================================
	* Main Program
	* ===========================================
	*/
	
	public static void main(String[] args) {
		readGivenFileName(args);
		printWelcomeMessage();
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
			printNullCommandTypeMessage();
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
			case EXIT:
				saveFile();
				System.exit(0);
			case INVALID:
			default:
				printInvalidFormatMessage(userInput);
		}
	}
	
	/**
	* Add text to be written into text file.
	*
	* @param userInput			User input to be added to text file.
	*/
	protected static void addText(String userInput) {
		String textToAdd = "";
		textToAdd = removeFirstWord(userInput);
		commandList.add(textToAdd);
		printAddedMessage(textToAdd);
	}
	
	/** Display content to be written into text file */
	protected static void displayContent() {
		if (commandList.isEmpty()) {
			printFileEmptyMessage();
		} else {
			for (int i = 0; i < commandList.size(); i++) {
				int textIndex = (i + ARRAY_OFFSET);
				String textLine = commandList.get(i);
				printTextLineByLine(textIndex, textLine);
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
		printLineDeletedMessage(removedCommand);
	}
	
	/** Clear content to be written into text file */
	protected static void clearContent(String userCommand) {
		commandList.clear();
		printClearedTextMessage();
	}

	/** Save content modified by user command into text file */
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
	protected static String formatTextLine(int textIndex, String textLine){
		return String.format(MESSAGE_DISPLAY, textIndex, textLine);
	}
	protected static void printWelcomeMessage() {
		System.out.println(String.format(MESSAGE_WELCOME, fileName));
	}
	
	private static void printCommandPromptMessage() {
		System.out.print(MESSAGE_COMMAND_PROMPT);
	}
	
	private static void printAddedMessage(String textToAdd) {
		System.out.println(String.format(MESSAGE_ADDED, fileName, textToAdd));
	}
	
	private static void printInvalidFormatMessage(String userInput) {
		System.out.println(String.format(MESSAGE_INVALID_FORMAT, userInput));
	}

	private static void printNullCommandTypeMessage() {
		System.out.println(String.format(MESSAGE_NULL_COMMAND_TYPE));
	}
	
	private static void printFileEmptyMessage() {
		System.out.println(String.format(MESSAGE_EMPTY, fileName));
	}
	
	private static void printClearedTextMessage() {
		System.out.println(String.format(MESSAGE_CLEAR, fileName));
	}
	
	private static void printLineDeletedMessage(String removedCommand) {
		System.out.println(String.format(MESSAGE_DELETE, fileName, removedCommand));
	}
	
	private static void printTextLineByLine(int textIndex, String textLine) {
		System.out.println(formatTextLine(textIndex, textLine));
	}
	
	protected static String removeFirstWord(String userInput) {
		return userInput.replace(getFirstWord(userInput), "").trim();
	}
	
	protected static String getFirstWord(String userInput) {
		return userInput.trim().split(REGEX_COMMAND_DELIMITER)[0];
	}
}
