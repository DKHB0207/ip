package snipe.core;

import snipe.exception.SnipeException;
import snipe.task.Task;

import java.util.ArrayList;

public class TaskList {

    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int index) throws SnipeException {
        if (index >= tasks.size() || index < 0) {
            throw new SnipeException("This list item does not exist!");
        }
        tasks.remove(index);
    }

    public Task getTask(int index) throws SnipeException {
        if (index >= tasks.size() || index < 0) {
            throw new SnipeException("This list item does not exist!");
        }
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public String listLength() {
        return String.format("\n Now you have %d %s in the list.", tasks.size(), tasks.size() == 1 ? "task" : "tasks");
    }
}
