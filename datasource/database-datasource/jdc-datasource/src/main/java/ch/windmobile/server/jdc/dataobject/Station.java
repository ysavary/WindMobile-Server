package ch.windmobile.server.jdc.dataobject;

import java.util.Set;

public class Station implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer clubId;
    private String shortName;
    private String name;
    private String type;
    private Integer status;
    private String description;
    private String wgs84Latitude;
    private String wgs84Longitude;
    private Integer altitude;
    private String comment;

    private Set<Sensor> sensors;

    public Station() {
    }

    public Integer getId() {
        return this.id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    public Integer getClubId() {
        return this.clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWgs84Latitude() {
        return wgs84Latitude;
    }

    public void setWgs84Latitude(String wgs84Latitude) {
        this.wgs84Latitude = wgs84Latitude;
    }

    public String getWgs84Longitude() {
        return wgs84Longitude;
    }

    public void setWgs84Longitude(String wgs84Longitude) {
        this.wgs84Longitude = wgs84Longitude;
    }

    public Integer getAltitude() {
        return this.altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }
}
