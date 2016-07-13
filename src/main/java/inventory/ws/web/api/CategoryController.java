package inventory.ws.web.api;

        import inventory.ws.model.Category;
        import inventory.ws.service.CategoryService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.Collection;

@RestController
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(
            value = "/api/categories",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Category>> getCategories() {
        logger.info("> getCategories");

        Collection<Category> categories = categoryService.findAll();

        logger.info("< getCategories");
        return new ResponseEntity<Collection<Category>>(categories,
                HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/categories/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        logger.info("> getCategory id:{}", id);

        Category category = categoryService.findOne(id);
        if (category == null) {
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        }

        logger.info("< getCategory id:{}", id);
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/categories",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category) {
        logger.info("> createCategory");

        Category savedCategory = categoryService.create(category);

        logger.info("< createCategory");
        return new ResponseEntity<Category>(savedCategory, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/api/categories/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> updateCategory(
            @RequestBody Category category) {
        logger.info("> updateCategory id:{}", category.getId());

        Category updatedCategory = categoryService.update(category);
        if (updatedCategory == null) {
            return new ResponseEntity<Category>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateCategory id:{}", category.getId());
        return new ResponseEntity<Category>(updatedCategory, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/categories/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(
            @PathVariable("id") Long id) {
        logger.info("> deleteCategory id:{}", id);

        categoryService.delete(id);

        logger.info("< deleteCategory id:{}", id);
        return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
    }
}
