package com.agapple.mapping.script;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

/**
 * @author jianghang 2011-5-25 下午08:08:45
 */
public class JexlScriptExecutor implements ScriptExecutor {

    private static final int DEFAULT_CACHE_SIZE = 1000;
    private JexlEngine       engine;
    private int              cacheSize          = DEFAULT_CACHE_SIZE;

    public JexlScriptExecutor(){
        initialize();
    }

    /**
     * 初始化function
     */
    public void initialize() {
        if (cacheSize <= 0) {// 不考虑cacheSize为0的情况，强制使用LRU cache机制
            cacheSize = DEFAULT_CACHE_SIZE;
        }

        engine = new JexlEngine();
        engine.setCache(cacheSize);
    }

    /**
     * <pre>
     * 1. 接受JexlScriptContext上下文
     * 2. script针对对应name下的script脚本
     * </pre>
     */
    public Object evaluate(ScriptContext context, String script) {
        Expression expr = engine.createExpression(script);
        return expr.evaluate((JexlContext) context);
    }

    // ============================ setter / getter ============================

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

}
