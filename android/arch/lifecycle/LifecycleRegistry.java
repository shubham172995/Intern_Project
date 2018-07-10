package android.arch.lifecycle;

import android.arch.core.internal.FastSafeIterableMap;
import android.arch.core.internal.SafeIterableMap.IteratorWithAdditions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class LifecycleRegistry
  extends Lifecycle
{
  private int mAddingObserverCounter = 0;
  private boolean mHandlingEvent = false;
  private final LifecycleOwner mLifecycleOwner;
  private boolean mNewEventOccurred = false;
  private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap();
  private ArrayList<Lifecycle.State> mParentStates = new ArrayList();
  private Lifecycle.State mState;
  
  public LifecycleRegistry(@NonNull LifecycleOwner paramLifecycleOwner)
  {
    this.mLifecycleOwner = paramLifecycleOwner;
    this.mState = Lifecycle.State.INITIALIZED;
  }
  
  private void backwardPass()
  {
    Iterator localIterator = this.mObserverMap.descendingIterator();
    while ((localIterator.hasNext()) && (!this.mNewEventOccurred))
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      ObserverWithState localObserverWithState = (ObserverWithState)localEntry.getValue();
      while ((localObserverWithState.mState.compareTo(this.mState) > 0) && (!this.mNewEventOccurred) && (this.mObserverMap.contains(localEntry.getKey())))
      {
        Lifecycle.Event localEvent = downEvent(localObserverWithState.mState);
        pushParentState(getStateAfter(localEvent));
        localObserverWithState.dispatchEvent(this.mLifecycleOwner, localEvent);
        popParentState();
      }
    }
  }
  
  private Lifecycle.State calculateTargetState(LifecycleObserver paramLifecycleObserver)
  {
    paramLifecycleObserver = this.mObserverMap.ceil(paramLifecycleObserver);
    if (paramLifecycleObserver != null)
    {
      paramLifecycleObserver = ((ObserverWithState)paramLifecycleObserver.getValue()).mState;
      if (this.mParentStates.isEmpty()) {
        break label74;
      }
    }
    label74:
    for (Lifecycle.State localState = (Lifecycle.State)this.mParentStates.get(this.mParentStates.size() - 1);; localState = null)
    {
      return min(min(this.mState, paramLifecycleObserver), localState);
      paramLifecycleObserver = null;
      break;
    }
  }
  
  private static Lifecycle.Event downEvent(Lifecycle.State paramState)
  {
    switch (1.$SwitchMap$android$arch$lifecycle$Lifecycle$State[paramState.ordinal()])
    {
    default: 
      throw new IllegalArgumentException("Unexpected state value " + paramState);
    case 1: 
      throw new IllegalArgumentException();
    case 2: 
      return Lifecycle.Event.ON_DESTROY;
    case 3: 
      return Lifecycle.Event.ON_STOP;
    case 4: 
      return Lifecycle.Event.ON_PAUSE;
    }
    throw new IllegalArgumentException();
  }
  
  private void forwardPass()
  {
    SafeIterableMap.IteratorWithAdditions localIteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
    while ((localIteratorWithAdditions.hasNext()) && (!this.mNewEventOccurred))
    {
      Map.Entry localEntry = (Map.Entry)localIteratorWithAdditions.next();
      ObserverWithState localObserverWithState = (ObserverWithState)localEntry.getValue();
      while ((localObserverWithState.mState.compareTo(this.mState) < 0) && (!this.mNewEventOccurred) && (this.mObserverMap.contains(localEntry.getKey())))
      {
        pushParentState(localObserverWithState.mState);
        localObserverWithState.dispatchEvent(this.mLifecycleOwner, upEvent(localObserverWithState.mState));
        popParentState();
      }
    }
  }
  
  static Lifecycle.State getStateAfter(Lifecycle.Event paramEvent)
  {
    switch (1.$SwitchMap$android$arch$lifecycle$Lifecycle$Event[paramEvent.ordinal()])
    {
    default: 
      throw new IllegalArgumentException("Unexpected event value " + paramEvent);
    case 1: 
    case 2: 
      return Lifecycle.State.CREATED;
    case 3: 
    case 4: 
      return Lifecycle.State.STARTED;
    case 5: 
      return Lifecycle.State.RESUMED;
    }
    return Lifecycle.State.DESTROYED;
  }
  
  private boolean isSynced()
  {
    if (this.mObserverMap.size() == 0) {
      return true;
    }
    Lifecycle.State localState1 = ((ObserverWithState)this.mObserverMap.eldest().getValue()).mState;
    Lifecycle.State localState2 = ((ObserverWithState)this.mObserverMap.newest().getValue()).mState;
    if ((localState1 == localState2) && (this.mState == localState2)) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  static Lifecycle.State min(@NonNull Lifecycle.State paramState1, @Nullable Lifecycle.State paramState2)
  {
    if ((paramState2 != null) && (paramState2.compareTo(paramState1) < 0)) {
      return paramState2;
    }
    return paramState1;
  }
  
  private void popParentState()
  {
    this.mParentStates.remove(this.mParentStates.size() - 1);
  }
  
  private void pushParentState(Lifecycle.State paramState)
  {
    this.mParentStates.add(paramState);
  }
  
  private void sync()
  {
    while (!isSynced())
    {
      this.mNewEventOccurred = false;
      if (this.mState.compareTo(((ObserverWithState)this.mObserverMap.eldest().getValue()).mState) < 0) {
        backwardPass();
      }
      Map.Entry localEntry = this.mObserverMap.newest();
      if ((!this.mNewEventOccurred) && (localEntry != null) && (this.mState.compareTo(((ObserverWithState)localEntry.getValue()).mState) > 0)) {
        forwardPass();
      }
    }
    this.mNewEventOccurred = false;
  }
  
  private static Lifecycle.Event upEvent(Lifecycle.State paramState)
  {
    switch (1.$SwitchMap$android$arch$lifecycle$Lifecycle$State[paramState.ordinal()])
    {
    default: 
      throw new IllegalArgumentException("Unexpected state value " + paramState);
    case 1: 
    case 5: 
      return Lifecycle.Event.ON_CREATE;
    case 2: 
      return Lifecycle.Event.ON_START;
    case 3: 
      return Lifecycle.Event.ON_RESUME;
    }
    throw new IllegalArgumentException();
  }
  
  public void addObserver(LifecycleObserver paramLifecycleObserver)
  {
    if (this.mState == Lifecycle.State.DESTROYED) {}
    ObserverWithState localObserverWithState;
    for (Lifecycle.State localState = Lifecycle.State.DESTROYED;; localState = Lifecycle.State.INITIALIZED)
    {
      localObserverWithState = new ObserverWithState(paramLifecycleObserver, localState);
      if ((ObserverWithState)this.mObserverMap.putIfAbsent(paramLifecycleObserver, localObserverWithState) == null) {
        break;
      }
      return;
    }
    if ((this.mAddingObserverCounter != 0) || (this.mHandlingEvent)) {}
    for (int i = 1;; i = 0)
    {
      localState = calculateTargetState(paramLifecycleObserver);
      this.mAddingObserverCounter += 1;
      while ((localObserverWithState.mState.compareTo(localState) < 0) && (this.mObserverMap.contains(paramLifecycleObserver)))
      {
        pushParentState(localObserverWithState.mState);
        localObserverWithState.dispatchEvent(this.mLifecycleOwner, upEvent(localObserverWithState.mState));
        popParentState();
        localState = calculateTargetState(paramLifecycleObserver);
      }
    }
    if (i == 0) {
      sync();
    }
    this.mAddingObserverCounter -= 1;
  }
  
  public Lifecycle.State getCurrentState()
  {
    return this.mState;
  }
  
  public int getObserverCount()
  {
    return this.mObserverMap.size();
  }
  
  public void handleLifecycleEvent(Lifecycle.Event paramEvent)
  {
    this.mState = getStateAfter(paramEvent);
    if ((this.mHandlingEvent) || (this.mAddingObserverCounter != 0))
    {
      this.mNewEventOccurred = true;
      return;
    }
    this.mHandlingEvent = true;
    sync();
    this.mHandlingEvent = false;
  }
  
  public void markState(Lifecycle.State paramState)
  {
    this.mState = paramState;
  }
  
  public void removeObserver(LifecycleObserver paramLifecycleObserver)
  {
    this.mObserverMap.remove(paramLifecycleObserver);
  }
  
  static class ObserverWithState
  {
    GenericLifecycleObserver mLifecycleObserver;
    Lifecycle.State mState;
    
    ObserverWithState(LifecycleObserver paramLifecycleObserver, Lifecycle.State paramState)
    {
      this.mLifecycleObserver = Lifecycling.getCallback(paramLifecycleObserver);
      this.mState = paramState;
    }
    
    void dispatchEvent(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent)
    {
      Lifecycle.State localState = LifecycleRegistry.getStateAfter(paramEvent);
      this.mState = LifecycleRegistry.min(this.mState, localState);
      this.mLifecycleObserver.onStateChanged(paramLifecycleOwner, paramEvent);
      this.mState = localState;
    }
  }
}


/* Location:           C:\Users\lousy\Desktop\New folder\dex2jar\classes-dex2jar.jar
 * Qualified Name:     android.arch.lifecycle.LifecycleRegistry
 * JD-Core Version:    0.7.0.1
 */