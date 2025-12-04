// Home Page JavaScript

let products = [];

function checkAuth() {
    const userId = sessionStorage.getItem('userId');
    const username = sessionStorage.getItem('username');

    if (!userId) {
        window.location.href = 'login.html';
        return;
    }

    document.getElementById('userName').textContent = username || 'User';
}

async function loadCartInfo() {
    const userId = sessionStorage.getItem('userId');
    if (!userId) return;

    try {
        const response = await fetch(`/api/cart/${userId}`);
        const data = await response.json();

        if (data.success) {
            const cartBadge = document.getElementById('cartItems');
            if (cartBadge) {
                cartBadge.textContent = data.totalItems || 0;
            }
        }
    } catch (error) {
        console.error('Error loading cart info:', error);
    }
}

async function loadProducts() {
    try {
        const response = await fetch('/api/products');
        const data = await response.json();
        
        products = data.map(product => {
            let categoryDisplay = product.category;
            
            if (product.category === 'XE_MAY') {
                categoryDisplay = 'Xe máy';
            } else if (product.category === 'PHU_KIEN') {
                categoryDisplay = 'Phụ kiện';
            }
            
            return {
                id: product.id,
                name: product.name,
                description: product.description || '',
                price: product.price,
                category: categoryDisplay,
                stock: product.stock,
                imageUrl: product.imageUrl
            };
        });
        
        renderProducts();
    } catch (error) {
        console.error('Error loading products:', error);
        alert('Không thể tải danh sách sản phẩm. Vui lòng thử lại sau.');
    }
}

function renderProducts() {
    const productsList = document.getElementById('productsList');
    
    if (products.length === 0) {
        productsList.innerHTML = '<div style="text-align: center; padding: 40px; color: #7a7a7a; grid-column: 1 / -1;">Không có sản phẩm nào</div>';
        return;
    }
    
    let html = '';
    products.forEach(product => {
        let stockClass = '';
        let stockText = `Còn ${product.stock} sản phẩm`;
        
        if (product.stock === 0) {
            stockClass = 'stock-out';
            stockText = 'Hết hàng';
        } else if (product.stock < 10) {
            stockClass = 'stock-low';
            stockText = `Chỉ còn ${product.stock} sản phẩm`;
        }

        const hasValidImage = product.imageUrl && product.imageUrl.trim() !== '' && product.imageUrl !== '/images/no-image.jpg' && product.imageUrl !== 'null';
        
        html += `
            <div class="product-card">
                <div class="product-image-container" onclick="viewProduct(${product.id})">
                    ${hasValidImage ? `<img src="${product.imageUrl}" alt="${product.name}" class="product-image">` : ''}
                    ${product.stock < 10 && product.stock > 0 ? '<div class="product-badge">Sắp hết</div>' : ''}
                    ${product.stock === 0 ? '<div class="product-badge" style="background: #f8d7da; color: #721c24;">Hết hàng</div>' : ''}
                </div>
                <div class="product-info">
                    <div class="product-category">${product.category}</div>
                    <div class="product-name" onclick="viewProduct(${product.id})" style="cursor: pointer;">${product.name}</div>
                    <div class="product-description">${product.description}</div>
                    <div class="product-footer">
                        <div>
                            <div class="product-price">${formatCurrency(product.price)}</div>
                            <div class="product-stock ${stockClass}">${stockText}</div>
                        </div>
                        <button class="btn-add-cart" onclick="addToCart(event, ${product.id})" ${product.stock === 0 ? 'disabled' : ''}>
                            ${product.stock === 0 ? 'Hết hàng' : 'Thêm'}
                        </button>
                    </div>
                </div>
            </div>
        `;
    });

    productsList.innerHTML = html;
}

function viewProduct(productId) {
    window.location.href = `product-detail.html?id=${productId}`;
}

async function addToCart(event, productId) {
    if (event) {
        event.preventDefault();
        event.stopPropagation();
    }
    
    const userId = sessionStorage.getItem('userId');
    if (!userId) {
        if (confirm('Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng. Chuyển đến trang đăng nhập?')) {
            window.location.href = 'login.html';
        }
        return;
    }

    const button = event ? event.target : null;
    const originalText = button ? button.innerHTML : '';
    
    if (button) {
        button.disabled = true;
        button.innerHTML = 'Đang thêm...';
    }

    try {
        const response = await fetch('/api/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: parseInt(userId),
                productId: productId,
                quantity: 1
            })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            if (button) {
                button.innerHTML = 'Đã thêm';
                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.disabled = false;
                }, 1500);
            }
            await loadCartInfo();
            showToast('Đã thêm sản phẩm vào giỏ hàng!', 'success');
        } else {
            alert(data.errorMessage || 'Không thể thêm vào giỏ hàng');
            if (button) {
                button.innerHTML = originalText;
                button.disabled = false;
            }
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        alert('Lỗi kết nối đến server. Vui lòng thử lại.');
        if (button) {
            button.innerHTML = originalText;
            button.disabled = false;
        }
    }
}

window.onload = function() {
    checkAuth();
    loadCartInfo();
    loadProducts();
};
