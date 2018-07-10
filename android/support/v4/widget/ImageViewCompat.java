package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

public class ImageViewCompat
{
  static final ImageViewCompatImpl IMPL = new BaseViewCompatImpl();
  
  static
  {
    if (Build.VERSION.SDK_INT >= 21)
    {
      IMPL = new LollipopViewCompatImpl();
      return;
    }
  }
  
  public static ColorStateList getImageTintList(ImageView paramImageView)
  {
    return IMPL.getImageTintList(paramImageView);
  }
  
  public static PorterDuff.Mode getImageTintMode(ImageView paramImageView)
  {
    return IMPL.getImageTintMode(paramImageView);
  }
  
  public static void setImageTintList(ImageView paramImageView, ColorStateList paramColorStateList)
  {
    IMPL.setImageTintList(paramImageView, paramColorStateList);
  }
  
  public static void setImageTintMode(ImageView paramImageView, PorterDuff.Mode paramMode)
  {
    IMPL.setImageTintMode(paramImageView, paramMode);
  }
  
  static class BaseViewCompatImpl
    implements ImageViewCompat.ImageViewCompatImpl
  {
    public ColorStateList getImageTintList(ImageView paramImageView)
    {
      if ((paramImageView instanceof TintableImageSourceView)) {
        return ((TintableImageSourceView)paramImageView).getSupportImageTintList();
      }
      return null;
    }
    
    public PorterDuff.Mode getImageTintMode(ImageView paramImageView)
    {
      if ((paramImageView instanceof TintableImageSourceView)) {
        return ((TintableImageSourceView)paramImageView).getSupportImageTintMode();
      }
      return null;
    }
    
    public void setImageTintList(ImageView paramImageView, ColorStateList paramColorStateList)
    {
      if ((paramImageView instanceof TintableImageSourceView)) {
        ((TintableImageSourceView)paramImageView).setSupportImageTintList(paramColorStateList);
      }
    }
    
    public void setImageTintMode(ImageView paramImageView, PorterDuff.Mode paramMode)
    {
      if ((paramImageView instanceof TintableImageSourceView)) {
        ((TintableImageSourceView)paramImageView).setSupportImageTintMode(paramMode);
      }
    }
  }
  
  static abstract interface ImageViewCompatImpl
  {
    public abstract ColorStateList getImageTintList(ImageView paramImageView);
    
    public abstract PorterDuff.Mode getImageTintMode(ImageView paramImageView);
    
    public abstract void setImageTintList(ImageView paramImageView, ColorStateList paramColorStateList);
    
    public abstract void setImageTintMode(ImageView paramImageView, PorterDuff.Mode paramMode);
  }
  
  @RequiresApi(21)
  static class LollipopViewCompatImpl
    extends ImageViewCompat.BaseViewCompatImpl
  {
    public ColorStateList getImageTintList(ImageView paramImageView)
    {
      return paramImageView.getImageTintList();
    }
    
    public PorterDuff.Mode getImageTintMode(ImageView paramImageView)
    {
      return paramImageView.getImageTintMode();
    }
    
    public void setImageTintList(ImageView paramImageView, ColorStateList paramColorStateList)
    {
      paramImageView.setImageTintList(paramColorStateList);
      if (Build.VERSION.SDK_INT == 21)
      {
        paramColorStateList = paramImageView.getDrawable();
        if ((paramImageView.getImageTintList() == null) || (paramImageView.getImageTintMode() == null)) {
          break label64;
        }
      }
      label64:
      for (int i = 1;; i = 0)
      {
        if ((paramColorStateList != null) && (i != 0))
        {
          if (paramColorStateList.isStateful()) {
            paramColorStateList.setState(paramImageView.getDrawableState());
          }
          paramImageView.setImageDrawable(paramColorStateList);
        }
        return;
      }
    }
    
    public void setImageTintMode(ImageView paramImageView, PorterDuff.Mode paramMode)
    {
      paramImageView.setImageTintMode(paramMode);
      if (Build.VERSION.SDK_INT == 21)
      {
        paramMode = paramImageView.getDrawable();
        if ((paramImageView.getImageTintList() == null) || (paramImageView.getImageTintMode() == null)) {
          break label64;
        }
      }
      label64:
      for (int i = 1;; i = 0)
      {
        if ((paramMode != null) && (i != 0))
        {
          if (paramMode.isStateful()) {
            paramMode.setState(paramImageView.getDrawableState());
          }
          paramImageView.setImageDrawable(paramMode);
        }
        return;
      }
    }
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.widget.ImageViewCompat
 * JD-Core Version:    0.7.0.1
 */