package com.cacf.corporate.mobileappdownloader.utils;

/**
 * Created by jug on 10/11/2014.
 */
public class Pair<F, S> {

    protected F first;

    protected S second;

    public Pair(F firstElement, S secondElement){
        this.first = firstElement;
        this.second = secondElement;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
