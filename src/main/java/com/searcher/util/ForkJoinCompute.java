package com.searcher.util;

import java.util.List;

@FunctionalInterface
public interface ForkJoinCompute<T> {
    List<T> compute();
}
