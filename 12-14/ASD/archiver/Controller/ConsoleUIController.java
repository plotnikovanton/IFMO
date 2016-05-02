package ru.ifmo.enf.plotnikov.t04_lang.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConsoleUIController {
    private MyLittleArchivator archivator;
    private String outputName = "./archive.zip";
    public boolean jokeson = false;
    private Scanner sc = new Scanner(System.in);

    public ConsoleUIController(MyLittleArchivator archivator) {
        this.archivator = archivator;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public void setJokeson(boolean jokeson) {
        this.jokeson = jokeson;
    }

    public ConsoleUIController(MyLittleArchivator archivator, String outputName, boolean jokeson) {
        this.archivator = archivator;
        this.outputName = outputName;
        this.jokeson = jokeson;
    }

    public void report(String message, boolean error) {
        System.out.print((jokeson && error ? genJoke() : "\t") + message + "\n> ");
    }

    public boolean ask(String question, boolean def) {
        report((question + (def ? " [Y/n]" : " [y/N]")), false);
        String line;
        for (; ; ) {
            line = sc.nextLine();
            if (line == null || line.equals("")) {
                return def;
            }
            switch (line) {
                case "y":
                case "Y":
                case "yes":
                case "Yes":
                case "YES":
                    return true;
                case "n":
                case "N":
                case "no":
                case "No":
                case "NO":
                    return false;
                default:
                    report("You should type answer" + (def ? " [Y/n]" : " [y/N]"), true);
                    break;
            }
        }
    }

    public String help() {
        return "To add file print \'add \"path-to-folder\" \"path_to_file_1\" [\"path_to_file_2\" ...] \n" +
                "\tTo remove file print \'rm \"file1_number\" \' \n" +
                "\tTo crate folder print \'mkdir \"path-to-folder-inside-archive\" \' (\".\" - for root) \"name1\" [\"name2\" ...]\n" +
                "\tTo crate folder print \'rmf \"path-to-folder-inside-archive\" \n" +
                "\tTo show file list print \'ls\' \n" +
                "\tTo show that menu print \'help\' \n" +
                "\tTo pack print \'pack\' \n" +
                "\tTo exit print \'exit\' ";
    }

    public String ls() {
        return lsRecursive(archivator.root, "");
    }

    public String lsRecursive(MyLittleArchivator.ZipFolder cur, String offset) {
        int i = 0;
        String list = "";
        for (MyLittleArchivator.ZipFolder f : cur.folders) {
            list += offset + i++ + "." + "[" + f.name+ "]" + "\n";
            if (f.folders.size() != 0) {
                list += lsRecursive(f, offset + "  ");
            }
        }
        i = 0;
        for (File f : cur.entries) {
            list += offset + i++ + "." + f.getName() + "\n";
        }
        return list;
    }

    public boolean add(String[] in) {
        if (in.length < 2) {
            report("\'add\' should have at least 2 argument.", true);
            return false;
        }
        MyLittleArchivator.ZipFolder cur = getDir(in[1]);
        if (cur == null) {
            return false;
        }
        for (int i = 2; i < in.length; i++) {
            File file = new File(in[i]);
            if (file.canRead() && file.isFile()) {
                cur.add(file);
                System.out.println("Adding " + file.getName());
            } else {
                report("Can't read: " + in[i], true);
                return false;
            }

        }
        System.out.print("> ");
        return true;
    }

    /**
     * @param in in[2..] - name in[1] - path
     * @return
     */
    public boolean mkdir(String[] in) {
        if (in.length < 3) {
            report("\'mkdir\' should have at least 2 args", true);
            return false;
        }
        MyLittleArchivator.ZipFolder cur = getDir(in[1]);
        if (cur == null) {
            return false;
        }
        //TODO validate folder name
        for (int i = 2; i<in.length; i++) {
            cur.add(new MyLittleArchivator.ZipFolder(in[i]));
        }
        System.out.print("> ");
        return true;
    }

    public boolean rmf(String[] in) {
        if (in.length != 2) {
            report("\'rmf\' should have 1 arg", true);
            return false;
        }

        //TODO rewrite that ugly part with regex
        String[] tmp = in[1].split("-");
        String path;
        if (tmp.length == 1){
            path = ".";
        } else {
            path = tmp[0];
            for (int i = 1; i < tmp.length-1; i++)
                path += "-" + tmp[i];
        }
        MyLittleArchivator.ZipFolder cur = getDir(path);
        if (ask("Are you sure(it will remove all included files)?", false)) {
            try {
                cur.folders.remove(Integer.parseInt(tmp[tmp.length - 1]));
                System.out.print("> ");
                return true;
            } catch (IndexOutOfBoundsException | NumberFormatException e){
                report("Bad path", true);
                return false;
            }
        }
        report("Aborted.", false);
        return false;
    }

    private MyLittleArchivator.ZipFolder getDir(String line) {
        String[] tmp = line.split("-");
        if (tmp[0].equals(".")) {
            return archivator.root;
        }
        int[] path = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            try {
                path[i] = Integer.parseInt(tmp[i]);
            } catch (NumberFormatException e) {
                report("NaN.", true);
                return null;
            }
        }
        MyLittleArchivator.ZipFolder cur = archivator.root;
        for (int i = 0; i < path.length; i++) {
            try {
                cur = cur.folders.get(path[i]);
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                report("Bad path.", true);
                return null;
            }
        }
        return cur;
    }

    public boolean remove(String[] in) {
        if (in.length != 2) {
            report("\'remove\' should have a 1 argument.", true);
            return false;
        }
        int n = Integer.parseInt(in[1]);
        if (n < archivator.root.entries.size() && n >= 0) {
            if (ask("Are u sure want to remove " + archivator.root.entries.get(n).getName() + "?", false)) {
                report("Removing " + archivator.root.entries.get(n).getName(), false);
                archivator.root.entries.remove(n);
                return true;
            } else {
                report("Aborting.", false);
                return false;
            }
        } else {
            report("Have not got file with number " + n, true);
            return false;
        }
    }

    public boolean pack() throws InterruptedException {
        if (ask("Are u sure want to pack in " + outputName + "?", true)) {
            try {
                archivator.pack(outputName);
            } catch (IOException e) {
                report("Can't create " + outputName, true);
                return false;
            }
        } else {
            report("Write new output file name.", false);
            String newName = sc.nextLine();
            try {
                archivator.pack(newName);
            } catch (IOException e) {
                report("Can't create " + newName, true);
                return false;
            }
        }
        System.out.print("> ");
        return true;
    }

    List<String> jokes = new ArrayList<String>() {{
        add("\tAre you kidding me? ");
        add("\tStop it! ");
        add("\tI hope this is just a joke... ");
        add("\tShut up, please! ");
        add("\tLOL. ");
        add("\tWtf are u doing !?! ");
        add("\tOne more try, pls! ");
        add("\tNice try. ");
        add("\tThat is not fun! ");
        add("\tForgot about it. ");
        add("\tToday is not your day. ");
        add("\tRelax, you doing fine. ");
        add("\tGood job. ");
        //TODO NEED MORE PHRASES
    }};
    Random rnd = new Random();

    private String genJoke() {
        return jokes.get(rnd.nextInt(jokes.size()));
    }
}
