package android.arch.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class ReflectiveGenericLifecycleObserver
  implements GenericLifecycleObserver
{
  private static final int CALL_TYPE_NO_ARG = 0;
  private static final int CALL_TYPE_PROVIDER = 1;
  private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
  static final Map<Class, CallbackInfo> sInfoCache = new HashMap();
  private final CallbackInfo mInfo;
  private final Object mWrapped;
  
  ReflectiveGenericLifecycleObserver(Object paramObject)
  {
    this.mWrapped = paramObject;
    this.mInfo = getInfo(this.mWrapped.getClass());
  }
  
  private static CallbackInfo createInfo(Class paramClass)
  {
    Object localObject2 = paramClass.getSuperclass();
    Object localObject1 = new HashMap();
    if (localObject2 != null)
    {
      localObject2 = getInfo((Class)localObject2);
      if (localObject2 != null) {
        ((Map)localObject1).putAll(((CallbackInfo)localObject2).mHandlerToEvent);
      }
    }
    localObject2 = paramClass.getDeclaredMethods();
    Class[] arrayOfClass = paramClass.getInterfaces();
    int j = arrayOfClass.length;
    int i = 0;
    Object localObject3;
    Object localObject4;
    while (i < j)
    {
      localObject3 = getInfo(arrayOfClass[i]).mHandlerToEvent.entrySet().iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject4 = (Map.Entry)((Iterator)localObject3).next();
        verifyAndPutHandler((Map)localObject1, (MethodReference)((Map.Entry)localObject4).getKey(), (Lifecycle.Event)((Map.Entry)localObject4).getValue(), paramClass);
      }
      i += 1;
    }
    int k = localObject2.length;
    j = 0;
    if (j < k)
    {
      arrayOfClass = localObject2[j];
      localObject4 = (OnLifecycleEvent)arrayOfClass.getAnnotation(OnLifecycleEvent.class);
      if (localObject4 == null) {}
      for (;;)
      {
        j += 1;
        break;
        localObject3 = arrayOfClass.getParameterTypes();
        i = 0;
        if (localObject3.length > 0)
        {
          i = 1;
          if (!localObject3[0].isAssignableFrom(LifecycleOwner.class)) {
            throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
          }
        }
        localObject4 = ((OnLifecycleEvent)localObject4).value();
        if (localObject3.length > 1)
        {
          i = 2;
          if (!localObject3[1].isAssignableFrom(Lifecycle.Event.class)) {
            throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
          }
          if (localObject4 != Lifecycle.Event.ON_ANY) {
            throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
          }
        }
        if (localObject3.length > 2) {
          throw new IllegalArgumentException("cannot have more than 2 params");
        }
        verifyAndPutHandler((Map)localObject1, new MethodReference(i, arrayOfClass), (Lifecycle.Event)localObject4, paramClass);
      }
    }
    localObject1 = new CallbackInfo((Map)localObject1);
    sInfoCache.put(paramClass, localObject1);
    return localObject1;
  }
  
  private static CallbackInfo getInfo(Class paramClass)
  {
    CallbackInfo localCallbackInfo = (CallbackInfo)sInfoCache.get(paramClass);
    if (localCallbackInfo != null) {
      return localCallbackInfo;
    }
    return createInfo(paramClass);
  }
  
  private void invokeCallback(MethodReference paramMethodReference, LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent)
  {
    try
    {
      switch (paramMethodReference.mCallType)
      {
      case 0: 
        paramMethodReference.mMethod.invoke(this.mWrapped, new Object[0]);
        return;
      }
    }
    catch (InvocationTargetException paramMethodReference)
    {
      throw new RuntimeException("Failed to call observer method", paramMethodReference.getCause());
      paramMethodReference.mMethod.invoke(this.mWrapped, new Object[] { paramLifecycleOwner });
      return;
    }
    catch (IllegalAccessException paramMethodReference)
    {
      throw new RuntimeException(paramMethodReference);
    }
    paramMethodReference.mMethod.invoke(this.mWrapped, new Object[] { paramLifecycleOwner, paramEvent });
    return;
  }
  
  private void invokeCallbacks(CallbackInfo paramCallbackInfo, LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent)
  {
    invokeMethodsForEvent((List)paramCallbackInfo.mEventToHandlers.get(paramEvent), paramLifecycleOwner, paramEvent);
    invokeMethodsForEvent((List)paramCallbackInfo.mEventToHandlers.get(Lifecycle.Event.ON_ANY), paramLifecycleOwner, paramEvent);
  }
  
  private void invokeMethodsForEvent(List<MethodReference> paramList, LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent)
  {
    if (paramList != null)
    {
      int i = paramList.size() - 1;
      while (i >= 0)
      {
        invokeCallback((MethodReference)paramList.get(i), paramLifecycleOwner, paramEvent);
        i -= 1;
      }
    }
  }
  
  private static void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> paramMap, MethodReference paramMethodReference, Lifecycle.Event paramEvent, Class paramClass)
  {
    Lifecycle.Event localEvent = (Lifecycle.Event)paramMap.get(paramMethodReference);
    if ((localEvent != null) && (paramEvent != localEvent))
    {
      paramMap = paramMethodReference.mMethod;
      throw new IllegalArgumentException("Method " + paramMap.getName() + " in " + paramClass.getName() + " already declared with different @OnLifecycleEvent value: previous" + " value " + localEvent + ", new value " + paramEvent);
    }
    if (localEvent == null) {
      paramMap.put(paramMethodReference, paramEvent);
    }
  }
  
  public void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent)
  {
    invokeCallbacks(this.mInfo, paramLifecycleOwner, paramEvent);
  }
  
  static class CallbackInfo
  {
    final Map<Lifecycle.Event, List<ReflectiveGenericLifecycleObserver.MethodReference>> mEventToHandlers;
    final Map<ReflectiveGenericLifecycleObserver.MethodReference, Lifecycle.Event> mHandlerToEvent;
    
    CallbackInfo(Map<ReflectiveGenericLifecycleObserver.MethodReference, Lifecycle.Event> paramMap)
    {
      this.mHandlerToEvent = paramMap;
      this.mEventToHandlers = new HashMap();
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Lifecycle.Event localEvent = (Lifecycle.Event)localEntry.getValue();
        List localList = (List)this.mEventToHandlers.get(localEvent);
        paramMap = localList;
        if (localList == null)
        {
          paramMap = new ArrayList();
          this.mEventToHandlers.put(localEvent, paramMap);
        }
        paramMap.add(localEntry.getKey());
      }
    }
  }
  
  static class MethodReference
  {
    final int mCallType;
    final Method mMethod;
    
    MethodReference(int paramInt, Method paramMethod)
    {
      this.mCallType = paramInt;
      this.mMethod = paramMethod;
      this.mMethod.setAccessible(true);
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {}
      do
      {
        return true;
        if ((paramObject == null) || (getClass() != paramObject.getClass())) {
          return false;
        }
        paramObject = (MethodReference)paramObject;
      } while ((this.mCallType == paramObject.mCallType) && (this.mMethod.getName().equals(paramObject.mMethod.getName())));
      return false;
    }
    
    public int hashCode()
    {
      return this.mCallType * 31 + this.mMethod.getName().hashCode();
    }
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.arch.lifecycle.ReflectiveGenericLifecycleObserver
 * JD-Core Version:    0.7.0.1
 */