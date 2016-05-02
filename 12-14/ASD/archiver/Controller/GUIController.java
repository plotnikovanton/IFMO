package ru.ifmo.enf.plotnikov.t04_lang.Controller;

import ru.ifmo.enf.plotnikov.t04_lang.GUI.Entity;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class GUIController {
    public MyLittleArchivator archivator;
    private Stack<MyLittleArchivator.ZipFolder> stack = new Stack<>();
    private JFileChooser fileChooser = new JFileChooser();
    private JFileChooser dirChooser = new JFileChooser();
    public JPanel folder;

    public GUIController(MyLittleArchivator archivator) {
        this.archivator = archivator;
        stack.push(archivator.root);
        dirChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
    }

    public void update() {
        MyLittleArchivator.ZipFolder curFolder = stack.peek();
        //JPanel folder = new JPanel(new GridBagLayout());
        folder.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        //c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;

        int i = 0;
        for (MyLittleArchivator.ZipFolder f : curFolder.folders){
            folder.add(new Entity(f.name, true, i++, this).getPane(), c);
        }

        i = 0;
        for (File f : curFolder.entries){
            folder.add(new Entity(f.getName(), false, i++, this).getPane(), c);
        }

        //c.fill = GridBagConstraints.BOTH;
        //folder.add(Box.createVerticalGlue(), c);

        folder.updateUI();

    }

    public void folderIn(int num) {
        stack.push(stack.peek().folders.get(num));
        update();
    }

    public void folderUp() {
        if (stack.size() > 1) {
            stack.pop();
            update();
        }
    }

    public String getDir() {
        dirChooser.showDialog(null, "Select directory");
//        System.out.println(dirChooser.getSelectedFile());
        return dirChooser.getSelectedFile() + "/";
    }

    public void add() {
        fileChooser.showDialog(null, "Select file");
        File file = fileChooser.getSelectedFile();
        boolean exist = false;
        for (File f : stack.peek().entries) {
            if (f.getName().equals(file.getName())) {
                exist = true;
                JOptionPane.showMessageDialog(null, "File already exist", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        if (file != null && file.isFile() && !exist) {
            stack.peek().add(file);
            update();
        }
    }

    public void mkdir() {
        String name = JOptionPane.showInputDialog(null, "Input folder name");
        if (name != null) {
            //TODO add validation
            stack.peek().add(new MyLittleArchivator.ZipFolder(name));
        }
        update();
    }

    public void rm(int num) {
        stack.peek().entries.remove(num);
        update();
    }

    public void rmf(int num) {
        stack.peek().folders.remove(num);
        update();
    }

    public void pack(String name) throws InterruptedException {
        try {
            archivator.pack(name);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Can't create " + name, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setLevel(int n) {
        archivator.setCompressionLevel(n);
    }

    public boolean amIRoot(){
        return stack.peek().equals(archivator.root);
    }

    public void setBar(JProgressBar bar) {
        archivator.setBar(bar);
    }
}