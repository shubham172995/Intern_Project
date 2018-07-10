package android.support.v4.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat.FontFamilyFilesResourceEntry;
import android.support.v4.content.res.FontResourcesParserCompat.FontFileResourceEntry;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

@RequiresApi(26)
@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatApi26Impl
  extends TypefaceCompatApi21Impl
{
  private static final String ABORT_CREATION_METHOD = "abortCreation";
  private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
  private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
  private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
  private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
  private static final String FREEZE_METHOD = "freeze";
  private static final int RESOLVE_BY_FONT_TABLE = -1;
  private static final String TAG = "TypefaceCompatApi26Impl";
  private static final Method sAbortCreation;
  private static final Method sAddFontFromAssetManager;
  private static final Method sAddFontFromBuffer;
  private static final Method sCreateFromFamiliesWithDefault;
  private static final Class sFontFamily;
  private static final Constructor sFontFamilyCtor;
  private static final Method sFreeze;
  
  static
  {
    try
    {
      localClass = Class.forName("android.graphics.FontFamily");
      localConstructor = localClass.getConstructor(new Class[0]);
      localMethod2 = localClass.getMethod("addFontFromAssetManager", new Class[] { AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, [Landroid.graphics.fonts.FontVariationAxis.class });
      localMethod3 = localClass.getMethod("addFontFromBuffer", new Class[] { ByteBuffer.class, Integer.TYPE, [Landroid.graphics.fonts.FontVariationAxis.class, Integer.TYPE, Integer.TYPE });
      localMethod5 = localClass.getMethod("freeze", new Class[0]);
      Method localMethod1 = localClass.getMethod("abortCreation", new Class[0]);
      localMethod4 = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", new Class[] { Array.newInstance(localClass, 1).getClass(), Integer.TYPE, Integer.TYPE });
      localMethod4.setAccessible(true);
      sFontFamilyCtor = localConstructor;
      sFontFamily = localClass;
      sAddFontFromAssetManager = localMethod2;
      sAddFontFromBuffer = localMethod3;
      sFreeze = localMethod5;
      sAbortCreation = localMethod1;
      sCreateFromFamiliesWithDefault = localMethod4;
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      for (;;)
      {
        Log.e("TypefaceCompatApi26Impl", "Unable to collect necessary methods for class " + localClassNotFoundException.getClass().getName(), localClassNotFoundException);
        Class localClass = null;
        Constructor localConstructor = null;
        Method localMethod2 = null;
        Method localMethod3 = null;
        Method localMethod5 = null;
        Object localObject = null;
        Method localMethod4 = null;
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      label217:
      break label217;
    }
  }
  
  private static boolean abortCreation(Object paramObject)
  {
    try
    {
      boolean bool = ((Boolean)sAbortCreation.invoke(paramObject, new Object[0])).booleanValue();
      return bool;
    }
    catch (IllegalAccessException paramObject)
    {
      throw new RuntimeException(paramObject);
    }
    catch (InvocationTargetException paramObject)
    {
      label21:
      break label21;
    }
  }
  
  private static boolean addFontFromAssetManager(Context paramContext, Object paramObject, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      boolean bool = ((Boolean)sAddFontFromAssetManager.invoke(paramObject, new Object[] { paramContext.getAssets(), paramString, Integer.valueOf(0), Boolean.valueOf(false), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), null })).booleanValue();
      return bool;
    }
    catch (IllegalAccessException paramContext)
    {
      throw new RuntimeException(paramContext);
    }
    catch (InvocationTargetException paramContext)
    {
      label78:
      break label78;
    }
  }
  
  private static boolean addFontFromBuffer(Object paramObject, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      boolean bool = ((Boolean)sAddFontFromBuffer.invoke(paramObject, new Object[] { paramByteBuffer, Integer.valueOf(paramInt1), null, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) })).booleanValue();
      return bool;
    }
    catch (IllegalAccessException paramObject)
    {
      throw new RuntimeException(paramObject);
    }
    catch (InvocationTargetException paramObject)
    {
      label53:
      break label53;
    }
  }
  
  private static Typeface createFromFamiliesWithDefault(Object paramObject)
  {
    try
    {
      Object localObject = Array.newInstance(sFontFamily, 1);
      Array.set(localObject, 0, paramObject);
      paramObject = (Typeface)sCreateFromFamiliesWithDefault.invoke(null, new Object[] { localObject, Integer.valueOf(-1), Integer.valueOf(-1) });
      return paramObject;
    }
    catch (IllegalAccessException paramObject)
    {
      throw new RuntimeException(paramObject);
    }
    catch (InvocationTargetException paramObject)
    {
      label50:
      break label50;
    }
  }
  
  private static boolean freeze(Object paramObject)
  {
    try
    {
      boolean bool = ((Boolean)sFreeze.invoke(paramObject, new Object[0])).booleanValue();
      return bool;
    }
    catch (IllegalAccessException paramObject)
    {
      throw new RuntimeException(paramObject);
    }
    catch (InvocationTargetException paramObject)
    {
      label21:
      break label21;
    }
  }
  
  private static boolean isFontFamilyPrivateAPIAvailable()
  {
    if (sAddFontFromAssetManager == null) {
      Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
    }
    return sAddFontFromAssetManager != null;
  }
  
  private static Object newFamily()
  {
    try
    {
      Object localObject = sFontFamilyCtor.newInstance(new Object[0]);
      return localObject;
    }
    catch (InstantiationException localInstantiationException)
    {
      throw new RuntimeException(localInstantiationException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label14;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      label14:
      break label14;
    }
  }
  
  public Typeface createFromFontFamilyFilesResourceEntry(Context paramContext, FontResourcesParserCompat.FontFamilyFilesResourceEntry paramFontFamilyFilesResourceEntry, Resources paramResources, int paramInt)
  {
    if (!isFontFamilyPrivateAPIAvailable()) {
      return super.createFromFontFamilyFilesResourceEntry(paramContext, paramFontFamilyFilesResourceEntry, paramResources, paramInt);
    }
    paramResources = newFamily();
    paramFontFamilyFilesResourceEntry = paramFontFamilyFilesResourceEntry.getEntries();
    int j = paramFontFamilyFilesResourceEntry.length;
    paramInt = 0;
    while (paramInt < j)
    {
      Object localObject = paramFontFamilyFilesResourceEntry[paramInt];
      String str = localObject.getFileName();
      int k = localObject.getWeight();
      if (localObject.isItalic()) {}
      for (int i = 1; !addFontFromAssetManager(paramContext, paramResources, str, 0, k, i); i = 0)
      {
        abortCreation(paramResources);
        return null;
      }
      paramInt += 1;
    }
    if (!freeze(paramResources)) {
      return null;
    }
    return createFromFamiliesWithDefault(paramResources);
  }
  
  /* Error */
  public Typeface createFromFontInfo(Context paramContext, @Nullable android.os.CancellationSignal paramCancellationSignal, @android.support.annotation.NonNull android.support.v4.provider.FontsContractCompat.FontInfo[] paramArrayOfFontInfo, int paramInt)
  {
    // Byte code:
    //   0: aload_3
    //   1: arraylength
    //   2: iconst_1
    //   3: if_icmpge +7 -> 10
    //   6: aconst_null
    //   7: astore_2
    //   8: aload_2
    //   9: areturn
    //   10: invokestatic 202	android/support/v4/graphics/TypefaceCompatApi26Impl:isFontFamilyPrivateAPIAvailable	()Z
    //   13: ifne +129 -> 142
    //   16: aload_0
    //   17: aload_3
    //   18: iload 4
    //   20: invokevirtual 244	android/support/v4/graphics/TypefaceCompatApi26Impl:findBestInfo	([Landroid/support/v4/provider/FontsContractCompat$FontInfo;I)Landroid/support/v4/provider/FontsContractCompat$FontInfo;
    //   23: astore 5
    //   25: aload_1
    //   26: invokevirtual 248	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   29: astore_1
    //   30: aload_1
    //   31: aload 5
    //   33: invokevirtual 254	android/support/v4/provider/FontsContractCompat$FontInfo:getUri	()Landroid/net/Uri;
    //   36: ldc_w 256
    //   39: aload_2
    //   40: invokevirtual 262	android/content/ContentResolver:openFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
    //   43: astore_3
    //   44: aconst_null
    //   45: astore_2
    //   46: new 264	android/graphics/Typeface$Builder
    //   49: dup
    //   50: aload_3
    //   51: invokevirtual 270	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   54: invokespecial 273	android/graphics/Typeface$Builder:<init>	(Ljava/io/FileDescriptor;)V
    //   57: aload 5
    //   59: invokevirtual 274	android/support/v4/provider/FontsContractCompat$FontInfo:getWeight	()I
    //   62: invokevirtual 278	android/graphics/Typeface$Builder:setWeight	(I)Landroid/graphics/Typeface$Builder;
    //   65: aload 5
    //   67: invokevirtual 279	android/support/v4/provider/FontsContractCompat$FontInfo:isItalic	()Z
    //   70: invokevirtual 283	android/graphics/Typeface$Builder:setItalic	(Z)Landroid/graphics/Typeface$Builder;
    //   73: invokevirtual 287	android/graphics/Typeface$Builder:build	()Landroid/graphics/Typeface;
    //   76: astore_1
    //   77: aload_1
    //   78: astore_2
    //   79: aload_3
    //   80: ifnull -72 -> 8
    //   83: iconst_0
    //   84: ifeq +18 -> 102
    //   87: aload_3
    //   88: invokevirtual 290	android/os/ParcelFileDescriptor:close	()V
    //   91: aload_1
    //   92: areturn
    //   93: astore_1
    //   94: new 292	java/lang/NullPointerException
    //   97: dup
    //   98: invokespecial 293	java/lang/NullPointerException:<init>	()V
    //   101: athrow
    //   102: aload_3
    //   103: invokevirtual 290	android/os/ParcelFileDescriptor:close	()V
    //   106: aload_1
    //   107: areturn
    //   108: astore_2
    //   109: aload_2
    //   110: athrow
    //   111: astore_1
    //   112: aload_3
    //   113: ifnull +11 -> 124
    //   116: aload_2
    //   117: ifnull +18 -> 135
    //   120: aload_3
    //   121: invokevirtual 290	android/os/ParcelFileDescriptor:close	()V
    //   124: aload_1
    //   125: athrow
    //   126: astore_3
    //   127: aload_2
    //   128: aload_3
    //   129: invokevirtual 296	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   132: goto -8 -> 124
    //   135: aload_3
    //   136: invokevirtual 290	android/os/ParcelFileDescriptor:close	()V
    //   139: goto -15 -> 124
    //   142: aload_1
    //   143: aload_3
    //   144: aload_2
    //   145: invokestatic 302	android/support/v4/provider/FontsContractCompat:prepareFontData	(Landroid/content/Context;[Landroid/support/v4/provider/FontsContractCompat$FontInfo;Landroid/os/CancellationSignal;)Ljava/util/Map;
    //   148: astore 5
    //   150: invokestatic 206	android/support/v4/graphics/TypefaceCompatApi26Impl:newFamily	()Ljava/lang/Object;
    //   153: astore 6
    //   155: iconst_0
    //   156: istore 7
    //   158: aload_3
    //   159: arraylength
    //   160: istore 8
    //   162: iconst_0
    //   163: istore 4
    //   165: iload 4
    //   167: iload 8
    //   169: if_icmpge +93 -> 262
    //   172: aload_3
    //   173: iload 4
    //   175: aaload
    //   176: astore_1
    //   177: aload 5
    //   179: aload_1
    //   180: invokevirtual 254	android/support/v4/provider/FontsContractCompat$FontInfo:getUri	()Landroid/net/Uri;
    //   183: invokeinterface 308 2 0
    //   188: checkcast 81	java/nio/ByteBuffer
    //   191: astore_2
    //   192: aload_2
    //   193: ifnonnull +12 -> 205
    //   196: iload 4
    //   198: iconst_1
    //   199: iadd
    //   200: istore 4
    //   202: goto -37 -> 165
    //   205: aload_1
    //   206: invokevirtual 311	android/support/v4/provider/FontsContractCompat$FontInfo:getTtcIndex	()I
    //   209: istore 10
    //   211: aload_1
    //   212: invokevirtual 274	android/support/v4/provider/FontsContractCompat$FontInfo:getWeight	()I
    //   215: istore 9
    //   217: aload_1
    //   218: invokevirtual 279	android/support/v4/provider/FontsContractCompat$FontInfo:isItalic	()Z
    //   221: ifeq +29 -> 250
    //   224: iconst_1
    //   225: istore 7
    //   227: aload 6
    //   229: aload_2
    //   230: iload 10
    //   232: iload 9
    //   234: iload 7
    //   236: invokestatic 313	android/support/v4/graphics/TypefaceCompatApi26Impl:addFontFromBuffer	(Ljava/lang/Object;Ljava/nio/ByteBuffer;III)Z
    //   239: ifne +17 -> 256
    //   242: aload 6
    //   244: invokestatic 228	android/support/v4/graphics/TypefaceCompatApi26Impl:abortCreation	(Ljava/lang/Object;)Z
    //   247: pop
    //   248: aconst_null
    //   249: areturn
    //   250: iconst_0
    //   251: istore 7
    //   253: goto -26 -> 227
    //   256: iconst_1
    //   257: istore 7
    //   259: goto -63 -> 196
    //   262: iload 7
    //   264: ifne +11 -> 275
    //   267: aload 6
    //   269: invokestatic 228	android/support/v4/graphics/TypefaceCompatApi26Impl:abortCreation	(Ljava/lang/Object;)Z
    //   272: pop
    //   273: aconst_null
    //   274: areturn
    //   275: aload 6
    //   277: invokestatic 230	android/support/v4/graphics/TypefaceCompatApi26Impl:freeze	(Ljava/lang/Object;)Z
    //   280: ifne +5 -> 285
    //   283: aconst_null
    //   284: areturn
    //   285: aload 6
    //   287: invokestatic 232	android/support/v4/graphics/TypefaceCompatApi26Impl:createFromFamiliesWithDefault	(Ljava/lang/Object;)Landroid/graphics/Typeface;
    //   290: areturn
    //   291: astore_1
    //   292: goto -180 -> 112
    //   295: astore_1
    //   296: aconst_null
    //   297: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	298	0	this	TypefaceCompatApi26Impl
    //   0	298	1	paramContext	Context
    //   0	298	2	paramCancellationSignal	android.os.CancellationSignal
    //   0	298	3	paramArrayOfFontInfo	android.support.v4.provider.FontsContractCompat.FontInfo[]
    //   0	298	4	paramInt	int
    //   23	155	5	localObject1	Object
    //   153	133	6	localObject2	Object
    //   156	107	7	i	int
    //   160	10	8	j	int
    //   215	18	9	k	int
    //   209	22	10	m	int
    // Exception table:
    //   from	to	target	type
    //   87	91	93	java/lang/Throwable
    //   46	77	108	java/lang/Throwable
    //   109	111	111	finally
    //   120	124	126	java/lang/Throwable
    //   46	77	291	finally
    //   30	44	295	java/io/IOException
    //   87	91	295	java/io/IOException
    //   94	102	295	java/io/IOException
    //   102	106	295	java/io/IOException
    //   120	124	295	java/io/IOException
    //   124	126	295	java/io/IOException
    //   127	132	295	java/io/IOException
    //   135	139	295	java/io/IOException
  }
  
  @Nullable
  public Typeface createFromResourcesFontFile(Context paramContext, Resources paramResources, int paramInt1, String paramString, int paramInt2)
  {
    if (!isFontFamilyPrivateAPIAvailable()) {
      return super.createFromResourcesFontFile(paramContext, paramResources, paramInt1, paramString, paramInt2);
    }
    paramResources = newFamily();
    if (!addFontFromAssetManager(paramContext, paramResources, paramString, 0, -1, -1))
    {
      abortCreation(paramResources);
      return null;
    }
    if (!freeze(paramResources)) {
      return null;
    }
    return createFromFamiliesWithDefault(paramResources);
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.graphics.TypefaceCompatApi26Impl
 * JD-Core Version:    0.7.0.1
 */