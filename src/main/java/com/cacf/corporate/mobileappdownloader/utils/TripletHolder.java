package com.cacf.corporate.mobileappdownloader.utils;

/**
 * Created by jug on 23/10/2014.
 */
public interface TripletHolder<F,S,T> {

    /**
     * Return first element of the triplet.
     * @return F declared type
     */
    F getFirst();

    /**
     * Return second element of the triplet.
     * @return S declared type
     */
    S getSecond();

    /**
     * Return third element of the triplet.
     * @return T declared type
     */
    T getThird();

}
