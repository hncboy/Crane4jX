package com.hncboy.crane4jx.entity;

import lombok.Getter;

import java.util.List;

/**
 * @author hncboy
 * Crane4jX 树父节点实体
 */
@Getter
public class Crane4jXTreeParentEntity {

    /**
     * 子节点列表
     */
    private final List<? extends Crane4jXTreeChildEntity> childEntities;

    public Crane4jXTreeParentEntity(List<? extends Crane4jXTreeChildEntity> childEntities) {
        this.childEntities = childEntities;
    }
}
