package com.heimdallauth.server.datamanagers.impl.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MongoBulkOperationsService {
    private final MongoTemplate mongoTemplate;

    public MongoBulkOperationsService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    protected  <T> List<String> executeMongoDBSaveOperation(List<T> objectCollection, String collectionName) {
        Collection<T> savedCollection = this.mongoTemplate.insert(objectCollection, collectionName);
        List<String> savedCollectionIds = new ArrayList<>();
        log.debug("Saved {} objects to MongoDB collection {}", savedCollection.size(), collectionName);

        if (!savedCollection.isEmpty()) {
            try {
                Method getIdMethod = savedCollection.iterator().next().getClass().getMethod("getId");
                for (T document : savedCollection) {
                    savedCollectionIds.add((String) getIdMethod.invoke(document));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Error retrieving IDs from saved documents", e);
            }
        }

        return savedCollectionIds;
    }
}
