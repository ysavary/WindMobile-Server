package ch.windmobile.server.windline.dataobject;

public class DataType implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer dataId;
    private String name;

    public DataType() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
