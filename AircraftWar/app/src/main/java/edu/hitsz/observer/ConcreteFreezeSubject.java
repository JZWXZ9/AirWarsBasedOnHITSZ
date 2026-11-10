package edu.hitsz.observer;

public class ConcreteFreezeSubject extends FreezeSubject{
    @Override
    protected void notifyFreezeStart() {
        for (FreezeObserver observer : observers) {
            observer.onFreezeStart();
        }
    }

    @Override
    public void trigger() {
        notifyFreezeStart();
    }
}
