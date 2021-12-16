package com.amazon.ata.deliveringonourpromise.comparators;

import com.amazon.ata.deliveringonourpromise.types.Promise;

import java.util.Comparator;

public class PromiseAsinComparator implements Comparator<Promise> {

    public int compare(Promise firstPromise, Promise secondPromise) {
        return firstPromise.getAsin().compareTo(secondPromise.getAsin());
    }
}
