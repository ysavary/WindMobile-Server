package ch.windmobile.server.windline.dataobject;

public class DataType implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int dataTypeId;
    private String name;

    public int getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(int dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
