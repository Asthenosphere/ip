import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Parser {
    static Command parse(String fullCommand) throws DukeException {
        String[] fullCommandArray = fullCommand.split(" ");
        fullCommand = fullCommand.strip();
        if (fullCommand.equals("bye")) {
            return new ByeCommand();
        } else if (fullCommand.equals("list")) {
            return new ListCommand();
        } else if (fullCommand.equals("show")) {
            throw new InvalidArgumentException("☹ OOPS!!! The show command requires a date in yyyy-mm-dd.");
        } else if (fullCommandArray[0].equals("show")) {
            try {
                LocalDate date = LocalDate.parse(fullCommandArray[1]);
                return new ShowCommand(date);
            } catch (DateTimeParseException e) {
                throw new InvalidArgumentException("☹ OOPS!!! The show command requires a date in yyyy-mm-dd.");
            }
        } else if (fullCommand.equals("done") || fullCommand.equals("delete")) {
            throw new InvalidCommandException("☹ OOPS!!! The index of a task cannot be empty.");
        } else if (fullCommandArray[0].equals("done")) {
            return new DoneCommand(Integer.parseInt(fullCommandArray[1]));
        } else if (fullCommandArray[0].equals("delete")) {
            return new DeleteCommand(Integer.parseInt(fullCommandArray[1]));
        } else {
            String type = fullCommand.split(" ")[0];
            String temp = fullCommand.strip();
            if (temp.equals(TaskType.TODO.getType()) || temp.equals(TaskType.DEADLINE.getType()) || temp.equals(TaskType.EVENT.getType())) {
                throw new InvalidArgumentException("☹ OOPS!!! The description of a " + temp + " cannot be empty.");
            } else if (temp.equals("")) {
                throw new InvalidTaskTypeException("☹ OOPS!!! The type of a task cannot be empty.");
            }
            if (type == null || (!type.equals(TaskType.TODO.getType()) && !type.equals(TaskType.DEADLINE.getType()) && !type.equals(TaskType.EVENT.getType()))) {
                throw new InvalidTaskTypeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
            String details = fullCommand.substring(type.length());
            if (type.equals(TaskType.TODO.getType())) {
                ToDo t = new ToDo(details.strip());
                return new AddCommand(t);
            } else if (type.equals(TaskType.DEADLINE.getType())) {
                String[] detailsArray = details.split("/by");
                LocalDate date;
                try {
                    date = LocalDate.parse(detailsArray[1].strip());
                } catch (DateTimeParseException e) {
                    throw new InvalidArgumentException("☹ OOPS!!! Please enter a date in yyyy-mm-dd format.");
                }
                Deadline d = new Deadline(detailsArray[0].strip(), date);
                return new AddCommand(d);
            } else {
                String[] detailsArray = details.split("/at");
                LocalDate date;
                try {
                    date = LocalDate.parse(detailsArray[1].strip());
                } catch (DateTimeParseException e) {
                    throw new InvalidArgumentException("☹ OOPS!!! Please enter a date in yyyy-mm-dd format.");
                }
                Event e = new Event(detailsArray[0].strip(), date);
                return new AddCommand(e);
            }
        }
    }
}
