package org.diveintojee.poc;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author louis.gueye@gmail.com
 */
@XmlRootElement
public class Station {
    private Long id;
    private String name;
    private String township;
    private StationType type;
    private Location location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public StationType getType() {
        return type;
    }

    public void setType(StationType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (!id.equals(station.id)) return false;
        if (location != null ? !location.equals(station.location) : station.location != null) return false;
        if (name != null ? !name.equals(station.name) : station.name != null) return false;
        if (township != null ? !township.equals(station.township) : station.township != null) return false;
        if (type != station.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (township != null ? township.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
