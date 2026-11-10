package edu.hitsz.observer;

import java.util.ArrayList;
import java.util.List;


public abstract class BombSubject {
    protected List<BombObserver> observers = new ArrayList<>();

    public void addObserver(BombObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BombObserver observer) {
        observers.remove(observer);
    }

    // 通知所有观察者炸弹爆炸
    protected abstract void notifyBombExplosion();

    // 触发炸弹
    public abstract void trigger();
}