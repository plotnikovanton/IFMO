package ru.ifmo.enf.plotnikov.t04_lang.GUI;

import ru.ifmo.enf.plotnikov.t04_lang.Controller.GUIController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class Entity{
    private JPanel mainPanel;
    private JButton delete;
    private JLabel name;
    private boolean isFolder;
    private String fileName;
    private GUIController controller;

    public int getNumber() {
        return number;
    }

    private int number;

    public Entity(String fileName, final boolean isFolder, final int number, final GUIController controller) {
        this.number = number;
        this.controller = controller;
        this.isFolder = isFolder;
        this.fileName = fileName;

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFolder) {
                    controller.rmf(number);
                } else {
                    controller.rm(number);
                }
            }
        });
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    controller.folderIn(number);
                }
            }
        });
    }

    public JPanel getPane(){
        return mainPanel;
    }

    private void createUIComponents() {
        URL imgURL;
        if (isFolder) {
            imgURL = getClass().getResource("resources/icons/folder.png");
        } else {
            imgURL = getClass().getResource("resources/icons/file.png");
        }
        name = new JLabel(fileName);
        name.setIcon(new ImageIcon(imgURL));
    }
}
