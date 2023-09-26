package com.msw.aldkli.meta;

public final class ApiParamMetaData {

    private String name;
    private boolean required;
    private String description;
    private String type;
    private String dataType;
    private String example;

    public ApiParamMetaData(String name, boolean required, String description, String type, String dataType, String example) {
        this.name = name;
        this.required = required;
        this.description = description;
        this.type = type;
        this.dataType = dataType;
        this.example = example;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    public String getExample() {
        return example;
    }

}
