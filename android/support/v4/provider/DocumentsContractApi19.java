package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

@RequiresApi(19)
class DocumentsContractApi19
{
  private static final int FLAG_VIRTUAL_DOCUMENT = 512;
  private static final String TAG = "DocumentFile";
  
  public static boolean canRead(Context paramContext, Uri paramUri)
  {
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 1) != 0) {}
    while (TextUtils.isEmpty(getRawType(paramContext, paramUri))) {
      return false;
    }
    return true;
  }
  
  public static boolean canWrite(Context paramContext, Uri paramUri)
  {
    if (paramContext.checkCallingOrSelfUriPermission(paramUri, 2) != 0) {}
    String str;
    int i;
    do
    {
      do
      {
        return false;
        str = getRawType(paramContext, paramUri);
        i = queryForInt(paramContext, paramUri, "flags", 0);
      } while (TextUtils.isEmpty(str));
      if ((i & 0x4) != 0) {
        return true;
      }
      if (("vnd.android.document/directory".equals(str)) && ((i & 0x8) != 0)) {
        return true;
      }
    } while ((TextUtils.isEmpty(str)) || ((i & 0x2) == 0));
    return true;
  }
  
  private static void closeQuietly(AutoCloseable paramAutoCloseable)
  {
    if (paramAutoCloseable != null) {}
    try
    {
      paramAutoCloseable.close();
      return;
    }
    catch (RuntimeException paramAutoCloseable)
    {
      throw paramAutoCloseable;
    }
    catch (Exception paramAutoCloseable) {}
  }
  
  public static boolean delete(Context paramContext, Uri paramUri)
  {
    try
    {
      boolean bool = DocumentsContract.deleteDocument(paramContext.getContentResolver(), paramUri);
      return bool;
    }
    catch (Exception paramContext) {}
    return false;
  }
  
  /* Error */
  public static boolean exists(Context paramContext, Uri paramUri)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 68	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: astore_3
    //   5: aconst_null
    //   6: astore_2
    //   7: aconst_null
    //   8: astore_0
    //   9: aload_3
    //   10: aload_1
    //   11: iconst_1
    //   12: anewarray 48	java/lang/String
    //   15: dup
    //   16: iconst_0
    //   17: ldc 77
    //   19: aastore
    //   20: aconst_null
    //   21: aconst_null
    //   22: aconst_null
    //   23: invokevirtual 83	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   26: astore_1
    //   27: aload_1
    //   28: astore_0
    //   29: aload_1
    //   30: astore_2
    //   31: aload_1
    //   32: invokeinterface 89 1 0
    //   37: istore 4
    //   39: iload 4
    //   41: ifle +13 -> 54
    //   44: iconst_1
    //   45: istore 5
    //   47: aload_1
    //   48: invokestatic 91	android/support/v4/provider/DocumentsContractApi19:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   51: iload 5
    //   53: ireturn
    //   54: iconst_0
    //   55: istore 5
    //   57: goto -10 -> 47
    //   60: astore_1
    //   61: aload_0
    //   62: astore_2
    //   63: ldc 14
    //   65: new 93	java/lang/StringBuilder
    //   68: dup
    //   69: invokespecial 94	java/lang/StringBuilder:<init>	()V
    //   72: ldc 96
    //   74: invokevirtual 100	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: aload_1
    //   78: invokevirtual 103	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   81: invokevirtual 107	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   84: invokestatic 113	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   87: pop
    //   88: aload_0
    //   89: invokestatic 91	android/support/v4/provider/DocumentsContractApi19:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   92: iconst_0
    //   93: ireturn
    //   94: astore_0
    //   95: aload_2
    //   96: invokestatic 91	android/support/v4/provider/DocumentsContractApi19:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   99: aload_0
    //   100: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	101	0	paramContext	Context
    //   0	101	1	paramUri	Uri
    //   6	90	2	localObject	Object
    //   4	6	3	localContentResolver	ContentResolver
    //   37	3	4	i	int
    //   45	11	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   9	27	60	java/lang/Exception
    //   31	39	60	java/lang/Exception
    //   9	27	94	finally
    //   31	39	94	finally
    //   63	88	94	finally
  }
  
  public static long getFlags(Context paramContext, Uri paramUri)
  {
    return queryForLong(paramContext, paramUri, "flags", 0L);
  }
  
  public static String getName(Context paramContext, Uri paramUri)
  {
    return queryForString(paramContext, paramUri, "_display_name", null);
  }
  
  private static String getRawType(Context paramContext, Uri paramUri)
  {
    return queryForString(paramContext, paramUri, "mime_type", null);
  }
  
  public static String getType(Context paramContext, Uri paramUri)
  {
    paramUri = getRawType(paramContext, paramUri);
    paramContext = paramUri;
    if ("vnd.android.document/directory".equals(paramUri)) {
      paramContext = null;
    }
    return paramContext;
  }
  
  public static boolean isDirectory(Context paramContext, Uri paramUri)
  {
    return "vnd.android.document/directory".equals(getRawType(paramContext, paramUri));
  }
  
  public static boolean isDocumentUri(Context paramContext, Uri paramUri)
  {
    return DocumentsContract.isDocumentUri(paramContext, paramUri);
  }
  
  public static boolean isFile(Context paramContext, Uri paramUri)
  {
    paramContext = getRawType(paramContext, paramUri);
    return (!"vnd.android.document/directory".equals(paramContext)) && (!TextUtils.isEmpty(paramContext));
  }
  
  public static boolean isVirtual(Context paramContext, Uri paramUri)
  {
    if (!isDocumentUri(paramContext, paramUri)) {}
    while ((getFlags(paramContext, paramUri) & 0x200) == 0L) {
      return false;
    }
    return true;
  }
  
  public static long lastModified(Context paramContext, Uri paramUri)
  {
    return queryForLong(paramContext, paramUri, "last_modified", 0L);
  }
  
  public static long length(Context paramContext, Uri paramUri)
  {
    return queryForLong(paramContext, paramUri, "_size", 0L);
  }
  
  private static int queryForInt(Context paramContext, Uri paramUri, String paramString, int paramInt)
  {
    return (int)queryForLong(paramContext, paramUri, paramString, paramInt);
  }
  
  private static long queryForLong(Context paramContext, Uri paramUri, String paramString, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Object localObject = null;
    paramContext = null;
    try
    {
      paramUri = localContentResolver.query(paramUri, new String[] { paramString }, null, null, null);
      paramContext = paramUri;
      localObject = paramUri;
      if (paramUri.moveToFirst())
      {
        paramContext = paramUri;
        localObject = paramUri;
        if (!paramUri.isNull(0))
        {
          paramContext = paramUri;
          localObject = paramUri;
          long l = paramUri.getLong(0);
          closeQuietly(paramUri);
          return l;
        }
      }
      closeQuietly(paramUri);
      return paramLong;
    }
    catch (Exception paramUri)
    {
      localObject = paramContext;
      Log.w("DocumentFile", "Failed query: " + paramUri);
      closeQuietly(paramContext);
      return paramLong;
    }
    finally
    {
      closeQuietly((AutoCloseable)localObject);
    }
  }
  
  private static String queryForString(Context paramContext, Uri paramUri, String paramString1, String paramString2)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Object localObject = null;
    paramContext = null;
    try
    {
      paramUri = localContentResolver.query(paramUri, new String[] { paramString1 }, null, null, null);
      paramContext = paramUri;
      localObject = paramUri;
      if (paramUri.moveToFirst())
      {
        paramContext = paramUri;
        localObject = paramUri;
        if (!paramUri.isNull(0))
        {
          paramContext = paramUri;
          localObject = paramUri;
          paramString1 = paramUri.getString(0);
          closeQuietly(paramUri);
          return paramString1;
        }
      }
      closeQuietly(paramUri);
      return paramString2;
    }
    catch (Exception paramUri)
    {
      localObject = paramContext;
      Log.w("DocumentFile", "Failed query: " + paramUri);
      closeQuietly(paramContext);
      return paramString2;
    }
    finally
    {
      closeQuietly((AutoCloseable)localObject);
    }
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.provider.DocumentsContractApi19
 * JD-Core Version:    0.7.0.1
 */