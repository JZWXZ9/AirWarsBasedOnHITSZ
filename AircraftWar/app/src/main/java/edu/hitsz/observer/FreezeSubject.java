package edu.hitsz.observer;

import edu.hitsz.aircraft.*;

import java.util.ArrayList;
import java.util.List;

public abstract class FreezeSubject {
    protected List<FreezeObserver> observers = new ArrayList<>();

    public void addObserver(FreezeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(FreezeObserver observer) {
        observers.remove(observer);
    }

    // 通知所有观察者冰冻开始
    protected abstract void notifyFreezeStart();

    public abstract void trigger();
}