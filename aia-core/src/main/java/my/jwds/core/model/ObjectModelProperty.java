package my.jwds.core.model;

public class ObjectModelProperty extends ModelProperty{

    private ModelProperty[] containerContent;


    private Model model;


    public ObjectModelProperty() {
    }
    public ObjectModelProperty(String name,Class javaType) {
        super(name,javaType);
    }
    public ObjectModelProperty(String name,Class javaType, ModelProperty[] containerContent, Model model) {
        super(name,javaType);
        this.containerContent = containerContent;
        this.model = model;
    }

    public ObjectModelProperty(ModelProperty[] containerContent, Model model) {
        this.containerContent = containerContent;
        this.model = model;
    }

    public ObjectModelProperty(String name, String type, Class javaType, ModelProperty[] containerContent, Model model) {
        super(name, type, javaType);
        this.containerContent = containerContent;
        this.model = model;
    }

    public ObjectModelProperty clone(){
        ModelProperty[] cloneArray = null;
        if (containerContent != null){
            cloneArray = new ModelProperty[containerContent.length];
            for (int i = 0; i < cloneArray.length; i++) {
                if (containerContent[i]!=null){
                    cloneArray[i] = containerContent[i].clone();
                }
            }
        }
        Model cloneModel = getModel() == null?null:getModel().clone();
        ObjectModelProperty modelProperty = new ObjectModelProperty();
        modelProperty.setName(getName());
        modelProperty.setType(getType());
        modelProperty.setJavaType(getJavaType());
        modelProperty.setDefinition(getDefinition());
        modelProperty.setModel(cloneModel);
        modelProperty.setContainerContent(cloneArray);
        return modelProperty;
    }



    public ModelProperty[] getContainerContent() {
        return containerContent;
    }

    public void setContainerContent(ModelProperty[] containerContent) {
        this.containerContent = containerContent;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

}
