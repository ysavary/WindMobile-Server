package ch.windmobile.server.jdc.dataobject;

import java.util.Date;

public class Data implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Date time;
    private double value;

    private Sensor sensor;

    public Data() {
    }

    public Integer getId() {
        return this.id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
