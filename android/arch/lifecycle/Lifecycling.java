package android.arch.lifecycle;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
class Lifecycling
{
  private static Map<Class, Constructor<? extends GenericLifecycleObserver>> sCallbackCache;
  private static Constructor<? extends GenericLifecycleObserver> sREFLECTIVE;
  
  static
  {
    try
    {
      sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(new Class[] { Object.class });
      label17:
      sCallbackCache = new HashMap();
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label17;
    }
  }
  
  static String getAdapterName(String paramString)
  {
    return paramString.replace(".", "_") + "_LifecycleAdapter";
  }
  
  /* Error */
  @android.support.annotation.NonNull
  static GenericLifecycleObserver getCallback(Object paramObject)
  {
    // Byte code:
    //   0: aload_0
    //   1: instanceof 73
    //   4: ifeq +8 -> 12
    //   7: aload_0
    //   8: checkcast 73	android/arch/lifecycle/GenericLifecycleObserver
    //   11: areturn
    //   12: aload_0
    //   13: invokevirtual 77	java/lang/Object:getClass	()Ljava/lang/Class;
    //   16: astore_3
    //   17: getstatic 35	android/arch/lifecycle/Lifecycling:sCallbackCache	Ljava/util/Map;
    //   20: aload_3
    //   21: invokeinterface 83 2 0
    //   26: checkcast 85	java/lang/reflect/Constructor
    //   29: astore_1
    //   30: aload_1
    //   31: ifnull +19 -> 50
    //   34: aload_1
    //   35: iconst_1
    //   36: anewarray 4	java/lang/Object
    //   39: dup
    //   40: iconst_0
    //   41: aload_0
    //   42: aastore
    //   43: invokevirtual 89	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   46: checkcast 73	android/arch/lifecycle/GenericLifecycleObserver
    //   49: areturn
    //   50: aload_3
    //   51: invokestatic 93	android/arch/lifecycle/Lifecycling:getGeneratedAdapterConstructor	(Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   54: astore_2
    //   55: aload_2
    //   56: ifnull +46 -> 102
    //   59: aload_2
    //   60: astore_1
    //   61: aload_2
    //   62: invokevirtual 97	java/lang/reflect/Constructor:isAccessible	()Z
    //   65: ifne +10 -> 75
    //   68: aload_2
    //   69: iconst_1
    //   70: invokevirtual 101	java/lang/reflect/Constructor:setAccessible	(Z)V
    //   73: aload_2
    //   74: astore_1
    //   75: getstatic 35	android/arch/lifecycle/Lifecycling:sCallbackCache	Ljava/util/Map;
    //   78: aload_3
    //   79: aload_1
    //   80: invokeinterface 105 3 0
    //   85: pop
    //   86: aload_1
    //   87: iconst_1
    //   88: anewarray 4	java/lang/Object
    //   91: dup
    //   92: iconst_0
    //   93: aload_0
    //   94: aastore
    //   95: invokevirtual 89	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   98: checkcast 73	android/arch/lifecycle/GenericLifecycleObserver
    //   101: areturn
    //   102: getstatic 28	android/arch/lifecycle/Lifecycling:sREFLECTIVE	Ljava/lang/reflect/Constructor;
    //   105: astore_1
    //   106: goto -31 -> 75
    //   109: astore_0
    //   110: new 107	java/lang/RuntimeException
    //   113: dup
    //   114: aload_0
    //   115: invokespecial 110	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   118: athrow
    //   119: astore_0
    //   120: new 107	java/lang/RuntimeException
    //   123: dup
    //   124: aload_0
    //   125: invokespecial 110	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   128: athrow
    //   129: astore_0
    //   130: new 107	java/lang/RuntimeException
    //   133: dup
    //   134: aload_0
    //   135: invokespecial 110	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   138: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	139	0	paramObject	Object
    //   29	77	1	localObject	Object
    //   54	20	2	localConstructor	Constructor
    //   16	63	3	localClass	Class
    // Exception table:
    //   from	to	target	type
    //   12	30	109	java/lang/IllegalAccessException
    //   34	50	109	java/lang/IllegalAccessException
    //   50	55	109	java/lang/IllegalAccessException
    //   61	73	109	java/lang/IllegalAccessException
    //   75	102	109	java/lang/IllegalAccessException
    //   102	106	109	java/lang/IllegalAccessException
    //   12	30	119	java/lang/InstantiationException
    //   34	50	119	java/lang/InstantiationException
    //   50	55	119	java/lang/InstantiationException
    //   61	73	119	java/lang/InstantiationException
    //   75	102	119	java/lang/InstantiationException
    //   102	106	119	java/lang/InstantiationException
    //   12	30	129	java/lang/reflect/InvocationTargetException
    //   34	50	129	java/lang/reflect/InvocationTargetException
    //   50	55	129	java/lang/reflect/InvocationTargetException
    //   61	73	129	java/lang/reflect/InvocationTargetException
    //   75	102	129	java/lang/reflect/InvocationTargetException
    //   102	106	129	java/lang/reflect/InvocationTargetException
  }
  
  @Nullable
  private static Constructor<? extends GenericLifecycleObserver> getGeneratedAdapterConstructor(Class<?> paramClass)
  {
    Object localObject = paramClass.getPackage();
    String str;
    if (localObject != null)
    {
      localObject = ((Package)localObject).getName();
      str = paramClass.getCanonicalName();
      if (str != null) {
        break label31;
      }
    }
    for (;;)
    {
      return null;
      localObject = "";
      break;
      label31:
      if (((String)localObject).isEmpty()) {
        str = getAdapterName(str);
      }
      try
      {
        if (((String)localObject).isEmpty()) {}
        for (localObject = str;; localObject = (String)localObject + "." + str)
        {
          localObject = Class.forName((String)localObject).getDeclaredConstructor(new Class[] { paramClass });
          return localObject;
          str = str.substring(((String)localObject).length() + 1);
          break;
        }
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        paramClass = paramClass.getSuperclass();
        if (paramClass == null) {
          continue;
        }
        return getGeneratedAdapterConstructor(paramClass);
      }
      catch (NoSuchMethodException paramClass)
      {
        throw new RuntimeException(paramClass);
      }
    }
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.arch.lifecycle.Lifecycling
 * JD-Core Version:    0.7.0.1
 */