// Home Page JavaScript

let products = [];
let allProducts = [];
let currentCategory = 'all';
let currentPage = 1;
const itemsPerPage = 12; // 3x4 grid

async function loadProducts() {
    try {
        const response = await fetch('/api/products');
        const data = await response.json();
        
        console.log('Loaded products from API:', data);
        
        allProducts = data.map(product => {
            return {
                id: product.id,
                name: product.name,
                description: product.description || '',
                price: product.price,
                category: product.category, // Keep original category
                categoryDisplay: product.category === 'XE_MAY' ? 'Xe máy' : 'Phụ kiện',
                stock: product.stock,
                imageUrl: product.imageUrl,
                createdDate: product.createdDate, // Ngày tạo từ backend
                // Motorbike specific
                brand: product.brand,
                model: product.model,
                color: product.color,
                engineCapacity: product.engineCapacity,
                year: product.year,
                // Accessory specific
                type: product.type,
                material: product.material,
                size: product.size
            };
        });
        
        products = [...allProducts];
        currentPage = 1;
        console.log('Total products loaded:', products.length);
        renderProducts();
        renderPagination();
    } catch (error) {
        console.error('Error loading products:', error);
        alert('Không thể tải danh sách sản phẩm. Vui lòng thử lại sau.');
    }
}

function filterByCategory(category) {
    currentCategory = category;
    currentPage = 1;
    
    // Update tab active state
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-category="${category}"]`).classList.add('active');
    
    // Filter products by category
    if (category === 'all') {
        products = [...allProducts];
    } else {
        products = allProducts.filter(p => p.category === category);
    }
    
    // Apply search if exists
    const searchInput = document.getElementById('searchInput');
    const searchTerm = searchInput ? searchInput.value.trim().toLowerCase() : '';
    if (searchTerm) {
        products = products.filter(p => 
            p.name.toLowerCase().includes(searchTerm) ||
            p.description.toLowerCase().includes(searchTerm)
        );
    }
    
    // Apply filters and sort
    applyFiltersAndSort();
}

function searchProducts() {
    currentPage = 1;
    const searchTerm = document.getElementById('searchInput').value.trim().toLowerCase();
    
    // Start with category filter
    let filtered = currentCategory === 'all' ? [...allProducts] : allProducts.filter(p => p.category === currentCategory);
    
    // Apply search
    if (searchTerm) {
        filtered = filtered.filter(p => 
            p.name.toLowerCase().includes(searchTerm) ||
            p.description.toLowerCase().includes(searchTerm)
        );
    }
    
    products = filtered;
    
    // Apply filters and sort
    applyFiltersAndSort();
}

function applySortAndFilter() {
    applyFiltersAndSort();
}

function applyFiltersAndSort() {
    currentPage = 1;
    
    // Start with current products (already filtered by category and search)
    let filtered = [...products];
    
    // Apply price filter
    const minPriceInput = document.getElementById('minPriceFilter');
    const maxPriceInput = document.getElementById('maxPriceFilter');
    
    if (minPriceInput && maxPriceInput) {
        const minPrice = parseFloat(minPriceInput.value) || 0;
        const maxPrice = parseFloat(maxPriceInput.value) || Infinity;
        
        if (minPrice > 0 || maxPrice < Infinity) {
            filtered = filtered.filter(p => {
                const price = parseFloat(p.price);
                return price >= minPrice && price <= maxPrice;
            });
        }
    }
    
    // Apply stock filter
    const inStockCheckbox = document.getElementById('inStockOnly');
    if (inStockCheckbox && inStockCheckbox.checked) {
        filtered = filtered.filter(p => p.stock > 0);
    }
    
    // Apply sorting
    const sortBySelect = document.getElementById('sortBy');
    const sortBy = sortBySelect ? sortBySelect.value : 'default';
    
    switch(sortBy) {
        case 'name-asc':
            filtered.sort((a, b) => a.name.localeCompare(b.name, 'vi'));
            break;
        case 'name-desc':
            filtered.sort((a, b) => b.name.localeCompare(a.name, 'vi'));
            break;
        case 'price-asc':
            filtered.sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
            break;
        case 'price-desc':
            filtered.sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
            break;
        case 'stock-asc':
            filtered.sort((a, b) => a.stock - b.stock);
            break;
        case 'stock-desc':
            filtered.sort((a, b) => b.stock - a.stock);
            break;
        case 'date-desc':
            filtered.sort((a, b) => {
                const dateA = new Date(a.createdDate);
                const dateB = new Date(b.createdDate);
                return dateB - dateA;
            });
            break;
        case 'date-asc':
            filtered.sort((a, b) => {
                const dateA = new Date(a.createdDate);
                const dateB = new Date(b.createdDate);
                return dateA - dateB;
            });
            break;
    }
    
    products = filtered;
    renderProducts();
    renderPagination();
}

function renderProducts() {
    const productsList = document.getElementById('productsList');
    
    console.log('Rendering products. Total:', products.length, 'Current page:', currentPage);
    
    if (products.length === 0) {
        productsList.innerHTML = '<div style="text-align: center; padding: 40px; color: #7a7a7a; grid-column: 1 / -1;">Không có sản phẩm nào</div>';
        return;
    }
    
    // Calculate pagination
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedProducts = products.slice(startIndex, endIndex);
    
    console.log('Displaying products from', startIndex, 'to', endIndex, ':', paginatedProducts.length, 'products');
    
    let html = '';
    paginatedProducts.forEach(product => {
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
                    <div class="product-category">${product.categoryDisplay}</div>
                    <div class="product-name" onclick="viewProduct(${product.id})" style="cursor: pointer;">${product.name}</div>
                    <div class="product-description">${product.description}</div>
                    <div class="product-footer">
                        <div class="product-price-row">
                            <div class="product-price">${formatCurrency(product.price)}</div>
                            <div class="product-stock ${stockClass}">${stockText}</div>
                        </div>
                        <div class="product-actions">
                            <button class="btn-view-detail" onclick="viewProduct(${product.id})">Chi tiết</button>
                            <button class="btn-add-cart" onclick="addToCart(event, ${product.id})" ${product.stock === 0 ? 'disabled' : ''}>
                                ${product.stock === 0 ? 'Hết hàng' : 'Thêm vào giỏ'}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });

    productsList.innerHTML = html;
}

function renderPagination() {
    const paginationContainer = document.getElementById('paginationContainer');
    const totalPages = Math.ceil(products.length / itemsPerPage);
    
    if (totalPages <= 1) {
        paginationContainer.innerHTML = '';
        return;
    }
    
    let html = '';
    
    // Previous button
    html += `<button class="pagination-button" onclick="changePage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''}>
        ◀ Trước
    </button>`;
    
    // Page numbers
    for (let i = 1; i <= totalPages; i++) {
        if (i === 1 || i === totalPages || (i >= currentPage - 1 && i <= currentPage + 1)) {
            html += `<button class="pagination-button ${i === currentPage ? 'active' : ''}" onclick="changePage(${i})">
                ${i}
            </button>`;
        } else if (i === currentPage - 2 || i === currentPage + 2) {
            html += `<span class="pagination-info">...</span>`;
        }
    }
    
    // Next button
    html += `<button class="pagination-button" onclick="changePage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''}>
        Sau ▶
    </button>`;
    
    // Info
    html += `<span class="pagination-info">Trang ${currentPage} / ${totalPages}</span>`;
    
    paginationContainer.innerHTML = html;
}

function changePage(page) {
    const totalPages = Math.ceil(products.length / itemsPerPage);
    if (page < 1 || page > totalPages) return;
    
    currentPage = page;
    renderProducts();
    renderPagination();
    
    // Scroll to top of products
    document.getElementById('productsList').scrollIntoView({ behavior: 'smooth', block: 'start' });
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
    const button = event ? event.target : null;
    const originalText = button ? button.innerHTML : '';
    
    if (button) {
        button.disabled = true;
        button.innerHTML = 'Đang thêm...';
    }

    // Guest user - use local storage
    if (!userId) {
        try {
            addToGuestCart(productId, 1);
            
            if (button) {
                button.innerHTML = 'Đã thêm';
                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.disabled = false;
                }, 1500);
            }
            updateCartBadge(); // Update cart badge
            showToast('Đã thêm sản phẩm vào giỏ hàng!', 'success');
        } catch (error) {
            console.error('Error adding to guest cart:', error);
            alert('Lỗi khi thêm vào giỏ hàng. Vui lòng thử lại.');
            if (button) {
                button.innerHTML = originalText;
                button.disabled = false;
            }
        }
        return;
    }

    // Logged in user - use API
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
            await updateCartBadge();
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
    // Initialize navbar (home page doesn't require authentication)
    initNavbar('home', false);
    initFooter();
    
    // Load cart info for both guest and logged in users
    updateCartBadge();
    
    loadProducts();
    
    // Setup search input Enter key listener
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                searchProducts();
            }
        });
    }
};

function clearFilters() {
    // Clear search
    const searchInput = document.getElementById('searchInput');
    if (searchInput) searchInput.value = '';
    
    // Clear sort
    const sortBy = document.getElementById('sortBy');
    if (sortBy) sortBy.value = 'default';
    
    // Clear price filters
    const minPrice = document.getElementById('minPriceFilter');
    if (minPrice) minPrice.value = '';
    
    const maxPrice = document.getElementById('maxPriceFilter');
    if (maxPrice) maxPrice.value = '';
    
    // Clear stock filter
    const inStockOnly = document.getElementById('inStockOnly');
    if (inStockOnly) inStockOnly.checked = false;
    
    // Re-apply filters
    applySortAndFilter();
}
