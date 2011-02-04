package ch.windmobile.server.windline.dataobject;

public class Property implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String key;

    public Property() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
