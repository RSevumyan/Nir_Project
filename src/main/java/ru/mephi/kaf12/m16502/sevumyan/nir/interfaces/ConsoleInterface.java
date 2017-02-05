package ru.mephi.kaf12.m16502.sevumyan.nir.interfaces;

import ru.mephi.kaf12.m16502.sevumyan.nir.model.Coordinates;
import ru.mephi.kaf12.m16502.sevumyan.nir.core.Controller;

import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {
    private Controller controller;
    private Scanner scaner;

    public ConsoleInterface() {
        controller = new Controller();
        scaner = new Scanner(System.in);
    }

    /**
     * Метод, организующий консольный диалоговый интерфейс
     */
    public void start() {
        System.out.println("Введите название начальной улицы: ");
        String startStreet = scaner.next();
        System.out.println("Введите название конечной улицы: ");
        String endStreet = scaner.next();
        List<Coordinates> coordinatesList = controller.getDirection(startStreet, endStreet);
        for (Coordinates coordinates : coordinatesList) {
            System.out.println("Координаты: " + coordinates.getLat() + "; " + coordinates.getLon());
        }
        System.out.println();
        System.out.println("Введите путь к директории, в которой будут сохраняться файлы:");
        String path = scaner.next();
        controller.getStreetViwsByDirection(coordinatesList, path);
    }
}
