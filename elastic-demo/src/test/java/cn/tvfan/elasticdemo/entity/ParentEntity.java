package cn.tvfan.elasticdemo.entity;

import cn.tvfan.elasticdemo.commons.IndexSetting;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = IndexSetting.INDEX, type = IndexSetting.PARENT_TYPE, shards = 1, replicas = 0, refreshInterval = "-1")
public class ParentEntity {

    @Id
    String id;

    @Field(type = FieldType.text, store = true)
    String name;

    public ParentEntity() {
    }

    public ParentEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("id", id).append("name", name).toString();
    }

}