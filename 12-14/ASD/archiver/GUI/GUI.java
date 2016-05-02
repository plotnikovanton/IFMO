package ru.ifmo.enf.plotnikov.t04_lang.GUI;

import ru.ifmo.enf.plotnikov.t04_lang.Controller.GUIController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame{
    private JPanel mainPane;
    private JList curFolder;
    private JPanel outputPanel;
    private JSlider level;
    private JTextField output;
    private JButton pack;
    private JScrollPane scrollPane;
    private JPanel navigation;
    private JButton add;
    private JButton mkdir;
    private JButton up;
    private JPanel curDir;
    private JPanel scrollWrap;
    private JButton dir;
    private JProgressBar progressBar;
    private GUIController controller;


    public GUI(final GUIController controller) {
        super("My little archivator");
        this.controller = controller;
        this.controller.folder = curDir;
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.add();
            }
        });
        mkdir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.mkdir();
            }
        });
        up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.folderUp();
            }
        });
        pack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO add validation
                if ( JOptionPane.showConfirmDialog (null, "Sure?") == JOptionPane.YES_OPTION) {
                    setDisable();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Packer task = new Packer();
                    task.execute();
                }
            }
        });
        dir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.setText(controller.getDir() + output.getText().split("/")[output.getText().split("/").length-1]);
            }
        });
        level.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                controller.setLevel(level.getValue());
            }
        });

        controller.setBar(progressBar);
    }

    public void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(mainPane);
        pack();
        setVisible(true);
    }

    private void createUIComponents() {
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
    }

    private class Packer extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            if (new File(output.getText()).canWrite())
                controller.pack(output.getText());
            else JOptionPane.showMessageDialog(null, "Wrong output file name.", "Input error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        @Override
        protected void done() {
            setCursor(null);
            progressBar.setValue(100);
            setEnable();
        }
    }

    private void setDisable() {
        pack.setEnabled(false);
        up.setEnabled(false);
        add.setEnabled(false);
        mkdir.setEnabled(false);
        dir.setEnabled(false);
    }

    private void setEnable() {
        pack.setEnabled(true);
        up.setEnabled(true);
        add.setEnabled(true);
        mkdir.setEnabled(true);
        dir.setEnabled(true);
    }
}