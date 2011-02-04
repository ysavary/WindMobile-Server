package ch.windmobile.server.windline.dataobject;

import java.util.Date;

public class Data implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Date time;
    private String value;
    private Station station;
    private DataType dataType;

    public Data() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
