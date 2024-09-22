package com.project.tarea1.rest.product;

import com.project.tarea1.logic.entity.category.Category;
import com.project.tarea1.logic.entity.category.CategoryRepository;
import com.project.tarea1.logic.entity.game.Game;
import com.project.tarea1.logic.entity.product.Product;
import com.project.tarea1.logic.entity.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Product addProduct(@RequestBody Product product) {
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isPresent()) {
            product.setCategory(category.get());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Categoría no encontrada");
        }
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }


    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());

            Optional<Category> category = categoryRepository.findById(updatedProduct.getCategory().getId());
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                throw new RuntimeException("Categoría no encontrada");
            }
            return productRepository.save(product);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public String deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Producto eliminado exitosamente.";
        } else {
            return "Producto no encontrado.";
        }
    }
}
