package ru.mephi.kaf12.m16502.sevumyan.nir.core;

import ru.mephi.kaf12.m16502.sevumyan.nir.interfaces.ConsoleInterface;
import ru.mephi.kaf12.m16502.sevumyan.nir.interfaces.GUIInterface;

public class MainClass {

    public static void main(String[] args) {
        if (args[0].length() == 1) {
            switch (args[0].charAt(0)) {

                case 'w':
                case 'W':
                    GUIInterface gui = new GUIInterface();
                    gui.start();
                    break;
                case 'c':
                case 'C':
                default:
                    ConsoleInterface consoleInterface = new ConsoleInterface();
                    consoleInterface.start();
                    break;
            }
        }
    }
}