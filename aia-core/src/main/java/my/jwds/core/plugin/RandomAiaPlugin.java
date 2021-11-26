package my.jwds.core.plugin;


import java.util.LinkedHashMap;
import java.util.Map;

public  class RandomAiaPlugin extends AbstractAiaPlugin{

    public RandomAiaPlugin() {
        super("随机数生成器", new LinkedHashMap<>());
        Map<String,String> dataModel = dataModel();
        dataModel.put("prefix","前缀");
        dataModel.put("suffix","后缀");
        dataModel.put("max","最大（默认10）");
        dataModel.put("min","最小（默认0）");
    }

    @Override
    public AiaPluginReturn invoke(Map<String, String> data) {
        int max = 10,min = 0;
        String prefix = data.get("prefix");
        String suffix = data.get("suffix");
        String maxString = data.get("max");
        String minString = data.get("min");
        if (isNumber(maxString)){
            max = Integer.parseInt(maxString);
        }
        if (isNumber(minString)){
            min = Integer.parseInt(minString);
        }
        int value = ((int)Math.random()*max)+min;
        String res = "";
        if (prefix != null){
            res += prefix;
        }
        res += value;
        if (suffix != null){
            res += suffix;
        }

        return new AiaPluginReturn(AiaPluginReturn.STRING_TYPE,res);
    }


    private boolean isNumber(String s){
        if (s == null || s.isEmpty())return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) >'9')return false;
        }
        return true;
    }
}
