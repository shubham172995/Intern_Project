package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.support.annotation.RestrictTo;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public abstract interface NotificationBuilderWithBuilderAccessor
{
  public abstract Notification build();
  
  public abstract Notification.Builder getBuilder();
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.app.NotificationBuilderWithBuilderAccessor
 * JD-Core Version:    0.7.0.1
 */