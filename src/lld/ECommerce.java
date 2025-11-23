package lld;

/*
    ref : https://chatgpt.com/share/6922d7f0-5d08-800e-9a9f-26abbac3d489

    Flow
    1. DB schema / Entities
    2. API design
    3. Classes & Design Patterns

    ==DB Schema==
    Category
    - id
    - name
    - parentId
    - List<Category> children

    Product
    - id
    - title
    - price
    - categoryId

    ==Classes==
    public class CategoryService {

        private final CategoryRepository categoryRepo;

        public CategoryService(CategoryRepository categoryRepo) {
            this.categoryRepo = categoryRepo;
        }

        public List<Long> getAllCategoryIds(Long categoryId) {
            List<Long> result = new ArrayList<>();
            dfs(categoryId, result);
            return result;
        }

        private void dfs(Long categoryId, List<Long> result) {
            result.add(categoryId);
            List<Category> subCats = categoryRepo.getSubcategories(categoryId);

            for (Category c : subCats) {
                dfs(c.getId(), result);
            }
        }
    }

    public class ProductService {

        private final ProductRepository productRepo;
        private final CategoryService categoryService;

        public ProductService(ProductRepository productRepo, CategoryService categoryService) {
            this.productRepo = productRepo;
            this.categoryService = categoryService;
        }

        public List<Product> getProductsForCategory(Long categoryId) {
            List<Long> categoryIds = categoryService.getAllCategoryIds(categoryId);
            return productRepo.getProductsByCategoryIds(categoryIds);
        }
    }

    ==Follow-ups==
    1. How to optimize fetching subcategories?
    - Precompute category tree in memory
    - Use adjacency list in Redis
    - Store category → all descendant category_ids mapping
    - → O(1) lookup instead of recursive DB hits

    2. Pagination?
    - GET /categories/{id}/products?page=1&size=20

    3. Sorting / Filtering?
    ProductFilter {
       Double minPrice;
       Double maxPrice;
       String sortBy;
    }

    4. Caching?
    - category hierarchy
    - product list per category
    - or final API response (with invalidation)

    5. What about millions of products?
    - Elasticsearch / Solr to index category IDs + product docs
    - Query: categoryId: {id OR all descendants}
    - Much faster than DB joins.


*/

public class ECommerce {
}
