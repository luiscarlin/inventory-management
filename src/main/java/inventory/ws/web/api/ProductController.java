package inventory.ws.web.api;

import inventory.ws.model.Product;
import inventory.ws.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @RequestMapping(
            value = "/api/products",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Product>> getCategories() {
        logger.info("> getCategories");

        Collection<Product> products = productService.findAll();

        logger.info("< getCategories");
        return new ResponseEntity<Collection<Product>>(products,
                HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/products/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        logger.info("> getProduct id:{}", id);

        Product product = productService.findOne(id);
        if (product == null) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }

        logger.info("< getProduct id:{}", id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/products",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product) {
        logger.info("> createProduct");

        Product savedProduct = productService.create(product);

        logger.info("< createProduct");
        return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/api/products/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(
            @RequestBody Product product) {
        logger.info("> updateProduct id:{}", product.getId());

        Product updatedProduct = productService.update(product);
        if (updatedProduct == null) {
            return new ResponseEntity<Product>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateProduct id:{}", product.getId());
        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/products/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProduct(
            @PathVariable("id") Long id) {
        logger.info("> deleteProduct id:{}", id);

        productService.delete(id);

        logger.info("< deleteProduct id:{}", id);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
}
