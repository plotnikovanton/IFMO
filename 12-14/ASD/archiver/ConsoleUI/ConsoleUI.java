package ru.ifmo.enf.plotnikov.t04_lang.ConsoleUI;

import ru.ifmo.enf.plotnikov.t04_lang.Controller.ConsoleUIController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private boolean alive = true;
    private Scanner sc = new Scanner(System.in);
    private ConsoleUIController controller;

    public ConsoleUI(ConsoleUIController controller) {
        this.controller = controller;
    }

    public ConsoleUI() {
        //Test
    }

    public void start() throws InterruptedException {
        System.out.println("Welcome to user friendly dialog!!!"); //Hello
        controller.report(controller.help(), false);
        while (alive) {
            parse(sc.nextLine());
        }

    }

    private void parse(String line) throws InterruptedException {
        String[] in = split(line);
        switch (in[0]) {
            case "help":
                controller.report(controller.help(), false);
                break;
            case "exit":
                if (controller.ask("Are you sure that you want to exit?", false)) {
                    alive = false;
                }
                break;
            case "add":
                controller.add(in);
                break;
            case "mkdir":
                controller.mkdir(in);
                break;
            case "pack":
                controller.pack();
                break;
            case "ls":
                System.out.println(controller.ls());
                System.out.print("> ");
                break;
            case "rm":
                controller.remove(in);
                break;
            case "rmf":
                controller.rmf(in);
                break;
            default:
                controller.report("Unknown or wrong command.", true);
                break;
        }
    }

    /**
     * @param line
     * @return 0 - command, 1-n args
     */
    String[] split(String line) {
        List<String> splitResult = new ArrayList<>();
        //Parse command
        Pattern commandPattern = Pattern.compile("^[a-z]{2,6}|^[a-z]{2,6}\\s.*");
        Matcher commandMatcher = commandPattern.matcher(line);
        if (commandMatcher.find()) {
            splitResult.add(commandMatcher.group());
        } else {
            //throw new IllegalArgumentException("Wrong command.");
            splitResult.add("");
        }
        //Parse args
        Pattern argPattern = Pattern.compile("\"([\\w/\\\\\\.\\s-]+)\"");
        Matcher argMatcher = argPattern.matcher(line);
        while (argMatcher.find()) {
            splitResult.add(argMatcher.group(1));
        }

        return splitResult.toArray(new String[splitResult.size()]);
    }


}
