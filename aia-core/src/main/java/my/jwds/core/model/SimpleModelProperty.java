package my.jwds.core.model;


/**
 * 简单模型属性，字符串和数值类型
 */
public abstract class SimpleModelProperty extends ModelProperty{
    public SimpleModelProperty() {
    }

    public SimpleModelProperty(String name,Class javaType) {
        super(name,javaType);
    }

    public SimpleModelProperty(String name, String type, Class javaType) {
        super(name, type, javaType);
    }

    public abstract Object getValue();

}
