package com.github.aia.core.security;

import com.github.aia.utils.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 对接口完全限定名进行匹配，使用ant模式
 */
public class QualifiedNameWhiteList implements WhiteList{


    private List<String> patternQualifiedNames = new ArrayList<>();

    private AntPathMatcher pathMatcher = new AntPathMatcher(".");

    /**
     * 新增白名单
     *
     * @param whiteList 白名单
     */
    @Override
    public void addWhiteList(Object whiteList) {
        patternQualifiedNames.add((String) whiteList);
    }

    /**
     * 是否包含
     *
     * @param o 判断的数据
     * @return 结果
     */
    @Override
    public boolean include(Object o) {
        String qualifiedName = (String) o;
        for (String pattern : patternQualifiedNames) {
            if (pathMatcher.match(pattern,qualifiedName)){
                return true;
            }
        }
        return false;
    }
}
