package com.motorbike.frameworks.config;

import com.motorbike.persistence.entity.ProductJpaEntity;
import com.motorbike.persistence.entity.UserJpaEntity;
import com.motorbike.persistence.repository.ProductJpaRepository;
import com.motorbike.persistence.repository.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Data Initialization Configuration
 * Loads sample data into the database on startup
 * NOTE: Enable/disable @Bean as needed
 */
@Configuration
public class DataInitializer {
    
    @Bean
    CommandLineRunner initDatabase(ProductJpaRepository productRepository, 
                                   UserJpaRepository userRepository) {
        return args -> {
            // Check if data already exists
            if (productRepository.count() > 0) {
                System.out.println("Sample data already exists. Skipping initialization.");
                System.out.println("Products: " + productRepository.count());
                System.out.println("Users: " + userRepository.count());
                return;
            }
            
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
            productRepository.save(honda);
            productRepository.save(yamaha);
            productRepository.save(suzuki);
            productRepository.save(helmet);
            productRepository.save(gloves);
            
            System.out.println("Sample data initialized: " + productRepository.count() + " products");
            
            // Sample Users
            // Note: In production, passwords should be hashed with BCrypt
            UserJpaEntity customer = new UserJpaEntity();
            customer.setEmail("customer@motorbike.com");
            customer.setUsername("customer1");
            customer.setPassword("password123"); // In production: BCrypt.hashpw("password123", BCrypt.gensalt())
            customer.setPhoneNumber("0901234567");
            customer.setRole("CUSTOMER");
            customer.setActive(true);
            
            UserJpaEntity admin = new UserJpaEntity();
            admin.setEmail("admin@motorbike.com");
            admin.setUsername("admin");
            admin.setPassword("admin123"); // In production: BCrypt.hashpw("admin123", BCrypt.gensalt())
            admin.setPhoneNumber("0909999999");
            admin.setRole("ADMIN");
            admin.setActive(true);
            
            UserJpaEntity customer2 = new UserJpaEntity();
            customer2.setEmail("john.doe@example.com");
            customer2.setUsername("johndoe");
            customer2.setPassword("john123"); // In production: BCrypt.hashpw("john123", BCrypt.gensalt())
            customer2.setPhoneNumber("0912345678");
            customer2.setRole("CUSTOMER");
            customer2.setActive(true);
            
            // Save all users
            userRepository.save(customer);
            userRepository.save(admin);
            userRepository.save(customer2);
            
            System.out.println("Sample users initialized: " + userRepository.count() + " users");
        };
    }
}
