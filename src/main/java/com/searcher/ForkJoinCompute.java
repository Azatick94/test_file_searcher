package com.searcher;

import java.util.List;

@FunctionalInterface
public interface ForkJoinCompute<T> {
    List<T> compute();
}
