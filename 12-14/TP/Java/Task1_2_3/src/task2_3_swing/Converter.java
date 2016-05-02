package task2_3_swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Converter extends JFrame {
    //Data
    //private static java.util.List<Entity> dataList = new ArrayList<>();
    Scanner scnr ;
    public JComboBox<Entity> markCBox; // To use in process method

    /**
     * Default constructor
     */
    private Converter() {
        super("Расчет стоимости топлива");
        try {
            scnr = new Scanner(new File("resources/data.input"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * Entry point
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Converter converter = new Converter();
                converter.createAndShowGUI();
            }
        });
    }

    /**
     * Creates and show GUI
     */
    private void createAndShowGUI() {
        JPanel mainPane = new JPanel(new GridBagLayout()); //Main panel, all content should be here
        GridBagConstraints c = new GridBagConstraints(); //To add components
        c.insets = new Insets(2,2,2,2); //Cell padding

        markCBox = new JComboBox<>(); //Combo box to choose mark
        new Reader().execute();

        //---- ADDING COMPONENTS----
        //Col 1
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        JLabel markLbl = new JLabel("Марка автомобиля"); //Mark of auto label
        c.gridy = 0;
        mainPane.add(markLbl, c);
        final JLabel fuelTypeLbl = new JLabel(); //Fuel cost label. Set default type
        c.gridy = 1;
        mainPane.add(fuelTypeLbl, c);
        JLabel mileageLbl = new JLabel("Годовой пробег"); //Annual mileage label
        c.gridy = 2;
        mainPane.add(mileageLbl, c);
        JButton recountBtn = new JButton("Стоимость"); //Recount button
        c.gridy = 3;
        mainPane.add(recountBtn, c);

        //Col 2
        //c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
//        final JComboBox<Entity> markCBox = new JComboBox<>(); //Combo box to choose mark
        c.gridy = 0;
        mainPane.add(markCBox, c);
        final JTextField mileageFld = new JTextField(); //Mileage field
        c.gridy = 1;
        mainPane.add(mileageFld, c);
        final JTextField fuelCostFld = new JTextField(); //Fuel cost field
        c.gridy = 2;
        mainPane.add(fuelCostFld, c);
        final JLabel output = new JLabel(); //Result label
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 3;
        mainPane.add(output, c);


        //Col 3
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        JLabel fuelCostLabelAmountLbl = new JLabel("руб./л"); //Fuel cost amount label
        c.gridy = 1;
        mainPane.add(fuelCostLabelAmountLbl, c);
        final JLabel mileageAmountLbl = new JLabel("км"); //Mileage amount label
        c.gridy = 2;
        mainPane.add(mileageAmountLbl, c);


        //---- ADDING LISTENERS ----
        markCBox.addActionListener(new ActionListener() { //Choose mark
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Entity currentItem = (Entity)markCBox.getSelectedItem();
                fuelTypeLbl.setText("Стоимость " + currentItem.getFuelTypeLabel()); //Set type in label
                pack();
            }
        });
        recountBtn.addActionListener(new ActionListener() { //Recount button listener
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Entity currentItem = (Entity)markCBox.getSelectedItem(); //Get current item
                try { //Check format
                    double mileage = Double.parseDouble(mileageFld.getText());
                    try {
                        double cost = Double.parseDouble(fuelCostFld.getText());

                        //Check input
                        if (cost > 0 && mileage > 0) {
                            //Calculate ant set output result
                            output.setText((cost * mileage * currentItem.getMileage() / 100) + " руб.");
                        } else { //If input is negative
                            JOptionPane.showMessageDialog(null, "Вводимые вами числа должны быть положительны.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            //Set focus on 1st wrong field
                            if (cost <= 0) {
                                fuelCostFld.requestFocus();
                            } else {
                                mileageFld.requestFocus();
                            }
                            output.setText("Negative value");
                        }
                    } catch (NumberFormatException e) {//Bad input format for cost
                        output.setText("NaN");
                        errorFormatMessage();
                        fuelCostFld.requestFocus(); //Set focus on mileage field
                    }
                } catch (NumberFormatException e) {//Bad input format for mileage
                    output.setText("NaN");
                    errorFormatMessage();
                    mileageAmountLbl.requestFocus(); //Set focus on cost field
                }
            }
        });

        //Set frame parameters
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPane); //Set main pane by default
        setResizable(false);
        setLocationRelativeTo(null); //Start on middle of the screen
        pack();
        setVisible(true);
    }

    /**
     * Show error format message
     */
    private void errorFormatMessage(){
        JOptionPane.showMessageDialog(null, "Вводимые данные должны быть числами.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Parse input string and build entity object
     * @param line  Input line
     * @return  Entity object
     */
    private Entity parse(String line) {
        String split[] = line.split("\t");//Split by tabs
        String mark = split[2];
        double mileage = 0;
        try {
            mileage = Double.parseDouble(split[1]);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Bad input data", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        //Generate lables for fuel type
        String fuelType;
        switch (split[0]) {
            case "92":
                fuelType = "92 бензина";
                break;
            case "95":
                fuelType = "95 бензина";
                break;
            case "98":
                fuelType = "98 бензина";
                break;
            case "ДТ":
                fuelType = "диз. топлива";
                break;
            default:
                fuelType = "unknown type";
                JOptionPane.showMessageDialog(null, "Bad input data", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
                break;
        }

        return new Entity(mark, mileage, fuelType);
    }

    /**
     * Entity class to avoid Strings in Combo Box and list combinations
     */
    private class Entity{
        //Just fields and getters
        private String mark;
        private double mileage;
        private String fuelTypeLabel;

        private Entity(String mark, Double mileage, String fuelTypeLabel) {
            this.mark = mark;
            this.mileage = mileage;
            this.fuelTypeLabel = fuelTypeLabel;
        }

        public double getMileage() {
            return mileage;
        }

        public String getFuelTypeLabel() {
            return fuelTypeLabel;
        }

        //Use mark name in Combo Box
        @Override
        public String toString() {
            return mark;
        }
    }

    /**
     * Class for reading data in background
     */
    private class Reader extends SwingWorker<Integer, Entity> {
        /**
         * Read data from scanner stream
         * @return  new data
         * @throws Exception
         */
        @Override
        protected Integer doInBackground() throws Exception {
            while (scnr.hasNext()) {
                publish(parse(scnr.nextLine()));
                Thread.sleep(1000);//Get data from far far away
            }
            return 0;
        }

        /**
         * Method to add new element in GUI
         * @param e
         */
        @Override
        protected void process(List<Entity> e) {
            for (Entity en : e) {
                markCBox.addItem(en);
                pack();
            }
        }
    }
}
