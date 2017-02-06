package ru.mephi.kaf12.m16502.sevumyan.nir.interfaces;

import ru.mephi.kaf12.m16502.sevumyan.nir.core.Controller;
import ru.mephi.kaf12.m16502.sevumyan.nir.model.Coordinates;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, реализующий консльный интерфейс.
 */
public class ConsoleInterface {
    private Controller controller;
    private Scanner scaner;
    private String path;

    /**
     * Стандартный конструктор.
     */
    public ConsoleInterface() {
        controller = new Controller();
        scaner = new Scanner(System.in);
        path = System.getProperty("user.dir") + "/StreetViews";
    }

    /**
     * Метод, организующий консольный диалоговый интерфейс.
     */
    public void start() {
        boolean isCommandRead = false;
        int command = 0;
        while (!isCommandRead) {
            System.out.println("Введите номер команды:\n 1 - GeocodingAPI \n 2 - Direction API \n 3 - StreetView API");
            try {
                command = Integer.parseInt(scaner.next());
                if (command > 0 && command < 4) {
                    isCommandRead = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число 1, 2 или 3");
            }
        }
        switch (command) {
            case 1:
                consoleGeocoding();
                break;
            case 2:
                consoleDirection();
                break;
            case 3:
                consoleStreetView();
                break;
            default:
                System.out.println("Quit");
                break;
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void consoleGeocoding() {
        System.out.println("Введите название  улицы: ");
        String street = waitForInput();
        Coordinates coordinates = controller.getGeocoding(street);
        System.out.println("Координаты " + street + ": " + coordinates.getLat() + "; " + coordinates.getLon());
    }

    private void consoleDirection() {
        System.out.println("Введите название начальной улицы: ");
        String startStreet = waitForInput();
        System.out.println("Введите название конечной улицы: ");
        String endStreet = waitForInput();
        List<Coordinates> coordinatesList = controller.getDirection(startStreet, endStreet);
        for (Coordinates coordinates : coordinatesList) {
            System.out.println("Координаты: " + coordinates.getLat() + "; " + coordinates.getLon());
        }
        controller.getStreetViwsByDirection(coordinatesList, path);
        System.out.println("Готово, снимки панорам сохранены в папке " + path);
    }

    private void consoleStreetView() {
        System.out.println("Введите широту");
        double lat = Double.parseDouble(waitForInput() );
        System.out.println("Введите долготу");
        double lon = Double.parseDouble(waitForInput() );
        Coordinates coordinate = new Coordinates(lat, lon);
        controller.getStreetViwsByDirection(Collections.singletonList(coordinate), path);
        System.out.println("Готово, снимок панорамы сохранен в папке " + path + " c именем" + coordinate.getLat() + "," + coordinate.getLon());
    }

    private String waitForInput() {
        String string = "";
        while ("".equals(string)) {
            string = scaner.nextLine();
        }
        return string;
    }
}
