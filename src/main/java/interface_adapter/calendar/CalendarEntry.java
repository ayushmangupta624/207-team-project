package interface_adapter.calendar;

import java.time.LocalDate;

// represents the event information needed by the calendar UI
public class CalendarEntry {

    private final String id; // original event
    private final String name; // text displayed on calendar
    private final LocalDate date; // determines which cell displays it

    public CalendarEntry(String id, String name, LocalDate date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}
