package com.hncboy.crane4jx.entity;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import lombok.Getter;

import java.util.Objects;

/**
 * @author hncboy
 * Crane4j 容器实体
 */
public class Crane4jContainerEntity {

    /**
     * PsiClass 或 PsiMethod
     */
    @Getter
    private final PsiElement psiElement;

    /**
     * 容器命名空间
     * 唯一
     */
    private final String namespace;

    /**
     * 全限定名
     */
    @Getter
    private String qualifiedName;

    public Crane4jContainerEntity(PsiElement psiElement, String namespace) {
        this.psiElement = psiElement;
        this.namespace = namespace;
        initQualifiedName();
    }

    /**
     * 初始化全限定名
     */
    private void initQualifiedName() {
        // 如果是 PsiClass，直接获取其全限定名
        if (psiElement instanceof PsiClass psiClass) {
            qualifiedName = psiClass.getQualifiedName();
        }
        // 如果是 PsiMethod，构建包含类名和方法名的字符串
        else if (psiElement instanceof PsiMethod psiMethod) {
            // 获取拥有这个方法的类
            PsiClass containingClass = psiMethod.getContainingClass();
            if (containingClass != null) {
                qualifiedName = containingClass.getQualifiedName() + "@" + psiMethod.getName();
            }
        }
        // 啥都不是
        else {
            qualifiedName = null;
        }
    }

    /**
     * 获取命名空间
     * 命名空间为空的就取全限定名称
     *
     * @return 命名空间
     */
    public String getNamespace() {
        return Objects.isNull(namespace) ? qualifiedName : namespace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // 理论上 namespace 就是唯一的，加上 qualifiedName 用于展示判断配置错误的情况
        Crane4jContainerEntity that = (Crane4jContainerEntity) o;
        return Objects.equals(getNamespace(), that.getNamespace())
                && Objects.equals(getQualifiedName(), that.getQualifiedName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNamespace(), getQualifiedName());
    }
}
