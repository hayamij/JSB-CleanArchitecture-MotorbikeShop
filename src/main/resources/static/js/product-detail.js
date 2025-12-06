// Product Detail Page JavaScript

let currentProduct = null;
let quantity = 1;

function getProductIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

async function loadProductDetail() {
    const productId = getProductIdFromUrl();
    
    if (!productId) {
        showAlert('Không tìm thấy mã sản phẩm', 'error');
        setTimeout(() => {
            window.location.href = 'home.html';
        }, 2000);
        return;
    }

    try {
        const response = await fetch(`/api/products/${productId}`);
        const data = await response.json();

        if (data.success) {
            currentProduct = data;
            renderProduct(data);
        } else {
            showAlert(data.errorMessage || 'Không thể tải thông tin sản phẩm', 'error');
            setTimeout(() => {
                window.location.href = 'home.html';
            }, 3000);
        }
    } catch (error) {
        console.error('Error loading product:', error);
        showAlert('Lỗi kết nối đến server', 'error');
        setTimeout(() => {
            window.location.href = 'home.html';
        }, 3000);
    } finally {
        document.getElementById('loadingIndicator').classList.add('hidden');
        document.getElementById('productContainer').classList.remove('hidden');
    }
}

function renderProduct(product) {
    const container = document.getElementById('productContainer');
    
    // Debug: Log product data
    console.log('Product data:', product);
    console.log('Image URL:', product.imageUrl);
    
    // Parse stockQuantity to number if it's a string
    const stockQty = typeof product.stockQuantity === 'string' 
        ? parseInt(product.stockQuantity.replace(/[^0-9]/g, '')) || 0
        : product.stockQuantity;
    
    // Store numeric stock quantity in currentProduct
    currentProduct.stockQuantity = stockQty;
    
    document.getElementById('breadcrumbCategory').textContent = product.categoryDisplay || 'Sản phẩm';
    document.getElementById('breadcrumbProduct').textContent = product.name;

    const isOutOfStock = stockQty === 0;
    const isLowStock = stockQty > 0 && stockQty < 10;
    
    let stockBadgeHtml = '';
    if (isOutOfStock) {
        stockBadgeHtml = '<div class="product-badge badge-out-of-stock">Hết hàng</div>';
    } else if (isLowStock) {
        stockBadgeHtml = '<div class="product-badge badge-low-stock">Sắp hết hàng</div>';
    }

    let stockStatusHtml = '';
    if (isOutOfStock) {
        stockStatusHtml = '<div class="stock-status stock-out"><span class="stock-text">Hết hàng</span></div>';
    } else if (isLowStock) {
        stockStatusHtml = `<div class="stock-status stock-low"><span class="stock-text">Chỉ còn ${stockQty} sản phẩm</span></div>`;
    } else {
        stockStatusHtml = `<div class="stock-status stock-available"><span class="stock-text">Còn ${stockQty} sản phẩm</span></div>`;
    }

    let specsHtml = '';
    if (product.specifications && product.specifications.trim() !== '' && product.specifications !== '{}') {
        specsHtml = `
            <div class="specifications-section">
                <div class="spec-title">Thông số kỹ thuật</div>
                <div class="spec-content">${product.specifications}</div>
            </div>
        `;
    }

    const hasImage = product.imageUrl && 
                     product.imageUrl.trim() !== '' && 
                     product.imageUrl !== 'null' && 
                     product.imageUrl !== '/images/no-image.jpg' &&
                     !product.imageUrl.includes('no-image');
    
    console.log('Has valid image:', hasImage);

    container.innerHTML = `
        <div class="product-container">
            <div class="product-image-section">
                <div class="main-image-container">
                    ${hasImage ? `<img src="${product.imageUrl}" alt="${product.name}" class="main-image" onerror="this.style.display='none'">` : '<div class="no-image-placeholder">📷</div>'}
                    ${stockBadgeHtml}
                </div>
            </div>

            <div class="product-details-section">
                <div class="product-category">${product.categoryDisplay || 'Sản phẩm'}</div>
                
                <h1 class="product-title">${product.name}</h1>

                <div class="product-price-section">
                    <div class="product-price">${formatCurrency(product.price)}</div>
                    ${stockStatusHtml}
                </div>

                ${product.description ? `
                <div class="product-description">
                    <div class="description-title">Mô tả sản phẩm</div>
                    <p>${product.description}</p>
                </div>
                ` : ''}

                ${specsHtml}

                <div class="quantity-selector">
                    <span class="quantity-label">Số lượng:</span>
                    <div class="quantity-controls">
                        <button class="quantity-btn" onclick="decreaseQuantity()" ${isOutOfStock ? 'disabled' : ''}>−</button>
                        <span class="quantity-display" id="quantityDisplay">1</span>
                        <button class="quantity-btn" onclick="increaseQuantity()" ${isOutOfStock ? 'disabled' : ''}>+</button>
                    </div>
                </div>

                <div class="action-buttons">
                    <button class="btn-add-cart" onclick="addToCart()" ${isOutOfStock ? 'disabled' : ''}>
                        Thêm vào giỏ hàng
                    </button>
                    <button class="btn-buy-now" onclick="buyNow()" ${isOutOfStock ? 'disabled' : ''}>
                        Mua ngay
                    </button>
                </div>
            </div>
        </div>
    `;
}

function decreaseQuantity() {
    if (quantity > 1) {
        quantity--;
        document.getElementById('quantityDisplay').textContent = quantity;
    }
}

function increaseQuantity() {
    if (!currentProduct) {
        showToast('Chưa tải được thông tin sản phẩm', 'error');
        return;
    }
    
    const maxStock = currentProduct.stockQuantity;
    console.log('Current quantity:', quantity, 'Max stock:', maxStock);
    
    if (quantity < maxStock) {
        quantity++;
        document.getElementById('quantityDisplay').textContent = quantity;
    } else {
        showToast(`Chỉ còn ${maxStock} sản phẩm trong kho`, 'error');
    }
}

async function addToCart() {
    const userId = sessionStorage.getItem('userId');

    if (!currentProduct) {
        showToast('Không tìm thấy thông tin sản phẩm', 'error');
        return;
    }

    const button = document.querySelector('.btn-add-cart');
    const originalText = button ? button.innerHTML : '';
    
    if (button) {
        button.disabled = true;
        button.innerHTML = 'Đang thêm...';
    }
    
    // Guest user - use local storage
    if (!userId) {
        try {
            addToGuestCart(currentProduct.productId, quantity);
            
            if (button) {
                button.innerHTML = '✓ Đã thêm';
                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.disabled = false;
                }, 1500);
            }
            
            // Reset quantity to 1
            quantity = 1;
            document.getElementById('quantityDisplay').textContent = quantity;
            
            updateCartBadge();
            showToast(`Đã thêm ${quantity} sản phẩm vào giỏ hàng!`, 'success');
            return;
        } catch (error) {
            console.error('Error adding to guest cart:', error);
            showToast('Lỗi khi thêm vào giỏ hàng. Vui lòng thử lại.', 'error');
            if (button) {
                button.innerHTML = originalText;
                button.disabled = false;
            }
            return;
        }
    }

    try {
        const response = await fetch('/api/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: parseInt(userId),
                productId: currentProduct.productId,
                quantity: quantity
            })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            if (button) {
                button.innerHTML = '✓ Đã thêm';
                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.disabled = false;
                }, 1500);
            }
            
            // Reset quantity to 1
            quantity = 1;
            document.getElementById('quantityDisplay').textContent = quantity;
            
            // Update cart badge
            updateCartBadge();
            
            showToast(`Đã thêm ${quantity} sản phẩm vào giỏ hàng!`, 'success');
        } else {
            showToast(data.errorMessage || 'Không thể thêm vào giỏ hàng', 'error');
            if (button) {
                button.innerHTML = originalText;
                button.disabled = false;
            }
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        showToast('Lỗi kết nối đến server. Vui lòng thử lại.', 'error');
        if (button) {
            button.innerHTML = originalText;
            button.disabled = false;
        }
    }
}

async function buyNow() {
    await addToCart();
    setTimeout(() => {
        window.location.href = 'cart.html';
    }, 1000);
}

window.onload = function() {
    // Initialize navbar and footer (no authentication required)
    initNavbar('', false);
    initFooter();
    
    // Update cart badge for both guest and logged-in users
    updateCartBadge();
    
    loadProductDetail();
};
