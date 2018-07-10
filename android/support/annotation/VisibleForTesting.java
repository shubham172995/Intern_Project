package android.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface VisibleForTesting
{
  public static final int NONE = 5;
  public static final int PACKAGE_PRIVATE = 3;
  public static final int PRIVATE = 2;
  public static final int PROTECTED = 4;
  
  int otherwise() default 2;
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.annotation.VisibleForTesting
 * JD-Core Version:    0.7.0.1
 */