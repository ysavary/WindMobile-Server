package ch.windmobile.server.windline.dataobject;

public class Property implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String key;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
