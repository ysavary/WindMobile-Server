/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.windmobile.server.jdc.dataobject;

public class Sensor implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String type;
    private Integer channel;
    private String name;
    private String unitId;
    private String options;
    private Boolean status;

    private Station station;

    public Sensor() {
    }

    public Integer getId() {
        return this.id;
    }

    @SuppressWarnings("unused")
    private void setId(Integer id) {
        this.id = id;
    }

    public int getChannel() {
        return this.channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitId() {
        return this.unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getOptions() {
        return this.options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Boolean isStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
