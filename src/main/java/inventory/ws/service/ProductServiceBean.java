package inventory.ws.service;

import inventory.ws.model.Product;
import inventory.ws.repository.ProductRepository;
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
public class ProductServiceBean implements ProductService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Collection<Product> findAll() {
        logger.info("> findAll");

        Collection<Product> products = productRepository.findAll();

        logger.info("< findAll");
        return products;
    }

    @Override
    @Cacheable(
            value = "products",
            key = "#id")
    public Product findOne(Long id) {
        logger.info("> findOne id:{}", id);

        Product product = productRepository.findOne(id);

        logger.info("< findOne id:{}", id);
        return product;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "products",
            key = "#result.id")
    public Product create(Product product) {
        logger.info("> create");

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
        if (product.getId() != null) {
            // Cannot create Product with specified ID value
            logger.error(
                    "Attempted to create a Product, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        Product savedProduct = productRepository.save(product);

        logger.info("< create");
        return savedProduct;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "product",
            key = "#product.id")
    public Product update(Product product) {
        logger.info("> update id:{}", product.getId());

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
        Product productToUpdate = findOne(product.getId());
        if (productToUpdate == null) {
            // Cannot update Product that hasn't been persisted
            logger.error(
                    "Attempted to update a Product, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());

        Product updatedProduct = productRepository.save(productToUpdate);

        logger.info("< update id:{}", product.getId());
        return updatedProduct;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = "products",
            key = "#id")
    public void delete(Long id) {
        logger.info("> delete id:{}", id);

        productRepository.delete(id);

        logger.info("< delete id:{}", id);
    }

    @Override
    @CacheEvict(
            value = "products",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}
