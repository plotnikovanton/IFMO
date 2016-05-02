public class NumbersDate implements SimpleDate{
    public static final int daysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private Month month;
    private int day;
    private int year;

    /**
     *
     * @param month month of date
     * @param day   day of date
     * @param year  year of date
     */
    public NumbersDate(Month month, int day, int year) {
        if (month != null) {
            this.month = month;
        } else {
            throw new IllegalArgumentException("Month should not be 'null'");
        }
        if (day > 0 && day < 32) {
            this.day = day;
        } else {
            throw new IllegalArgumentException("Day should be in range of 1-31");
        }
        if (year >= 0) {
            this.year = year;
        } else {
            throw new IllegalArgumentException("Year should be positive");
        }
    }

    /**
     *
     * @return day of a month
     */
    @Override
    public int getDay() {
        return day;
    }

    /**
     *
     * @return month
     */
    @Override
    public Month getMonth() {
        return month;
    }

    /**
     *
     * @return year
     */
    @Override
    public int getYear() {
        return year;
    }

    /**
     *
     * @param other
     * @return days between these date and other
     */
    @Override
    public int from(SimpleDate other) {
        if (this.compareTo(other) >= 0) {
            int cnt = 0;

            for (int i = other.getYear(); i < this.year; ++i) {
                cnt += 365;
                if (isLeap(i)) cnt++;
            }

            int thisMonth = numOfMun(this.month) - 1;
            int otherMonth = numOfMun(other.getMonth()) - 1;

            if (thisMonth > otherMonth) for (int i = otherMonth; i < thisMonth; ++i) cnt += daysInMonth[i];
            if (thisMonth < otherMonth) for (int i = thisMonth; i < otherMonth; ++i) cnt -= daysInMonth[i];

            cnt += this.day - other.getDay();
            
            return cnt;
        } else {
            throw new IllegalArgumentException("Other date should not be after these date");
        }
    }

    private int numOfMun(Month month) {
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

    private boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    @Override
    public int compareTo(SimpleDate other) {
        if (this.year > other.getYear()) {
            return 1;
        } else if (this.year < other.getYear()) {
            return -1;
        } else if (numOfMun(this.month) > numOfMun(other.getMonth())) {
            return 1;
        } else if (numOfMun(this.month) < numOfMun(other.getMonth())) {
            return -1;
        } else if (this.day > other.getDay()) {
            return 1;
        } else if (this.day < other.getDay()) {
            return -1;
        }
        return 0;
    }
}
