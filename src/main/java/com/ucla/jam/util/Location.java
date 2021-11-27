package com.ucla.jam.util;

import lombok.Value;

@Value
public class Location {
    String longitude;
    String latitude;

    /**
     * Calculate distance between two points in latitude and longitude. Uses Haversine method as its base.
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     * @param other Location to calculate distance to
     * @return Distance between this and other location
     */
    public Distance distance(Location other) {

        final int R = 6371; // Radius of the earth

        double lat1 = Double.parseDouble(this.getLatitude());
        double lon1 = Double.parseDouble(this.getLongitude());
        double lat2 = Double.parseDouble(other.getLatitude());
        double lon2 = Double.parseDouble(other.getLongitude());

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        return new Distance(Math.sqrt(distance) / 1000.0, Distance.Unit.KILOMETERS);
    }
}
