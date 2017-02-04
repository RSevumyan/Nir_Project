package nir_project;

import common.Coordinates;
import service.JsonParser;
import service.RestService;

import java.io.PrintWriter;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        RestService rs = new RestService();
        try {
            String json = rs.getGeocoding("Москворечье");
            JsonParser parser = new JsonParser();
            Coordinates coor = parser.parseGeocode(json);
            System.out.println("Координаты: " + coor.getLat() + " " + coor.getLon() + System.lineSeparator());

            String res = rs.getDestinations("Москворечье", "Пролетарский проспект");
            PrintWriter writer = new PrintWriter("a.txt", "UTF-8");
            writer.println(res);
            writer.close();

            List<Coordinates> coorList = parser.getDrection(res);
            int i = 1;
            for (Coordinates coordinates : coorList) {
                System.out.println("Координаты: " + coordinates.getLat() + " " + coordinates.getLon() + System.lineSeparator());
                rs.getPanoram(coordinates, "img\\b" + i + ".jpg");
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
