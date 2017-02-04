package service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import common.Coordinates;

public class RestService {
	private static final String GEOCODING_URL_FORMAT = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyDZvb2R8tCxXLOJQMo7i-38-wzRGmPKKLE";
	private static final String DESTINATION_URL_FORMAT = "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&mode=driving&key=AIzaSyDZvb2R8tCxXLOJQMo7i-38-wzRGmPKKLE";
	private static final String STREET_VIEW_URL_FORMAT = "https://maps.googleapis.com/maps/api/streetview?size=600x300&location=%f,%f&heading=151.78&pitch=-0.76&key=AIzaSyDZvb2R8tCxXLOJQMo7i-38-wzRGmPKKLE";

	public String getGeocoding(String urlToRead) throws Exception {

		StringBuilder result = new StringBuilder();
		urlToRead = String.format(GEOCODING_URL_FORMAT, URLEncoder.encode(urlToRead, "UTF-8"));
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	public String getDestinations(String from, String to) throws Exception {
		StringBuilder result = new StringBuilder();
		String urlToRead = String.format(DESTINATION_URL_FORMAT, URLEncoder.encode(from, "UTF-8"),
				URLEncoder.encode(to, "UTF-8"));
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	public void getPanoram(Coordinates coor, String fileName) throws Exception {
		StringBuilder result = new StringBuilder();
		String urlToRead = String.format(STREET_VIEW_URL_FORMAT, coor.getLat(), coor.getLon());
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		OutputStream os = new FileOutputStream(fileName);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();

	}
}
