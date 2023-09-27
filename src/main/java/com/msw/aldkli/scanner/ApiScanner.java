package com.msw.aldkli.scanner;

import com.msw.aldkli.annotation.Api;
import com.msw.aldkli.annotation.ApiEntity;
import com.msw.aldkli.annotation.ApiGroup;
import com.msw.aldkli.annotation.ApiParam;
import com.msw.aldkli.annotation.ApiParams;
import com.msw.aldkli.annotation.ApiProperty;
import com.msw.aldkli.annotation.ApiReturnType;
import com.msw.aldkli.meta.ApiGroupMetaData;
import com.msw.aldkli.meta.ApiMetaData;
import com.msw.aldkli.meta.ApiParamMetaData;
import com.msw.aldkli.meta.ApiReturnTypeMetaData;
import com.msw.aldkli.meta.MethodType;
import com.msw.aldkli.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.msw.aldkli.util.MultipleReturnValue;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public final class ApiScanner {

     public List<ApiGroupMetaData> scan(String scanPackage)   {
        List<Class> apiGroupClassList = ClassUtil.scanClass(scanPackage,c ->
           c.getAnnotation(ApiGroup.class) != null
        );
        List<ApiGroupMetaData> apiGroupMetaDataList = new ArrayList<>(apiGroupClassList.size());
        for (Class apiGroupClass : apiGroupClassList) {
            ApiGroup apiGroup = (ApiGroup) apiGroupClass.getDeclaredAnnotation(ApiGroup.class);
            RequestMapping requestMapping = (RequestMapping) apiGroupClass.getDeclaredAnnotation(RequestMapping.class);
            Method[] declaredMethods = apiGroupClass.getDeclaredMethods();
            List<ApiMetaData> apiMetaDataList = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                Api api = declaredMethod.getDeclaredAnnotation(Api.class);
                MultipleReturnValue values  = getMethodAndPathList(declaredMethod);
                List<ApiParamMetaData> apiParamMetaDataList = getParameters(declaredMethod);
                ApiReturnTypeMetaData apiReturnTypeMetaData = getReturnType(declaredMethod);
                MethodType methodType = values.get(0);
                List<String> pathList = values.get(1);
                apiMetaDataList.add(new ApiMetaData(api.value(),methodType,pathList,apiParamMetaDataList,apiReturnTypeMetaData));
            }
            apiGroupMetaDataList.add(new ApiGroupMetaData(apiGroup.value(),Arrays.asList(requestMapping.value()),apiMetaDataList));
        }
        return apiGroupMetaDataList;
    }

    private MultipleReturnValue getMethodAndPathList(Method declaredMethod) {
        MethodType methodType = null;
        String[] pathList = new String[0];
        for (Annotation annotation : declaredMethod.getDeclaredAnnotations()) {
            if (annotation instanceof RequestMapping) {
                methodType = MethodType.ALL;
                pathList = ((RequestMapping) annotation).value();
            }
            if (annotation instanceof GetMapping) {
                methodType = MethodType.GET;
                pathList = ((GetMapping) annotation).value();
            }
            if (annotation instanceof PostMapping) {
                methodType = MethodType.POST;
                pathList = ((PostMapping) annotation).value();
            }
            if (annotation instanceof PutMapping) {
                methodType = MethodType.PUT;
                pathList = ((PutMapping) annotation).value();
            }
            if (annotation instanceof DeleteMapping) {
                methodType = MethodType.DELETE;
                pathList = ((DeleteMapping) annotation).value();
            }
            if (annotation instanceof PatchMapping) {
                methodType = MethodType.PATCH;
                pathList = ((PatchMapping) annotation).value();
            }
        }
        List<Object> values = Arrays.asList(methodType,Arrays.asList(pathList));
        return new MultipleReturnValue() {
            @Override
            public <T> T get(int index) {
                return (T) values.get(index);
            }
        };
    }

    private List<ApiParamMetaData> getParameters(Method declaredMethod) {
        List<ApiParamMetaData> apiParamMetaDataList = new ArrayList<>();
        ApiParams apiParams = declaredMethod.getDeclaredAnnotation(ApiParams.class);
        Parameter[] parameters = declaredMethod.getParameters();
        if (apiParams == null) {
            for (Parameter parameter : parameters) {
                apiParamMetaDataList.add(toApiParam(null,parameter));
            }
        } else {
            Map<String,Parameter> parameterMap = Arrays.stream(parameters).collect(Collectors.toMap(Parameter::getName, Function.identity()));
            for (ApiParam apiParam : apiParams.value()) {
                Parameter parameter = parameterMap.get(apiParam.param());
                apiParamMetaDataList.add(toApiParam(apiParam,parameter));
            }
        }
        return apiParamMetaDataList;
    }

    private ApiParamMetaData toApiParam(ApiParam apiParam,Parameter parameter) {
        String name = apiParam != null ? apiParam.param() : parameter.getName();
        MultipleReturnValue values = getRequiredAndType(parameter);
        String description = apiParam != null ? apiParam.description() : "";
        String dataType = parameter != null ? ClassUtil.getGenericTypeName(parameter.getParameterizedType()) : "";
        String example = apiParam != null ? apiParam.example() : "";
        boolean required = values.get(0);
        String type = values.get(1);
        return new ApiParamMetaData(name,required,description,type,dataType,example);
    }

    private MultipleReturnValue getRequiredAndType(Parameter parameter) {
        boolean required = false;
        String type = "";
        if (parameter != null) {
            RequestParam requestParam = parameter.getDeclaredAnnotation(RequestParam.class);
            if (requestParam != null) {
                required = requestParam.required();
                type = "search";
            }
            PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
            if (pathVariable != null) {
                required = pathVariable.required();
                type = "urlPath";
            }
        }
        List<Object> values = Arrays.asList(required, type);
        return new MultipleReturnValue() {
            @Override
            public <T> T get(int index) {
                return (T) values.get(index);
            }
        };
    }

    private ApiReturnTypeMetaData getReturnType(Method declaredMethod) {
        ApiReturnType apiReturnType = declaredMethod.getDeclaredAnnotation(ApiReturnType.class);
        if (apiReturnType != null) {
            return convertToApiReturnTypeMetaData(apiReturnType);
        } else {
            Type genericReturnType = declaredMethod.getGenericReturnType();
            String dataType = ClassUtil.getGenericTypeName(genericReturnType);
            MultipleReturnValue values = resolveApiEntity(genericReturnType);
            String description = values.get(0) != null ? ((ApiEntity)values.get(0)).value() : "";
            List<ApiReturnTypeMetaData> children = values.get(1);
            return new ApiReturnTypeMetaData("Result",description,dataType,children);
        }
    }

    private ApiReturnTypeMetaData convertToApiReturnTypeMetaData(ApiReturnType apiReturnType)  {
        return new ApiReturnTypeMetaData(apiReturnType.name(),apiReturnType.description(),apiReturnType.dataType(),new ArrayList<>(0));
    }

    private MultipleReturnValue resolveApiEntity(Type genericType) {
        Class apiEntityClass = getApiEntityClass(genericType);
        ApiEntity apiEntry = null;
        List<ApiReturnTypeMetaData> children = new ArrayList<>(0);
        if (apiEntityClass != null) {
            apiEntry = (ApiEntity) apiEntityClass.getDeclaredAnnotation(ApiEntity.class);
            if (apiEntry != null) {
                Field[] declaredFields = apiEntityClass.getDeclaredFields();
                children = new ArrayList<>(declaredFields.length);
                for (Field declaredField : declaredFields) {
                    ApiProperty apiProperty = declaredField.getDeclaredAnnotation(ApiProperty.class);
                    if (apiProperty == null) continue;
                    String dataType = ClassUtil.getGenericTypeName(declaredField.getGenericType());
                    children.add(new ApiReturnTypeMetaData(declaredField.getName(), apiProperty.value(), dataType,resolveApiEntity(declaredField.getGenericType()).get(1)));
                }
            }
        }
        List<Object> values = Arrays.asList(apiEntry, children);
        return new MultipleReturnValue() {
            @Override
            public <T> T get(int index) {
                return (T) values.get(index);
            }
        };
    }

    private Class getApiEntityClass(Type genericType) {
        Type apiEntryClass = genericType instanceof ParameterizedType ? ((ParameterizedType) genericType).getActualTypeArguments()[0] : genericType;
        if (!(apiEntryClass instanceof Class)) return null;
        return (Class) apiEntryClass;
    }
    
}
