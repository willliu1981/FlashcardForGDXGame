package idv.kuan.flashcard.gdx.game.database.entity;


import java.util.HashMap;
import java.util.Map;

import idv.kuan.libs.databases.models.MetadataEntityUtil;

public class TestMetadata implements MetadataEntityUtil.Metadata {

    private Map<String, MetadataEntityUtil.MetadataObject> metadataObjectMap = new HashMap<>();

    public void addMetadataObject(String name, MetadataEntityUtil.MetadataObject metadataObject) {
        this.metadataObjectMap.put(name, metadataObject);
    }

    @Override
    public MetadataEntityUtil.MetadataObject getMetadataObject(String name) {


        return metadataObjectMap.get(name);
    }

    @Override
    public String toString() {
        return "TestMetadata{}";
    }
}
