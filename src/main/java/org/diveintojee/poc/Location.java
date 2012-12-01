package org.diveintojee.poc;

import java.math.BigDecimal;

/**
 * @author louis.gueye@gmail.com
 */
public class Location {

    private BigDecimal lat;
    private BigDecimal lon;

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (!lat.equals(location.lat)) return false;
        if (!lon.equals(location.lon)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lat.hashCode();
        result = 31 * result + lon.hashCode();
        return result;
    }
}
