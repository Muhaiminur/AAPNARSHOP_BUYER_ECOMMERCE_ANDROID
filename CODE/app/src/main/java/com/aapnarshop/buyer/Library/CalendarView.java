package com.aapnarshop.buyer.Library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aapnarshop.buyer.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class CalendarView extends LinearLayout
{
    // for logging
   // private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private final Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;
    private Date currentTime;


    public CalendarView(Context context)
    {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);
        currentTime = Calendar.getInstance().getTime();

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try
        {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        }
        finally
        {
            ta.recycle();
        }
    }
    private void assignUiElements()
    {
        // layout is inflated, assign local variables to components
        // internal components
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDate = findViewById(R.id.calendar_date_display);
        grid = findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers()
    {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentDate.getTime().after(currentTime)) {
                    currentDate.add(Calendar.MONTH, 1);
                    updateCalendar();
                }
            }
        });

        // subtract one month and refresh UI

        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });


    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events)
    {
        ArrayList<Date> cells = new ArrayList<>();
        if(!currentDate.getTime().after(currentTime)) {
            Calendar calendar = (Calendar) currentDate.clone();

            // determine the cell for current month's beginning
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            // move calendar backwards to the beginning of the week
            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

            // fill cells
            while (cells.size() < DAYS_COUNT) {
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // update grid
            grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

            // update title
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            txtDate.setText(sdf.format(currentDate.getTime()));
            txtDate.setTextColor(getContext().getResources().getColor(R.color.white));
            txtDate.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        }

    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private final HashSet<Date> eventDays;

        // for view inflation
        private final LayoutInflater inflater;

        CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @NotNull
        @Override
        public View getView(int position, View view, @NotNull ViewGroup parent)
        {
            // day in question
            Calendar cal = Calendar.getInstance();
            Date date = getItem(position);
            cal.setTime(Objects.requireNonNull(date));

            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year =cal.get(Calendar.YEAR);

            // today
            Date today = new Date();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(Objects.requireNonNull(today));

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (eventHandler != null)

                        eventHandler.onDayLongPress((Date)parent.getItemAtPosition(position));

                }
            });
            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null)
            {
                for (Date eventDate : eventDays)
                {
                   // Calendar cal3 = Calendar.getInstance();
                    cal2.setTime(Objects.requireNonNull(eventDate));
                    if (cal2.get(Calendar.DAY_OF_MONTH) == day &&
                            cal2.get(Calendar.MONTH) == month &&
                            cal2.get(Calendar.YEAR) == year)
                    {
                        // mark this day for event
                        //view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            ((TextView)view).setTypeface(null, Typeface.NORMAL);
            ((TextView)view).setTextColor(Color.BLACK);

            if (day == cal2.get(Calendar.DATE))
            {
                // if it is today, set it to blue/bold
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(getResources().getColor(R.color.today_calendar));
            }

            // set text
            ((TextView)view).setText(String.valueOf(cal.get(Calendar.DATE)));

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayLongPress(Date date);
    }
}
