package service;

import common.Coordinates;

import java.io.StringReader;
import javax.json.JsonReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
	public Coordinates parseGeocode(String json) {
		JsonReader reader = Json.createReader(new StringReader(json));
		JsonObject jsonst = (JsonObject) reader.read();
		JsonArray array = jsonst.getJsonArray("results");
		JsonObject first = array.getJsonObject(0);
		JsonObject northeast = first.getJsonObject("geometry").getJsonObject("bounds").getJsonObject("northeast");
		System.out.println(northeast.getJsonNumber("lat"));
		return new Coordinates(Double.parseDouble(northeast.getJsonNumber("lat").toString()),
				Double.parseDouble(northeast.getJsonNumber("lng").toString()));
	}

	public List<Coordinates> getDrection(String json) {
		JsonReader reader = Json.createReader(new StringReader(json));
		JsonObject jsonst = (JsonObject) reader.read();
		List<Coordinates> coorList = new ArrayList<Coordinates>();
		JsonArray array = jsonst.getJsonArray("routes").getJsonObject(0).getJsonArray("legs").getJsonObject(0)
				.getJsonArray("steps");
		JsonObject jObj;
		for (int i = 0; i < array.size(); i++) {
			jObj = array.getJsonObject(i).getJsonObject("start_location");
			coorList.add(new Coordinates(Double.parseDouble(jObj.getJsonNumber("lat").toString()),
					Double.parseDouble(jObj.getJsonNumber("lng").toString())));
			JsonObject nobj = array.getJsonObject(i).getJsonObject("polyline");
			String polyString = array.getJsonObject(i).getJsonObject("polyline").getJsonString("points").toString();
			coorList.addAll(decodePoly(polyString.substring(1, polyString.length() - 1)));
			// coorList.addAll(decodePoly("}wjiGtdpcNrAlBJZ"));
		}
		return coorList;
	}

	private List<Coordinates> decodePoly(String encoded) {

		List<Coordinates> poly = new ArrayList<Coordinates>();
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
