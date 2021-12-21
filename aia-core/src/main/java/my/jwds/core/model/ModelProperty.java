package my.jwds.core.model;


/**
 * @see my.jwds.core.model
 */
public class ModelProperty {

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    /**
     * 注释
     */
    private String definition;


    public String getName() {
        return name;
    }

    /**
     * 设置名称
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public ModelProperty(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ModelProperty() {
    }

    public ModelProperty(String name, String type, String definition) {
        this.name = name;
        this.type = type;
        this.definition = definition;
    }

    public ModelProperty clone(){
        return new ModelProperty(name,type,definition);
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
