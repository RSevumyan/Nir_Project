package ru.mephi.kaf12.m16502.sevumyan.nir.core;

import ru.mephi.kaf12.m16502.sevumyan.nir.model.Coordinates;
import ru.mephi.kaf12.m16502.sevumyan.nir.service.JsonParser;
import ru.mephi.kaf12.m16502.sevumyan.nir.service.RestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Класс контроллер приложения.
 */
public class Controller {
    private RestService restService;
    private JsonParser jsonParser;

    /**
     * Стандартный конструктор.
     */
    public Controller() {
        restService = new RestService();
        jsonParser = new JsonParser();
    }

    /**
     * Получить координаты конкретной улицы.
     *
     * @param streetName название улицы, по которой требуется получить координаты.
     * @return объект {@link Coordinates}
     */
    public Coordinates getGeocoding(String streetName) {
        Coordinates coordinates = null;
        try {
            String json = restService.getGeocoding(streetName);
            coordinates = jsonParser.parseGeocode(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    /**
     * Получить список координат пути между двумя улицами.
     *
     * @param startStreet начальная улица.
     * @param endStreet   конечная улица.
     * @return список кординат {@link Coordinates}.
     */
    public List<Coordinates> getDirection(String startStreet, String endStreet) {
        List<Coordinates> coordinates = null;
        try {
            String res = restService.getDirection(startStreet, endStreet);
            PrintWriter writer = new PrintWriter("coordinates", "UTF-8");
            writer.println(res);
            writer.close();

            coordinates = jsonParser.parseDirection(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    /**
     * Получить фотографии пути по списку координат.
     *
     * @param coordinates список координат {@link Coordinates}.
     * @param path        путь к директории сохранения фотографий.
     */
    public void getStreetViwsByDirection(List<Coordinates> coordinates, String path) {
        File file = new File(path);
        boolean mkdirResult = false;

        if (file.exists()) {
            mkdirResult = true;
        } else {
            try {
                mkdirResult = file.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        if (mkdirResult) {
            for (Coordinates coordinate : coordinates) {
                getStreetView(coordinate, path + "/" + coordinate.getLat() + "," + coordinate.getLon());
            }
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void getStreetView(Coordinates coordinates, String fileName) {
        try {
            byte[] bytesArray = restService.getStreetView(coordinates);
            OutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(bytesArray, 0, bytesArray.length);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
