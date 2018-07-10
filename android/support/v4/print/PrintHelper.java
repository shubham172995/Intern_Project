package android.support.v4.print;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintHelper
{
  public static final int COLOR_MODE_COLOR = 2;
  public static final int COLOR_MODE_MONOCHROME = 1;
  public static final int ORIENTATION_LANDSCAPE = 1;
  public static final int ORIENTATION_PORTRAIT = 2;
  public static final int SCALE_MODE_FILL = 2;
  public static final int SCALE_MODE_FIT = 1;
  private final PrintHelperVersionImpl mImpl;
  
  public PrintHelper(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 24)
    {
      this.mImpl = new PrintHelperApi24(paramContext);
      return;
    }
    if (Build.VERSION.SDK_INT >= 23)
    {
      this.mImpl = new PrintHelperApi23(paramContext);
      return;
    }
    if (Build.VERSION.SDK_INT >= 20)
    {
      this.mImpl = new PrintHelperApi20(paramContext);
      return;
    }
    if (Build.VERSION.SDK_INT >= 19)
    {
      this.mImpl = new PrintHelperApi19(paramContext);
      return;
    }
    this.mImpl = new PrintHelperStub(null);
  }
  
  public static boolean systemSupportsPrint()
  {
    return Build.VERSION.SDK_INT >= 19;
  }
  
  public int getColorMode()
  {
    return this.mImpl.getColorMode();
  }
  
  public int getOrientation()
  {
    return this.mImpl.getOrientation();
  }
  
  public int getScaleMode()
  {
    return this.mImpl.getScaleMode();
  }
  
  public void printBitmap(String paramString, Bitmap paramBitmap)
  {
    this.mImpl.printBitmap(paramString, paramBitmap, null);
  }
  
  public void printBitmap(String paramString, Bitmap paramBitmap, OnPrintFinishCallback paramOnPrintFinishCallback)
  {
    this.mImpl.printBitmap(paramString, paramBitmap, paramOnPrintFinishCallback);
  }
  
  public void printBitmap(String paramString, Uri paramUri)
    throws FileNotFoundException
  {
    this.mImpl.printBitmap(paramString, paramUri, null);
  }
  
  public void printBitmap(String paramString, Uri paramUri, OnPrintFinishCallback paramOnPrintFinishCallback)
    throws FileNotFoundException
  {
    this.mImpl.printBitmap(paramString, paramUri, paramOnPrintFinishCallback);
  }
  
  public void setColorMode(int paramInt)
  {
    this.mImpl.setColorMode(paramInt);
  }
  
  public void setOrientation(int paramInt)
  {
    this.mImpl.setOrientation(paramInt);
  }
  
  public void setScaleMode(int paramInt)
  {
    this.mImpl.setScaleMode(paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface ColorMode {}
  
  public static abstract interface OnPrintFinishCallback
  {
    public abstract void onFinish();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface Orientation {}
  
  @RequiresApi(19)
  private static class PrintHelperApi19
    implements PrintHelper.PrintHelperVersionImpl
  {
    private static final String LOG_TAG = "PrintHelperApi19";
    private static final int MAX_PRINT_SIZE = 3500;
    int mColorMode = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    protected boolean mIsMinMarginsHandlingCorrect = true;
    private final Object mLock = new Object();
    int mOrientation;
    protected boolean mPrintActivityRespectsOrientation = true;
    int mScaleMode = 2;
    
    PrintHelperApi19(Context paramContext)
    {
      this.mContext = paramContext;
    }
    
    private Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt)
    {
      if (paramInt != 1) {
        return paramBitmap;
      }
      Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      Paint localPaint = new Paint();
      ColorMatrix localColorMatrix = new ColorMatrix();
      localColorMatrix.setSaturation(0.0F);
      localPaint.setColorFilter(new ColorMatrixColorFilter(localColorMatrix));
      localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, localPaint);
      localCanvas.setBitmap(null);
      return localBitmap;
    }
    
    private Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3)
    {
      Matrix localMatrix = new Matrix();
      float f = paramRectF.width() / paramInt1;
      if (paramInt3 == 2) {}
      for (f = Math.max(f, paramRectF.height() / paramInt2);; f = Math.min(f, paramRectF.height() / paramInt2))
      {
        localMatrix.postScale(f, f);
        localMatrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
        return localMatrix;
      }
    }
    
    private static boolean isPortrait(Bitmap paramBitmap)
    {
      return paramBitmap.getWidth() <= paramBitmap.getHeight();
    }
    
    private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions)
      throws FileNotFoundException
    {
      if ((paramUri == null) || (this.mContext == null)) {
        throw new IllegalArgumentException("bad argument to loadBitmap");
      }
      localUri = null;
      try
      {
        paramUri = this.mContext.getContentResolver().openInputStream(paramUri);
        localUri = paramUri;
        paramOptions = BitmapFactory.decodeStream(paramUri, null, paramOptions);
        if (paramUri != null) {}
        try
        {
          paramUri.close();
          return paramOptions;
        }
        catch (IOException paramUri)
        {
          Log.w("PrintHelperApi19", "close fail ", paramUri);
          return paramOptions;
        }
        try
        {
          localUri.close();
          throw paramUri;
        }
        catch (IOException paramOptions)
        {
          for (;;)
          {
            Log.w("PrintHelperApi19", "close fail ", paramOptions);
          }
        }
      }
      finally
      {
        if (localUri == null) {}
      }
    }
    
    /* Error */
    private Bitmap loadConstrainedBitmap(Uri paramUri)
      throws FileNotFoundException
    {
      // Byte code:
      //   0: aload_1
      //   1: ifnull +10 -> 11
      //   4: aload_0
      //   5: getfield 59	android/support/v4/print/PrintHelper$PrintHelperApi19:mContext	Landroid/content/Context;
      //   8: ifnonnull +13 -> 21
      //   11: new 178	java/lang/IllegalArgumentException
      //   14: dup
      //   15: ldc 216
      //   17: invokespecial 183	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
      //   20: athrow
      //   21: new 218	android/graphics/BitmapFactory$Options
      //   24: dup
      //   25: invokespecial 219	android/graphics/BitmapFactory$Options:<init>	()V
      //   28: astore_2
      //   29: aload_2
      //   30: iconst_1
      //   31: putfield 222	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
      //   34: aload_0
      //   35: aload_1
      //   36: aload_2
      //   37: invokespecial 224	android/support/v4/print/PrintHelper$PrintHelperApi19:loadBitmap	(Landroid/net/Uri;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   40: pop
      //   41: aload_2
      //   42: getfield 227	android/graphics/BitmapFactory$Options:outWidth	I
      //   45: istore 6
      //   47: aload_2
      //   48: getfield 230	android/graphics/BitmapFactory$Options:outHeight	I
      //   51: istore 7
      //   53: iload 6
      //   55: ifle +8 -> 63
      //   58: iload 7
      //   60: ifgt +5 -> 65
      //   63: aconst_null
      //   64: areturn
      //   65: iload 6
      //   67: iload 7
      //   69: invokestatic 233	java/lang/Math:max	(II)I
      //   72: istore 5
      //   74: iconst_1
      //   75: istore 4
      //   77: iload 5
      //   79: sipush 3500
      //   82: if_icmple +18 -> 100
      //   85: iload 5
      //   87: iconst_1
      //   88: iushr
      //   89: istore 5
      //   91: iload 4
      //   93: iconst_1
      //   94: ishl
      //   95: istore 4
      //   97: goto -20 -> 77
      //   100: iload 4
      //   102: ifle -39 -> 63
      //   105: iload 6
      //   107: iload 7
      //   109: invokestatic 235	java/lang/Math:min	(II)I
      //   112: iload 4
      //   114: idiv
      //   115: ifle -52 -> 63
      //   118: aload_0
      //   119: getfield 49	android/support/v4/print/PrintHelper$PrintHelperApi19:mLock	Ljava/lang/Object;
      //   122: astore_2
      //   123: aload_2
      //   124: monitorenter
      //   125: aload_0
      //   126: new 218	android/graphics/BitmapFactory$Options
      //   129: dup
      //   130: invokespecial 219	android/graphics/BitmapFactory$Options:<init>	()V
      //   133: putfield 47	android/support/v4/print/PrintHelper$PrintHelperApi19:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
      //   136: aload_0
      //   137: getfield 47	android/support/v4/print/PrintHelper$PrintHelperApi19:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
      //   140: iconst_1
      //   141: putfield 238	android/graphics/BitmapFactory$Options:inMutable	Z
      //   144: aload_0
      //   145: getfield 47	android/support/v4/print/PrintHelper$PrintHelperApi19:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
      //   148: iload 4
      //   150: putfield 241	android/graphics/BitmapFactory$Options:inSampleSize	I
      //   153: aload_0
      //   154: getfield 47	android/support/v4/print/PrintHelper$PrintHelperApi19:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
      //   157: astore_3
      //   158: aload_2
      //   159: monitorexit
      //   160: aload_0
      //   161: aload_1
      //   162: aload_3
      //   163: invokespecial 224	android/support/v4/print/PrintHelper$PrintHelperApi19:loadBitmap	(Landroid/net/Uri;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
      //   166: astore_2
      //   167: aload_0
      //   168: getfield 49	android/support/v4/print/PrintHelper$PrintHelperApi19:mLock	Ljava/lang/Object;
      //   171: astore_1
      //   172: aload_1
      //   173: monitorenter
      //   174: aload_0
      //   175: aconst_null
      //   176: putfield 47	android/support/v4/print/PrintHelper$PrintHelperApi19:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
      //   179: aload_1
      //   180: monitorexit
      //   181: aload_2
      //   182: areturn
      //   183: astore_2
      //   184: aload_1
      //   185: monitorexit
      //   186: aload_2
      //   187: athrow
      //   188: astore_1
      //   189: aload_2
      //   190: monitorexit
      //   191: aload_1
      //   192: athrow
      //   193: astore_2
      //   194: aload_0
      //   195: getfield 49	android/support/v4/print/PrintHelper$PrintHelperApi19:mLock	Ljava/lang/Object;
      //   198: astore_1
      //   199: aload_1
      //   200: monitorenter
      //   201: aload_0
      //   202: aconst_null
      //   203: putfield 47	android/support/v4/print/PrintHelper$PrintHelperApi19:mDecodeOptions	Landroid/graphics/BitmapFactory$Options;
      //   206: aload_1
      //   207: monitorexit
      //   208: aload_2
      //   209: athrow
      //   210: astore_2
      //   211: aload_1
      //   212: monitorexit
      //   213: aload_2
      //   214: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	215	0	this	PrintHelperApi19
      //   0	215	1	paramUri	Uri
      //   28	154	2	localObject1	Object
      //   183	7	2	localObject2	Object
      //   193	16	2	localObject3	Object
      //   210	4	2	localObject4	Object
      //   157	6	3	localOptions	BitmapFactory.Options
      //   75	74	4	i	int
      //   72	18	5	j	int
      //   45	61	6	k	int
      //   51	57	7	m	int
      // Exception table:
      //   from	to	target	type
      //   174	181	183	finally
      //   184	186	183	finally
      //   125	160	188	finally
      //   189	191	188	finally
      //   160	167	193	finally
      //   201	208	210	finally
      //   211	213	210	finally
    }
    
    private void writeBitmap(final PrintAttributes paramPrintAttributes, final int paramInt, final Bitmap paramBitmap, final ParcelFileDescriptor paramParcelFileDescriptor, final CancellationSignal paramCancellationSignal, final PrintDocumentAdapter.WriteResultCallback paramWriteResultCallback)
    {
      if (this.mIsMinMarginsHandlingCorrect) {}
      for (final PrintAttributes localPrintAttributes = paramPrintAttributes;; localPrintAttributes = copyAttributes(paramPrintAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build())
      {
        new AsyncTask()
        {
          /* Error */
          protected Throwable doInBackground(Void... paramAnonymousVarArgs)
          {
            // Byte code:
            //   0: aload_0
            //   1: getfield 34	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$cancellationSignal	Landroid/os/CancellationSignal;
            //   4: invokevirtual 67	android/os/CancellationSignal:isCanceled	()Z
            //   7: ifeq +5 -> 12
            //   10: aconst_null
            //   11: areturn
            //   12: new 69	android/print/pdf/PrintedPdfDocument
            //   15: dup
            //   16: aload_0
            //   17: getfield 32	android/support/v4/print/PrintHelper$PrintHelperApi19$2:this$0	Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
            //   20: getfield 73	android/support/v4/print/PrintHelper$PrintHelperApi19:mContext	Landroid/content/Context;
            //   23: aload_0
            //   24: getfield 36	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$pdfAttributes	Landroid/print/PrintAttributes;
            //   27: invokespecial 76	android/print/pdf/PrintedPdfDocument:<init>	(Landroid/content/Context;Landroid/print/PrintAttributes;)V
            //   30: astore_3
            //   31: aload_0
            //   32: getfield 32	android/support/v4/print/PrintHelper$PrintHelperApi19$2:this$0	Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
            //   35: aload_0
            //   36: getfield 38	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$bitmap	Landroid/graphics/Bitmap;
            //   39: aload_0
            //   40: getfield 36	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$pdfAttributes	Landroid/print/PrintAttributes;
            //   43: invokevirtual 82	android/print/PrintAttributes:getColorMode	()I
            //   46: invokestatic 86	android/support/v4/print/PrintHelper$PrintHelperApi19:access$100	(Landroid/support/v4/print/PrintHelper$PrintHelperApi19;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
            //   49: astore_2
            //   50: aload_0
            //   51: getfield 34	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$cancellationSignal	Landroid/os/CancellationSignal;
            //   54: invokevirtual 67	android/os/CancellationSignal:isCanceled	()Z
            //   57: istore 7
            //   59: iload 7
            //   61: ifne +322 -> 383
            //   64: aload_3
            //   65: iconst_1
            //   66: invokevirtual 90	android/print/pdf/PrintedPdfDocument:startPage	(I)Landroid/graphics/pdf/PdfDocument$Page;
            //   69: astore 4
            //   71: aload_0
            //   72: getfield 32	android/support/v4/print/PrintHelper$PrintHelperApi19$2:this$0	Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
            //   75: getfield 94	android/support/v4/print/PrintHelper$PrintHelperApi19:mIsMinMarginsHandlingCorrect	Z
            //   78: ifeq +120 -> 198
            //   81: new 96	android/graphics/RectF
            //   84: dup
            //   85: aload 4
            //   87: invokevirtual 102	android/graphics/pdf/PdfDocument$Page:getInfo	()Landroid/graphics/pdf/PdfDocument$PageInfo;
            //   90: invokevirtual 108	android/graphics/pdf/PdfDocument$PageInfo:getContentRect	()Landroid/graphics/Rect;
            //   93: invokespecial 111	android/graphics/RectF:<init>	(Landroid/graphics/Rect;)V
            //   96: astore_1
            //   97: aload_0
            //   98: getfield 32	android/support/v4/print/PrintHelper$PrintHelperApi19$2:this$0	Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
            //   101: aload_2
            //   102: invokevirtual 116	android/graphics/Bitmap:getWidth	()I
            //   105: aload_2
            //   106: invokevirtual 119	android/graphics/Bitmap:getHeight	()I
            //   109: aload_1
            //   110: aload_0
            //   111: getfield 42	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fittingMode	I
            //   114: invokestatic 123	android/support/v4/print/PrintHelper$PrintHelperApi19:access$200	(Landroid/support/v4/print/PrintHelper$PrintHelperApi19;IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
            //   117: astore 5
            //   119: aload_0
            //   120: getfield 32	android/support/v4/print/PrintHelper$PrintHelperApi19$2:this$0	Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
            //   123: getfield 94	android/support/v4/print/PrintHelper$PrintHelperApi19:mIsMinMarginsHandlingCorrect	Z
            //   126: ifeq +166 -> 292
            //   129: aload 4
            //   131: invokevirtual 127	android/graphics/pdf/PdfDocument$Page:getCanvas	()Landroid/graphics/Canvas;
            //   134: aload_2
            //   135: aload 5
            //   137: aconst_null
            //   138: invokevirtual 133	android/graphics/Canvas:drawBitmap	(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
            //   141: aload_3
            //   142: aload 4
            //   144: invokevirtual 137	android/print/pdf/PrintedPdfDocument:finishPage	(Landroid/graphics/pdf/PdfDocument$Page;)V
            //   147: aload_0
            //   148: getfield 34	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$cancellationSignal	Landroid/os/CancellationSignal;
            //   151: invokevirtual 67	android/os/CancellationSignal:isCanceled	()Z
            //   154: istore 7
            //   156: iload 7
            //   158: ifeq +161 -> 319
            //   161: aload_3
            //   162: invokevirtual 140	android/print/pdf/PrintedPdfDocument:close	()V
            //   165: aload_0
            //   166: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   169: astore_1
            //   170: aload_1
            //   171: ifnull +10 -> 181
            //   174: aload_0
            //   175: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   178: invokevirtual 143	android/os/ParcelFileDescriptor:close	()V
            //   181: aload_2
            //   182: aload_0
            //   183: getfield 38	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$bitmap	Landroid/graphics/Bitmap;
            //   186: if_acmpeq +197 -> 383
            //   189: aload_2
            //   190: invokevirtual 146	android/graphics/Bitmap:recycle	()V
            //   193: aconst_null
            //   194: areturn
            //   195: astore_1
            //   196: aload_1
            //   197: areturn
            //   198: new 69	android/print/pdf/PrintedPdfDocument
            //   201: dup
            //   202: aload_0
            //   203: getfield 32	android/support/v4/print/PrintHelper$PrintHelperApi19$2:this$0	Landroid/support/v4/print/PrintHelper$PrintHelperApi19;
            //   206: getfield 73	android/support/v4/print/PrintHelper$PrintHelperApi19:mContext	Landroid/content/Context;
            //   209: aload_0
            //   210: getfield 40	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$attributes	Landroid/print/PrintAttributes;
            //   213: invokespecial 76	android/print/pdf/PrintedPdfDocument:<init>	(Landroid/content/Context;Landroid/print/PrintAttributes;)V
            //   216: astore 5
            //   218: aload 5
            //   220: iconst_1
            //   221: invokevirtual 90	android/print/pdf/PrintedPdfDocument:startPage	(I)Landroid/graphics/pdf/PdfDocument$Page;
            //   224: astore 6
            //   226: new 96	android/graphics/RectF
            //   229: dup
            //   230: aload 6
            //   232: invokevirtual 102	android/graphics/pdf/PdfDocument$Page:getInfo	()Landroid/graphics/pdf/PdfDocument$PageInfo;
            //   235: invokevirtual 108	android/graphics/pdf/PdfDocument$PageInfo:getContentRect	()Landroid/graphics/Rect;
            //   238: invokespecial 111	android/graphics/RectF:<init>	(Landroid/graphics/Rect;)V
            //   241: astore_1
            //   242: aload 5
            //   244: aload 6
            //   246: invokevirtual 137	android/print/pdf/PrintedPdfDocument:finishPage	(Landroid/graphics/pdf/PdfDocument$Page;)V
            //   249: aload 5
            //   251: invokevirtual 140	android/print/pdf/PrintedPdfDocument:close	()V
            //   254: goto -157 -> 97
            //   257: astore_1
            //   258: aload_3
            //   259: invokevirtual 140	android/print/pdf/PrintedPdfDocument:close	()V
            //   262: aload_0
            //   263: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   266: astore_3
            //   267: aload_3
            //   268: ifnull +10 -> 278
            //   271: aload_0
            //   272: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   275: invokevirtual 143	android/os/ParcelFileDescriptor:close	()V
            //   278: aload_2
            //   279: aload_0
            //   280: getfield 38	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$bitmap	Landroid/graphics/Bitmap;
            //   283: if_acmpeq +7 -> 290
            //   286: aload_2
            //   287: invokevirtual 146	android/graphics/Bitmap:recycle	()V
            //   290: aload_1
            //   291: athrow
            //   292: aload 5
            //   294: aload_1
            //   295: getfield 150	android/graphics/RectF:left	F
            //   298: aload_1
            //   299: getfield 153	android/graphics/RectF:top	F
            //   302: invokevirtual 159	android/graphics/Matrix:postTranslate	(FF)Z
            //   305: pop
            //   306: aload 4
            //   308: invokevirtual 127	android/graphics/pdf/PdfDocument$Page:getCanvas	()Landroid/graphics/Canvas;
            //   311: aload_1
            //   312: invokevirtual 163	android/graphics/Canvas:clipRect	(Landroid/graphics/RectF;)Z
            //   315: pop
            //   316: goto -187 -> 129
            //   319: aload_3
            //   320: new 165	java/io/FileOutputStream
            //   323: dup
            //   324: aload_0
            //   325: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   328: invokevirtual 169	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
            //   331: invokespecial 172	java/io/FileOutputStream:<init>	(Ljava/io/FileDescriptor;)V
            //   334: invokevirtual 176	android/print/pdf/PrintedPdfDocument:writeTo	(Ljava/io/OutputStream;)V
            //   337: aload_3
            //   338: invokevirtual 140	android/print/pdf/PrintedPdfDocument:close	()V
            //   341: aload_0
            //   342: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   345: astore_1
            //   346: aload_1
            //   347: ifnull +10 -> 357
            //   350: aload_0
            //   351: getfield 44	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$fileDescriptor	Landroid/os/ParcelFileDescriptor;
            //   354: invokevirtual 143	android/os/ParcelFileDescriptor:close	()V
            //   357: aload_2
            //   358: aload_0
            //   359: getfield 38	android/support/v4/print/PrintHelper$PrintHelperApi19$2:val$bitmap	Landroid/graphics/Bitmap;
            //   362: if_acmpeq +21 -> 383
            //   365: aload_2
            //   366: invokevirtual 146	android/graphics/Bitmap:recycle	()V
            //   369: aconst_null
            //   370: areturn
            //   371: astore_3
            //   372: goto -94 -> 278
            //   375: astore_1
            //   376: goto -19 -> 357
            //   379: astore_1
            //   380: goto -199 -> 181
            //   383: aconst_null
            //   384: areturn
            // Local variable table:
            //   start	length	slot	name	signature
            //   0	385	0	this	2
            //   0	385	1	paramAnonymousVarArgs	Void[]
            //   49	317	2	localBitmap	Bitmap
            //   30	308	3	localObject1	Object
            //   371	1	3	localIOException	IOException
            //   69	238	4	localPage1	android.graphics.pdf.PdfDocument.Page
            //   117	176	5	localObject2	Object
            //   224	21	6	localPage2	android.graphics.pdf.PdfDocument.Page
            //   57	100	7	bool	boolean
            // Exception table:
            //   from	to	target	type
            //   0	10	195	java/lang/Throwable
            //   12	59	195	java/lang/Throwable
            //   161	170	195	java/lang/Throwable
            //   174	181	195	java/lang/Throwable
            //   181	193	195	java/lang/Throwable
            //   258	267	195	java/lang/Throwable
            //   271	278	195	java/lang/Throwable
            //   278	290	195	java/lang/Throwable
            //   290	292	195	java/lang/Throwable
            //   337	346	195	java/lang/Throwable
            //   350	357	195	java/lang/Throwable
            //   357	369	195	java/lang/Throwable
            //   64	97	257	finally
            //   97	129	257	finally
            //   129	156	257	finally
            //   198	254	257	finally
            //   292	316	257	finally
            //   319	337	257	finally
            //   271	278	371	java/io/IOException
            //   350	357	375	java/io/IOException
            //   174	181	379	java/io/IOException
          }
          
          protected void onPostExecute(Throwable paramAnonymousThrowable)
          {
            if (paramCancellationSignal.isCanceled())
            {
              paramWriteResultCallback.onWriteCancelled();
              return;
            }
            if (paramAnonymousThrowable == null)
            {
              paramWriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
              return;
            }
            Log.e("PrintHelperApi19", "Error writing printed content", paramAnonymousThrowable);
            paramWriteResultCallback.onWriteFailed(null);
          }
        }.execute(new Void[0]);
        return;
      }
    }
    
    protected PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes)
    {
      PrintAttributes.Builder localBuilder = new PrintAttributes.Builder().setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
      if (paramPrintAttributes.getColorMode() != 0) {
        localBuilder.setColorMode(paramPrintAttributes.getColorMode());
      }
      return localBuilder;
    }
    
    public int getColorMode()
    {
      return this.mColorMode;
    }
    
    public int getOrientation()
    {
      if (this.mOrientation == 0) {
        return 1;
      }
      return this.mOrientation;
    }
    
    public int getScaleMode()
    {
      return this.mScaleMode;
    }
    
    public void printBitmap(final String paramString, final Bitmap paramBitmap, final PrintHelper.OnPrintFinishCallback paramOnPrintFinishCallback)
    {
      if (paramBitmap == null) {
        return;
      }
      final int i = this.mScaleMode;
      PrintManager localPrintManager = (PrintManager)this.mContext.getSystemService("print");
      if (isPortrait(paramBitmap)) {}
      for (Object localObject = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;; localObject = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE)
      {
        localObject = new PrintAttributes.Builder().setMediaSize((PrintAttributes.MediaSize)localObject).setColorMode(this.mColorMode).build();
        localPrintManager.print(paramString, new PrintDocumentAdapter()
        {
          private PrintAttributes mAttributes;
          
          public void onFinish()
          {
            if (paramOnPrintFinishCallback != null) {
              paramOnPrintFinishCallback.onFinish();
            }
          }
          
          public void onLayout(PrintAttributes paramAnonymousPrintAttributes1, PrintAttributes paramAnonymousPrintAttributes2, CancellationSignal paramAnonymousCancellationSignal, PrintDocumentAdapter.LayoutResultCallback paramAnonymousLayoutResultCallback, Bundle paramAnonymousBundle)
          {
            boolean bool = true;
            this.mAttributes = paramAnonymousPrintAttributes2;
            paramAnonymousCancellationSignal = new PrintDocumentInfo.Builder(paramString).setContentType(1).setPageCount(1).build();
            if (!paramAnonymousPrintAttributes2.equals(paramAnonymousPrintAttributes1)) {}
            for (;;)
            {
              paramAnonymousLayoutResultCallback.onLayoutFinished(paramAnonymousCancellationSignal, bool);
              return;
              bool = false;
            }
          }
          
          public void onWrite(PageRange[] paramAnonymousArrayOfPageRange, ParcelFileDescriptor paramAnonymousParcelFileDescriptor, CancellationSignal paramAnonymousCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramAnonymousWriteResultCallback)
          {
            PrintHelper.PrintHelperApi19.this.writeBitmap(this.mAttributes, i, paramBitmap, paramAnonymousParcelFileDescriptor, paramAnonymousCancellationSignal, paramAnonymousWriteResultCallback);
          }
        }, (PrintAttributes)localObject);
        return;
      }
    }
    
    public void printBitmap(final String paramString, final Uri paramUri, final PrintHelper.OnPrintFinishCallback paramOnPrintFinishCallback)
      throws FileNotFoundException
    {
      paramUri = new PrintDocumentAdapter()
      {
        private PrintAttributes mAttributes;
        Bitmap mBitmap = null;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        
        private void cancelLoad()
        {
          synchronized (PrintHelper.PrintHelperApi19.this.mLock)
          {
            if (PrintHelper.PrintHelperApi19.this.mDecodeOptions != null)
            {
              PrintHelper.PrintHelperApi19.this.mDecodeOptions.requestCancelDecode();
              PrintHelper.PrintHelperApi19.this.mDecodeOptions = null;
            }
            return;
          }
        }
        
        public void onFinish()
        {
          super.onFinish();
          cancelLoad();
          if (this.mLoadBitmap != null) {
            this.mLoadBitmap.cancel(true);
          }
          if (paramOnPrintFinishCallback != null) {
            paramOnPrintFinishCallback.onFinish();
          }
          if (this.mBitmap != null)
          {
            this.mBitmap.recycle();
            this.mBitmap = null;
          }
        }
        
        public void onLayout(final PrintAttributes paramAnonymousPrintAttributes1, final PrintAttributes paramAnonymousPrintAttributes2, final CancellationSignal paramAnonymousCancellationSignal, final PrintDocumentAdapter.LayoutResultCallback paramAnonymousLayoutResultCallback, Bundle paramAnonymousBundle)
        {
          boolean bool = true;
          try
          {
            this.mAttributes = paramAnonymousPrintAttributes2;
            if (paramAnonymousCancellationSignal.isCanceled())
            {
              paramAnonymousLayoutResultCallback.onLayoutCancelled();
              return;
            }
          }
          finally {}
          if (this.mBitmap != null)
          {
            paramAnonymousCancellationSignal = new PrintDocumentInfo.Builder(paramString).setContentType(1).setPageCount(1).build();
            if (!paramAnonymousPrintAttributes2.equals(paramAnonymousPrintAttributes1)) {}
            for (;;)
            {
              paramAnonymousLayoutResultCallback.onLayoutFinished(paramAnonymousCancellationSignal, bool);
              return;
              bool = false;
            }
          }
          this.mLoadBitmap = new AsyncTask()
          {
            protected Bitmap doInBackground(Uri... paramAnonymous2VarArgs)
            {
              try
              {
                paramAnonymous2VarArgs = PrintHelper.PrintHelperApi19.this.loadConstrainedBitmap(PrintHelper.PrintHelperApi19.3.this.val$imageFile);
                return paramAnonymous2VarArgs;
              }
              catch (FileNotFoundException paramAnonymous2VarArgs) {}
              return null;
            }
            
            protected void onCancelled(Bitmap paramAnonymous2Bitmap)
            {
              paramAnonymousLayoutResultCallback.onLayoutCancelled();
              PrintHelper.PrintHelperApi19.3.this.mLoadBitmap = null;
            }
            
            protected void onPostExecute(Bitmap paramAnonymous2Bitmap)
            {
              super.onPostExecute(paramAnonymous2Bitmap);
              Object localObject = paramAnonymous2Bitmap;
              if (paramAnonymous2Bitmap != null) {
                if (PrintHelper.PrintHelperApi19.this.mPrintActivityRespectsOrientation)
                {
                  localObject = paramAnonymous2Bitmap;
                  if (PrintHelper.PrintHelperApi19.this.mOrientation != 0) {
                    break label105;
                  }
                }
              }
              for (;;)
              {
                try
                {
                  PrintAttributes.MediaSize localMediaSize = PrintHelper.PrintHelperApi19.3.this.mAttributes.getMediaSize();
                  localObject = paramAnonymous2Bitmap;
                  if (localMediaSize != null)
                  {
                    localObject = paramAnonymous2Bitmap;
                    if (localMediaSize.isPortrait() != PrintHelper.PrintHelperApi19.isPortrait(paramAnonymous2Bitmap))
                    {
                      localObject = new Matrix();
                      ((Matrix)localObject).postRotate(90.0F);
                      localObject = Bitmap.createBitmap(paramAnonymous2Bitmap, 0, 0, paramAnonymous2Bitmap.getWidth(), paramAnonymous2Bitmap.getHeight(), (Matrix)localObject, true);
                    }
                  }
                  label105:
                  PrintHelper.PrintHelperApi19.3.this.mBitmap = ((Bitmap)localObject);
                  if (localObject == null) {
                    break label190;
                  }
                  paramAnonymous2Bitmap = new PrintDocumentInfo.Builder(PrintHelper.PrintHelperApi19.3.this.val$jobName).setContentType(1).setPageCount(1).build();
                  if (!paramAnonymousPrintAttributes2.equals(paramAnonymousPrintAttributes1))
                  {
                    bool = true;
                    paramAnonymousLayoutResultCallback.onLayoutFinished(paramAnonymous2Bitmap, bool);
                    PrintHelper.PrintHelperApi19.3.this.mLoadBitmap = null;
                    return;
                  }
                }
                finally {}
                boolean bool = false;
                continue;
                label190:
                paramAnonymousLayoutResultCallback.onLayoutFailed(null);
              }
            }
            
            protected void onPreExecute()
            {
              paramAnonymousCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener()
              {
                public void onCancel()
                {
                  PrintHelper.PrintHelperApi19.3.this.cancelLoad();
                  PrintHelper.PrintHelperApi19.3.1.this.cancel(false);
                }
              });
            }
          }.execute(new Uri[0]);
        }
        
        public void onWrite(PageRange[] paramAnonymousArrayOfPageRange, ParcelFileDescriptor paramAnonymousParcelFileDescriptor, CancellationSignal paramAnonymousCancellationSignal, PrintDocumentAdapter.WriteResultCallback paramAnonymousWriteResultCallback)
        {
          PrintHelper.PrintHelperApi19.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, paramAnonymousParcelFileDescriptor, paramAnonymousCancellationSignal, paramAnonymousWriteResultCallback);
        }
      };
      paramOnPrintFinishCallback = (PrintManager)this.mContext.getSystemService("print");
      PrintAttributes.Builder localBuilder = new PrintAttributes.Builder();
      localBuilder.setColorMode(this.mColorMode);
      if ((this.mOrientation == 1) || (this.mOrientation == 0)) {
        localBuilder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
      }
      for (;;)
      {
        paramOnPrintFinishCallback.print(paramString, paramUri, localBuilder.build());
        return;
        if (this.mOrientation == 2) {
          localBuilder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
        }
      }
    }
    
    public void setColorMode(int paramInt)
    {
      this.mColorMode = paramInt;
    }
    
    public void setOrientation(int paramInt)
    {
      this.mOrientation = paramInt;
    }
    
    public void setScaleMode(int paramInt)
    {
      this.mScaleMode = paramInt;
    }
  }
  
  @RequiresApi(20)
  private static class PrintHelperApi20
    extends PrintHelper.PrintHelperApi19
  {
    PrintHelperApi20(Context paramContext)
    {
      super();
      this.mPrintActivityRespectsOrientation = false;
    }
  }
  
  @RequiresApi(23)
  private static class PrintHelperApi23
    extends PrintHelper.PrintHelperApi20
  {
    PrintHelperApi23(Context paramContext)
    {
      super();
      this.mIsMinMarginsHandlingCorrect = false;
    }
    
    protected PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes)
    {
      PrintAttributes.Builder localBuilder = super.copyAttributes(paramPrintAttributes);
      if (paramPrintAttributes.getDuplexMode() != 0) {
        localBuilder.setDuplexMode(paramPrintAttributes.getDuplexMode());
      }
      return localBuilder;
    }
  }
  
  @RequiresApi(24)
  private static class PrintHelperApi24
    extends PrintHelper.PrintHelperApi23
  {
    PrintHelperApi24(Context paramContext)
    {
      super();
      this.mIsMinMarginsHandlingCorrect = true;
      this.mPrintActivityRespectsOrientation = true;
    }
  }
  
  private static final class PrintHelperStub
    implements PrintHelper.PrintHelperVersionImpl
  {
    int mColorMode = 2;
    int mOrientation = 1;
    int mScaleMode = 2;
    
    public int getColorMode()
    {
      return this.mColorMode;
    }
    
    public int getOrientation()
    {
      return this.mOrientation;
    }
    
    public int getScaleMode()
    {
      return this.mScaleMode;
    }
    
    public void printBitmap(String paramString, Bitmap paramBitmap, PrintHelper.OnPrintFinishCallback paramOnPrintFinishCallback) {}
    
    public void printBitmap(String paramString, Uri paramUri, PrintHelper.OnPrintFinishCallback paramOnPrintFinishCallback) {}
    
    public void setColorMode(int paramInt)
    {
      this.mColorMode = paramInt;
    }
    
    public void setOrientation(int paramInt)
    {
      this.mOrientation = paramInt;
    }
    
    public void setScaleMode(int paramInt)
    {
      this.mScaleMode = paramInt;
    }
  }
  
  static abstract interface PrintHelperVersionImpl
  {
    public abstract int getColorMode();
    
    public abstract int getOrientation();
    
    public abstract int getScaleMode();
    
    public abstract void printBitmap(String paramString, Bitmap paramBitmap, PrintHelper.OnPrintFinishCallback paramOnPrintFinishCallback);
    
    public abstract void printBitmap(String paramString, Uri paramUri, PrintHelper.OnPrintFinishCallback paramOnPrintFinishCallback)
      throws FileNotFoundException;
    
    public abstract void setColorMode(int paramInt);
    
    public abstract void setOrientation(int paramInt);
    
    public abstract void setScaleMode(int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface ScaleMode {}
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.print.PrintHelper
 * JD-Core Version:    0.7.0.1
 */