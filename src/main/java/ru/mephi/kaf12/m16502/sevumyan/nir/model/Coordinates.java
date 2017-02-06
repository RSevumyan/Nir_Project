package ru.mephi.kaf12.m16502.sevumyan.nir.model;

/**
 * Класс, инкапсулирующий в себе географические координаты (широту и долготу) и методы работы с ними.
 */
public class Coordinates {
    private double lat;
    private double lon;

    /**
     * Конструктор, задающий поля широты и долготы.
     *
     * @param lat широта
     * @param lon долгота
     */
    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Получить значение широты
     *
     * @return широта
     */
    public double getLat() {
        return lat;
    }

    /**
     * Задать значение широты
     *
     * @param lat широта
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Получить значение долготы
     *
     * @return - долготы
     */
    public double getLon() {
        return lon;
    }

    /**
     * Задать значение долготы
     *
     * @param lon долгота
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
