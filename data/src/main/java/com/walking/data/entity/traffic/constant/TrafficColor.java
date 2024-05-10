package com.walking.data.entity.traffic.constant;

public enum TrafficColor {
	RED,
	GREEN;

	public boolean isRed() {
		return this.equals(RED);
	}

	public boolean isGreen() {
		return this.equals(GREEN);
	}
}
