package com.agapple.mapping.process.script;

import java.util.Map;

import com.agapple.mapping.process.convetor.ConvertorHelper;

/**
 * script的function class操作helper类
 * 
 * @author jianghang 2011-6-27 下午07:32:25
 */
public class FunctionClassHelper {

    private static volatile FunctionClassHelper singleton  = null;
    private FunctionClassRepository             repository = null;

    public FunctionClassHelper(){
        repository = new FunctionClassRepository();
    }

    public FunctionClassHelper(FunctionClassRepository repository){
        // 允许传入自定义仓库
        this.repository = repository;
    }

    /**
     * 单例方法
     */
    public static FunctionClassHelper getInstance() {
        if (singleton == null) {
            synchronized (ConvertorHelper.class) {
                if (singleton == null) { // double check
                    singleton = new FunctionClassHelper();
                }
            }
        }
        return singleton;
    }

    /**
     * 注册对应的function，并绑定为指定的name
     */
    public void registerFunctionClass(String name, Class function) {
        this.repository.registerFunctionClass(name, function);
    }

    /**
     * 获取所有的functions
     * 
     * @return
     */
    public Map<String, Class> getAllFunctionClasses() {
        return this.repository.getAllFunctionClasses();
    }

    // ========================= setter / getter ===================

    public void setRepository(FunctionClassRepository repository) {
        this.repository = repository;
    }
}
