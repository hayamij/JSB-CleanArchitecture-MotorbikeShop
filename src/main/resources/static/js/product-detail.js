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
    
    document.getElementById('breadcrumbCategory').textContent = product.categoryDisplay || 'Sản phẩm';
    document.getElementById('breadcrumbProduct').textContent = product.name;

    const isOutOfStock = product.stockQuantity === 0;
    const isLowStock = product.stockQuantity > 0 && product.stockQuantity < 10;
    
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
        stockStatusHtml = `<div class="stock-status stock-low"><span class="stock-text">Chỉ còn ${product.stockQuantity} sản phẩm</span></div>`;
    } else {
        stockStatusHtml = `<div class="stock-status stock-available"><span class="stock-text">Còn ${product.stockQuantity} sản phẩm</span></div>`;
    }

    let specsHtml = '';
    if (product.specifications && Object.keys(product.specifications).length > 0) {
        specsHtml = '<div class="specifications-section"><div class="spec-title">Thông số kỹ thuật</div><div class="spec-grid">';
        for (const [key, value] of Object.entries(product.specifications)) {
            specsHtml += `
                <div class="spec-item">
                    <div class="spec-label">${key}</div>
                    <div class="spec-value">${value}</div>
                </div>
            `;
        }
        specsHtml += '</div></div>';
    }

    const hasImage = product.imageUrl && product.imageUrl.trim() !== '' && product.imageUrl !== '/images/no-image.jpg' && product.imageUrl !== 'null';

    container.innerHTML = `
        <div class="product-container">
            <div class="product-image-section">
                <div class="main-image-container">
                    ${hasImage ? `<img src="${product.imageUrl}" alt="${product.name}" class="main-image">` : ''}
                    ${stockBadgeHtml}
                </div>
            </div>

            <div class="product-details-section">
                <div class="product-category">${product.categoryDisplay || 'Sản phẩm'}</div>
                
                <h1 class="product-title">${product.name}</h1>

                <div class="product-price-section">
                    <div class="product-price">${product.formattedPrice || formatCurrency(product.price)}</div>
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
    if (currentProduct && quantity < currentProduct.stockQuantity) {
        quantity++;
        document.getElementById('quantityDisplay').textContent = quantity;
    } else if (currentProduct) {
        showAlert('Không đủ hàng trong kho', 'error');
    }
}

async function addToCart() {
    const userId = sessionStorage.getItem('userId');
    
    if (!userId) {
        if (confirm('Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng. Chuyển đến trang đăng nhập?')) {
            window.location.href = 'login.html';
        }
        return;
    }

    if (!currentProduct) {
        showAlert('Không tìm thấy thông tin sản phẩm', 'error');
        return;
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
            showAlert(`Đã thêm ${quantity} sản phẩm vào giỏ hàng!`, 'success');
            
            quantity = 1;
            document.getElementById('quantityDisplay').textContent = quantity;
            
            setTimeout(() => {
                if (confirm('Xem giỏ hàng ngay?')) {
                    window.location.href = 'cart.html';
                }
            }, 1000);
        } else {
            showAlert(data.errorMessage || 'Không thể thêm vào giỏ hàng', 'error');
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        showAlert('Lỗi kết nối đến server', 'error');
    }
}

async function buyNow() {
    await addToCart();
    setTimeout(() => {
        window.location.href = 'cart.html';
    }, 1000);
}

window.onload = function() {
    loadProductDetail();
};
