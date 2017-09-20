package cn.tvfan.elasticdemo.entity;

import cn.tvfan.elasticdemo.commons.IndexSetting;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

@Document(indexName = IndexSetting.INDEX, type = IndexSetting.CHILD_TYPE, shards = 1, replicas = 0, refreshInterval = "-1")
public class ChildEntity {

    @Id
    String id;

    @Parent(type = IndexSetting.PARENT_TYPE)
    String parent;

    @Field(type = FieldType.text, store = true)
    String name;

    public ChildEntity() {}

    public ChildEntity(String id, String parent, String name) {
        this.id = id;
        this.parent = parent;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("id", id).append("parent", parent).append("name", name).toString();
    }
}
