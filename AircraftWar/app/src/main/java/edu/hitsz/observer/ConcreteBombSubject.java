package edu.hitsz.observer;

public class ConcreteBombSubject extends BombSubject{
    @Override
    public void notifyBombExplosion(){
        for(BombObserver observer : observers){
            observer.onBombExplosion();
        }
    }

    @Override
    public void trigger() {
        notifyBombExplosion();
    }
}
