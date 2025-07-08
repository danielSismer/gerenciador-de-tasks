package net.intra.model;

import java.time.LocalDate;
import java.util.UUID;

public class TaskWithDeadline extends Task {

    private LocalDate deadline;

    public TaskWithDeadline(String id, String description, Priority priority, Status status, LocalDate creationDate, LocalDate deadline) {
        super(id, description, priority, status, creationDate);
        this.deadline = deadline;
    }

    public TaskWithDeadline(String description, Priority priority, Status status, LocalDate deadline) {
        this(UUID.randomUUID().toString(), description, priority, status, LocalDate.now(), deadline);
    }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    @Override
    public String toString() {
        return super.toString() + "\nPrazo: " + deadline;
    }
}
