package com.hncboy.crane4jx.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hncboy
 * Crane4jX 容器常量
 */
public interface Crane4jXContainerConstant {

    /**
     * Spring 框架方法上 @Bean 注解全限定名
     */
    String SPRING_METHOD_BEAN_ANNOTATION_QUALIFIED_NAME = "org.springframework.context.annotation.Bean";

    /**
     * Crane4j 框架类上 @ContainerEnum 注解全限定名
     */
    String CRANE4J_CLASS_CONTAINER_ENUM_ANNOTATION_QUALIFIED_NAME = "cn.crane4j.annotation.ContainerEnum";

    /**
     * Crane4j 框架类上 @ContainerConstant 注解全限定名
     */
    String CRANE4J_CLASS_CONTAINER_CONSTANT_ANNOTATION_QUALIFIED_NAME = "cn.crane4j.annotation.ContainerConstant";

    /**
     * Crane4j 框架方法上 @ContainerMethod 注解全限定名
     */
    String CRANE4J_METHOD_CONTAINER_METHOD_ANNOTATION_QUALIFIED_NAME = "cn.crane4j.annotation.ContainerMethod";

    /**
     * Crane4j 框架容器根接口全限定名
     */
    String CRANE4J_CONTAINER_ROOT_INTERFACE_QUALIFIED_NAME = "cn.crane4j.core.container";

    /**
     * Crane4j 框架上注解中 namespace 的参数名
     */
    String CRANE4J_ANNOTATION_NAMESPACE_PARAM_NAME = "namespace";

    /**
     * Crane4j 框架类上注解全限定名集合
     * key：注解全限定名
     * value：注解中 namespace 的方法名
     */
    Map<String, String> CRANE4J_CLASS_ANNOTATION_QUALIFIED_NAME_MAP = new HashMap<>() {
        {
            put(CRANE4J_CLASS_CONTAINER_ENUM_ANNOTATION_QUALIFIED_NAME, CRANE4J_ANNOTATION_NAMESPACE_PARAM_NAME);
            put(CRANE4J_CLASS_CONTAINER_CONSTANT_ANNOTATION_QUALIFIED_NAME, CRANE4J_ANNOTATION_NAMESPACE_PARAM_NAME);
        }
    };
}
