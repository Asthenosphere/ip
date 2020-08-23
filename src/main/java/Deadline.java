import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDate endDate;

    Deadline(String description, LocalDate endDate) {
        super(description);
        this.endDate = endDate;
    }

    Deadline(String description, LocalDate endDate, String completionStatus) {
        super(description, completionStatus);
        this.endDate = endDate;
    }

    @Override
    String getType() {
        return "E";
    }

    @Override
    LocalDate getDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + endDate.format(DateTimeFormatter.ofPattern("MMMM d yyyy")) + ")";
    }
}
