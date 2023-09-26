package com.msw.aldkli.util;

@FunctionalInterface
public interface MultipleReturnValue {

   <T> T get(int index);

}
