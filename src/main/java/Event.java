import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDate date;

    Event(String description, LocalDate date) {
        super(description);
        this.date = date;
    }

    Event(String description, LocalDate date, String completionStatus) {
        super(description, completionStatus);
        this.date = date;
    }

    @Override
    String getType() {
        return "E";
    }

    @Override
    LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: " + date.format(DateTimeFormatter.ofPattern("MMMM d yyyy")) + ")";
    }
}
