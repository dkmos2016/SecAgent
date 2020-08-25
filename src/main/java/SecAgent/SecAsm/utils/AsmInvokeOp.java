package SecAgent.SecAsm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AsmInvokeOp {
    public static Object invokestatic(String classname, String methodname, Class[] paramTypes, Object[] params) {
        Object result;
        try{
         Class cls = Thread.currentThread().getContextClassLoader().loadClass(classname);
         Method method = cls.getDeclaredMethod(methodname, paramTypes);

         result = method.invoke(null, params);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            result = "";
        }

        System.out.println(result);
        return result;
    }
}
