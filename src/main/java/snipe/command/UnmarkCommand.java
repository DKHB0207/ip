package snipe.command;

import snipe.core.TaskList;
import snipe.exception.SnipeException;
import snipe.storage.Storage;
import snipe.task.Task;
import snipe.util.Ui;

import java.io.IOException;

/**
 * The {@code UnmarkCommand} class represents a command to unmark a task in the task list as completed.
 * It updates the task status, saves the updated task list to storage, and provides feedback to the user via the user interface.
 */
public class UnmarkCommand extends Command{
    private int num;

    /**
     * Constructs a {@code UnmarkCommand} with the specified index of the task to be marked as not done.
     *
     * @param num The index of the task to be marked as completed (0-based index).
     */
    public UnmarkCommand(int num) {
        this.num = num;
    }

    /**
     * Executes the unmark command by changing the status of the specified task to "not done" if it is currently marked as done,
     * saving the updated task list to storage, and returning a confirmation message to the user.
     * If the task is already unmarked, returns a message indicating that.
     *
     * @param tasks   The {@link TaskList} containing the task to be unmarked.
     * @param ui      The {@link Ui} instance used to display messages to the user.
     * @param storage The {@link Storage} instance used to save the updated task list.
     * @return A message confirming the task has been marked as not done, or a message indicating the task was already unmarked.
     * @throws SnipeException If the specified task index is out of range or if an error occurs while saving the task list.
     * @throws IOException    If an I/O error occurs during the saving process.
     */
    @Override
    public String getResponse(TaskList tasks, Ui ui, Storage storage) throws SnipeException, IOException {
        if (this.num > tasks.size() - 1) {
            throw new SnipeException("This list item does not exist!\n"
                    + tasks.listLength());
        } else {
            Task task = tasks.getTask(this.num);
            if (task.getStatus()) {
                task.changeStatus();
                String msg = "OK, I've marked this task as not done yet:\n" +
                        task.toString();
                storage.saveTaskList(tasks);
                return msg;
            } else {
                String msg = "This task is currently not done yet!";
                return msg;
            }
        }
    }
}
