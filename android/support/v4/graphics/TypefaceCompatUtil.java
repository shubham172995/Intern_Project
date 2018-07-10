package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatUtil
{
  private static final String CACHE_FILE_PREFIX = ".font";
  private static final String TAG = "TypefaceCompatUtil";
  
  public static void closeQuietly(Closeable paramCloseable)
  {
    if (paramCloseable != null) {}
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException paramCloseable) {}
  }
  
  @RequiresApi(19)
  public static ByteBuffer copyToDirectBuffer(Context paramContext, Resources paramResources, int paramInt)
  {
    paramContext = getTempFile(paramContext);
    if (paramContext == null) {
      return null;
    }
    try
    {
      boolean bool = copyToFile(paramContext, paramResources, paramInt);
      if (!bool) {
        return null;
      }
      paramResources = mmap(paramContext);
      return paramResources;
    }
    finally
    {
      paramContext.delete();
    }
  }
  
  public static boolean copyToFile(File paramFile, Resources paramResources, int paramInt)
  {
    Resources localResources = null;
    try
    {
      paramResources = paramResources.openRawResource(paramInt);
      localResources = paramResources;
      boolean bool = copyToFile(paramFile, paramResources);
      return bool;
    }
    finally
    {
      closeQuietly(localResources);
    }
  }
  
  /* Error */
  public static boolean copyToFile(File paramFile, java.io.InputStream paramInputStream)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 65	java/io/FileOutputStream
    //   7: dup
    //   8: aload_0
    //   9: iconst_0
    //   10: invokespecial 68	java/io/FileOutputStream:<init>	(Ljava/io/File;Z)V
    //   13: astore_0
    //   14: sipush 1024
    //   17: newarray byte
    //   19: astore_2
    //   20: aload_1
    //   21: aload_2
    //   22: invokevirtual 74	java/io/InputStream:read	([B)I
    //   25: istore 4
    //   27: iload 4
    //   29: iconst_m1
    //   30: if_icmpeq +51 -> 81
    //   33: aload_0
    //   34: aload_2
    //   35: iconst_0
    //   36: iload 4
    //   38: invokevirtual 78	java/io/FileOutputStream:write	([BII)V
    //   41: goto -21 -> 20
    //   44: astore_1
    //   45: aload_0
    //   46: astore_2
    //   47: ldc 15
    //   49: new 80	java/lang/StringBuilder
    //   52: dup
    //   53: invokespecial 81	java/lang/StringBuilder:<init>	()V
    //   56: ldc 83
    //   58: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: aload_1
    //   62: invokevirtual 91	java/io/IOException:getMessage	()Ljava/lang/String;
    //   65: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: invokevirtual 94	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   71: invokestatic 100	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   74: pop
    //   75: aload_0
    //   76: invokestatic 63	android/support/v4/graphics/TypefaceCompatUtil:closeQuietly	(Ljava/io/Closeable;)V
    //   79: iconst_0
    //   80: ireturn
    //   81: aload_0
    //   82: invokestatic 63	android/support/v4/graphics/TypefaceCompatUtil:closeQuietly	(Ljava/io/Closeable;)V
    //   85: iconst_1
    //   86: ireturn
    //   87: astore_0
    //   88: aload_2
    //   89: invokestatic 63	android/support/v4/graphics/TypefaceCompatUtil:closeQuietly	(Ljava/io/Closeable;)V
    //   92: aload_0
    //   93: athrow
    //   94: astore_1
    //   95: aload_0
    //   96: astore_2
    //   97: aload_1
    //   98: astore_0
    //   99: goto -11 -> 88
    //   102: astore_1
    //   103: aload_3
    //   104: astore_0
    //   105: goto -60 -> 45
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	108	0	paramFile	File
    //   0	108	1	paramInputStream	java.io.InputStream
    //   1	96	2	localObject1	Object
    //   3	101	3	localObject2	Object
    //   25	12	4	i	int
    // Exception table:
    //   from	to	target	type
    //   14	20	44	java/io/IOException
    //   20	27	44	java/io/IOException
    //   33	41	44	java/io/IOException
    //   4	14	87	finally
    //   47	75	87	finally
    //   14	20	94	finally
    //   20	27	94	finally
    //   33	41	94	finally
    //   4	14	102	java/io/IOException
  }
  
  public static File getTempFile(Context paramContext)
  {
    String str = ".font" + Process.myPid() + "-" + Process.myTid() + "-";
    int i = 0;
    while (i < 100)
    {
      File localFile = new File(paramContext.getCacheDir(), str + i);
      try
      {
        boolean bool = localFile.createNewFile();
        if (bool) {
          return localFile;
        }
      }
      catch (IOException localIOException)
      {
        i += 1;
      }
    }
    return null;
  }
  
  /* Error */
  @RequiresApi(19)
  public static ByteBuffer mmap(Context paramContext, android.os.CancellationSignal paramCancellationSignal, android.net.Uri paramUri)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 133	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: astore_0
    //   5: aload_0
    //   6: aload_2
    //   7: ldc 135
    //   9: aload_1
    //   10: invokevirtual 141	android/content/ContentResolver:openFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
    //   13: astore_2
    //   14: new 143	java/io/FileInputStream
    //   17: dup
    //   18: aload_2
    //   19: invokevirtual 149	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   22: invokespecial 152	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   25: astore_3
    //   26: aload_3
    //   27: invokevirtual 156	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   30: astore_0
    //   31: aload_0
    //   32: invokevirtual 162	java/nio/channels/FileChannel:size	()J
    //   35: lstore 4
    //   37: aload_0
    //   38: getstatic 168	java/nio/channels/FileChannel$MapMode:READ_ONLY	Ljava/nio/channels/FileChannel$MapMode;
    //   41: lconst_0
    //   42: lload 4
    //   44: invokevirtual 172	java/nio/channels/FileChannel:map	(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
    //   47: astore_0
    //   48: aload_3
    //   49: ifnull +11 -> 60
    //   52: iconst_0
    //   53: ifeq +51 -> 104
    //   56: aload_3
    //   57: invokevirtual 173	java/io/FileInputStream:close	()V
    //   60: aload_2
    //   61: ifnull +11 -> 72
    //   64: iconst_0
    //   65: ifeq +61 -> 126
    //   68: aload_2
    //   69: invokevirtual 174	android/os/ParcelFileDescriptor:close	()V
    //   72: aload_0
    //   73: areturn
    //   74: astore_0
    //   75: new 176	java/lang/NullPointerException
    //   78: dup
    //   79: invokespecial 177	java/lang/NullPointerException:<init>	()V
    //   82: athrow
    //   83: astore_1
    //   84: aload_1
    //   85: athrow
    //   86: astore_0
    //   87: aload_2
    //   88: ifnull +11 -> 99
    //   91: aload_1
    //   92: ifnull +83 -> 175
    //   95: aload_2
    //   96: invokevirtual 174	android/os/ParcelFileDescriptor:close	()V
    //   99: aload_0
    //   100: athrow
    //   101: astore_0
    //   102: aconst_null
    //   103: areturn
    //   104: aload_3
    //   105: invokevirtual 173	java/io/FileInputStream:close	()V
    //   108: goto -48 -> 60
    //   111: astore_0
    //   112: aconst_null
    //   113: astore_1
    //   114: goto -27 -> 87
    //   117: astore_0
    //   118: new 176	java/lang/NullPointerException
    //   121: dup
    //   122: invokespecial 177	java/lang/NullPointerException:<init>	()V
    //   125: athrow
    //   126: aload_2
    //   127: invokevirtual 174	android/os/ParcelFileDescriptor:close	()V
    //   130: aload_0
    //   131: areturn
    //   132: astore_1
    //   133: aload_1
    //   134: athrow
    //   135: astore_0
    //   136: aload_3
    //   137: ifnull +11 -> 148
    //   140: aload_1
    //   141: ifnull +18 -> 159
    //   144: aload_3
    //   145: invokevirtual 173	java/io/FileInputStream:close	()V
    //   148: aload_0
    //   149: athrow
    //   150: astore_3
    //   151: aload_1
    //   152: aload_3
    //   153: invokevirtual 181	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   156: goto -8 -> 148
    //   159: aload_3
    //   160: invokevirtual 173	java/io/FileInputStream:close	()V
    //   163: goto -15 -> 148
    //   166: astore_2
    //   167: aload_1
    //   168: aload_2
    //   169: invokevirtual 181	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   172: goto -73 -> 99
    //   175: aload_2
    //   176: invokevirtual 174	android/os/ParcelFileDescriptor:close	()V
    //   179: goto -80 -> 99
    //   182: astore_0
    //   183: aconst_null
    //   184: astore_1
    //   185: goto -49 -> 136
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	188	0	paramContext	Context
    //   0	188	1	paramCancellationSignal	android.os.CancellationSignal
    //   0	188	2	paramUri	android.net.Uri
    //   25	120	3	localFileInputStream	java.io.FileInputStream
    //   150	10	3	localThrowable	java.lang.Throwable
    //   35	8	4	l	long
    // Exception table:
    //   from	to	target	type
    //   56	60	74	java/lang/Throwable
    //   14	26	83	java/lang/Throwable
    //   75	83	83	java/lang/Throwable
    //   104	108	83	java/lang/Throwable
    //   148	150	83	java/lang/Throwable
    //   151	156	83	java/lang/Throwable
    //   159	163	83	java/lang/Throwable
    //   84	86	86	finally
    //   5	14	101	java/io/IOException
    //   68	72	101	java/io/IOException
    //   95	99	101	java/io/IOException
    //   99	101	101	java/io/IOException
    //   118	126	101	java/io/IOException
    //   126	130	101	java/io/IOException
    //   167	172	101	java/io/IOException
    //   175	179	101	java/io/IOException
    //   14	26	111	finally
    //   56	60	111	finally
    //   75	83	111	finally
    //   104	108	111	finally
    //   144	148	111	finally
    //   148	150	111	finally
    //   151	156	111	finally
    //   159	163	111	finally
    //   68	72	117	java/lang/Throwable
    //   26	48	132	java/lang/Throwable
    //   133	135	135	finally
    //   144	148	150	java/lang/Throwable
    //   95	99	166	java/lang/Throwable
    //   26	48	182	finally
  }
  
  /* Error */
  @RequiresApi(19)
  private static ByteBuffer mmap(File paramFile)
  {
    // Byte code:
    //   0: new 143	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 184	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   8: astore_2
    //   9: aload_2
    //   10: invokevirtual 156	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   13: astore_0
    //   14: aload_0
    //   15: invokevirtual 162	java/nio/channels/FileChannel:size	()J
    //   18: lstore_3
    //   19: aload_0
    //   20: getstatic 168	java/nio/channels/FileChannel$MapMode:READ_ONLY	Ljava/nio/channels/FileChannel$MapMode;
    //   23: lconst_0
    //   24: lload_3
    //   25: invokevirtual 172	java/nio/channels/FileChannel:map	(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
    //   28: astore_0
    //   29: aload_2
    //   30: ifnull +11 -> 41
    //   33: iconst_0
    //   34: ifeq +18 -> 52
    //   37: aload_2
    //   38: invokevirtual 173	java/io/FileInputStream:close	()V
    //   41: aload_0
    //   42: areturn
    //   43: astore_0
    //   44: new 176	java/lang/NullPointerException
    //   47: dup
    //   48: invokespecial 177	java/lang/NullPointerException:<init>	()V
    //   51: athrow
    //   52: aload_2
    //   53: invokevirtual 173	java/io/FileInputStream:close	()V
    //   56: aload_0
    //   57: areturn
    //   58: astore_1
    //   59: aload_1
    //   60: athrow
    //   61: astore_0
    //   62: aload_2
    //   63: ifnull +11 -> 74
    //   66: aload_1
    //   67: ifnull +18 -> 85
    //   70: aload_2
    //   71: invokevirtual 173	java/io/FileInputStream:close	()V
    //   74: aload_0
    //   75: athrow
    //   76: astore_2
    //   77: aload_1
    //   78: aload_2
    //   79: invokevirtual 181	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   82: goto -8 -> 74
    //   85: aload_2
    //   86: invokevirtual 173	java/io/FileInputStream:close	()V
    //   89: goto -15 -> 74
    //   92: astore_0
    //   93: aconst_null
    //   94: astore_1
    //   95: goto -33 -> 62
    //   98: astore_0
    //   99: aconst_null
    //   100: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	paramFile	File
    //   58	20	1	localThrowable1	java.lang.Throwable
    //   94	1	1	localObject	Object
    //   8	63	2	localFileInputStream	java.io.FileInputStream
    //   76	10	2	localThrowable2	java.lang.Throwable
    //   18	7	3	l	long
    // Exception table:
    //   from	to	target	type
    //   37	41	43	java/lang/Throwable
    //   9	29	58	java/lang/Throwable
    //   59	61	61	finally
    //   70	74	76	java/lang/Throwable
    //   9	29	92	finally
    //   0	9	98	java/io/IOException
    //   37	41	98	java/io/IOException
    //   44	52	98	java/io/IOException
    //   52	56	98	java/io/IOException
    //   70	74	98	java/io/IOException
    //   74	76	98	java/io/IOException
    //   77	82	98	java/io/IOException
    //   85	89	98	java/io/IOException
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.graphics.TypefaceCompatUtil
 * JD-Core Version:    0.7.0.1
 */