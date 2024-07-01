package com.hncboy.crane4jx.service;

import com.hncboy.crane4jx.entity.Crane4jContainerEntity;
import com.hncboy.crane4jx.entity.Crane4jXTreeContainerChildEntity;
import com.hncboy.crane4jx.entity.Crane4jXTreeContainerParentEntity;
import com.hncboy.crane4jx.entity.Crane4jXTreeParentEntity;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author hncboy
 * Crane4jX 服务
 */
@Service(Service.Level.PROJECT)
public final class Crane4jXService {

    private final Crane4jContainerService crane4jContainerService;

    public Crane4jXService(Project project) {
        this.crane4jContainerService = Crane4jContainerService.getInstance(project);
    }

    public static Crane4jXService getInstance(@NotNull Project project) {
        return project.getService(Crane4jXService.class);
    }

    /**
     * 加载树数据
     *
     * @return 树数据
     */
    public List<Crane4jXTreeParentEntity> loadTreeData() {
        List<Crane4jXTreeParentEntity> result = new ArrayList<>();
        result.add(loadContainerTreeData());
        return result;
    }

    /**
     * 加载容器树数据
     *
     * @return 容器树数据
     */
    private Crane4jXTreeContainerParentEntity loadContainerTreeData() {
        List<Crane4jContainerEntity> crane4jContainerEntities = crane4jContainerService.load();

        List<Crane4jXTreeContainerChildEntity> childEntities = new ArrayList<>();
        for (Crane4jContainerEntity crane4jContainerEntity : crane4jContainerEntities) {
            Crane4jXTreeContainerChildEntity crane4JXTreeContainerChildEntity = new Crane4jXTreeContainerChildEntity(crane4jContainerEntity.getPsiElement(), crane4jContainerEntity.getNamespace(), crane4jContainerEntity.getQualifiedName());
            childEntities.add(crane4JXTreeContainerChildEntity);
        }

        // 按照 namespace 和全限定名升序
        childEntities.sort(Comparator.comparing(Crane4jXTreeContainerChildEntity::getNamespace)
                .thenComparing(Crane4jXTreeContainerChildEntity::getQualifiedName));

        return new Crane4jXTreeContainerParentEntity(childEntities);
    }
}
