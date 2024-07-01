package com.hncboy.crane4jx.entity;

import com.intellij.icons.AllIcons;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import lombok.Getter;

import javax.swing.Icon;

/**
 * @author hncboy
 * Crane4jX 树容器子节点实体
 */
@Getter
public class Crane4jXTreeContainerChildEntity extends Crane4jXTreeChildEntity {

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
    private final String qualifiedName;

    /**
     * 图标
     */
    private final Icon icon;

    public Crane4jXTreeContainerChildEntity(PsiElement psiElement, String namespace, String qualifiedName) {
        this.psiElement = psiElement;
        this.namespace = namespace;
        this.qualifiedName = qualifiedName;
        this.icon = AllIcons.Modules.ResourcesRoot;
    }

    /**
     * 导航到指定位置
     *
     * @param focus 是否聚焦
     */
    public void navigate(boolean focus) {
        if (psiElement != null) {
            ((Navigatable)psiElement).navigate(focus);
        }
    }
}
