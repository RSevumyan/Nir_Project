package service;

import ru.mephi.kaf12.m16502.sevumyan.nir.model.Coordinates;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RestService {
    private static final String GOOGLE_API_KEY = "AIzaSyDZvb2R8tCxXLOJQMo7i-38-wzRGmPKKLE";
    private static final String GEOCODING_URL_FORMAT =
            "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";
    private static final String DESTINATION_URL_FORMAT =
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&mode=driving&key=%s";
    private static final String STREET_VIEW_URL_FORMAT =
            "https://maps.googleapis.com/maps/api/streetview?size=600x300&location=%f,%f&heading=151.78&pitch=-0.76&key=%s";

    private static final int BYTES_LENGTH = 2048;

    /**
     * Запрос к Google Geocoding API дляя получения координат по топониму
     *
     * @param address - топоним
     * @return - строка, содержащая Json с ответом на запрос
     * @throws Exception
     */
    public String getGeocoding(String address) throws Exception {
        StringBuilder result = new StringBuilder();
        address = String.format(GEOCODING_URL_FORMAT, URLEncoder.encode(address, "UTF-8"), URLEncoder.encode(GOOGLE_API_KEY, "UTF-8"));
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }

    /**
     * Запрос к Google Direction API дляя получения координат пути между двумя топонимами
     *
     * @param from - топоним начала пути
     * @param to   - топоним конца пути
     * @return - строка, содержащая Json с ответом на запрос
     * @throws Exception
     */
    public String getDirection(String from, String to) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(String
                .format(DESTINATION_URL_FORMAT, URLEncoder.encode(from, "UTF-8"), URLEncoder.encode(to, "UTF-8"), URLEncoder.encode(GOOGLE_API_KEY, "UTF-8")));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }

    /**
     * Запрос к Google StreetView API дляя получения снимка панромы по географической координате
     *
     * @param coordinates - координаты искомой панорамы
     * @return - массив байтов полученного файла
     * @throws Exception
     */
    public byte[] getStreetView(Coordinates coordinates) throws Exception {
        URL url = new URL(String.format(STREET_VIEW_URL_FORMAT, coordinates.getLat(), coordinates.getLon(), URLEncoder.encode(GOOGLE_API_KEY, "UTF-8")));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();

        byte[] byteArray = new byte[BYTES_LENGTH];
        int length;
        int finalLength = 0;
        List<byte[]> bytesList = new ArrayList<>();
        while ((length = inputStream.read(byteArray)) != -1) {
            finalLength += length;
            if (length == BYTES_LENGTH) {
                bytesList.add(byteArray);
            } else {
                byte[] shortcutBytes = new byte[length];
                System.arraycopy(byteArray, 0, shortcutBytes, 0, length);
                bytesList.add(shortcutBytes);
            }
        }
        inputStream.close();

        byte[] finalByteArr = new byte[finalLength];
        int position = 0;
        for (byte[] bytes : bytesList) {
            System.arraycopy(bytes, 0, finalByteArr, position, bytes.length);
            position += bytes.length;
        }
        return finalByteArr;
    }
}
