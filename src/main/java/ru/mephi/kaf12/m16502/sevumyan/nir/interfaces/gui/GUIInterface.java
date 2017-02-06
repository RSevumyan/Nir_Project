package ru.mephi.kaf12.m16502.sevumyan.nir.interfaces.gui;

import ru.mephi.kaf12.m16502.sevumyan.nir.core.Controller;
import ru.mephi.kaf12.m16502.sevumyan.nir.model.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * Класс, реализующий графический интерфейс.
 */
public class GUIInterface {
    private Controller controller;
    private JButton loadButton;
    private JPanel streetViewLoaderPanel;
    private JTextField firstStreet;
    private JTextField secondStreet;
    private JTextField pathFiled;
    private JFrame frame;

    /**
     * Стандартный конструктор.
     */
    public GUIInterface() {
        controller = new Controller();
        frame = new JFrame("Загрузчик панорам");
    }

    /**
     * Метод, организующий графический интерфейс.
     */
    public void startGUI() {
        frame.setSize(new Dimension(600, 200));
        frame.setLocation(500, 150);
        frame.setContentPane(this.streetViewLoaderPanel);
        streetViewLoaderPanel.setPreferredSize(new Dimension(100, 100));
        URL url = getClass().getClassLoader().getResource("/resources/street-view-icon.png");
        if (url != null) {
            String iconPath = url.getPath();
            frame.setIconImage(new ImageIcon(iconPath).getImage());
        }
        pathFiled.setText(System.getProperty("user.dir") + "/StreetViews");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loadButton.addActionListener(actionEvent -> {
            List<Coordinates> coordinatesList = controller.getDirection(firstStreet.getText(), secondStreet.getText());
            controller.getStreetViwsByDirection(coordinatesList, pathFiled.getText());
        });
    }
}
