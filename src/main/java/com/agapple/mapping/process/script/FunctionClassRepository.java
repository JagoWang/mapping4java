package com.agapple.mapping.process.script;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义script的function仓库
 * 
 * @author jianghang 2011-6-27 下午07:26:32
 */
public class FunctionClassRepository {

    private Map<String, Class> functions = new ConcurrentHashMap<String, Class>(10);

    /**
     * 注册对应的function class，并绑定为指定的name
     */
    public void registerFunctionClass(String name, Class function) {
        this.functions.put(name, function);
    }

    /**
     * 获取所有的functions
     * 
     * @return
     */
    public Map<String, Class> getAllFunctionClasses() {
        Map result = new HashMap<String, Class>();
        result.putAll(this.functions);
        return result;
    }
}
