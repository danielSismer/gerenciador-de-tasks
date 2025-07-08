package net.intra.model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {

    protected String id;
    protected String description;
    protected LocalDate creationDate;
    protected Priority priority;
    protected Status status;

    public enum Priority {
        LOW, MEDIUM, HIGH;
    }

    public enum Status {
        TO_DO, IN_PROGRESS, DONE;
    }

    public Task(String id, String description, Priority priority, Status status, LocalDate creationDate) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.creationDate = creationDate != null ? creationDate : LocalDate.now();
    }

    public Task(String description, Priority priority, Status status) {
        this(UUID.randomUUID().toString(), description, priority, status, LocalDate.now());
    }

    // Getters e setters
    public String getId() { return id; }
    public String getDescription() { return description; }
    public LocalDate getCreationDate() { return creationDate; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }

    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nDescrição: " + description +
                "\nCriada em: " + creationDate +
                "\nPrioridade: " + priority +
                "\nStatus: " + status;
    }
}
