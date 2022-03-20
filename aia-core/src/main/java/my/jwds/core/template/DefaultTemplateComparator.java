package my.jwds.core.template;

import java.util.Comparator;

public class DefaultTemplateComparator implements Comparator<AiaTemplate> {
    @Override
    public int compare(AiaTemplate o1, AiaTemplate o2) {
        return  o1.getName().compareTo(o2.getName());
    }
}
