package xyz.suiwo.imooc.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {
    public static List<Class<?>> scannerClass(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList= new ArrayList<>();
        String path = packageName.replaceAll("\\.", "/");

        // 获取默认类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 获取资源文件的路径
        Enumeration<URL> resources = classLoader.getResources(path);
        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();

            // 判断资源类型
            if(resource.getProtocol().contains("jar")){

                // 如果资源类型是jar包，则我们先获取jar包的绝对路径
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();

                // 获取这个jar包下所有的类
                classList.addAll(getClassesFromJar(jarFilePath, path));
            }else {
                // todo 处理非jar包的情况
            }
        }
        return classList;
    }

    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        //初始化一个容器用于存储类
        List<Class<?>> classes = new ArrayList<>();

        // 通过路径获取JarFile实例
        JarFile jarFile = new JarFile(jarFilePath);

        // 遍历jar包，每个jarEntry都是jar包里的一个文件
        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
        while(jarEntryEnumeration.hasMoreElements()){
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            String entryName = jarEntry.getName();  // xyz/suiwo/imooc/test/Test.class
            if(entryName.startsWith(path) && entryName.endsWith(".class")){
                // 把分隔符换成点，并去除.class后缀
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
