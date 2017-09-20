package cn.tvfan.elasticdemo;

import cn.tvfan.elasticdemo.commons.IndexSetting;
import cn.tvfan.elasticdemo.entity.ChildEntity;
import cn.tvfan.elasticdemo.entity.ParentEntity;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.RoutingMissingException;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.join.query.JoinQueryBuilders.hasChildQuery;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:elasticsearch-template-test.xml")
public class ParentChildTest {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void before() {
        clean();
        elasticsearchTemplate.createIndex(ParentEntity.class);
        elasticsearchTemplate.createIndex(ChildEntity.class);
        elasticsearchTemplate.putMapping(ChildEntity.class);
    }

    @After
    public void clean() {
        elasticsearchTemplate.deleteIndex(ChildEntity.class);
        elasticsearchTemplate.deleteIndex(ParentEntity.class);
    }

    @Test
    public void shouldIndexParentChildEntity() {
        // index two parents
        ParentEntity parent1 = index("parent1", "First Parent");
        ParentEntity parent2 = index("parent2", "Second Parent");

        // index a child for each parent
        String child1name = "First";
        index("child1", parent1.getId(), child1name);
        index("child2", parent2.getId(), "Second");

        elasticsearchTemplate.refresh(ParentEntity.class);
        elasticsearchTemplate.refresh(ChildEntity.class);

        // find all parents that have the first child
        QueryBuilder query = hasChildQuery(IndexSetting.CHILD_TYPE, QueryBuilders.termQuery("name", child1name.toLowerCase()), ScoreMode.None);
        List<ParentEntity> parents = elasticsearchTemplate.queryForList(new NativeSearchQuery(query), ParentEntity.class);

        for (ParentEntity entity: parents) {
            System.out.println(entity);
        }

        // we're expecting only the first parent as result
        assertThat("parents", parents, contains(hasProperty("id", is(parent1.getId()))));
    }

    @Test
    public void shouldUpdateChild() throws Exception {
        // index parent and child
        ParentEntity parent = index("parent", "Parent");
        ChildEntity child = index("child", parent.getId(), "Child");
        String newChildName = "New Child Name";

        // update the child, not forgetting to set the parent id as routing parameter
        UpdateRequest updateRequest = new UpdateRequest(IndexSetting.INDEX, IndexSetting.CHILD_TYPE, child.getId());
        updateRequest.routing(parent.getId());
        XContentBuilder builder;
        builder = jsonBuilder().startObject().field("name", newChildName).endObject();
        updateRequest.doc(builder);
        final UpdateResponse response = update(updateRequest);

        assertThat(response.getShardInfo().getSuccessful(), is(1));
    }

    @Test(expected = RoutingMissingException.class)
    public void shouldFailWithRoutingMissingExceptionOnUpdateChildIfNotRoutingSetOnUpdateRequest() throws Exception {
        // index parent and child
        ParentEntity parent = index("parent", "Parent");
        ChildEntity child = index("child", parent.getId(), "Child");
        String newChildName = "New Child Name";

        // update the child, forget routing parameter
        UpdateRequest updateRequest = new UpdateRequest(IndexSetting.INDEX, IndexSetting.CHILD_TYPE, child.getId());
        XContentBuilder builder;
        builder = jsonBuilder().startObject().field("name", newChildName).endObject();
        updateRequest.doc(builder);
        update(updateRequest);
    }

    @Test(expected = RoutingMissingException.class)
    public void shouldFailWithRoutingMissingExceptionOnUpdateChildIfRoutingOnlySetOnRequestDoc() throws Exception {
        // index parent and child
        ParentEntity parent = index("parent", "Parent");
        ChildEntity child = index("child", parent.getId(), "Child");
        String newChildName = "New Child Name";

        // update the child
        UpdateRequest updateRequest = new UpdateRequest(IndexSetting.INDEX, IndexSetting.CHILD_TYPE, child.getId());
        XContentBuilder builder;
        builder = jsonBuilder().startObject().field("name", newChildName).endObject();
        updateRequest.doc(builder);
        updateRequest.doc().routing(parent.getId());
        update(updateRequest);
    }

    private ParentEntity index(String parentId, String name) {
        ParentEntity parent = new ParentEntity(parentId, name);
        IndexQuery index = new IndexQuery();
        index.setId(parent.getId());
        index.setObject(parent);
        elasticsearchTemplate.index(index);

        return parent;
    }

    private ChildEntity index(String childId, String parent, String name) {
        ChildEntity child = new ChildEntity(childId, parent, name);
        IndexQuery index = new IndexQuery();
        index.setId(child.getId());
        index.setObject(child);
        index.setParentId(child.getParent());
        elasticsearchTemplate.index(index);

        return child;
    }

    private UpdateResponse update(UpdateRequest updateRequest) {
        final UpdateQuery update = new UpdateQuery();
        update.setId(updateRequest.id());
        update.setType(updateRequest.type());
        update.setIndexName(updateRequest.index());
        update.setUpdateRequest(updateRequest);
        return elasticsearchTemplate.update(update);
    }
}
