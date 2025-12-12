package com.example.jwellery.Data;

import com.example.jwellery.Entity.Category;
import com.example.jwellery.Entity.Product;
import com.example.jwellery.Entity.ProductImage;
import com.example.jwellery.Repositories.CategoryRepository;
import com.example.jwellery.Repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataLoader(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    // Your 4 fixed image URLs
    private static final String[] IMAGE_URLS = {
            "https://images.unsplash.com/photo-1651160670627-2896ddf7822f?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8andlbGxlcnl8ZW58MHx8MHx8fDA%3D",
            "https://images.unsplash.com/photo-1656428361240-47e1737b7dce?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8andlbGxlcnl8ZW58MHx8MHx8fDA%3D",
            "https://images.unsplash.com/photo-1656428852088-dc40d6c76885?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8andlbGxlcnl8ZW58MHx8MHx8fDA%3D",
            "https://images.unsplash.com/photo-1731586249471-82bb9b2f769a?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGp3ZWxsZXJ5fGVufDB8fDB8fHww"
    };

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0 && productRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Categories
        Category rings = new Category();
        rings.setName("Rings");
        categoryRepository.save(rings);

        Category necklaces = new Category();
        necklaces.setName("Necklaces");
        categoryRepository.save(necklaces);

        Category earrings = new Category();
        earrings.setName("Earrings");
        categoryRepository.save(earrings);

        // ===== RINGS =====
        addProduct("Diamond Solitaire Ring", 2500.00, "Classic diamond solitaire set in platinum.", 8, rings, true, IMAGE_URLS[0], IMAGE_URLS[1]);
        addProduct("18k Gold Band", 1200.00, "Timeless 18k yellow gold wedding band.", 15, rings, false, IMAGE_URLS[1], IMAGE_URLS[2]);
        addProduct("Rose Gold Morganite Ring", 1850.00, "Cushion-cut morganite in rose gold.", 7, rings, true, IMAGE_URLS[2], IMAGE_URLS[3]);
        addProduct("Emerald Halo Ring", 3200.00, "Emerald center with diamond halo.", 5, rings, true, IMAGE_URLS[3], IMAGE_URLS[0]);
        addProduct("Sapphire Eternity Band", 2100.00, "Blue sapphire eternity band, white gold.", 10, rings, false, IMAGE_URLS[0], IMAGE_URLS[2]);
        addProduct("Vintage Opal Ring", 1700.00, "Opal cabochon with filigree details.", 6, rings, false, IMAGE_URLS[1], IMAGE_URLS[3]);
        addProduct("Platinum Bezel Diamond Ring", 2800.00, "Bezel-set round brilliant diamond.", 4, rings, true, IMAGE_URLS[0], IMAGE_URLS[3]);
        addProduct("Princess-Cut Engagement Ring", 4500.00, "Princess cut diamond on pave band.", 3, rings, true, IMAGE_URLS[1], IMAGE_URLS[2]);
        addProduct("Stackable Thin Gold Ring", 350.00, "Minimal stackable 14k gold ring.", 30, rings, false, IMAGE_URLS[2], IMAGE_URLS[0]);
        addProduct("Black Onyx Signet", 950.00, "Onyx signet ring with smooth shoulders.", 9, rings, false, IMAGE_URLS[3], IMAGE_URLS[1]);

        // ===== NECKLACES =====
        addProduct("Pearl Strand Necklace", 4000.00, "Hand-knotted freshwater pearls.", 5, necklaces, true, IMAGE_URLS[0], IMAGE_URLS[1]);
        addProduct("Gold Disc Pendant", 950.00, "18k gold disc pendant on fine chain.", 20, necklaces, false, IMAGE_URLS[1], IMAGE_URLS[2]);
        addProduct("Diamond Tennis Necklace", 7200.00, "Graduated diamond tennis necklace.", 2, necklaces, true, IMAGE_URLS[2], IMAGE_URLS[3]);
        addProduct("Sapphire Teardrop Pendant", 2200.00, "Teardrop sapphire with diamond halo.", 4, necklaces, true, IMAGE_URLS[3], IMAGE_URLS[0]);
        addProduct("Heart Locket", 780.00, "Polished heart locket, engravable.", 18, necklaces, false, IMAGE_URLS[0], IMAGE_URLS[2]);
        addProduct("Cuban Link Chain", 1450.00, "Solid 14k gold Cuban link chain.", 7, necklaces, false, IMAGE_URLS[1], IMAGE_URLS[3]);
        addProduct("Minimal Bar Necklace", 520.00, "Sleek horizontal bar, adjustable chain.", 25, necklaces, false, IMAGE_URLS[0], IMAGE_URLS[3]);
        addProduct("Layered Coin Necklaces", 680.00, "Set of layered coin pendants.", 12, necklaces, false, IMAGE_URLS[1], IMAGE_URLS[2]);
        addProduct("Birthstone Pendant", 430.00, "Round birthstone bezel pendant.", 22, necklaces, false, IMAGE_URLS[2], IMAGE_URLS[0]);
        addProduct("Nameplate Necklace", 890.00, "Customizable cursive nameplate.", 14, necklaces, true, IMAGE_URLS[3], IMAGE_URLS[1]);

        // ===== EARRINGS =====
        addProduct("Diamond Stud Earrings", 1600.00, "Round brilliant diamond studs.", 16, earrings, true, IMAGE_URLS[0], IMAGE_URLS[1]);
        addProduct("Gold Hoop Earrings", 900.00, "Classic medium gold hoops.", 25, earrings, false, IMAGE_URLS[1], IMAGE_URLS[2]);
        addProduct("Pearl Drop Earrings", 1250.00, "Pearl drops on slim hooks.", 10, earrings, true, IMAGE_URLS[2], IMAGE_URLS[3]);
        addProduct("Emerald Studs", 2100.00, "Emerald studs with bezel setting.", 6, earrings, false, IMAGE_URLS[3], IMAGE_URLS[0]);
        addProduct("Sapphire Halo Studs", 2400.00, "Sapphire centers with diamond halo.", 5, earrings, true, IMAGE_URLS[0], IMAGE_URLS[2]);
        addProduct("Huggie Hoops", 520.00, "Petite huggie hoop earrings.", 28, earrings, false, IMAGE_URLS[1], IMAGE_URLS[3]);
        addProduct("Threader Earrings", 460.00, "Delicate chain threaders.", 22, earrings, false, IMAGE_URLS[0], IMAGE_URLS[3]);
        addProduct("Teardrop Dangles", 780.00, "Polished teardrop dangle earrings.", 12, earrings, false, IMAGE_URLS[1], IMAGE_URLS[2]);
        addProduct("Ear Cuff", 320.00, "No-pierce smooth ear cuff.", 30, earrings, false, IMAGE_URLS[2], IMAGE_URLS[0]);
        addProduct("Rose Gold Hoops", 980.00, "Warm rose gold hoop pair.", 15, earrings, true, IMAGE_URLS[3], IMAGE_URLS[1]);
    }

    private void addProduct(String name, Double price, String description, int stock,
                            Category category, boolean featured, String... imageUrls) {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStock(stock);
        product.setCategory(category);
        product.setFeatured(featured);

        for (String url : imageUrls) {
            ProductImage img = new ProductImage();
            img.setImageUrl(url);
            img.setProduct(product);
            product.getImages().add(img);
        }

        productRepository.save(product);
    }
}
