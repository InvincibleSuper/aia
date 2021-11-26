package my.jwds.core.model;

public class JsonModelFormat implements ModelFormat<String>{
    @Override
    public String format(ModelProperty property) {
        StringBuilder res = new StringBuilder();
        if (property.getName() != null){
            res.append("\"");
            res.append(property.getName());
            res.append("\":");
        }
        if (property instanceof NumberModelProperty){
            res.append(((NumberModelProperty) property).getValue());
            res.append(",");
        }else if (property instanceof StringModelProperty){
            res.append("\"");
            res.append(((StringModelProperty) property).getValue());
            res.append("\",");
        }else if (property instanceof ArrayModelProperty){
            res.append("[");
            res.append(format(((ArrayModelProperty) property).getComponent()));
            res.append("],");
        }else if (property instanceof ObjectModelProperty){
            res.append("{");
            ObjectModelProperty o = (ObjectModelProperty) property;
            if (o.getModel() != null){
                if (o.getModel().getProperties() != null){
                    for (ModelProperty modelProperty : o.getModel().getProperties()) {
                        res.append(format(modelProperty));
                    }
                }
            }else{
                res.append(format(o.getContainerContent()[0]));
                res.append(":");
                res.append(format(o.getContainerContent()[1]));
            }
            res.append("},");
        }
        return res.toString();
    }


}
