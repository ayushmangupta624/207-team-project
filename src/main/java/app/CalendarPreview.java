package app;

import view.CalendarView;
import interface_adapter.calendar.CalendarEntry;
import interface_adapter.calendar.CalendarViewModel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class CalendarPreview {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            CalendarViewModel viewModel = new CalendarViewModel();

            viewModel.setEvents(List.of(
                    new CalendarEntry("1", "CSC207 Project", LocalDate.now()),
                    new CalendarEntry("2", "Study Session", LocalDate.now()),
                    new CalendarEntry("3", "Assignment", LocalDate.now().plusDays(2))
            ));

            CalendarView calendar = new CalendarView(viewModel); // pass view model into calendar
            JFrame frame = new JFrame("QuercusPro"); // create the display window

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // configure the window
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.add(calendar); // insert the calendar
            frame.setVisible(true); // display the window
        });
    }
}
