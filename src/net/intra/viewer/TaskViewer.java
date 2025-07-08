package net.intra.viewer;

import net.intra.model.Task;
import net.intra.model.TaskWithDeadline;
import net.intra.model.Task.Priority;
import net.intra.model.Task.Status;
import net.intra.service.TaskService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TaskViewer {

    private final Scanner scanner = new Scanner(System.in);
    private final TaskService service;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TaskViewer(TaskService service) {
        this.service = service;
    }

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            System.out.println("\n================= MENU DE TAREFAS =================");
            System.out.println(" 1. Criar tarefa simples");
            System.out.println(" 2. Criar tarefa com prazo");
            System.out.println(" 3. Listar todas as tarefas");
            System.out.println(" 4. Atualizar status da tarefa");
            System.out.println(" 5. Filtrar tarefas por status");
            System.out.println(" 6. Filtrar tarefas por prioridade");
            System.out.println(" 7. Exibir resumo por status");
            System.out.println(" 0. Sair");
            System.out.println("===================================================");
            System.out.print("Escolha uma opção: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada inválida. Digite um número.");
                continue;
            }

            System.out.println();

            try {
                switch (option) {
                    case 1 -> criarTarefa(false);
                    case 2 -> criarTarefa(true);
                    case 3 -> listarTarefas(service.getAllTasks());
                    case 4 -> atualizarStatus();
                    case 5 -> filtrarPorStatus();
                    case 6 -> filtrarPorPrioridade();
                    case 7 -> mostrarResumo();
                    case 0 -> System.out.println("Encerrando o sistema. Até logo!");
                    default -> System.out.println("[!] Opção inválida. Tente novamente.");
                }
            } catch (SQLException e) {
                System.out.println("[!] Erro de banco: " + e.getMessage());
            }
        }
    }

    private void criarTarefa(boolean comPrazo) throws SQLException {
        try {
            System.out.println("------ Criar Tarefa " + (comPrazo ? "com Prazo" : "Simples") + " ------");

            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            Priority prioridade = escolherPrioridade();

            if (comPrazo) {
                System.out.print("Data de prazo (dd/MM/yyyy): ");
                String dataStr = scanner.nextLine();
                LocalDate prazo = LocalDate.parse(dataStr, dateFormatter);
                service.addTaskWithDeadline(descricao, prioridade, prazo);
            } else {
                service.addTask(descricao, prioridade);
            }

            System.out.println("[✔] Tarefa criada com sucesso!");
        } catch (IllegalArgumentException | DateTimeParseException e) {
            System.out.println("[!] Erro ao criar tarefa: " + e.getMessage());
        }
    }

    private void listarTarefas(List<Task> tarefas) {
        System.out.println("------ Lista de Tarefas ------");

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
            return;
        }

        int i = 1;
        for (Task task : tarefas) {
            System.out.printf("[%d] %s\n", i++, formatarTarefa(task));
        }
    }

    private void atualizarStatus() throws SQLException {
        System.out.println("------ Atualizar Status da Tarefa ------");

        System.out.print("ID da tarefa: ");
        String id = scanner.nextLine();

        try {
            Status novoStatus = escolherStatus();
            boolean atualizada = service.updateStatus(id, novoStatus);
            System.out.println(atualizada ? "[✔] Status atualizado!" : "[!] Tarefa não encontrada.");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Status inválido.");
        }
    }

    private void filtrarPorStatus() throws SQLException {
        try {
            Status status = escolherStatus();
            List<Task> filtradas = service.filterByStatus(status);
            System.out.println("------ Tarefas com status: " + status + " ------");
            listarTarefas(filtradas);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Status inválido.");
        }
    }

    private void filtrarPorPrioridade() throws SQLException {
        try {
            Priority prioridade = escolherPrioridade();
            List<Task> filtradas = service.filterByPriority(prioridade);
            System.out.println("------ Tarefas com prioridade: " + prioridade + " ------");
            listarTarefas(filtradas);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Prioridade inválida.");
        }
    }

    private void mostrarResumo() throws SQLException {
        System.out.println("------ Resumo de Tarefas por Status ------");
        var resumo = service.getStatusSummary();

        if (resumo.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            resumo.forEach((status, total) ->
                    System.out.printf("- %s: %d tarefas%n", formatarEnum(status.name()), total)
            );
        }
    }

    private Priority escolherPrioridade() {
        System.out.print("Prioridade (LOW, MEDIUM, HIGH): ");
        return Priority.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private Status escolherStatus() {
        System.out.print("Status (TO_DO, IN_PROGRESS, DONE): ");
        return Status.valueOf(scanner.nextLine().trim().toUpperCase());
    }

    private String formatarTarefa(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(task.getId()).append("\n");
        sb.append("   📌 Descrição: ").append(task.getDescription()).append("\n");
        sb.append("   🕒 Criada em: ").append(task.getCreationDate()).append("\n");
        sb.append("   🏷  Prioridade: ").append(formatarEnum(task.getPriority().name())).append("\n");
        sb.append("   📂 Status: ").append(formatarEnum(task.getStatus().name()));
        System.out.println("===================================================");

        if (task instanceof TaskWithDeadline tdl) {
            sb.append("\n   ⏳ Prazo: ").append(tdl.getDeadline());
        }

        return sb.toString();
    }

    private String formatarEnum(String valorEnum) {
        String formatado = valorEnum.replace("_", " ").toLowerCase();
        return Character.toUpperCase(formatado.charAt(0)) + formatado.substring(1);
    }
}
