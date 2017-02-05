package ru.mephi.kaf12.m16502.sevumyan.nir.model;

public class Coordinates {
	private double lat;
	private double lng;

	/**
	 * 
	 * @param lat
	 * @param lng
	 */
	public Coordinates(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lng;
	}

	public void setLon(double lon) {
		this.lng = lon;
	}

}
