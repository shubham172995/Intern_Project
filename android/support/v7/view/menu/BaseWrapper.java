package android.support.v7.view.menu;

class BaseWrapper<T>
{
  final T mWrappedObject;
  
  BaseWrapper(T paramT)
  {
    if (paramT == null) {
      throw new IllegalArgumentException("Wrapped Object can not be null.");
    }
    this.mWrappedObject = paramT;
  }
  
  public T getWrappedObject()
  {
    return this.mWrappedObject;
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.view.menu.BaseWrapper
 * JD-Core Version:    0.7.0.1
 */