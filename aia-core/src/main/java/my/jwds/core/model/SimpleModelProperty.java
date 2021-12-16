package my.jwds.core.model;


/**
 * 简单模型属性，字符串和数值类型
 */
public abstract class SimpleModelProperty extends ModelProperty{

    public SimpleModelProperty(String name, String type) {
        super(name, type);
    }

    public SimpleModelProperty() {
    }

    public SimpleModelProperty(String name, String type, String definition) {
        super(name, type, definition);
    }

    public abstract Object getValue();

}
