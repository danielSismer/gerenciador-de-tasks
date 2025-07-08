package net.intra.main;

import net.intra.database.DatabaseConnection;
import net.intra.service.TaskService;
import net.intra.viewer.TaskViewer;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            TaskService service = new TaskService(conn);
            TaskViewer viewer = new TaskViewer(service);
            viewer.showMenu();
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
