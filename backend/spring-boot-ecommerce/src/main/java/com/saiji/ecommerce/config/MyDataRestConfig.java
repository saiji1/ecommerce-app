package com.saiji.ecommerce.config;

import com.saiji.ecommerce.entity.Product;
import com.saiji.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.POST};

        //disable http methods for products and cat
        config.getExposureConfiguration().forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));

        config.getExposureConfiguration().forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));


        // call internal helper for calling id
        exposeIds(config);
     }

     private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity ids

         //get list of all entity classes
         Set<EntityType<?>> entities = (Set<EntityType<?>>) entityManager.getMetamodel().getEntities();

         // create array with enitity types
         List<Class> entityClasses = new ArrayList<>();


        // get the entity types for the entities
         for (EntityType tempEntityType : entities) {
             entityClasses.add(tempEntityType.getJavaType());
         }

        // expose entity ids for the array of entity domain types
         Class[] domainTypes = entityClasses.toArray(new Class[0]);
         config.exposeIdsFor(domainTypes);
    }
}
