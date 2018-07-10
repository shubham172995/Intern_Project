package android.support.v7.app;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

@RequiresApi(14)
class AppCompatDelegateImplV11
  extends AppCompatDelegateImplV9
{
  AppCompatDelegateImplV11(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback)
  {
    super(paramContext, paramWindow, paramAppCompatCallback);
  }
  
  View callActivityOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    return null;
  }
  
  public boolean hasWindowFeature(int paramInt)
  {
    return (super.hasWindowFeature(paramInt)) || (this.mWindow.hasFeature(paramInt));
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.app.AppCompatDelegateImplV11
 * JD-Core Version:    0.7.0.1
 */