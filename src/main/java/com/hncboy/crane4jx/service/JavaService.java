package com.hncboy.crane4jx.service;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * @author hncboy
 * Java 服务
 */
@Service(Service.Level.PROJECT)
public final class JavaService {

    private final JavaPsiFacade javaPsiFacade;

    private final Project project;

    public JavaService(@NotNull Project project) {
        this.project = project;
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
    }

    public static JavaService getInstance(@NotNull Project project) {
        return project.getService(JavaService.class);
    }


    /**
     * 根据提供的全限定名获取 PsiClass 对象
     *
     * @param qualifiedName 类的全限定名
     * @return PsiClass 对象
     */
    public PsiClass findPsiClass(@NotNull String qualifiedName) {
        return javaPsiFacade.findClass(qualifiedName, GlobalSearchScope.allScope(project));
    }

    /**
     * 递归地根据全限定名查找所有子类
     *
     * @param qualifiedName 全限定名
     * @return 包含所有子类的 PsiClass 列表
     */
    public List<PsiClass> findAllSubPsiClassByQualifiedName(@NotNull String qualifiedName) {
        PsiClass psiClass = javaPsiFacade.findClass(qualifiedName, GlobalSearchScope.allScope(project));
        if (Objects.isNull(psiClass)) {
            return Collections.emptyList();
        }

        Set<PsiClass> subPsiClassSet = new HashSet<>();
        collectAllSubPsiClass(psiClass, subPsiClassSet);

        return new ArrayList<>(subPsiClassSet);
    }

    /**
     * 递归父类的收集所有子类
     *
     * @param parentPsiClass 父 PsiClass 对象
     * @param subPsiClassSet 用于存储找到的子类的集合
     */
    private void collectAllSubPsiClass(PsiClass parentPsiClass, Set<PsiClass> subPsiClassSet) {
        // 获取直接实现该接口的所有类
        PsiReferenceList psiReferenceList = parentPsiClass.getImplementsList();
        if (Objects.isNull(psiReferenceList)) {
            return;
        }

        for (PsiJavaCodeReferenceElement referenceElement : psiReferenceList.getReferenceElements()) {
            PsiElement resolved = referenceElement.resolve();
            if (resolved instanceof PsiClass subclass) {
                // 避免添加重复的子类
                if (subPsiClassSet.add(subclass)) {
                    // 递归查找子类的子类
                    collectAllSubPsiClass(subclass, subPsiClassSet);
                }
            }
        }
    }

    /**
     * 根据注解的全限定名获取所有的 PsiClass 对象
     *
     * @param qualifiedName 注解的全限定名
     * @return PsiClass 对象集合
     */
    public Collection<PsiClass> findPsiClassesByAnnotationQualifiedName(@NotNull String qualifiedName) {
        PsiClass psiClass = findPsiClass(qualifiedName);
        if (Objects.isNull(psiClass)) {
            return Collections.emptyList();
        }
        return AnnotatedElementsSearch.searchPsiClasses(psiClass, GlobalSearchScope.projectScope(project)).findAll();
    }

    /**
     * 根据注解的全限定名获取所有的 PsiMethod 对象
     *
     * @param qualifiedName 注解的全限定名
     * @return PsiMethod 对象集合
     */
    public Collection<PsiMethod> findMethodsByAnnotationQualifiedName(@NotNull String qualifiedName) {
        PsiClass psiClass = findPsiClass(qualifiedName);
        if (Objects.isNull(psiClass)) {
            return Collections.emptyList();
        }
        return AnnotatedElementsSearch.searchPsiMethods(psiClass, GlobalSearchScope.projectScope(project)).findAll();
    }

    /**
     * 获取指定注解的属性值。
     * 此方法用于从 PsiModifierListOwner 元素中获取指定注解的属性值。
     * 如果指定的注解不存在或属性值不存在，则返回null。
     *
     * @param psiModifierListOwner 包含注解的 Psi 元素，例如 PsiMethod、PsiClass 等
     * @param qualifiedName 注解的全限定名，例如"java.lang.Override"
     * @param attribute 注解的属性名称，例如 value
     * @return 注解的属性值的文本表示，如果属性不存在则返回 null
     */
    public String getAnnotationAttributeValue(@NotNull PsiModifierListOwner psiModifierListOwner, @NotNull String qualifiedName, String attribute) {
        // 从Psi元素中获取指定的注解
        PsiAnnotation psiAnnotation = psiModifierListOwner.getAnnotation(qualifiedName);
        if (Objects.isNull(psiAnnotation)) {
            return null;
        }

        // 在注解中查找指定的属性值
        PsiAnnotationMemberValue attributeValue = psiAnnotation.findAttributeValue(attribute);
        if (Objects.isNull(attributeValue)) {
            return null;
        }

        // 返回属性值的文本表示，去除引号
        return attributeValue.getText().replace("\"", "");
    }
}
