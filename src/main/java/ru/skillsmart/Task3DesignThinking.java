package ru.skillsmart;

import lombok.Getter;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Task3DesignThinking {
    /**
     * По правде говоря, раньше я всегда писал тесты уже после кода, поэтому было интересно попробовать сделать наоборот.
     * Я выбрал для этого задания написание простенького консольного таск трекера, и TaskTrackerTest - первое, что я написал в проекте.
     * Дальше, как полагается, я набросал костяк основного сервисного класса TaskManager и пару моделей, затем написал реализацию методов.
     * Тесты есть, код для их работы написан, однако же, первая мысль после окончания этой части задания - что код не самый удачный и годится только к этим тестам.
     * Что-то поменять - и тесты придется переписывать (уже позже увидел эту мысль в 7) Три уровня рассуждений о программной системе - 2).
     * К тому же, с самим этим кодом в дальнейшем не очень удобно будет работать из-за его разрастания.
     * Прочитав материалы по мышлению о программе, стал думать, как она должна была бы выглядеть, будь она проектом помасштабнее и с некоторым запасом прочности.
     * Возможно, в будущем таск трекер стоило бы интегрировать с календарем, добавить подзадачи к задачам, и т.д.
     * Так что лучше выделить отдельно некую спецификацию того, что делает программа (пускай, в интерфейс), оставить класс TaskManager для описания взаимодействия с консолью,
     * а реализацию методов выдеть в отдельный класс.
     * С добавлением новой функциональности код стал выглядеть как ниже после слов Design Thinking approach.
     * Не сказать, что тесты при изменении подхода сильно поменялись, но, на мой вгзляд, в приложении появилась некая целостность и идея, которая раньше не прослеживалась.
     * В сухом остатке, для меня это хороший урок того, что лучше потратить побольше времени на продумывание дизайна и логической архитектуры, чем сразу садиться писать код,
     * или даже тесты к коду. Вот когда дизайн продуман, тогда можно и TDD использовать, и никаких побочных эффектов от этого не будет.
     * Спасибо.
     **/

    //TDD approach:
    public class TaskTrackerTest {

        /**
         * Task: id, name, description, status
         * TaskManager:    List<Task>
         * idCounter
         * actions:    addTask(String name, String description)
         * editTask(id, name, description)
         * changeStatus(id, TaskStatus)
         * displayAllTasks()
         */

        @Test
        public void addNewTask() {
            TaskManager taskManager = new TaskManager();
            String name = "New Task";
            String description = "New Task Description";
            taskManager.addTask(name, description);
            Task task1 = taskManager.getTaskList().get(0);
            assertEquals(0, task1.getId());
            assertEquals("New Task", task1.getName());
            assertEquals("New Task Description", task1.getDescription());
            assertEquals(TaskStatus.OPEN, task1.getStatus());
        }

        @Test
        public void editTask() {
            TaskManager taskManager = new TaskManager();

            String name = "New Task";
            String description = "New Task Description";
            taskManager.addTask(name, description);

            String editedName = "New Task updated";
            String editedDescription = "New Task Description updated";
            taskManager.editTask(0, editedName, editedDescription);

            Task task = taskManager.getTaskList().get(0);
            assertEquals(0, task.getId());
            assertEquals("New Task updated", task.getName());
            assertEquals("New Task Description updated", task.getDescription());
            assertEquals(TaskStatus.OPEN, task.getStatus());
        }

        @Test
        public void changeStatus() {
            TaskManager taskManager = new TaskManager();

            String name = "New Task";
            String description = "New Task Description";
            taskManager.addTask(name, description);

            taskManager.changeStatus(0, "completed");

            Task task = taskManager.getTaskList().get(0);
            assertEquals(TaskStatus.COMPLETED, task.getStatus());
        }

        @Test
        public void displayAllTasks() {
            TaskManager taskManager = new TaskManager();
            taskManager.addTask("New Task 1", "New Task Description 1");
            taskManager.addTask("New Task 2", "New Task Description 2");

            assertTrue(taskManager.displayAllTasks().contains("New Task 1"));
            assertTrue(taskManager.displayAllTasks().contains("New Task 2"));
        }
    }

    @ShellComponent
    public class TaskManager {

        @Getter
        private final List<Task> taskList = new ArrayList<>();
        private int idCounter = 0;

        private Task createTask(String name, String description) {
            return new Task(idCounter++, name, description, TaskStatus.OPEN);
        }

        @ShellMethod(key = "add", value = "Add a new task")
        public Task addTask(
                @ShellOption("-n") String name,
                @ShellOption(value = "-d", defaultValue = "") String description
        ) {
            Task newTask = createTask(name, description);
            taskList.add(newTask);
            return newTask;
        }

        @ShellMethod(key = "edit", value = "Edit task")
        public Task editTask(int id,
                             @ShellOption("-n") String name,
                             @ShellOption("-d") String description) {
            Task task = taskList.get(id);
            task.setName(name);
            task.setDescription(description);
            return task;
        }

        @ShellMethod("Change task status")
        public Task changeStatus(int id,
                                 String taskStatusName) {
            Task task = taskList.get(id);
            task.setStatus(TaskStatus.valueOf(taskStatusName.toUpperCase()));
            return task;
        }

        @ShellMethod("Display all tasks")
        public String displayAllTasks() {
            return this.taskList.toString();
        }
    }

    //Design Thinking approach:
    public interface TaskService {
        Task createTask(String name, String description, TaskPriority priority);

        Task editTask(int id, String name, String description, TaskPriority priority);

        Task changeStatus(int id, TaskStatus status);

        boolean deleteTask(int id);

        List<Task> searchTasksByName(String name);

        List<Task> sortTasksByPriority();

        List<Task> getAllTasks();
    }

    @ShellComponent
    public class TaskManager {

        private final TaskService taskService;
        private final StorageService storageService;

        public TaskManager(TaskService taskService, StorageService storageService) {
            this.taskService = taskService;
            this.storageService = storageService;
        }

        @ShellMethod(key = "add", value = "Add a new task")
        public Task addTask(@ShellOption("-n") String name,
                            @ShellOption(value = "-d", defaultValue = "") String description,
                            @ShellOption(value = "-p", defaultValue = "MEDIUM") TaskPriority priority) {
            return taskService.createTask(name, description, priority);
        }

        @ShellMethod(key = "edit", value = "Edit task")
        public Task editTask(int id,
                             @ShellOption("-n") String name,
                             @ShellOption("-d") String description,
                             @ShellOption(value = "-p", defaultValue = "MEDIUM") TaskPriority priority) {
            return taskService.editTask(id, name, description, priority);
        }

        @ShellMethod("Change task status")
        public Task changeStatus(int id, String taskStatusName) {
            return taskService.changeStatus(id, TaskStatus.valueOf(taskStatusName.toUpperCase()));
        }

        @ShellMethod(key = "delete", value = "Delete a task")
        public String deleteTask(int id) {
            return taskService.deleteTask(id) ? "Task deleted successfully" : "Task not found";
        }

        @ShellMethod(key = "search", value = "Search tasks by name")
        public List<Task> searchTasksByName(@ShellOption("-n") String name) {
            return taskService.searchTasksByName(name);
        }

        @ShellMethod(key = "sort", value = "Sort tasks by priority")
        public List<Task> sortTasksByPriority() {
            return taskService.sortTasksByPriority();
        }

        @ShellMethod("Display all tasks")
        public String displayAllTasks() {
            return taskService.getAllTasks().toString();
        }

        @ShellMethod("Save tasks to file")
        public String saveTasksToFile(@ShellOption("-f") String filename) {
            try {
                storageService.saveTasksToFile(filename);
                return "Tasks saved successfully";
            } catch (IOException e) {
                return "Error saving tasks: " + e.getMessage();
            }
        }

        @ShellMethod("Load tasks from file")
        public String loadTasksFromFile(@ShellOption("-f") String filename) {
            try {
                storageService.loadTasksFromFile(filename);
                return "Tasks loaded successfully";
            } catch (IOException | ClassNotFoundException e) {
                return "Error loading tasks: " + e.getMessage();
            }
        }
    }


    @Service
    public class TaskServiceImpl implements TaskService {
        private final List<Task> taskList = new ArrayList<>();
        private int idCounter = 0;

        @Override
        public Task createTask(String name, String description, TaskPriority priority) {
            Task newTask = new Task(idCounter++, name, description, TaskStatus.OPEN, priority);
            taskList.add(newTask);
            return newTask;
        }

        @Override
        public Task editTask(int id, String name, String description, TaskPriority priority) {
            Task task = taskList.get(id);
            task.setName(name);
            task.setDescription(description);
            task.setPriority(priority);
            return task;
        }

        @Override
        public Task changeStatus(int id, TaskStatus status) {
            Task task = taskList.get(id);
            task.setStatus(status);
            return task;
        }

        @Override
        public boolean deleteTask(int id) {
            if (id < 0 || id >= taskList.size()) {
                return false;
            }
            taskList.remove(id);
            return true;
        }

        @Override
        public List<Task> searchTasksByName(String name) {
            return taskList.stream()
                    .filter(task -> task.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        @Override
        public List<Task> sortTasksByPriority() {
            return taskList.stream()
                    .sorted(Comparator.comparing(Task::getPriority))
                    .collect(Collectors.toList());
        }

        @Override
        public List<Task> getAllTasks() {
            return new ArrayList<>(taskList);
        }
    }

    public class TaskTrackerTestUpdated {
        private TaskManager taskManager;
        private TaskService taskService;
        private StorageService storageService;

        @BeforeEach
        public void setup() {
            taskService = new TaskServiceImpl();
            taskManager = new TaskManager(taskService, storageService);
        }

        @Test
        public void addNewTask() {
            String name = "New Task";
            String description = "New Task Description";
            taskManager.addTask(name, description, TaskPriority.MEDIUM);
            Task task1 = taskService.getAllTasks().get(0);
            assertEquals(0, task1.getId());
            assertEquals("New Task", task1.getName());
            assertEquals("New Task Description", task1.getDescription());
            assertEquals(TaskStatus.OPEN, task1.getStatus());
        }

        @Test
        public void editTask() {
            String name = "New Task";
            String description = "New Task Description";
            taskManager.addTask(name, description, TaskPriority.MEDIUM);

            String editedName = "New Task updated";
            String editedDescription = "New Task Description updated";
            taskManager.editTask(0, editedName, editedDescription, TaskPriority.MEDIUM);

            Task task = taskService.getAllTasks().get(0);
            assertEquals(0, task.getId());
            assertEquals("New Task updated", task.getName());
            assertEquals("New Task Description updated", task.getDescription());
            assertEquals(TaskStatus.OPEN, task.getStatus());
        }

        @Test
        public void changeStatus() {
            String name = "New Task";
            String description = "New Task Description";
            taskManager.addTask(name, description, TaskPriority.MEDIUM);

            taskManager.changeStatus(0, "COMPLETED");

            Task task = taskService.getAllTasks().get(0);
            assertEquals(TaskStatus.COMPLETED, task.getStatus());
        }

        @Test
        public void displayAllTasks() {
            taskManager.addTask("New Task 1", "New Task Description 1", TaskPriority.MEDIUM);
            taskManager.addTask("New Task 2", "New Task Description 2", TaskPriority.MEDIUM);

            String allTasks = taskManager.displayAllTasks();
            assertTrue(allTasks.contains("New Task 1"));
            assertTrue(allTasks.contains("New Task 2"));
        }

        @Test
        public void deleteTask() {
            taskManager.addTask("Task to be deleted", "This task will be deleted", TaskPriority.MEDIUM);
            assertEquals(1, taskService.getAllTasks().size());

            taskManager.deleteTask(0);
            assertEquals(0, taskService.getAllTasks().size());
        }
    }
}
