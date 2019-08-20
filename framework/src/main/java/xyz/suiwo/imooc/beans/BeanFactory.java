package xyz.suiwo.imooc.beans;

import xyz.suiwo.imooc.web.mvc.Controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    public static Object getBean(Class<?> cls){
        return classToBean.get(cls);
    }

    public static void init(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() > 0){
            int remainSize = toCreate.size();
            for(int i = 0; i < toCreate.size(); i++){
                // 返回true则说明创建成功或者说当前类不是一个bean
                // 返回false则此时可能存存在当前需要创建的bean的依赖还没有创建所以暂时先跳过
                if(finishCreate(toCreate.get(i))){
                    toCreate.remove(i);
                }
            }
            // 如果数量没有改变则说明出现了死循环，抛出异常
            if(toCreate.size() == remainSize){
                throw new Exception("死循环");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        // 如果没有满足的注解，则直接返回true
        if(!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)){
            return true;
        }
        Object bean = cls.newInstance();
        for(Field field : cls.getDeclaredFields()){
            if(field.isAnnotationPresent(Autowired.class)){
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);

                // 如果为空，则说明当前类中的字段所依赖的类还没有注入，所以返回false，先跳过，等到所需要依赖注入之后再创建
                if(reliantBean == null){
                    return false;
                }

                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }

        // 将创建好的bean放入容器中
        classToBean.put(cls, bean);
        return true;
    }
}
