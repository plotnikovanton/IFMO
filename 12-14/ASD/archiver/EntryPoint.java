package ru.ifmo.enf.plotnikov.t04_lang;

import org.apache.commons.cli.*;
import ru.ifmo.enf.plotnikov.t04_lang.ConsoleUI.ConsoleUI;
import ru.ifmo.enf.plotnikov.t04_lang.Controller.ConsoleUIController;
import ru.ifmo.enf.plotnikov.t04_lang.Controller.GUIController;
import ru.ifmo.enf.plotnikov.t04_lang.Controller.MyLittleArchivator;
import ru.ifmo.enf.plotnikov.t04_lang.GUI.GUI;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Just entry point and nothing else (and little parsing)
 */
public class EntryPoint {
    public static void main(String[] args) {
        final MyLittleArchivator archivator = new MyLittleArchivator(); //Create archivator object
        Options posixOptions = new Options();

        //Command line keys
        Option verbose = new Option("v", "verbose", false, "Turn on verbose output");
        posixOptions.addOption(verbose);
        Option level = new Option("q", "level", true, "Set compression level 0-9");
        level.setArgs(1);
        posixOptions.addOption(level);
        Option buffer = new Option("b", "buffer", true, "Set buffer size");
        level.setArgs(1);
        posixOptions.addOption(buffer);
        Option out = new Option("o", true, "Output file name");
        level.setArgs(1);
        posixOptions.addOption(out);
        Option nogui = new Option("c", "no-gui", false,  "Run in console mod");
        posixOptions.addOption(nogui);
        Option egg = new Option("egg", false,  "oO mooo~~~~");
        posixOptions.addOption(egg);

        //Time to parse some lines
        CommandLineParser cmdLinePosixParser = new PosixParser();
        try {
            CommandLine commandLine = cmdLinePosixParser.parse(posixOptions, args);
            if (commandLine.hasOption("verbose")) {
                archivator.setVerbose(true);
            }
            if (commandLine.hasOption("level")) {
                int i = Integer.parseInt(commandLine.getOptionValue("level"));
                if (!archivator.setCompressionLevel(i)){
                    throw new IllegalArgumentException("Level should be in 0-9");
                }
            }
            if (commandLine.hasOption("buffer")) {
                int i = Integer.parseInt(commandLine.getOptionValue("Buffer"));
                if (!archivator.setBufferSize(i)){
                    throw new IllegalArgumentException("Buffer size should be positive");
                }
            }

            if (commandLine.hasOption("no-gui")) {
                ConsoleUIController controller = new ConsoleUIController(archivator);

                if (commandLine.hasOption("o")) {
                    String name = commandLine.getOptionValue("o");
                    controller.setOutputName(name);
                }
                if (commandLine.hasOption("egg")) {
                    controller.setJokeson(true);
                }

                ConsoleUI consoleUI = new ConsoleUI(controller);
                consoleUI.start();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        GUIController controller = new GUIController(archivator);
                        GUI gui = new GUI(controller);
                        gui.createAndShowGUI();
                    }
                });
            }


        } catch (ParseException e) {
            printHelp(
                    posixOptions, // опции по которым составляем help
                    80, // ширина строки вывода
                    "Options", // строка предшествующая выводу
                    "-- HELP --", // строка следующая за выводом
                    3, // число пробелов перед выводом опции
                    5, // число пробелов перед выводом опцисания опции
                    true, // выводить ли в строке usage список команд
                    System.out // куда производить вывод
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void printHelp (
            final Options options,
            final int printedRowWidth,
            final String header,
            final String footer,
            final int spacesBeforeOption,
            final int spacesBeforeOptionDescription,
            final boolean displayUsage,
            final OutputStream out)
    {
        final String commandLineSyntax = "java -jar MyLittleConverter.jar";//подсказка по запуску самой программы
        final PrintWriter writer = new PrintWriter(out);// куда печатаем help
        final HelpFormatter helpFormatter = new HelpFormatter();// создаем объект для вывода help`а
        helpFormatter.printHelp(
                writer,
                printedRowWidth,
                commandLineSyntax,
                header,
                options,
                spacesBeforeOption,
                spacesBeforeOptionDescription,
                footer,
                displayUsage);//формирование справки
        writer.flush(); // вывод
    }
}
