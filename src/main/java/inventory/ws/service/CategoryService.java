package inventory.ws.service;

import inventory.ws.model.Category;

import java.util.Collection;

public interface CategoryService {
    Collection<Category> findAll();

    Category findOne(Long id);

    Category create(Category category);

    Category update(Category category);

    void delete(Long id);

    void evictCache();
}
