import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * This class is used to add, display, delete and clear texts that the user
 * inputs into the system. If the user enters an invalid command, the message
 * "invalid command format" will be shown to the user. The TextBuddy program
 * will save the final user input into a text file which will be created upon
 * exiting the program.
 * 
 * For regression testing, the command line input was:
 * javac TextBuddy.java
 * java TextBuddy testoutput.txt < testinput.txt
 * fc testoutput.txt expected.txt
 * 
 * @author Rebekah Low Jin Yan
 */

public class TextBuddy {

	private static final String MESSAGE_EMPTY = "%1s is empty";
	private static final String MESSAGE_ADDED = "added to %1s: %2$s";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1s is ready for use";
	private static final String MESSAGE_NULL_COMMAND_TYPE = "command type string cannot be null!";

	enum CommandType {
		ADD_TEXT, DISPLAY_CONTENT, DELETE_TEXT, CLEAR_CONTENT, INVALID, EXIT
	};

	private static Scanner scanner = new Scanner(System.in);

	private static ArrayList<String> commandList = new ArrayList<String>();
	private static String fileName;

	public static void main(String[] args) {

		readGivenFileName(args);
		printWelcomeMessage();
		beginCommandLoop();

	}

	private static void printWelcomeMessage() {
		System.out.println(String.format(MESSAGE_WELCOME, fileName));
	}

	private static void beginCommandLoop() {
		while (true) {
			String userCommand = enterCommand();
			executeCommand(userCommand);
		}
	}

	private static String enterCommand() {
		System.out.print("command: ");
		String userCommand = scanner.nextLine();
		return userCommand;
	}

	public static void executeCommand(String userCommand) {
		if (isInvalidCommand(userCommand)){
			System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
		} else {
			String commandTypeString = getFirstWord(userCommand);
			CommandType commandType = determineCommandType(commandTypeString);
			executeCommandType(userCommand, commandType);
		}
	}

	private static boolean isInvalidCommand(String userCommand) {
		if (userCommand.trim().equals("")){
			return true;
		} else {
			return false;
		}
	}

	private static void executeCommandType(String userCommand, CommandType commandType) throws Error {
		switch (commandType) {
			case ADD_TEXT:
				addText(userCommand);
				break;
			case DISPLAY_CONTENT:
				displayContent();
				break;
			case DELETE_TEXT:
				deleteText(userCommand);
				break;
			case CLEAR_CONTENT:
				clearContent(userCommand);
				break;
			case INVALID:
				System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
			case EXIT:
				saveFile();
				System.exit(0);
			default:
				throw new Error("Unrecognized command type");
		}
	}

	private static CommandType determineCommandType(String commandTypeString) {
		handleNullCommand(commandTypeString);
		return getCommandType(commandTypeString);
	}

	private static void handleNullCommand(String commandTypeString) throws Error {
		if (commandTypeString == null){
			throw new Error(MESSAGE_NULL_COMMAND_TYPE);
		}
	}

	private static CommandType getCommandType(String commandTypeString) {
		if (commandTypeString.equalsIgnoreCase("add")) {
			return CommandType.ADD_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return CommandType.DISPLAY_CONTENT;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE_TEXT;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return CommandType.CLEAR_CONTENT;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}

	static void addText(String userCommand) {
		String commandText = "";
		commandText = removeFirstWord(userCommand);
		commandList.add(commandText);

		System.out.println(String.format(MESSAGE_ADDED, fileName, commandText));
	}

	private static void displayContent() {
		if (commandList.size() == 0) {
			System.out.println(String.format(MESSAGE_EMPTY, fileName));
		} else {
			for (int i = 0; i < commandList.size(); i++) {
				System.out.println((i + 1) + ". " + commandList.get(i));
			}
		}
	}

	private static void deleteText(String userCommand) {
		int lineNumberToDelete = -1;
		lineNumberToDelete = Integer.parseInt(removeFirstWord(userCommand));
		String removedCommand = commandList.remove(lineNumberToDelete - 1);
		System.out.println("deleted from " + fileName + ": " + removedCommand);
	}

	private static void clearContent(String userCommand) {
		commandList.clear();
		System.out.println(String.format(MESSAGE_CLEAR, fileName));

	}

	private static void saveFile() {
		try {
			FileOutputStream input = new FileOutputStream(fileName);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(input);
			Writer writer = new BufferedWriter(outputStreamWriter);
			for (int i = 0; i < commandList.size(); i++) {
				writer.write((i + 1) + ". " + commandList.get(i) + "\r\n");
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readGivenFileName(String[] args) {
		fileName = (args[0]);
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}
}
