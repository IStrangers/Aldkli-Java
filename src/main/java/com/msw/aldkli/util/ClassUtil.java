package com.msw.aldkli.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ClassUtil {

    public static List<Class> scanClass(String packagePath, Function<Class,Boolean> classFilter) {
        return findClassList(packagePath,classFilter);
    }

    public static List<Class> findClassList(String packagePath,Function<Class,Boolean> classFilter) {
        List<Class> documentClassSet = new ArrayList<>();
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
        ClassLoader loader = ClassUtil.class.getClassLoader();

        try {
            packagePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packagePath) + "/**/*.class";
            Resource[] resources = pathMatchingResourcePatternResolver.getResources(packagePath);
            for (Resource resource : resources) {
                MetadataReader reader = cachingMetadataReaderFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class c = loader.loadClass(className);
                if (classFilter.apply(c)) {
                    documentClassSet.add(c);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("[Elasticsearch] 加载 Document Class 失败",e);
        }
        return documentClassSet;
    }

    public static String getGenericTypeName(Type genericType) {
        StringBuilder typeName = new StringBuilder();
        if (genericType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) genericType).getRawType();
            typeName.append(rawType instanceof Class ? ((Class<?>) rawType).getSimpleName() : rawType.getTypeName());
            Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (actualTypeArguments.length != 0) {
                typeName.append("<");
                typeName.append(Arrays.stream(actualTypeArguments).map(actualTypeArgument -> getGenericTypeName(actualTypeArgument)).collect(Collectors.joining(",")));
                typeName.append(">");
            }
        } else {
            typeName.append(genericType instanceof Class ? ((Class<?>) genericType).getSimpleName() : genericType.getTypeName());
        }
        return typeName.toString();
    }

}
