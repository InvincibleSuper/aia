package com.github.aia.core.api;

import java.io.Serializable;

/**
 * 执行定义，也就是注释
 */
public class InvokeDefinition implements Serializable{


    /**
     * 定义，也就是注释
     */
    private String definition;


    public String getDefinition() {
        return definition;
    }

    public InvokeDefinition setDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    public InvokeDefinition(String definition) {
        this.definition = definition;
    }

    public InvokeDefinition() {
    }
}
