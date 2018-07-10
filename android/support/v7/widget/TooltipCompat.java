package android.support.v7.widget;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class TooltipCompat
{
  private static final ViewCompatImpl IMPL = new BaseViewCompatImpl(null);
  
  static
  {
    if (Build.VERSION.SDK_INT >= 26)
    {
      IMPL = new Api26ViewCompatImpl(null);
      return;
    }
  }
  
  public static void setTooltipText(@NonNull View paramView, @Nullable CharSequence paramCharSequence)
  {
    IMPL.setTooltipText(paramView, paramCharSequence);
  }
  
  @TargetApi(26)
  private static class Api26ViewCompatImpl
    implements TooltipCompat.ViewCompatImpl
  {
    public void setTooltipText(@NonNull View paramView, @Nullable CharSequence paramCharSequence)
    {
      paramView.setTooltipText(paramCharSequence);
    }
  }
  
  private static class BaseViewCompatImpl
    implements TooltipCompat.ViewCompatImpl
  {
    public void setTooltipText(@NonNull View paramView, @Nullable CharSequence paramCharSequence)
    {
      TooltipCompatHandler.setTooltipText(paramView, paramCharSequence);
    }
  }
  
  private static abstract interface ViewCompatImpl
  {
    public abstract void setTooltipText(@NonNull View paramView, @Nullable CharSequence paramCharSequence);
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.widget.TooltipCompat
 * JD-Core Version:    0.7.0.1
 */