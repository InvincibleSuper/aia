package my.jwds.utils;

public class ClassUtils {


    /**
     * 使用此类的类加载器加载传入的类完全限定名
     * @param fullyName 类完全限定名
     * @return
     * @throws ClassNotFoundException
     */
    public static Class loadClass(String fullyName) throws ClassNotFoundException {
        return ClassUtils.class.getClassLoader().loadClass(fullyName);
    }

    /**
     * 获取类文件的根路径
     * @return
     */
    public static String classesPath(){
        Class clz = ClassUtils.class;
        String path = clz.getResource("/").getPath();
        return path;
    }

    /**
     * 获取源代码地址，不准确的，目的是给一个默认值
     * @return
     */
    public static String originPath(){
        String classesPath = classesPath();
        int targetIndex = classesPath.lastIndexOf("target"),classesIndex = classesPath.lastIndexOf("classes");
        String projectPath = classesPath;
        if (targetIndex != -1){
            projectPath = classesPath.substring(0,targetIndex);
        }else if (classesIndex != -1){
            projectPath = classesPath.substring(0,classesIndex);
        }
        return projectPath+"src/main/java";
    }
}
