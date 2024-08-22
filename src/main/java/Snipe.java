import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Snipe {
    private static final String NAME = "Snipe";
    private static final String LOGO
            = "  _________      .__               \n"
            + " /   _____/ ____ |__|_____   ____  \n"
            + " \\_____  \\ /    \\|  \\____ \\_/ __ \\ \n"
            + " /        \\   |  \\  |  |_> >  ___/ \n"
            + "/_______  /___|  /__|   __/ \\___  >\n"
            + "        \\/     \\/   |__|        \\/ \n";
    private static final String HORIZONTAL_LINE = "_".repeat(60);
    private static ArrayList<Task> list = new ArrayList<Task>();
    public void initChat() {
        greetUser();
        handleUserInput();
    }

    private void greetUser() {
        String OPENING_MESSAGE = "Hello! I'm\n" + NAME +"\nWhat can I do for you?";
        printWithLines(OPENING_MESSAGE);
    }
    private void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("bye")) {
                exitChat(scanner);
                break;
            }
            handleSpecialInputs(userInput, scanner);
        }
    }
    private String isValidInput(String userInput) {
        String[][] validInputs = {
                {"misc", "list", "delete", "help"},
                {"done", "mark", "unmark"},
                {"task", "todo", "deadline", "event"}
        };
        for (String[] validInput : validInputs) {
            for (int i = 1; i < validInput.length; i++) {
                if (userInput.startsWith(validInput[i])) {
                    return validInput[0];
                }
            }

        }
        return null;
    }
    private void handleSpecialInputs(String userInput, Scanner scanner) {
        try {
             String inputType = isValidInput(userInput);
            if (inputType != null) {
                if (inputType.equals("misc")) {
                    if (userInput.equalsIgnoreCase("list")) {
                        returnList();
                    } else if (userInput.equalsIgnoreCase("help")) {
                        String filePath = "src/main/txt/helpinstructions.txt"; // Instructions manual
                        try {
                            String content = new String(Files.readAllBytes(Paths.get(filePath)));
                            printWithLines(content); // Print the instructions
                        } catch (IOException e) {
                            System.out.println("Error reading the file: " + e.getMessage());
                        }
                    } else if (userInput.startsWith("delete")) {
                        String[] split = userInput.split(" ", 2);
                        int index = Integer.valueOf(split[1]) - 1;
                        Task toRemove = list.get(index);
                        list.remove(index);
                        String message = "Noted. I've removed this task:\n"
                                + toRemove.toString()
                                + listLength();
                        printWithLines(message);
                    }
                } else if (inputType.equals("done")) {
                    String[] split = userInput.split(" ");
                    markAndUnmarkTask(split);
                } else if (inputType.equals("task")){
                    String[] split = userInput.split(" ", 2);
                    Task newTask = addTask(split);
                    list.add(newTask);
                    int listSize = list.size();
                    String message = " Got it. I've added this task:\n  "
                            + newTask.toString()
                            + listLength();
                    printWithLines(message);
                }
            } else {
                throw new SnipeException("That is not a valid input!\nTo see list of valid inputs, try the 'help' command");
            }
        } catch(SnipeException e) {
            printWithLines(e.getMessage());
        }
    }
    private String listLength() {
        return String.format("\n Now you have %d %s in the list.", list.size(), list.size() == 1 ? "task" : "tasks");
    }
    private void markAndUnmarkTask(String[] split) throws SnipeException{
        int index = Integer.valueOf(split[1]) - 1;
        if (index > list.size() - 1) {
            throw new SnipeException("This list item does not exist!\n"
                    + listLength());
        } else {
            Task task = list.get(index);
            String userInput = split[0];
            boolean successful = task.getStatus() ^ userInput.equals("mark");
            if (successful) {
                task.changeStatus();
                String markAndNotDone = "Nice! I've marked this task as done:\n" +
                        task.toString();
                String unmarkAndDone = "OK, I've marked this task as not done yet:\n" +
                        task.toString();
                printWithLines(userInput.equals("mark") ? markAndNotDone : unmarkAndDone);
            } else {
                String markAndDone = "This task is already marked done!";
                String unmarkAndNotDone = "This task is currently not done yet!";
                printWithLines(userInput.equals("mark") ? markAndDone : unmarkAndNotDone);
            }
        }
    }
    private Task addTask(String[] split) throws SnipeException {
        try {
            if (split.length < 2 || split[1].trim().isEmpty()) {
                throw new SnipeException("NOOOOO!! The description of a task cannot be empty." +
                        "\nTo see how the commands can be implemented, use 'help'.");
            } else {
                String taskType = split[0];
                if (taskType.equals("todo")) {
                    return new ToDo(split[1]);
                } else if (taskType.equals("deadline")) {
                    String[] toSplit = split[1].split(" /by ", 2);
                    String description = toSplit[0];
                    String deadline = toSplit[1];
                    return new Deadline(description, deadline);
                } else if (taskType.equals("event")) {
                    String[] toSplit = split[1].split(" /from | /to ");
                    String description = toSplit[0];
                    String start = toSplit[1];
                    String end = toSplit[2];
                    return new Event(description, start, end);
                }
                throw new SnipeException("Invalid Task given");
            }
        } catch (Exception e) {
            throw new SnipeException("Your task input format is wrong!" +
                    "\nTo see how the commands can be implemented, use 'help'.");
        }
    }
    private void returnList() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("Here are the tasks in your list:");
        for(int i = 0; i < list.size(); i++) {
            String item = String.format("%d. ", i + 1) + list.get(i).toString();
            System.out.println(item);
        }
        System.out.println(HORIZONTAL_LINE);
    }
    private void printWithLines(String message) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(message);
        System.out.println(HORIZONTAL_LINE);
    }
    private void exitChat(Scanner scanner) {
        String CLOSING_MESSAGE = "Bye. Hope to see you again soon!";
        printWithLines(CLOSING_MESSAGE);
        scanner.close();
    }
    public static void main(String[] args) {
        Snipe snipe = new Snipe();
        snipe.initChat();
    }
}


