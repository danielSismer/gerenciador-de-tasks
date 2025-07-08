package net.intra.service;

import net.intra.model.Task;
import net.intra.model.TaskWithDeadline;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskService {

    private final Connection connection;

    public TaskService(Connection connection) {
        this.connection = connection;
    }

    public void addTask(String description, Task.Priority priority) throws SQLException {
        String sql = "INSERT INTO tasks (id, description, priority, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, description);
            stmt.setString(3, priority.name());
            stmt.setString(4, Task.Status.TO_DO.name());
            stmt.executeUpdate();
        }
    }

    public void addTaskWithDeadline(String description, Task.Priority priority, LocalDate deadline) throws SQLException {
        String sql = "INSERT INTO tasks (id, description, priority, status, deadline) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, description);
            stmt.setString(3, priority.name());
            stmt.setString(4, Task.Status.TO_DO.name());
            stmt.setDate(5, Date.valueOf(deadline));
            stmt.executeUpdate();
        }
    }

    public boolean updateStatus(String id, Task.Status newStatus) throws SQLException {
        String selectSQL = "SELECT status FROM tasks WHERE id = ?";
        String updateSQL = "UPDATE tasks SET status = ? WHERE id = ?";
        String insertHistorySQL = "INSERT INTO task_status_history (task_id, old_status, new_status) VALUES (?, ?, ?)";

        try (
                PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
                PreparedStatement updateStmt = connection.prepareStatement(updateSQL);
                PreparedStatement insertStmt = connection.prepareStatement(insertHistorySQL)
        ) {
            selectStmt.setString(1, id);
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) return false;

            String oldStatus = rs.getString("status");

            updateStmt.setString(1, newStatus.name());
            updateStmt.setString(2, id);
            updateStmt.executeUpdate();

            insertStmt.setString(1, id);
            insertStmt.setString(2, oldStatus);
            insertStmt.setString(3, newStatus.name());
            insertStmt.executeUpdate();

            return true;
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, description, priority, status, creation_date, deadline FROM tasks";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate deadline = rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null;
                LocalDate creationDate = rs.getDate("creation_date") != null ? rs.getDate("creation_date").toLocalDate() : null;

                Task task;
                if (deadline != null) {
                    task = new TaskWithDeadline(
                            rs.getString("id"),
                            rs.getString("description"),
                            Task.Priority.valueOf(rs.getString("priority")),
                            Task.Status.valueOf(rs.getString("status")),
                            creationDate,
                            deadline
                    );
                } else {
                    task = new Task(
                            rs.getString("id"),
                            rs.getString("description"),
                            Task.Priority.valueOf(rs.getString("priority")),
                            Task.Status.valueOf(rs.getString("status")),
                            creationDate
                    );
                }

                tasks.add(task);
            }
        }
        return tasks;
    }

    public List<Task> filterByStatus(Task.Status status) throws SQLException {
        List<Task> filtered = new ArrayList<>();
        String sql = "SELECT id, description, priority, status, creation_date, deadline FROM tasks WHERE status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate deadline = rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null;
                    LocalDate creationDate = rs.getDate("creation_date") != null ? rs.getDate("creation_date").toLocalDate() : null;

                    Task task;
                    if (deadline != null) {
                        task = new TaskWithDeadline(
                                rs.getString("id"),
                                rs.getString("description"),
                                Task.Priority.valueOf(rs.getString("priority")),
                                Task.Status.valueOf(rs.getString("status")),
                                creationDate,
                                deadline
                        );
                    } else {
                        task = new Task(
                                rs.getString("id"),
                                rs.getString("description"),
                                Task.Priority.valueOf(rs.getString("priority")),
                                Task.Status.valueOf(rs.getString("status")),
                                creationDate
                        );
                    }
                    filtered.add(task);
                }
            }
        }
        return filtered;
    }

    public List<Task> filterByPriority(Task.Priority priority) throws SQLException {
        List<Task> filtered = new ArrayList<>();
        String sql = "SELECT id, description, priority, status, creation_date, deadline FROM tasks WHERE priority = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, priority.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate deadline = rs.getDate("deadline") != null ? rs.getDate("deadline").toLocalDate() : null;
                    LocalDate creationDate = rs.getDate("creation_date") != null ? rs.getDate("creation_date").toLocalDate() : null;

                    Task task;
                    if (deadline != null) {
                        task = new TaskWithDeadline(
                                rs.getString("id"),
                                rs.getString("description"),
                                Task.Priority.valueOf(rs.getString("priority")),
                                Task.Status.valueOf(rs.getString("status")),
                                creationDate,
                                deadline
                        );
                    } else {
                        task = new Task(
                                rs.getString("id"),
                                rs.getString("description"),
                                Task.Priority.valueOf(rs.getString("priority")),
                                Task.Status.valueOf(rs.getString("status")),
                                creationDate
                        );
                    }
                    filtered.add(task);
                }
            }
        }
        return filtered;
    }

    public Map<Task.Status, Long> getStatusSummary() throws SQLException {
        String sql = "SELECT status, COUNT(*) as total FROM tasks GROUP BY status";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            Map<Task.Status, Long> map = new java.util.HashMap<>();
            while (rs.next()) {
                Task.Status status = Task.Status.valueOf(rs.getString("status"));
                long total = rs.getLong("total");
                map.put(status, total);
            }
            return map;
        }
    }
}
