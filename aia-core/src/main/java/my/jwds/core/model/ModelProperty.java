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
     * 注释
     */
    private String definition;

    /**
     * java类型
     */
    private Class javaType;


    /**
     * 通用类型说明
     */
    private String type;



    public ModelProperty() { }
    public ModelProperty(String name, Class javaType) {
        this(name,javaType,null);
    }
    public ModelProperty(String name,Class javaType, String definition) {
        this.name = name;
        this.definition = definition;
        this.javaType = javaType;
    }

    public ModelProperty(String name, String type, Class javaType) {
        this.name = name;
        this.javaType = javaType;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ModelProperty clone(){
        ModelProperty modelProperty = new ModelProperty();
        modelProperty.setName(getName());
        modelProperty.setType(getType());
        modelProperty.setJavaType(getJavaType());
        modelProperty.setDefinition(getDefinition());
        return modelProperty;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
