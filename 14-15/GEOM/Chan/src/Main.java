import javax.sound.midi.Soundbank;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Reader
        try {
            Scanner scr = new Scanner(new FileInputStream(args[0]));
            List<Point> points = new ArrayList<>();
            while (scr.hasNext()) {
                String dataLine[] = scr.nextLine().split(" ");
                points.add(new Point(Double.valueOf(dataLine[0]), Double.valueOf(dataLine[1])));
            }

            PrintWriter writer = new PrintWriter("data.out");
            List<Point> hull = Chan.chanHull(points);

            for (Point p : hull) {
                writer.println(p.getX() + " " + p.getY());
            }
            writer.close();

            //execute app for visualisation
            if (args.length > 1){
                Runtime.getRuntime().exec(args[1]);
            }
        } catch (ArrayIndexOutOfBoundsException | FileNotFoundException e) {
            System.out.println("Syntax err");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
