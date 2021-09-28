package my.jwds;

import com.sun.javadoc.*;
import my.jwds.utils.ClassUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DocTest<T> {
    private static RootDoc root;

    /**
     * 111
     * @return 111
     */
    public T TEST(T t,DocTest<? extends Class> docTest){
        return null;
    }
    /**
     * 111
     * @return
     */
    public T TEST2(T t,DocTest<T> docTest){
        return null;
    }
    /**
     * 111
     * @return
     * @return
     */
    public T TEST2(T t,T[] docTest){
        return null;
    }


    // 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
    // 参见参考资料6
    public static  class Doclet {
        class g{}

        public Doclet() {
        }
        public static boolean start(RootDoc root) {
            DocTest.root = root;
            return true;
        }
    }
    // 显示DocRoot中的基本信息
    public static void show() throws Exception {
        System.out.println(Doclet.class.getClassLoader() == DocTest.class.getClassLoader());
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < 1; ++i) {
            System.out.println(classes[i]);
            System.out.println(classes[i].commentText());
            System.out.println(classes[i].typeName());
            Class clz = DocTest.class.getClassLoader().loadClass(classes[i].toString());
            System.out.println(clz);
            System.out.println(classes[i].superclass().commentText());
            for(MethodDoc method:classes[i].methods()){

                System.out.printf("\t%s\n", method);
                for (Parameter parameter : method.parameters()) {
                    System.out.println(parameter.type());
                }

            }
            for (Method method : clz.getDeclaredMethods()) {
                System.out.println(method);
            }
            clz.getDeclaredMethod("TEST2",Object.class,DocTest.class);
            System.out.println(classes[i].innerClasses(false)[0].innerClasses(false)[0].innerClasses(false));
            System.out.println(clz.getPackage().getName());
        }
    }

    /**
     *
     * @return
     */
    public static RootDoc getRoot() {
        return root;
    }
    public DocTest() {

    }
    public static void main(final String ... args) throws Exception{
        File file = new File(ClassUtil.classesPath());
        System.out.println(file.exists());

    }

    class d{}
}
