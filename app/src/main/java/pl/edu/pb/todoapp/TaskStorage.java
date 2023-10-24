package pl.edu.pb.todoapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static TaskStorage instance = new TaskStorage();
    private List<Task> tasks;

    private TaskStorage() {
        final int tasksCount = 20;
        tasks = new ArrayList<Task>(tasksCount);
        for (int i = 1; i <= tasksCount; ++i) {
            Task task = new Task();
            task.setName("Task" + i);
            task.setDone(i % 3 == 0);
            tasks.add(task);
        }
    }

    public static TaskStorage getInstance() {
        return instance;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id) {
        for (Task task : tasks) {
            if (task.getId().equals(id))
                return task;
        }
        return null;
    }
}
