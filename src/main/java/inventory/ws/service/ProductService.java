package inventory.ws.service;

import inventory.ws.model.Product;

import java.util.Collection;

public interface ProductService {
    Collection<Product> findAll();

    Product findOne(Long id);

    Product create(Product product);

    Product update(Product product);

    void delete(Long id);

    void evictCache();
}
