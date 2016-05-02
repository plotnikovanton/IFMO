import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarDate implements SimpleDate {
    Calendar date;

    /**
     *
     * @param month month of date
     * @param day   day of date
     * @param year  year of date
     */
    public CalendarDate(Month month, int day, int year) {
        if (month == null) {
            throw new IllegalArgumentException("Month should not be 'null'");
        }
        int munNum = numOfMun(month) - 1;
        if (!(day > 0 && day <= NumbersDate.daysInMonth[munNum])) {
            throw new IllegalArgumentException("Day should be in range of 1-31");
        }
        if (year < 0) {
            throw new IllegalArgumentException("Year should be positive");
        }

        this.date = new GregorianCalendar(year, munNum , day);
    }

    @Override
    public int getDay() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public Month getMonth() {
        return munOfNum(date.get(Calendar.MONTH) + 1);
    }

    @Override
    public int getYear() {
        return date.get(Calendar.YEAR);
    }

    @Override
    public int from(SimpleDate a) {
        if(this.compareTo(a) >= 0) {
            Calendar other = new GregorianCalendar(a.getYear(), numOfMun(a.getMonth()), a.getDay());
            long end = date.getTimeInMillis();
            long start = other.getTimeInMillis();
            return (int)((end - start)/(1000 * 60 * 60 * 24));
        } else {
            throw new IllegalArgumentException("Other date should not be after these date");
        }
    }

    public static Month munOfNum(int month) {
        switch (month) {
            case 1:
                return Month.JANUARY;
            case 2:
                return Month.FEBRUARY;
            case 3:
                return Month.MARCH;
            case 4:
                return Month.APRIL;
            case 5:
                return Month.MAY;
            case 6:
                return Month.JUNE;
            case 7:
                return Month.JULY;
            case 8:
                return Month.AUGUST;
            case 9:
                return Month.SEPTEMBER;
            case 10:
                return Month.OCTOBER;
            case 11:
                return Month.NOVEMBER;
            case 12:
                return Month.DECEMBER;
        }
        int i = 0;

        return null;
    }

    public static int numOfMun(Month month) {
        switch (month) {
            case JANUARY:
                return 1;
            case FEBRUARY:
                return 2;
            case MARCH:
                return 3;
            case APRIL:
                return 4;
            case MAY:
                return 5;
            case JUNE:
                return 6;
            case JULY:
                return 7;
            case AUGUST:
                return 8;
            case SEPTEMBER:
                return 9;
            case OCTOBER:
                return 10;
            case NOVEMBER:
                return 11;
            case DECEMBER:
                return 12;
        }
        return  -1;
    }

    @Override
    public int compareTo(SimpleDate a) {
        Calendar other = new GregorianCalendar(a.getYear(), numOfMun(a.getMonth()) - 1, a.getDay());
        return date.compareTo(other);
    }
}
