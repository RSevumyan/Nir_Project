package ru.mephi.kaf12.m16502.sevumyan.nir.service;

import ru.mephi.kaf12.m16502.sevumyan.nir.model.Coordinates;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, в котором реализованы методы для разбора ответных сообщений от Google Map API.
 */
public class JsonParser {

    /**
     * Разобрать ответный Json, полученный от Google Geocode API
     *
     * @param json ответный Json.
     * @return Объект {@link Coordinates}, содержащий географические координаты.
     */
    public Coordinates parseGeocode(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonst = (JsonObject) reader.read();
        JsonArray array = jsonst.getJsonArray("results");
        JsonObject first = array.getJsonObject(0);
        JsonObject northeast = first.getJsonObject("geometry").getJsonObject("bounds").getJsonObject("northeast");
        return new Coordinates(Double.parseDouble(northeast.getJsonNumber("lat").toString()),
                Double.parseDouble(northeast.getJsonNumber("lng").toString()));
    }

    /**
     * Разобрать ответный Json, полученный от Google Direction API
     *
     * @param json - ответный Json.
     * @return Список объектов {@link Coordinates}, в которых содержаться координаты пути.
     */
    public List<Coordinates> parseDirection(String json) {
        List<Coordinates> coordinatesList = new ArrayList<>();
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray steps = ((JsonObject) reader.read()).getJsonArray("routes").getJsonObject(0).getJsonArray("legs").getJsonObject(0).getJsonArray("steps");

        JsonObject startLocation;
        for (int i = 0; i < steps.size(); i++) {
            startLocation = steps.getJsonObject(i).getJsonObject("start_location");
            coordinatesList.add(new Coordinates(Double.parseDouble(startLocation.getJsonNumber("lat").toString()),
                    Double.parseDouble(startLocation.getJsonNumber("lng").toString())));
            String polyString = steps.getJsonObject(i).getJsonObject("polyline").getJsonString("points").toString();
            coordinatesList.addAll(decodePoly(polyString.substring(1, polyString.length() - 1)));
        }
        return coordinatesList;
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Coordinates> decodePoly(String encoded) {

        List<Coordinates> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            if (index < len) {
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20 && index < len);
            }
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            Coordinates p = new Coordinates((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
