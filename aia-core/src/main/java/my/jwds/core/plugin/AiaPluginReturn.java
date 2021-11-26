package my.jwds.core.plugin;

public class AiaPluginReturn {

    public static final String STRING_TYPE = "string";

    public static final String Object_TYPE = "object";

    private String type;


    private String data;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public AiaPluginReturn(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public AiaPluginReturn() {
    }
}
