package com.motorbike.frameworks.config;

import com.motorbike.persistence.entity.ProductJpaEntity;
import com.motorbike.persistence.repository.ProductJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Data Initialization Configuration
 * Loads sample data into the database on startup
 * NOTE: Disabled because data is already in SQL Server database
 */
@Configuration
public class DataInitializer {
    
    // @Bean - Commented out to avoid duplicate data
    CommandLineRunner initDatabase(ProductJpaRepository repository) {
        return args -> {
            // Sample Motorcycle Products
            ProductJpaEntity honda = new ProductJpaEntity();
            honda.setName("Honda Wave RSX");
            honda.setDescription("Xe số tiết kiệm nhiên liệu, phù hợp cho di chuyển trong thành phố");
            honda.setPrice(new BigDecimal("38000000"));
            honda.setImageUrl("/images/honda-wave-rsx.jpg");
            honda.setSpecifications("{\"engine\":\"110cc\",\"fuelCapacity\":\"3.5L\",\"weight\":\"98kg\"}");
            honda.setCategory("MOTORCYCLE");
            honda.setStockQuantity(15);
            honda.setAvailable(true);
            
            ProductJpaEntity yamaha = new ProductJpaEntity();
            yamaha.setName("Yamaha Exciter 155");
            yamaha.setDescription("Xe côn tay thể thao, động cơ mạnh mẽ");
            yamaha.setPrice(new BigDecimal("47000000"));
            yamaha.setImageUrl("/images/yamaha-exciter-155.jpg");
            yamaha.setSpecifications("{\"engine\":\"155cc\",\"fuelCapacity\":\"4.6L\",\"weight\":\"118kg\"}");
            yamaha.setCategory("MOTORCYCLE");
            yamaha.setStockQuantity(10);
            yamaha.setAvailable(true);
            
            ProductJpaEntity suzuki = new ProductJpaEntity();
            suzuki.setName("Suzuki Raider 150");
            suzuki.setDescription("Xe côn tay cơ bắp, thiết kế nam tính");
            suzuki.setPrice(new BigDecimal("50000000"));
            suzuki.setImageUrl("/images/suzuki-raider-150.jpg");
            suzuki.setSpecifications("{\"engine\":\"150cc\",\"fuelCapacity\":\"4.5L\",\"weight\":\"125kg\"}");
            suzuki.setCategory("MOTORCYCLE");
            suzuki.setStockQuantity(8);
            suzuki.setAvailable(true);
            
            // Sample Accessory Products
            ProductJpaEntity helmet = new ProductJpaEntity();
            helmet.setName("Mũ bảo hiểm Royal M139");
            helmet.setDescription("Mũ bảo hiểm fullface cao cấp, tiêu chuẩn Châu Âu");
            helmet.setPrice(new BigDecimal("850000"));
            helmet.setImageUrl("/images/helmet-royal.jpg");
            helmet.setSpecifications("{\"size\":\"L, XL\",\"weight\":\"1.5kg\",\"standard\":\"ECE 22.05\"}");
            helmet.setCategory("ACCESSORY");
            helmet.setStockQuantity(50);
            helmet.setAvailable(true);
            
            ProductJpaEntity gloves = new ProductJpaEntity();
            gloves.setName("Găng tay Scoyco MC29");
            gloves.setDescription("Găng tay đi xe máy, chống trượt và bảo vệ tốt");
            gloves.setPrice(new BigDecimal("350000"));
            gloves.setImageUrl("/images/gloves-scoyco.jpg");
            gloves.setSpecifications("{\"material\":\"Leather + Fabric\",\"protection\":\"Knuckle Guard\"}");
            gloves.setCategory("ACCESSORY");
            gloves.setStockQuantity(30);
            gloves.setAvailable(true);
            
            // Save all products
            repository.save(honda);
            repository.save(yamaha);
            repository.save(suzuki);
            repository.save(helmet);
            repository.save(gloves);
            
            System.out.println("Sample data initialized: " + repository.count() + " products");
        };
    }
}
