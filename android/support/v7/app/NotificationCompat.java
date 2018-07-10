package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat.Token;

@Deprecated
public class NotificationCompat
  extends android.support.v4.app.NotificationCompat
{
  @Deprecated
  public static MediaSessionCompat.Token getMediaSession(Notification paramNotification)
  {
    paramNotification = getExtras(paramNotification);
    if (paramNotification != null) {
      if (Build.VERSION.SDK_INT >= 21)
      {
        paramNotification = paramNotification.getParcelable("android.mediaSession");
        if (paramNotification != null) {
          return MediaSessionCompat.Token.fromToken(paramNotification);
        }
      }
      else
      {
        Object localObject = BundleCompat.getBinder(paramNotification, "android.mediaSession");
        if (localObject != null)
        {
          paramNotification = Parcel.obtain();
          paramNotification.writeStrongBinder((IBinder)localObject);
          paramNotification.setDataPosition(0);
          localObject = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(paramNotification);
          paramNotification.recycle();
          return localObject;
        }
      }
    }
    return null;
  }
  
  @Deprecated
  public static class Builder
    extends android.support.v4.app.NotificationCompat.Builder
  {
    @Deprecated
    public Builder(Context paramContext)
    {
      super();
    }
  }
  
  @Deprecated
  public static class DecoratedCustomViewStyle
    extends android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle
  {}
  
  @Deprecated
  public static class DecoratedMediaCustomViewStyle
    extends android.support.v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle
  {}
  
  @Deprecated
  public static class MediaStyle
    extends android.support.v4.media.app.NotificationCompat.MediaStyle
  {
    @Deprecated
    public MediaStyle() {}
    
    @Deprecated
    public MediaStyle(android.support.v4.app.NotificationCompat.Builder paramBuilder)
    {
      super();
    }
    
    @Deprecated
    public MediaStyle setCancelButtonIntent(PendingIntent paramPendingIntent)
    {
      return (MediaStyle)super.setCancelButtonIntent(paramPendingIntent);
    }
    
    @Deprecated
    public MediaStyle setMediaSession(MediaSessionCompat.Token paramToken)
    {
      return (MediaStyle)super.setMediaSession(paramToken);
    }
    
    @Deprecated
    public MediaStyle setShowActionsInCompactView(int... paramVarArgs)
    {
      return (MediaStyle)super.setShowActionsInCompactView(paramVarArgs);
    }
    
    @Deprecated
    public MediaStyle setShowCancelButton(boolean paramBoolean)
    {
      return (MediaStyle)super.setShowCancelButton(paramBoolean);
    }
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.app.NotificationCompat
 * JD-Core Version:    0.7.0.1
 */