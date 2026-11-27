package interface_adapter.calendar;

import java.util.List;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class CalendarViewModel {

    private List<CalendarEntry> events = List.of(); // initially empty, no events

    // Java built-in: notifies listeners about changes
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    // getter
    public List<CalendarEntry> getEvents() {
        return events;
    }

    // setter
    public void setEvents(List<CalendarEntry> newEvents) {
        List<CalendarEntry> oldEvents = events; // save previous list
        events = List.copyOf(newEvents); // replace list
        support.firePropertyChange("events", oldEvents, events); // notify listeners of change
    }

    // allow view to subscribe to changes
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
