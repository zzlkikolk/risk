package com.api.zhangzl.retrydemo.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * <h3>RetryDemo</h3>
 *  检查注解的值是否合法
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/7 16:32
 **/
@SupportedAnnotationTypes("com.api.zhangzl.retrydemo.annotation.CheckRetryValue")
public class CheckRetryValueProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements=roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                if(element instanceof ExecutableElement){
                    ExecutableElement method= (ExecutableElement) element;
                    if(!method.getReturnType().toString().equals("boolean")){
                        throw new RuntimeException("方法返回值必须为boolean类型");
                    }
                }
            }
        }
        return true;
    }
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
