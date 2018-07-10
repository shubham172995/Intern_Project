package android.support.v4.provider;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.GuardedBy;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
public class SelfDestructiveThread
{
  private static final int MSG_DESTRUCTION = 0;
  private static final int MSG_INVOKE_RUNNABLE = 1;
  private Handler.Callback mCallback = new Handler.Callback()
  {
    public boolean handleMessage(Message paramAnonymousMessage)
    {
      switch (paramAnonymousMessage.what)
      {
      default: 
        return true;
      case 1: 
        SelfDestructiveThread.this.onInvokeRunnable((Runnable)paramAnonymousMessage.obj);
        return true;
      }
      SelfDestructiveThread.this.onDestruction();
      return true;
    }
  };
  private final int mDestructAfterMillisec;
  @GuardedBy("mLock")
  private int mGeneration;
  @GuardedBy("mLock")
  private Handler mHandler;
  private final Object mLock = new Object();
  private final int mPriority;
  @GuardedBy("mLock")
  private HandlerThread mThread;
  private final String mThreadName;
  
  public SelfDestructiveThread(String paramString, int paramInt1, int paramInt2)
  {
    this.mThreadName = paramString;
    this.mPriority = paramInt1;
    this.mDestructAfterMillisec = paramInt2;
    this.mGeneration = 0;
  }
  
  private void onDestruction()
  {
    synchronized (this.mLock)
    {
      if (this.mHandler.hasMessages(1)) {
        return;
      }
      this.mThread.quit();
      this.mThread = null;
      this.mHandler = null;
      return;
    }
  }
  
  private void onInvokeRunnable(Runnable arg1)
  {
    ???.run();
    synchronized (this.mLock)
    {
      this.mHandler.removeMessages(0);
      this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), this.mDestructAfterMillisec);
      return;
    }
  }
  
  private void post(Runnable paramRunnable)
  {
    synchronized (this.mLock)
    {
      if (this.mThread == null)
      {
        this.mThread = new HandlerThread(this.mThreadName, this.mPriority);
        this.mThread.start();
        this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
        this.mGeneration += 1;
      }
      this.mHandler.removeMessages(0);
      this.mHandler.sendMessage(this.mHandler.obtainMessage(1, paramRunnable));
      return;
    }
  }
  
  @VisibleForTesting
  public int getGeneration()
  {
    synchronized (this.mLock)
    {
      int i = this.mGeneration;
      return i;
    }
  }
  
  @VisibleForTesting
  public boolean isRunning()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mThread != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public <T> void postAndReply(final Callable<T> paramCallable, final ReplyCallback<T> paramReplyCallback)
  {
    post(new Runnable()
    {
      public void run()
      {
        try
        {
          final Object localObject1 = paramCallable.call();
          this.val$callingHandler.post(new Runnable()
          {
            public void run()
            {
              SelfDestructiveThread.2.this.val$reply.onReply(localObject1);
            }
          });
          return;
        }
        catch (Exception localException)
        {
          for (;;)
          {
            Object localObject2 = null;
          }
        }
      }
    });
  }
  
  /* Error */
  public <T> T postAndWait(final Callable<T> paramCallable, int paramInt)
    throws java.lang.InterruptedException
  {
    // Byte code:
    //   0: new 144	java/util/concurrent/locks/ReentrantLock
    //   3: dup
    //   4: invokespecial 145	java/util/concurrent/locks/ReentrantLock:<init>	()V
    //   7: astore_3
    //   8: aload_3
    //   9: invokevirtual 149	java/util/concurrent/locks/ReentrantLock:newCondition	()Ljava/util/concurrent/locks/Condition;
    //   12: astore 4
    //   14: new 151	java/util/concurrent/atomic/AtomicReference
    //   17: dup
    //   18: invokespecial 152	java/util/concurrent/atomic/AtomicReference:<init>	()V
    //   21: astore 5
    //   23: new 154	java/util/concurrent/atomic/AtomicBoolean
    //   26: dup
    //   27: iconst_1
    //   28: invokespecial 157	java/util/concurrent/atomic/AtomicBoolean:<init>	(Z)V
    //   31: astore 6
    //   33: aload_0
    //   34: new 12	android/support/v4/provider/SelfDestructiveThread$3
    //   37: dup
    //   38: aload_0
    //   39: aload 5
    //   41: aload_1
    //   42: aload_3
    //   43: aload 6
    //   45: aload 4
    //   47: invokespecial 160	android/support/v4/provider/SelfDestructiveThread$3:<init>	(Landroid/support/v4/provider/SelfDestructiveThread;Ljava/util/concurrent/atomic/AtomicReference;Ljava/util/concurrent/Callable;Ljava/util/concurrent/locks/ReentrantLock;Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/locks/Condition;)V
    //   50: invokespecial 136	android/support/v4/provider/SelfDestructiveThread:post	(Ljava/lang/Runnable;)V
    //   53: aload_3
    //   54: invokevirtual 163	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   57: aload 6
    //   59: invokevirtual 166	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   62: ifne +15 -> 77
    //   65: aload 5
    //   67: invokevirtual 169	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   70: astore_1
    //   71: aload_3
    //   72: invokevirtual 172	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   75: aload_1
    //   76: areturn
    //   77: getstatic 178	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   80: iload_2
    //   81: i2l
    //   82: invokevirtual 182	java/util/concurrent/TimeUnit:toNanos	(J)J
    //   85: lstore 7
    //   87: aload 4
    //   89: lload 7
    //   91: invokeinterface 187 3 0
    //   96: lstore 9
    //   98: aload 6
    //   100: invokevirtual 166	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   103: ifne +15 -> 118
    //   106: aload 5
    //   108: invokevirtual 169	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
    //   111: astore_1
    //   112: aload_3
    //   113: invokevirtual 172	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   116: aload_1
    //   117: areturn
    //   118: lload 9
    //   120: lstore 7
    //   122: lload 9
    //   124: lconst_0
    //   125: lcmp
    //   126: ifgt -39 -> 87
    //   129: new 142	java/lang/InterruptedException
    //   132: dup
    //   133: ldc 189
    //   135: invokespecial 192	java/lang/InterruptedException:<init>	(Ljava/lang/String;)V
    //   138: athrow
    //   139: astore_1
    //   140: aload_3
    //   141: invokevirtual 172	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   144: aload_1
    //   145: athrow
    //   146: astore_1
    //   147: lload 7
    //   149: lstore 9
    //   151: goto -53 -> 98
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	154	0	this	SelfDestructiveThread
    //   0	154	1	paramCallable	Callable<T>
    //   0	154	2	paramInt	int
    //   7	134	3	localReentrantLock	ReentrantLock
    //   12	76	4	localCondition	Condition
    //   21	86	5	localAtomicReference	AtomicReference
    //   31	68	6	localAtomicBoolean	AtomicBoolean
    //   85	63	7	l1	long
    //   96	54	9	l2	long
    // Exception table:
    //   from	to	target	type
    //   57	71	139	finally
    //   77	87	139	finally
    //   87	98	139	finally
    //   98	112	139	finally
    //   129	139	139	finally
    //   87	98	146	java/lang/InterruptedException
  }
  
  public static abstract interface ReplyCallback<T>
  {
    public abstract void onReply(T paramT);
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.provider.SelfDestructiveThread
 * JD-Core Version:    0.7.0.1
 */