import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class Test {
    public static final long NUM_OF_TEST = 1000000;
    private static Random rnd = new Random();

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException { // let program crash if smt wrong(Java way) >:}

        Class<SimpleDate> simpleDateClass = (Class<SimpleDate>) Class.forName(args[0]);

        SimpleDate from;
        SimpleDate to;

        long totalTime = 0;
        for (long i = 0; i<NUM_OF_TEST; ++i) {

            from = genDate(simpleDateClass);
            to = genDate(simpleDateClass);

            if (to.compareTo(from) < 0) {
                SimpleDate tmp = from;
                from = to;
                to = tmp;
            }

            long timer = System.currentTimeMillis();
            to.from(from);
            timer = System.currentTimeMillis() - timer;

            totalTime += timer;
        }

        System.out.printf("Summary working time of method 'int from(SimpleDate date)' of class %s in %d tests is: %d",
                args[0], NUM_OF_TEST, totalTime);
    }

    private static SimpleDate genDate(Class<SimpleDate> simpleDateClass) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        SimpleDate.Month month = CalendarDate.munOfNum(rnd.nextInt(12) + 1);
        int day = rnd.nextInt(NumbersDate.daysInMonth[ CalendarDate.numOfMun(month) - 1 ]) + 1;
        int year = rnd.nextInt(2014-1800) + 1800;
        return simpleDateClass.getConstructor(SimpleDate.Month.class, int.class, int.class)
                .newInstance(month, day, year);
    }
}
