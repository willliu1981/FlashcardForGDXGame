package idv.kuan.flashcard.gdx.game.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import idv.kuan.flashcard.gdx.game.database.entity.Word;
import idv.kuan.libs.databases.QueryBuilder;

import idv.kuan.libs.databases.daos.CommonDao;
import idv.kuan.libs.databases.models.MetadataEntityUtil;

public class WordDao extends CommonDao<Word> {


    @Override
    protected Word createNewEntity() {
        return new Word();
    }

    @Override
    protected void populateColumnBuilderWithEntityProperties(QueryBuilder builder, Word entity) {
        builder.addColumnValue("term", entity.getTerm());
        builder.addColumnValue("translation", entity.getTranslation());
        builder.addColumnValue("version", entity.getVersion());
        //metadata
        byte[] dataToSave = MetadataEntityUtil.serializeMetadata(entity.getMetadata());
        builder.addColumnValue("metadata", dataToSave);


        //Timestamp atCreated = (Timestamp) entity.getMetadata().getDataObject(MetadataEntityUtil.DefaultMetadata.ATCREATED).getData();
        //Timestamp atUpdated = (Timestamp) entity.getMetadata().getDataObject(MetadataEntityUtil.DefaultMetadata.ATUPDATED).getData();
        //builder.addColumnValue("at_created", atCreated.toString());
        //builder.addColumnValue("at_updated", atUpdated.toString());
    }

    @Override
    protected void mapResultSetToEntity(Word entity, ResultSet resultSet) throws SQLException {
        entity.setTerm(resultSet.getString("term"));
        entity.setTranslation(resultSet.getString("translation"));
        entity.setVersion(resultSet.getInt("version"));
        //metadata
        byte[] retrievedData = resultSet.getBytes("metadata");
        MetadataEntityUtil.DefaultMetadata metadata = MetadataEntityUtil.metadataBuilder().
                setData(retrievedData).
                setVersoion(entity.getVersion()).
                buildMetadata();
        entity.setMetadata(metadata);
    }

    @Override
    protected String getTableName() {
        return "word";
    }
}
