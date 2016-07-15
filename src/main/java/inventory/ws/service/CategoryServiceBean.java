package inventory.ws.service;

import inventory.ws.model.Category;
import inventory.ws.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;

@Service
@Transactional(
        propagation = Propagation.SUPPORTS,
        readOnly = true)
public class CategoryServiceBean implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Collection<Category> findAll() {
        logger.info("> findAll");

        Collection<Category> categories = categoryRepository.findAll();

        logger.info("< findAll");
        return categories;
    }

    @Override
    @Cacheable(
            value = "categories",
            key = "#id")
    public Category findOne(Long id) {
        logger.info("> findOne id:{}", id);

        Category category = categoryRepository.findOne(id);

        logger.info("< findOne id:{}", id);
        return category;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "categories",
            key = "#result.id")
    public Category create(Category category) {
        logger.info("> create");

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
        if (category.getId() != null) {
            // Cannot create Category with specified ID value
            logger.error(
                    "Attempted to create a Category, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        Category savedCategory = categoryRepository.save(category);

        logger.info("< create");
        return savedCategory;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "category",
            key = "#category.id")
    public Category update(Category category) {
        logger.info("> update id:{}", category.getId());

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
        Category categoryToUpdate = findOne(category.getId());
        if (categoryToUpdate == null) {
            // Cannot update Category that hasn't been persisted
            logger.error(
                    "Attempted to update a Category, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setDescription(category.getDescription());

        Category updatedCategory = categoryRepository.save(categoryToUpdate);

        logger.info("< update id:{}", category.getId());
        return updatedCategory;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = "categories",
            key = "#id")
    public void delete(Long id) {
        logger.info("> delete id:{}", id);

        categoryRepository.delete(id);

        logger.info("< delete id:{}", id);
    }

    @Override
    @CacheEvict(
            value = "categories",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}
