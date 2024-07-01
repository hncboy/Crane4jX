package com.hncboy.crane4jx.entity;

import com.intellij.icons.AllIcons;
import lombok.Getter;

import javax.swing.Icon;
import java.util.List;

/**
 * @author hncboy
 * Crane4jX 树容器父节点实体
 */
@Getter
public class Crane4jXTreeContainerParentEntity extends Crane4jXTreeParentEntity {

    /**
     * 名称
     */
    private final String name;

    /**
     * 子节点数量
     */
    private final int childCount;

    /**
     * 图标
     */
    private final Icon icon;

    public Crane4jXTreeContainerParentEntity(List<Crane4jXTreeContainerChildEntity> crane4jXTreeContainerChildEntities) {
        super(crane4jXTreeContainerChildEntities);
        this.name = "Container";
        this.childCount = crane4jXTreeContainerChildEntities.size();
        this.icon = AllIcons.Modules.SourceRoot;
    }
}
