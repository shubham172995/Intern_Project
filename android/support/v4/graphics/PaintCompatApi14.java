package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

class PaintCompatApi14
{
  private static final String EM_STRING = "m";
  private static final String TOFU_STRING = "󟿽";
  private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal();
  
  static boolean hasGlyph(@NonNull Paint paramPaint, @NonNull String paramString)
  {
    boolean bool2 = false;
    int j = paramString.length();
    if ((j == 1) && (Character.isWhitespace(paramString.charAt(0)))) {
      bool1 = true;
    }
    float f2;
    float f1;
    float f3;
    do
    {
      do
      {
        do
        {
          return bool1;
          f2 = paramPaint.measureText("󟿽");
          f1 = paramPaint.measureText("m");
          f3 = paramPaint.measureText(paramString);
          bool1 = bool2;
        } while (f3 == 0.0F);
        if (paramString.codePointCount(0, paramString.length()) <= 1) {
          break;
        }
        bool1 = bool2;
      } while (f3 > 2.0F * f1);
      f1 = 0.0F;
      int i = 0;
      while (i < j)
      {
        int k = Character.charCount(paramString.codePointAt(i));
        f1 += paramPaint.measureText(paramString, i, i + k);
        i += k;
      }
      bool1 = bool2;
    } while (f3 >= f1);
    if (f3 != f2) {
      return true;
    }
    Pair localPair = obtainEmptyRects();
    paramPaint.getTextBounds("󟿽", 0, "󟿽".length(), (Rect)localPair.first);
    paramPaint.getTextBounds(paramString, 0, j, (Rect)localPair.second);
    if (!((Rect)localPair.first).equals(localPair.second)) {}
    for (boolean bool1 = true;; bool1 = false) {
      return bool1;
    }
  }
  
  private static Pair<Rect, Rect> obtainEmptyRects()
  {
    Pair localPair = (Pair)sRectThreadLocal.get();
    if (localPair == null)
    {
      localPair = new Pair(new Rect(), new Rect());
      sRectThreadLocal.set(localPair);
      return localPair;
    }
    ((Rect)localPair.first).setEmpty();
    ((Rect)localPair.second).setEmpty();
    return localPair;
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.graphics.PaintCompatApi14
 * JD-Core Version:    0.7.0.1
 */