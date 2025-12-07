let motorbikes = [];

window.onload = function () {
    checkAuth();
    loadCartInfo();
    loadMotorbikes();
};

async function loadMotorbikes() {
    try {
        const response = await fetch("/api/motorbikes"); // ⭐ lấy từ UseCase Motorbike
        const data = await response.json();

        motorbikes = data.map(item => ({
            id: item.id,
            name: item.name,
            description: item.description || "",
            price: item.price,
            stock: item.stock,
            imageUrl: item.imageUrl,
            category: "Xe máy",
            brand: item.brand,
            model: item.model,
            year: item.year,
            displacement: item.displacement
        }));

        renderMotorbikes();

    } catch (error) {
        console.error("Error loading motorbikes:", error);
        alert("Không thể tải danh sách xe máy");
    }
}

function renderMotorbikes() {
    const container = document.getElementById("productsList");

    if (motorbikes.length === 0) {
        container.innerHTML = 
        `<div style="text-align:center; padding:40px; color:#777;">
            Không có xe máy nào
        </div>`;
        return;
    }

    let html = "";

    motorbikes.forEach(item => {
        let stockText = item.stock === 0
            ? "Hết hàng"
            : item.stock < 10
                ? `Chỉ còn ${item.stock} chiếc`
                : `Còn ${item.stock} chiếc`;

        let stockClass =
            item.stock === 0 ? "stock-out" :
            item.stock < 10 ? "stock-low" : "";

        html += `
            <div class="product-card">
                <div class="product-image-container" onclick="viewDetail(${item.id})">
                    ${item.imageUrl ? `<img src="${item.imageUrl}" class="product-image">` : ""}
                    ${item.stock === 0 
                        ? `<div class="product-badge" style="background:#f8d7da;color:#721c24;">Hết hàng</div>`
                        : item.stock < 10 
                        ? `<div class="product-badge">Sắp hết</div>` 
                        : ""}
                </div>

                <div class="product-info">
                    <div class="product-category">Xe máy</div>
                    <div class="product-name" onclick="viewDetail(${item.id})">${item.name}</div>
                    <div class="product-description">${item.description}</div>

                    <div class="product-footer">
                        <div>
                            <div class="product-price">${formatCurrency(item.price)}</div>
                            <div class="product-stock ${stockClass}">${stockText}</div>
                        </div>

                        <button class="btn-add-cart"
                            onclick="addToCart(event, ${item.id})"
                            ${item.stock === 0 ? "disabled" : ""}>
                            ${item.stock === 0 ? "Hết hàng" : "Thêm"}
                        </button>
                    </div>
                </div>
            </div>
        `;
    });

    container.innerHTML = html;
}

function viewDetail(id) {
    window.location.href = `product-detail.html?id=${id}`;
}
