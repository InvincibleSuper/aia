package my.jwds.core.model;

public class ObjectModelProperty extends ModelProperty{

    private ModelProperty[] containerContent;


    private Model model;


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

    public ObjectModelProperty(String name, String type, ModelProperty[] containerContent, Model model) {
        super(name, type);
        this.containerContent = containerContent;
        this.model = model;
    }

    public ObjectModelProperty(ModelProperty[] containerContent, Model model) {
        this.containerContent = containerContent;
        this.model = model;
    }

    public ObjectModelProperty(String name, String type) {
        super(name, type);
    }

    public ObjectModelProperty() {
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
        return new ObjectModelProperty(getName(),getType(),cloneArray,cloneModel);
    }
}
