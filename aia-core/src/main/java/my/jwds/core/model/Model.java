package my.jwds.core.model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<ModelProperty> properties;

    public List<ModelProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ModelProperty> properties) {
        this.properties = properties;
    }

    public Model(List<ModelProperty> properties) {
        this.properties = properties;
    }

    public Model() {
    }

    public Model clone(){
        List<ModelProperty> newList = null;
        if (properties != null){
            newList = new ArrayList<>();
            for (int i = 0; i < properties.size(); i++) {
                newList.add(properties.get(i).clone());
            }
        }

        return new Model(newList);
    }
}
