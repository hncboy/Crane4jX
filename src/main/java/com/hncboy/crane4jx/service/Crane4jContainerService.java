package com.hncboy.crane4jx.service;

import com.hncboy.crane4jx.constant.Crane4jXContainerConstant;
import com.hncboy.crane4jx.entity.Crane4jContainerEntity;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hncboy
 * Crane4j 容器服务
 */
@Service(Service.Level.PROJECT)
public final class Crane4jContainerService {

    private final JavaService javaService;

    public Crane4jContainerService(Project project) {
        this.javaService = JavaService.getInstance(project);
    }

    public static Crane4jContainerService getInstance(@NotNull Project project) {
        return project.getService(Crane4jContainerService.class);
    }

    /**
     * 加载所有的 Crane4j 容器实体
     *
     * @return Crane4j 容器实体列表
     */
    public List<Crane4jContainerEntity> load() {
        List<Crane4jContainerEntity> result = new ArrayList<>();

        List<Crane4jContainerEntity> containerEntityByRootInterfaceImplement = findContainerEntityByRootInterfaceImplement();
        result.addAll(containerEntityByRootInterfaceImplement);
        result.addAll(findContainerEntityBySpringMethodAnnotation(containerEntityByRootInterfaceImplement));
        result.addAll(findContainerEntityByCrane4jClassAnnotation());
        result.addAll(findContainerEntityByCrane4jMethodAnnotation());

        return result.stream().distinct().toList();
    }

    /**
     * 根据实现了 Crane4j 框架容器根接口查找容器实体
     *
     * @return 容器实体列表
     */
    private List<Crane4jContainerEntity> findContainerEntityByRootInterfaceImplement() {
        List<Crane4jContainerEntity> containerEntities = new ArrayList<>();

        List<PsiClass> subPsiClasses = javaService.findAllSubPsiClassByQualifiedName(Crane4jXContainerConstant.CRANE4J_CONTAINER_ROOT_INTERFACE_QUALIFIED_NAME);
        for (PsiClass subPsiClass : subPsiClasses) {
            if (Objects.isNull(subPsiClass.getQualifiedName())) {
                continue;
            }
            // 排除抽象类
            if (subPsiClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
                continue;
            }
            // 排除接口
            if (subPsiClass.isInterface()) {
                continue;
            }
            containerEntities.add(new Crane4jContainerEntity(subPsiClass, null));
        }
        return containerEntities;
    }

    /**
     * 根据 Crane4j 框架方法上的注解查找容器实体
     *
     * @return 容器实体列表
     */
    private List<Crane4jContainerEntity> findContainerEntityByCrane4jMethodAnnotation() {
        // 获取所有的 Crane4j 框架方法上的注解
        String crane4jMethodContainerMethodAnnotationQualifiedName = Crane4jXContainerConstant.CRANE4J_METHOD_CONTAINER_METHOD_ANNOTATION_QUALIFIED_NAME;
        Collection<PsiMethod> psiMethods = javaService.findMethodsByAnnotationQualifiedName(crane4jMethodContainerMethodAnnotationQualifiedName);

        List<Crane4jContainerEntity> containerEntities = new ArrayList<>();
        for (PsiMethod psiMethod : psiMethods) {
            // 获取注解中的 namespace 参数的值
            String namespaceValue = javaService.getAnnotationAttributeValue(psiMethod, crane4jMethodContainerMethodAnnotationQualifiedName, Crane4jXContainerConstant.CRANE4J_ANNOTATION_NAMESPACE_PARAM_NAME);
            containerEntities.add(new Crane4jContainerEntity(psiMethod, namespaceValue));
        }

        return containerEntities;
    }

    /**
     * 根据 Spring 框架方法上的注解查找容器实体
     *
     * @return 容器实体列表
     */
    private List<Crane4jContainerEntity> findContainerEntityBySpringMethodAnnotation(List<Crane4jContainerEntity> containerEntityByRootInterfaceImplement) {
        Collection<PsiMethod> psiMethods = javaService.findMethodsByAnnotationQualifiedName(Crane4jXContainerConstant.SPRING_METHOD_BEAN_ANNOTATION_QUALIFIED_NAME);

        List<Crane4jContainerEntity> containerEntities = new ArrayList<>();
        for (PsiMethod psiMethod : psiMethods) {
            PsiType returnType = psiMethod.getReturnType();

            if (!(returnType instanceof PsiClassType psiClassType)) {
                continue;
            }

            PsiClass psiClass = psiClassType.resolve();
            if (Objects.isNull(psiClass)) {
                continue;
            }

            String crane4jContainerRootInterfaceQualifiedName = Crane4jXContainerConstant.CRANE4J_CONTAINER_ROOT_INTERFACE_QUALIFIED_NAME;

            // 返回的直接是根接口
            if (Objects.equals(psiClass.getQualifiedName(), crane4jContainerRootInterfaceQualifiedName)) {
                containerEntities.add(new Crane4jContainerEntity(psiMethod, null));
            } else {
                Crane4jContainerEntity containerEntity = new Crane4jContainerEntity(psiMethod, null);
                // 存在再添加
                if (containerEntityByRootInterfaceImplement.contains(containerEntity)) {
                    containerEntities.add(containerEntity);
                }
            }
        }

        return containerEntities;
    }

    /**
     * 根据 Crane4j 框架类上的注解查找容器实体
     *
     * @return 容器实体列表
     */
    private List<Crane4jContainerEntity> findContainerEntityByCrane4jClassAnnotation() {
        List<Crane4jContainerEntity> containerEntities = new ArrayList<>();
        // 获取所有的 Crane4j 框架类上的注解
        Map<String, String> crane4jClassAnnotationQualifiedNameMap = Crane4jXContainerConstant.CRANE4J_CLASS_ANNOTATION_QUALIFIED_NAME_MAP;
        for (String crane4jClassAnnotationQualifiedName : crane4jClassAnnotationQualifiedNameMap.keySet()) {
            Collection<PsiClass> psiClasses = javaService.findPsiClassesByAnnotationQualifiedName(crane4jClassAnnotationQualifiedName);
            for (PsiClass psiClass : psiClasses) {
                if (Objects.isNull(psiClass.getQualifiedName())) {
                    continue;
                }
                // 排除抽象类
                if (psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
                    continue;
                }

                // 获取参数名
                String namespaceParamName = crane4jClassAnnotationQualifiedNameMap.get(crane4jClassAnnotationQualifiedName);
                // 获取注解中的 namespace 参数的值
                String namespaceValue = javaService.getAnnotationAttributeValue(psiClass, crane4jClassAnnotationQualifiedName, namespaceParamName);
                containerEntities.add(new Crane4jContainerEntity(psiClass, namespaceValue));
            }
        }

        return containerEntities;
    }
}
