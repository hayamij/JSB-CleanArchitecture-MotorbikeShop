# CLEAN ARCHITECTURE - MOTORBIKE SHOP

## 📊 Project Status

**Total Use Cases Implemented**: 3/7  
**Total Tests**: 51 tests  
**Test Status**: ✅ All Passing  
**Last Updated**: 2025-11-12

---

## ✅ Completed Use Cases

### Use Case 1: Xem Chi Tiết Sản Phẩm ✅
**Status**: COMPLETED - 10/10 tests passing

### Use Case 2: Đăng Nhập ✅
**Status**: COMPLETED - 18/18 tests passing

### Use Case 3: Đăng Ký Tài Khoản ✅
**Status**: COMPLETED - 23/23 tests passing

---

# Use Case 1: Xem Chi Tiết Sản Phẩm - HOÀN THÀNH ✅

## 📋 Tổng quan

**Use Case**: Get Product Detail (Xem chi tiết sản phẩm)  
**Status**: ✅ COMPLETED - All tests passing (10/10)  
**Test Coverage**: Full integration testing

---

## 🎯 Business Rules Implemented

✅ **Sản phẩm phải tồn tại trong hệ thống**
   - Kiểm tra product existence
   - Return error nếu không tìm thấy

✅ **Hiển thị đầy đủ thông tin**
   - Tên, giá, mô tả
   - Hình ảnh, specifications
   - Số lượng tồn kho

✅ **Không yêu cầu đăng nhập**
   - Guest có thể xem
   - Không cần authentication

✅ **Hiển thị thông tin loại sản phẩm (category)**
   - MOTORCYCLE → "Xe máy"
   - ACCESSORY → "Phụ kiện"

---

## 📁 Files Created (Clean Architecture)

### **LAYER 1: Domain (Entities)** ✅
Already created:
- `Product.java` - Domain entity với business logic
- `ProductCategory.java` - Enum

### **LAYER 2: Use Cases** ✅

**DTOs:**
```
business/dto/productdetail/
├── GetProductDetailInputData.java      ✅ Input DTO
└── GetProductDetailOutputData.java     ✅ Output DTO (with all product fields)
```

**Boundaries (Interfaces):**
```
business/usecase/
├── GetProductDetailInputBoundary.java  ✅ Use case interface
└── GetProductDetailOutputBoundary.java ✅ Presenter interface
```

**Repository Port:**
```
business/ports/repository/
└── ProductRepository.java              ✅ Repository interface
```

**Use Case Implementation:**
```
business/usecase/impl/
└── GetProductDetailUseCaseImpl.java    ✅ Complete implementation with:
    - Input validation
    - Product retrieval
    - Business rules enforcement
    - Error handling
```

### **LAYER 3: Adapters** ✅

**Presenters:**
```
adapters/presenters/
└── ProductDetailPresenter.java         ✅ Presentation logic:
    - Price formatting (VND)
    - Category display (Vietnamese)
    - Stock status determination
    - Color assignment
    - Error message formatting
```

**ViewModels:**
```
adapters/viewmodels/
└── ProductDetailViewModel.java         ✅ UI-ready data:
    - Formatted strings
    - Display colors
    - Error information
```

### **TEST** ✅
```
test/java/com/motorbike/business/usecase/impl/
└── GetProductDetailUseCaseImplTest.java ✅ 10 comprehensive tests
```

---

## 🧪 Test Coverage (10/10 Tests Passing)

### ✅ Success Cases
1. **testGetProductDetail_Success**
   - Product exists with full data
   - All fields populated correctly
   - Price formatted to VND
   - Category displayed in Vietnamese

2. **testGetProductDetail_AccessoryCategory**
   - ACCESSORY category → "Phụ kiện"

3. **testGetProductDetail_LowStock**
   - Stock < 5 → Shows "Sắp hết" warning

### ✅ Error Cases
4. **testGetProductDetail_ProductNotFound**
   - Product ID doesn't exist
   - Error message in Vietnamese

5. **testGetProductDetail_NullProductId**
   - Invalid input (null ID)
   - Proper error handling

### ✅ Edge Cases
6. **testGetProductDetail_OutOfStock**
   - Stock = 0
   - Status: "Hết hàng" (ORANGE)

7. **testGetProductDetail_NotAvailable**
   - Available = false
   - Status: "Không có sẵn" (RED)

8. **testGetProductDetail_NullDescription**
   - Description = null
   - Shows "No description available"

9. **testGetProductDetail_NullImageUrl**
   - Image URL = null
   - Defaults to "/images/no-image.jpg"

### ✅ Architecture Tests
10. **testGetProductDetail_PresenterCalledOnce**
    - Verifies presenter called exactly once
    - Output data captured correctly

---

## 🔄 Data Flow

```
1. Input
   └── GetProductDetailInputData { productId: Long }

2. Use Case (GetProductDetailUseCaseImpl)
   ├── Validate input (productId not null)
   ├── Fetch from repository
   ├── Check business rules:
   │   ├── Product exists?
   │   ├── Product in stock? (entity.isInStock())
   │   └── Available status
   └── Create OutputData

3. Presenter (ProductDetailPresenter)
   ├── Format price → VND currency
   ├── Format category → Vietnamese
   ├── Determine stock status & color:
   │   ├── In stock → "Còn hàng" (GREEN)
   │   ├── Out of stock → "Hết hàng" (ORANGE)
   │   └── Not available → "Không có sẵn" (RED)
   └── Update ViewModel

4. Output
   └── ProductDetailViewModel {
       - All fields formatted for display
       - Colors assigned
       - Error handling
   }
```

---

## 🎨 Presentation Logic

### Price Formatting
```java
BigDecimal 38000000 → "₫38.000.000,00"
```

### Category Display
```java
MOTORCYCLE → "Xe máy"
ACCESSORY  → "Phụ kiện"
null       → "Chưa phân loại"
```

### Stock Status
```java
inStock=true, qty>0  → "Còn hàng" (GREEN)
inStock=false, qty=0 → "Hết hàng" (ORANGE)
available=false      → "Không có sẵn" (RED)
```

### Stock Quantity Display
```java
qty = 0       → "Hết hàng"
qty < 5       → "X sản phẩm (Sắp hết)"
qty >= 5      → "X sản phẩm"
```

---

## 🏗️ Clean Architecture Principles Applied

### ✅ Dependency Rule
- Use Case **KHÔNG** phụ thuộc vào UI
- Use Case **KHÔNG** phụ thuộc vào database implementation
- Use Case chỉ phụ thuộc vào:
  - Domain entities (Product)
  - Port interfaces (ProductRepository)
  - Boundary interfaces (Input/Output)

### ✅ Separation of Concerns
- **Entity**: Business logic (isInStock, validation)
- **Use Case**: Orchestration, business flow
- **Presenter**: Formatting, display logic
- **ViewModel**: Pure data container

### ✅ Testability
- Mock repository for testing
- No framework dependencies in tests
- Fast unit/integration tests (0.212s)

### ✅ Single Responsibility
- Each class has ONE clear responsibility
- Easy to understand and maintain

---

## 📊 Test Results

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running Get Product Detail Use Case Tests
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Time elapsed: 0.212 s
[INFO] BUILD SUCCESS
```

**10/10 tests passing** ✅  
**100% success rate** ✅  
**Fast execution** (212ms) ✅

---

## 🚀 How to Run Tests

```bash
# Run all tests
mvn test

# Run only this use case test
mvn test -Dtest=GetProductDetailUseCaseImplTest

# Run specific test method
mvn test -Dtest=GetProductDetailUseCaseImplTest#testGetProductDetail_Success
```

---

## 📝 Usage Example (Integration)

```java
// Setup dependencies
ProductRepository repository = new ProductRepositoryImpl();
ProductDetailViewModel viewModel = new ProductDetailViewModel();
ProductDetailPresenter presenter = new ProductDetailPresenter(viewModel);
GetProductDetailInputBoundary useCase = 
    new GetProductDetailUseCaseImpl(presenter, repository);

// Execute use case
GetProductDetailInputData input = new GetProductDetailInputData(1L);
useCase.execute(input);

// Access formatted data from ViewModel
if (!viewModel.hasError) {
    System.out.println("Product: " + viewModel.name);
    System.out.println("Price: " + viewModel.formattedPrice);
    System.out.println("Status: " + viewModel.availabilityStatus);
} else {
    System.out.println("Error: " + viewModel.errorMessage);
}
```

---

## ✅ Checklist

- [x] Domain entities created
- [x] Business exceptions defined
- [x] DTOs created (Input/Output)
- [x] Boundaries defined (Input/Output)
- [x] Repository interface created
- [x] Use case implemented
- [x] Presenter with formatting logic
- [x] ViewModel created
- [x] All business rules implemented
- [x] Error handling complete
- [x] Edge cases handled
- [x] Tests written (10 tests)
- [x] All tests passing
- [x] Documentation complete

---

## 🎓 What's Next?

This use case serves as a **TEMPLATE** for implementing the remaining 6 use cases:

1. ✅ **Xem chi tiết sản phẩm** - DONE
2. 📝 Đăng nhập
3. 📝 Đăng ký tài khoản
4. 📝 Thêm vào giỏ hàng
5. 📝 Thanh toán
6. 📝 Xem giỏ hàng
7. 📝 Chỉnh số lượng sản phẩm trong giỏ hàng

Follow the same pattern:
- DTOs → Boundaries → Repository → Use Case → Presenter → ViewModel → Tests

---

**Document Version**: 1.0  
**Last Updated**: 2025-11-12  
**Status**: ✅ COMPLETED & TESTED

---
---

# Use Case 2: Đăng Nhập - HOÀN THÀNH ✅

## 📋 Tổng quan

**Use Case**: Login (Đăng nhập)  
**Status**: ✅ COMPLETED - All tests passing (18/18)  
**Test Coverage**: Full integration testing with all business rules

---

## 🎯 Business Rules Implemented

✅ **Email phải tồn tại trong hệ thống**
   - Kiểm tra user existence
   - Return error nếu không tìm thấy

✅ **Mật khẩu phải khớp với mật khẩu đã mã hóa**
   - Verify password (hiện tại simple comparison, sẽ dùng BCrypt)
   - Return error nếu sai mật khẩu

✅ **Sau khi đăng nhập thành công, tạo session/token**
   - Generate session token
   - Token format: SESSION_{userId}_{timestamp}

✅ **Phân biệt role: customer vs admin**
   - Display role trong UI: "Khách hàng" / "Quản trị viên"
   - Role được lưu trong session

✅ **Merge giỏ hàng guest với giỏ hàng user**
   - Nếu user chưa có giỏ → assign guest cart
   - Nếu user đã có giỏ → merge items
   - Show thông báo số items merged

✅ **Validate input data**
   - Email format validation
   - Password minimum length (6 chars)
   - Non-null/non-empty checks

✅ **Check user active status**
   - Only active users can login
   - Return error nếu account bị vô hiệu hóa

✅ **Update last login timestamp**
   - Record login time in database

---

## 📁 Files Created (Clean Architecture)

### **LAYER 1: Domain (Entities)** ✅
Already created:
- `User.java` - Domain entity với business logic
- `UserRole.java` - Enum (CUSTOMER, ADMIN, GUEST)
- `Cart.java` - Cart entity with merge logic

### **LAYER 2: Use Cases** ✅

**DTOs:**
```
business/dto/login/
├── LoginInputData.java      ✅ Input DTO (email, password, guestCartId)
└── LoginOutputData.java     ✅ Output DTO (user data + error handling)
```

**Boundaries (Interfaces):**
```
business/usecase/
├── LoginInputBoundary.java  ✅ Use case interface
└── LoginOutputBoundary.java ✅ Presenter interface
```

**Repository Ports:**
```
business/ports/repository/
├── UserRepository.java      ✅ User repository interface
└── CartRepository.java      ✅ Cart repository interface
```

**Use Case Implementation:**
```
business/usecase/impl/
└── LoginUseCaseImpl.java    ✅ Complete implementation with:
    - Email/password validation
    - User authentication
    - Password verification
    - Cart merging logic
    - Session token generation
    - Last login update
    - Error handling
```

### **LAYER 3: Adapters** ✅

**Presenters:**
```
adapters/presenters/
└── LoginPresenter.java      ✅ Presentation logic:
    - Role formatting (Vietnamese)
    - DateTime formatting
    - Error message formatting
    - Cart merge message generation
```

**ViewModels:**
```
adapters/viewmodels/
└── LoginViewModel.java      ✅ UI-ready data:
    - Formatted strings
    - Display colors
    - Error information
    - Success/failure state
```

### **TEST** ✅
```
test/java/com/motorbike/business/usecase/impl/
└── LoginUseCaseImplTest.java ✅ 18 comprehensive tests
```

---

## 🧪 Test Coverage (18/18 Tests Passing)

### ✅ Success Cases
1. **testLogin_Success**
   - Valid email + password
   - User found and authenticated
   - Session token generated
   - Last login updated

2. **testLogin_AdminRole**
   - Admin user login
   - Role displayed as "Quản trị viên"

3. **testLogin_MergeGuestCartToNewUserCart**
   - User has no cart
   - Guest cart assigned to user

4. **testLogin_MergeGuestCartToExistingUserCart**
   - User has existing cart
   - Guest cart items merged
   - Item count tracked

5. **testLogin_NoGuestCart**
   - Login without guest cart
   - No cart operations performed

6. **testLogin_SessionTokenGenerated**
   - Session token created
   - Token format validated

7. **testLogin_UpdateLastLogin**
   - Last login timestamp updated

8. **testLogin_LastLoginFormatting**
   - DateTime formatted correctly (dd/MM/yyyy HH:mm:ss)

### ✅ Error Cases
9. **testLogin_EmailNotFound**
   - Email doesn't exist
   - Error: "EMAIL_NOT_FOUND"

10. **testLogin_InvalidPassword**
    - Wrong password
    - Error: "INVALID_PASSWORD"

11. **testLogin_UserInactive**
    - User account deactivated
    - Error: "USER_INACTIVE"

12. **testLogin_NullEmail**
    - Email is null
    - Error: "EMPTY_EMAIL"

13. **testLogin_EmptyEmail**
    - Email is empty string
    - Error: "EMPTY_EMAIL"

14. **testLogin_InvalidEmailFormat**
    - Email doesn't match pattern
    - Error: "INVALID_EMAIL_FORMAT"

15. **testLogin_NullPassword**
    - Password is null
    - Error: "EMPTY_PASSWORD"

16. **testLogin_EmptyPassword**
    - Password is empty string
    - Error: "EMPTY_PASSWORD"

17. **testLogin_PasswordTooShort**
    - Password < 6 characters
    - Error: "PASSWORD_TOO_SHORT"

### ✅ Architecture Tests
18. **testLogin_PresenterCalledOnce**
    - Verifies presenter called exactly once
    - Output data captured correctly

---

## 🔄 Data Flow

```
1. Input
   └── LoginInputData { 
       email: String, 
       password: String, 
       guestCartId: Long (optional) 
   }

2. Use Case (LoginUseCaseImpl)
   ├── Validate input (email format, password length)
   ├── Find user by email
   ├── Verify password
   ├── Check user active status
   ├── Merge guest cart (if provided):
   │   ├── User has no cart → assign guest cart
   │   └── User has cart → merge items
   ├── Update last login timestamp
   ├── Generate session token
   └── Create OutputData

3. Presenter (LoginPresenter)
   ├── Format role → Vietnamese ("Khách hàng" / "Quản trị viên")
   ├── Format datetime → dd/MM/yyyy HH:mm:ss
   ├── Generate cart merge message
   ├── Format error messages
   └── Update ViewModel

4. Output
   └── LoginViewModel {
       - User data (formatted)
       - Session token
       - Cart merge info
       - Success/error state
   }
```

---

## 🎨 Presentation Logic

### Role Display
```java
CUSTOMER → "Khách hàng"
ADMIN    → "Quản trị viên"
GUEST    → "Khách"
null     → "Không xác định"
```

### DateTime Formatting
```java
LocalDateTime → "12/11/2025 21:58:16"
null → ""
```

### Cart Merge Messages
```java
Merged 3 items → "Đã thêm 3 sản phẩm từ giỏ hàng tạm vào giỏ hàng của bạn"
No merge → null
```

### Error Messages (User-Friendly)
```java
EMAIL_NOT_FOUND → "Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại..."
INVALID_PASSWORD → "Mật khẩu không chính xác. Vui lòng thử lại."
USER_INACTIVE → "Tài khoản đã bị vô hiệu hóa..."
EMPTY_EMAIL → "Vui lòng nhập địa chỉ email."
INVALID_EMAIL_FORMAT → "Định dạng email không hợp lệ..."
PASSWORD_TOO_SHORT → "Mật khẩu phải có ít nhất 6 ký tự."
```

---

## 🏗️ Clean Architecture Principles Applied

### ✅ Dependency Rule
- Use Case **KHÔNG** phụ thuộc vào UI
- Use Case **KHÔNG** phụ thuộc vào database implementation
- Use Case chỉ phụ thuộc vào:
  - Domain entities (User, Cart)
  - Port interfaces (UserRepository, CartRepository)
  - Boundary interfaces (Input/Output)

### ✅ Separation of Concerns
- **Entity**: Business logic (validation, canLogin)
- **Use Case**: Authentication flow, cart merging
- **Presenter**: Formatting, display logic
- **ViewModel**: Pure data container

### ✅ Testability
- Mock repositories for testing
- No framework dependencies in tests
- Fast unit/integration tests (6.180s)

### ✅ Single Responsibility
- Each class has ONE clear responsibility
- Easy to understand and maintain

---

## 📊 Test Results

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running Login Use Case Tests
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] Time elapsed: 6.180 s
[INFO] 
[INFO] Running Get Product Detail Use Case Tests
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Time elapsed: 0.340 s
[INFO]
[INFO] Results:
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**28/28 tests passing** ✅  
**100% success rate** ✅  
**Fast execution** ✅

---

## 🚀 How to Run Tests

```bash
# Run all tests
mvn test

# Run only Login use case test
mvn test -Dtest=LoginUseCaseImplTest

# Run specific test method
mvn test -Dtest=LoginUseCaseImplTest#testLogin_Success
```

---

## 📝 Usage Example (Integration)

```java
// Setup dependencies
UserRepository userRepository = new UserRepositoryImpl();
CartRepository cartRepository = new CartRepositoryImpl();
LoginViewModel viewModel = new LoginViewModel();
LoginPresenter presenter = new LoginPresenter(viewModel);
LoginInputBoundary useCase = 
    new LoginUseCaseImpl(presenter, userRepository, cartRepository);

// Execute use case
LoginInputData input = new LoginInputData(
    "user@example.com", 
    "password123",
    guestCartId // optional
);
useCase.execute(input);

// Access formatted data from ViewModel
if (viewModel.success) {
    System.out.println("Welcome: " + viewModel.username);
    System.out.println("Role: " + viewModel.roleDisplay);
    System.out.println("Token: " + viewModel.sessionToken);
    
    if (viewModel.cartMerged) {
        System.out.println(viewModel.cartMergeMessage);
    }
} else {
    System.out.println("Error: " + viewModel.errorMessage);
}
```

---

## 🔐 Security Notes

### Current Implementation (For Testing)
- Password comparison: Simple string comparison
- Session token: Simple format with timestamp

### TODO for Production
```java
// Password hashing (implement later)
// import org.springframework.security.crypto.bcrypt.BCrypt;
// return BCrypt.checkpw(plainPassword, hashedPassword);

// JWT token (implement later)
// import io.jsonwebtoken.Jwts;
// return Jwts.builder()
//     .setSubject(user.getId().toString())
//     .signWith(secretKey)
//     .compact();
```

---

## ✅ Checklist

- [x] Domain entities enhanced (User, Cart)
- [x] Business exceptions defined
- [x] DTOs created (Input/Output)
- [x] Boundaries defined (Input/Output)
- [x] Repository interfaces created (User, Cart)
- [x] Use case implemented
- [x] Presenter with formatting logic
- [x] ViewModel created
- [x] All business rules implemented
- [x] Error handling complete
- [x] Edge cases handled
- [x] Tests written (18 tests)
- [x] All tests passing
- [x] Cart merge logic implemented
- [x] Session token generation
- [x] Documentation complete

---

## 🎓 What's Next?

Remaining use cases to implement:

1. ✅ **Xem chi tiết sản phẩm** - DONE (10 tests)
2. ✅ **Đăng nhập** - DONE (18 tests)
3. ✅ **Đăng ký tài khoản** - DONE (23 tests)
4. 📝 Thêm vào giỏ hàng
5. 📝 Thanh toán
6. 📝 Xem giỏ hàng
7. 📝 Chỉnh số lượng sản phẩm trong giỏ hàng

**Progress**: 3/7 use cases completed (42.9%)

---

**Document Version**: 2.0  
**Last Updated**: 2025-11-12  
**Status**: ✅ COMPLETED & TESTED

---
---

# Use Case 3: Đăng Ký Tài Khoản - HOÀN THÀNH ✅

## 📋 Tổng quan

**Use Case**: Register (Đăng ký tài khoản)  
**Status**: ✅ COMPLETED - All tests passing (23/23)  
**Test Coverage**: Full integration testing with comprehensive validation

---

## 🎯 Business Rules Implemented

✅ **Email phải unique (không trùng với tài khoản khác)**
   - Check email existence in database
   - Return error nếu email đã tồn tại

✅ **Email phải đúng định dạng**
   - Validate email pattern
   - Must match standard email format

✅ **Mật khẩu phải đáp ứng yêu cầu bảo mật**
   - Minimum length: 6 characters
   - Will be hashed before storage

✅ **Mật khẩu phải được mã hóa trước khi lưu vào database**
   - Hash password (current: simple prefix, TODO: BCrypt)
   - Never store plain text passwords

✅ **Mặc định role là "customer"**
   - New users automatically assigned CUSTOMER role
   - Admin must be created separately

✅ **Các thông tin bắt buộc**
   - Email (required, unique, valid format)
   - Username (required, 3-50 chars)
   - Password (required, min 6 chars)
   - Confirm Password (required, must match)
   - Phone number (required, valid format)

✅ **Password và confirmPassword phải khớp nhau**
   - Verify password confirmation
   - Return error nếu không khớp

---

## 📁 Files Created (Clean Architecture)

### **LAYER 1: Domain (Entities)** ✅
Already created:
- `User.java` - Domain entity với validation logic
- `UserRole.java` - Enum (CUSTOMER, ADMIN, GUEST)

### **LAYER 2: Use Cases** ✅

**DTOs:**
```
business/dto/register/
├── RegisterInputData.java      ✅ Input DTO (email, username, password, confirmPassword, phone)
└── RegisterOutputData.java     ✅ Output DTO (user data or error)
```

**Boundaries (Interfaces):**
```
business/usecase/
├── RegisterInputBoundary.java  ✅ Use case interface
└── RegisterOutputBoundary.java ✅ Presenter interface
```

**Repository Port:**
```
business/ports/repository/
└── UserRepository.java         ✅ Already created (used by Login)
```

**Use Case Implementation:**
```
business/usecase/impl/
└── RegisterUseCaseImpl.java    ✅ Complete implementation with:
    - Comprehensive input validation
    - Email uniqueness check
    - Password confirmation
    - Password hashing
    - Default role assignment
    - Error handling
```

### **LAYER 3: Adapters** ✅

**Presenters:**
```
adapters/presenters/
└── RegisterPresenter.java      ✅ Presentation logic:
    - Role formatting (Vietnamese)
    - DateTime formatting
    - Error message formatting
    - Field-specific error mapping
    - Redirect URL assignment
```

**ViewModels:**
```
adapters/viewmodels/
└── RegisterViewModel.java      ✅ UI-ready data:
    - Formatted strings
    - Display colors
    - Field-specific errors
    - Redirect information
```

### **TEST** ✅
```
test/java/com/motorbike/business/usecase/impl/
└── RegisterUseCaseImplTest.java ✅ 23 comprehensive tests
```

---

## 🧪 Test Coverage (23/23 Tests Passing)

### ✅ Success Cases
1. **testRegister_Success**
   - Valid registration data
   - User created successfully
   - Password hashed
   - Default role assigned

2. **testRegister_PasswordHashing**
   - Password not stored as plain text
   - Hashed with prefix "HASHED_"

3. **testRegister_DefaultRole**
   - New user role = CUSTOMER
   - Displayed as "Khách hàng"

4. **testRegister_DateTimeFormatting**
   - Registration time formatted correctly
   - Format: dd/MM/yyyy HH:mm:ss

5. **testRegister_RedirectUrl**
   - Redirect to /login after success
   - Ready for user to login

### ✅ Email Validation Errors
6. **testRegister_NullEmail**
   - Error: "EMPTY_EMAIL"
   
7. **testRegister_EmptyEmail**
   - Error: "EMPTY_EMAIL"

8. **testRegister_InvalidEmailFormat**
   - Error: "INVALID_EMAIL_FORMAT"

9. **testRegister_EmailAlreadyExists**
   - Error: "EMAIL_ALREADY_EXISTS"
   - Email uniqueness enforced

### ✅ Username Validation Errors
10. **testRegister_NullUsername**
    - Error: "EMPTY_USERNAME"

11. **testRegister_EmptyUsername**
    - Error: "EMPTY_USERNAME"

12. **testRegister_UsernameTooShort**
    - Username < 3 chars
    - Error: "USERNAME_TOO_SHORT"

13. **testRegister_UsernameTooLong**
    - Username > 50 chars
    - Error: "USERNAME_TOO_LONG"

### ✅ Password Validation Errors
14. **testRegister_NullPassword**
    - Error: "EMPTY_PASSWORD"

15. **testRegister_EmptyPassword**
    - Error: "EMPTY_PASSWORD"

16. **testRegister_PasswordTooShort**
    - Password < 6 chars
    - Error: "PASSWORD_TOO_SHORT"

17. **testRegister_PasswordMismatch**
    - Password ≠ confirmPassword
    - Error: "PASSWORD_MISMATCH"

18. **testRegister_NullConfirmPassword**
    - Error: "EMPTY_CONFIRM_PASSWORD"

19. **testRegister_EmptyConfirmPassword**
    - Error: "EMPTY_CONFIRM_PASSWORD"

### ✅ Phone Validation Errors
20. **testRegister_NullPhone**
    - Error: "EMPTY_PHONE"

21. **testRegister_EmptyPhone**
    - Error: "EMPTY_PHONE"

22. **testRegister_InvalidPhoneFormat**
    - Invalid phone format
    - Error: "INVALID_PHONE_FORMAT"

### ✅ Architecture Tests
23. **testRegister_PresenterCalledOnce**
    - Verifies presenter called exactly once
    - Output data captured correctly

---

## 🔄 Data Flow

```
1. Input
   └── RegisterInputData { 
       email: String,
       username: String,
       password: String,
       confirmPassword: String,
       phoneNumber: String,
       address: String (optional)
   }

2. Use Case (RegisterUseCaseImpl)
   ├── Validate all input fields
   ├── Check password confirmation match
   ├── Check email uniqueness (repository)
   ├── Hash password (security)
   ├── Create User entity with default CUSTOMER role
   ├── Save to database (repository)
   └── Create OutputData

3. Presenter (RegisterPresenter)
   ├── Format role → Vietnamese
   ├── Format datetime → dd/MM/yyyy HH:mm:ss
   ├── Map errors to specific fields
   ├── Set redirect URL
   └── Update ViewModel

4. Output
   └── RegisterViewModel {
       - User data (formatted)
       - Field-specific errors
       - Redirect URL
       - Success/error state
   }
```

---

## 🎨 Presentation Logic

### Role Display
```java
CUSTOMER → "Khách hàng" (default for new users)
ADMIN    → "Quản trị viên"
null     → "Không xác định"
```

### DateTime Formatting
```java
LocalDateTime → "12/11/2025 22:06:25"
null → ""
```

### Field Error Mapping
```java
Email errors → viewModel.emailError
Username errors → viewModel.usernameError
Password errors → viewModel.passwordError
Phone errors → viewModel.phoneError
```

### Redirect URLs
```java
Success without auto-login → "/login"
Success with auto-login → "/home"
```

### Error Messages (User-Friendly)
```java
EMAIL_ALREADY_EXISTS → "Email này đã được đăng ký. Vui lòng sử dụng email khác..."
PASSWORD_MISMATCH → "Mật khẩu xác nhận không khớp. Vui lòng nhập lại."
EMPTY_EMAIL → "Vui lòng nhập địa chỉ email."
INVALID_EMAIL_FORMAT → "Định dạng email không hợp lệ..."
USERNAME_TOO_SHORT → "Tên người dùng phải có ít nhất 3 ký tự."
PASSWORD_TOO_SHORT → "Mật khẩu phải có ít nhất 6 ký tự."
INVALID_PHONE_FORMAT → "Số điện thoại không hợp lệ..."
```

---

## 🏗️ Clean Architecture Principles Applied

### ✅ Dependency Rule
- Use Case **KHÔNG** phụ thuộc vào UI
- Use Case **KHÔNG** phụ thuộc vào database implementation
- Use Case chỉ phụ thuộc vào:
  - Domain entities (User)
  - Port interfaces (UserRepository)
  - Boundary interfaces (Input/Output)

### ✅ Separation of Concerns
- **Entity**: Validation rules (email, username, password, phone)
- **Use Case**: Registration flow, email uniqueness, password hashing
- **Presenter**: Formatting, field error mapping
- **ViewModel**: Pure data container

### ✅ Testability
- Mock repositories for testing
- No framework dependencies in tests
- Fast unit/integration tests (4.933s for 23 tests)

### ✅ Single Responsibility
- Each class has ONE clear responsibility
- Easy to understand and maintain

---

## 📊 Test Results

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running Register Use Case Tests
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
[INFO] Time elapsed: 4.933 s
[INFO] 
[INFO] Running Login Use Case Tests
[INFO] Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
[INFO] Time elapsed: 4.244 s
[INFO]
[INFO] Running Get Product Detail Use Case Tests
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] Time elapsed: 0.603 s
[INFO]
[INFO] Results:
[INFO] Tests run: 51, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**51/51 tests passing** ✅  
**100% success rate** ✅  
**Fast execution** ✅

---

## 🚀 How to Run Tests

```bash
# Run all tests
mvn test

# Run only Register use case test
mvn test -Dtest=RegisterUseCaseImplTest

# Run specific test method
mvn test -Dtest=RegisterUseCaseImplTest#testRegister_Success
```

---

## 📝 Usage Example (Integration)

```java
// Setup dependencies
UserRepository userRepository = new UserRepositoryImpl();
RegisterViewModel viewModel = new RegisterViewModel();
RegisterPresenter presenter = new RegisterPresenter(viewModel);
RegisterInputBoundary useCase = 
    new RegisterUseCaseImpl(presenter, userRepository);

// Execute use case
RegisterInputData input = new RegisterInputData(
    "newuser@example.com",
    "John Doe",
    "password123",
    "password123",
    "0912345678"
);
useCase.execute(input);

// Access formatted data from ViewModel
if (viewModel.success) {
    System.out.println("Registration successful!");
    System.out.println("User: " + viewModel.username);
    System.out.println("Role: " + viewModel.roleDisplay);
    System.out.println("Redirect to: " + viewModel.redirectUrl);
} else {
    System.out.println("Error: " + viewModel.errorMessage);
    
    // Display field-specific errors
    if (viewModel.emailError != null) {
        System.out.println("Email: " + viewModel.emailError);
    }
    if (viewModel.passwordError != null) {
        System.out.println("Password: " + viewModel.passwordError);
    }
}
```

---

## 🔐 Security Notes

### Current Implementation (For Testing)
- Password hashing: Simple prefix "HASHED_"
- Email uniqueness: Checked via repository

### TODO for Production
```java
// Password hashing with BCrypt (implement later)
// import org.springframework.security.crypto.bcrypt.BCrypt;
// return BCrypt.hashpw(plainPassword, BCrypt.gensalt());

// Email verification (implement later)
// Send verification email with token
// User must verify email before login

// Rate limiting (implement later)
// Prevent brute force registration attempts
```

---

## ✅ Checklist

- [x] Domain entities validated (User)
- [x] Business exceptions used
- [x] DTOs created (Input/Output)
- [x] Boundaries defined (Input/Output)
- [x] Repository interface reused (UserRepository)
- [x] Use case implemented
- [x] Presenter with formatting logic
- [x] ViewModel created
- [x] All business rules implemented
- [x] Error handling complete
- [x] Field-specific error mapping
- [x] Edge cases handled
- [x] Tests written (23 tests)
- [x] All tests passing
- [x] Password hashing implemented
- [x] Email uniqueness enforced
- [x] Documentation complete

---

## 🎓 What's Next?

Remaining use cases to implement:

1. ✅ **Xem chi tiết sản phẩm** - DONE (10 tests)
2. ✅ **Đăng nhập** - DONE (18 tests)
3. ✅ **Đăng ký tài khoản** - DONE (23 tests)
4. 📝 Thêm vào giỏ hàng
5. 📝 Thanh toán
6. 📝 Xem giỏ hàng
7. 📝 Chỉnh số lượng sản phẩm trong giỏ hàng

**Progress**: 3/7 use cases completed (42.9%)

---

**Document Version**: 3.0  
**Last Updated**: 2025-11-12  
**Status**: ✅ COMPLETED & TESTED
