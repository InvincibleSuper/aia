package my.jwds.core.model.resolver;

import my.jwds.core.api.definition.resolver.DefinitionResolver;
import my.jwds.core.api.definition.resolver.JavadocDefinitionResolver;
import my.jwds.core.api.definition.resolver.PriorityDefinitionResolver;
import my.jwds.core.model.ModelProperty;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * test
 */
public class DefaultModelResolverTest {
    @Test
    public void testResolver() throws Exception {
        List<DefinitionResolver> list = new ArrayList<>();
        list.add(new JavadocDefinitionResolver("E:\\java\\workSpace\\aia\\aia-core\\src\\test\\java"));
        ModelResolver resolver = new DefaultModelResolver(new PriorityDefinitionResolver(list));
        ModelProperty property = resolver.resolve(Map.class);
        System.out.println(property);
    }


}
