package my.jwds.utils;

public class ClassUtil {

    public static Class loadClass(String fullyName) throws ClassNotFoundException {
        return ClassUtil.class.getClassLoader().loadClass(fullyName);
    }


    public static String classesPath(){
        Class clz = ClassUtil.class;
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
