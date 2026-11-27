package view;

import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import interface_adapter.calendar.CalendarEntry;
import interface_adapter.calendar.CalendarViewModel;

public class CalendarView extends JPanel {

    private YearMonth currentMonth; // current year and month
    private JLabel monthLabel; // text label for year and month
    private JPanel calendarGrid; // container that will hold individual days
    private final CalendarViewModel viewModel; // event information that calendar displays

    public CalendarView(CalendarViewModel viewModel) {

        this.viewModel = viewModel;
        currentMonth = YearMonth.now(); // initialize calendar to current year and month
        setLayout(new BorderLayout()); // arrange components into regions (north, south, etc.)
        createNavigationBar(); // month header with buttons
        createCalendarGrid(); // grid with days

        viewModel.addPropertyChangeListener((e -> {
            SwingUtilities.invokeLater(this::renderCalendar); // redraw calendar
        }));
    }

    // navigation bar at the top of calendar
    private void createNavigationBar() {
        JPanel navigationBar = new JPanel(new FlowLayout()); // arranges components horizontally
        JButton previousButton = new JButton("<");
        JButton nextButton = new JButton(">");

        monthLabel = new JLabel(); //initialize label
        updateMonthLabel(); // update text, same as 'monthLabel = new JLabel(currentMonth.toString())';



        previousButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1); // prev month
            updateMonthLabel(); // update text
            renderCalendar(); // redraw new month
        });

        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1); // next month
            updateMonthLabel();
            renderCalendar();
        });


        navigationBar.add(previousButton); // left
        navigationBar.add(monthLabel); // middle
        navigationBar.add(nextButton); // right

        add(navigationBar, BorderLayout.NORTH); // add to top of calendar
    }

    // format the month label
    private void updateMonthLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy"); // full month name and 4-digit year
        monthLabel.setText(currentMonth.format(formatter)); // update label
    }

    // create the container that holds the calendar days
    private void createCalendarGrid() {

        // 7 rows and 7 columns; first row is weekdays, last 6 are the days
        calendarGrid = new JPanel((new GridLayout(7, 7)));

        add(calendarGrid, BorderLayout.CENTER); // place calendar grid under navigation bar

        renderCalendar(); // populate grid with dates
    }

    // redraw calendar for selected month
    private void renderCalendar() {

        calendarGrid.removeAll(); // clear
        String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        // add day labels into first row
        for (String day : weekdays) {
            calendarGrid.add(new JLabel(day, SwingConstants.CENTER)); // label of day with centered text
        }

        LocalDate firstDay = currentMonth.atDay(1);

        // java numbers weekdays from 1-monday to 7-sunday
        // this converts weekdays into 0-sunday to 6-satuday
        int offset = firstDay.getDayOfWeek().getValue() % 7;

        int monthLength = currentMonth.lengthOfMonth();

        // populating lower 6 rows
        for (int i = 0; i < 42; i++) {

            int day = i - offset + 1; // which day belongs in cell <i>
            JPanel cell = new JPanel(new BorderLayout()); // one cell
            cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // cell border

            // only display days within the boundary
            if (day >= 1 && day <= monthLength) {
                JLabel dateLabel = new JLabel(String.valueOf(day)); // integer to text
                cell.add(dateLabel, BorderLayout.NORTH); // position date at top of cell
                LocalDate cellDate = currentMonth.atDay(day); // converts day number into actual date
                JPanel eventPanel = new JPanel(); // panel will hold events for this date
                eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS)); // arrange events stacked verticallly

                // loop through events supplied by the view model
                for (CalendarEntry event : viewModel.getEvents()) {

                    // check whether any events belong to this date
                    if (event.getDate().equals(cellDate)) {
                        JLabel eventLabel = new JLabel(event.getName()); // label for event name
                        eventPanel.add(eventLabel); // add event label to day panel
                    }
                }

                cell.add(eventPanel, BorderLayout.CENTER); // add event under date number
            }

            calendarGrid.add(cell); // add cell into grid
        }

        calendarGrid.revalidate(); // redo layout
        calendarGrid.repaint(); // redraw components
    }
}
