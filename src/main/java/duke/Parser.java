package duke;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import duke.command.AddCommand;
import duke.command.ByeCommand;
import duke.command.Command;
import duke.command.CompletedCommand;
import duke.command.DeleteCommand;
import duke.command.DoneCommand;
import duke.command.FindCommand;
import duke.command.ListCommand;
import duke.command.PendingCommand;
import duke.command.ShowCommand;
import duke.exception.DukeException;
import duke.exception.InvalidArgumentException;
import duke.exception.InvalidCommandException;
import duke.exception.InvalidTaskTypeException;
import duke.task.CommandType;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.TaskType;
import duke.task.ToDo;

/**
 * The Parser class parses and processes user input.
 */
public class Parser {
    /**
     * Parses the given command based on different keywords.
     * Returns the appropriate {@link Command} to execute next.
     *
     * @param fullCommand A String read from user input.
     * @return The corresponding {@link Command} for the input.
     * @throws DukeException Exception when parsing the input.
     */
    public static Command parse(String fullCommand) throws DukeException {
        fullCommand = fullCommand.toLowerCase();
        String[] fullCommandArray = fullCommand.split(" ");
        fullCommand = fullCommand.strip();

        /*
        Returns the correct command based on user input.
        Invalid input will cause respective Exceptions to be thrown.
         */
        if (fullCommand.equals(CommandType.BYE.getType())) {
            return new ByeCommand();
        } else if (fullCommand.equals(CommandType.LIST.getType())) {
            return new ListCommand();
        } else if (fullCommand.equals(CommandType.PENDING.getType())) {
            return new PendingCommand();
        } else if (fullCommand.equals(CommandType.COMPLETED.getType())) {
            return new CompletedCommand();
        } else if (fullCommand.equals(CommandType.SHOW.getType())) {
            throw new InvalidArgumentException("☹ OOPS!!! The show command requires a date in yyyy-mm-dd.");
        } else if (fullCommand.equals(CommandType.FIND.getType())) {
            throw new InvalidArgumentException("☹ OOPS!!! The find command requires keyword.");
        } else if (fullCommand.equals(CommandType.DELETE.getType())) {
            throw new InvalidCommandException("☹ OOPS!!! The delete command requires the index of a task.");
        } else if (fullCommand.equals(CommandType.DONE.getType())) {
            throw new InvalidCommandException("☹ OOPS!!! The done command requires the index of a task.");
        } else if (fullCommandArray[0].equals(CommandType.LIST.getType())) {
            throw new InvalidArgumentException("☹ OOPS!!! The list command does not take any additional argument(s).");
        } else if (fullCommandArray[0].equals(CommandType.BYE.getType())) {
            throw new InvalidArgumentException("☹ OOPS!!! The bye command does not take any additional argument(s).");
        } else if (fullCommandArray[0].equals(CommandType.PENDING.getType())) {
            throw new InvalidArgumentException(
                    "☹ OOPS!!! The pending command does not take any additional argument(s).");
        } else if (fullCommandArray[0].equals(CommandType.COMPLETED.getType())) {
            throw new InvalidArgumentException(
                    "☹ OOPS!!! The completed command does not take any additional argument(s).");
        } else if (fullCommandArray[0].equals(CommandType.SHOW.getType())) {
            try {
                LocalDate date = LocalDate.parse(fullCommandArray[1]);
                return new ShowCommand(date);
            } catch (DateTimeParseException e) {
                throw new InvalidArgumentException("☹ OOPS!!! The show command requires a date in yyyy-mm-dd.");
            }
        } else if (fullCommandArray[0].equals(CommandType.DONE.getType())) {
            return new DoneCommand(Integer.parseInt(fullCommandArray[1]));
        } else if (fullCommandArray[0].equals(CommandType.DELETE.getType())) {
            return new DeleteCommand(Integer.parseInt(fullCommandArray[1]));
        } else if (fullCommandArray[0].equals(CommandType.FIND.getType())) {
            String findKeywords = fullCommand.replaceFirst("^find", "");
            findKeywords = findKeywords.strip();
            return new FindCommand(findKeywords.split(" "));
        } else {
            return Parser.parseTasks(fullCommand);
        }
    }

    /**
     * A helper method to parse specifically a {@link ToDo}, an {@link Event} or a {@link Deadline}.
     *
     * @param fullCommand A String representing user input.
     * @return The corresponding {@link Command}.
     * @throws DukeException Thrown if there is error parsing the user input.
     */
    private static Command parseTasks(String fullCommand) throws DukeException {
        String type = fullCommand.split(" ")[0];
        String temp = fullCommand.strip();
        if (temp.equals(TaskType.TODO.getType())
                || temp.equals(TaskType.DEADLINE.getType())
                || temp.equals(TaskType.EVENT.getType())) {
            throw new InvalidArgumentException(
                    "☹ OOPS!!! The description of "
                            + (temp.equals(TaskType.EVENT.getType()) ? "an " : "a ")
                            + temp
                            + " cannot be empty.");
        } else if (temp.equals("")) {
            throw new InvalidTaskTypeException("☹ OOPS!!! The type of a task cannot be empty.");
        }
        if (type == null
                || (!type.equals(TaskType.TODO.getType())
                && !type.equals(TaskType.DEADLINE.getType())
                && !type.equals(TaskType.EVENT.getType()))) {
            throw new InvalidTaskTypeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        String details = fullCommand.substring(type.length());
        if (type.equals(TaskType.TODO.getType())) {
            ToDo t = new ToDo(details.strip());
            return new AddCommand(t);
        } else if (type.equals(TaskType.DEADLINE.getType())) {
            String[] detailsArray = details.split("/by");
            if (detailsArray.length <= 1) {
                throw new InvalidArgumentException("☹ OOPS!!! Please specify a due date for the deadline.");
            }
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
            if (detailsArray.length <= 1) {
                throw new InvalidArgumentException("☹ OOPS!!! Please specify a date for the event.");
            }
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
