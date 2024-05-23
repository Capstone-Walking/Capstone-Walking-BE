package com.walking.data.entity.traffic.constant;

public enum TrafficColor {
	RED,
	GREEN,
	DARK;

	public boolean isRed() {
		return this.equals(RED);
	}

	public boolean isGreen() {
		return this.equals(GREEN);
	}

	public static TrafficColor getNextColor(TrafficColor now) {
		if (now.isGreen()) {
			return RED;
		} else {
			return GREEN;
		}
	}

	public static TrafficColor apiRequestOf(String request) {
		if (request.equals("Stop")) {
			return RED;
		}
		return GREEN;
	}
}
