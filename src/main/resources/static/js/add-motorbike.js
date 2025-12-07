console.log("ADD-MOTORBIKE.JS ĐÃ CHẠY");

const API_BASE_URL = "http://localhost:8080/api/motorbikes/add";

document.addEventListener("DOMContentLoaded", () => {

    // ❌ GỌI BỊ LỖI → COMMENT LẠI
    // updateAdminName();

    document.getElementById("addMotorbikeForm")
        .addEventListener("submit", submitForm);
});

async function submitForm(e) {
    e.preventDefault();
    console.log("ĐÃ NHẬN SUBMIT");

    const payload = {
        name: document.getElementById("name").value.trim(),
        price: Number(document.getElementById("price").value),
        description: document.getElementById("description").value.trim(),
        imageUrl: document.getElementById("imageUrl").value.trim(),
        stock: Number(document.getElementById("stock").value),
        brand: document.getElementById("brand").value.trim(),
        model: document.getElementById("model").value.trim(),
        color: document.getElementById("color").value.trim(),
        year: Number(document.getElementById("year").value),
        displacement: Number(document.getElementById("displacement").value),
        productType: "XE_MAY"
    };

    // ============================
    // VALIDATE
    // ============================
    if (payload.name.length < 2) {
        showToast("❌ Tên xe quá ngắn", "error");
        return;
    }
    if (payload.price <= 0) {
        showToast("❌ Giá phải > 0", "error");
        return;
    }
    if (payload.stock < 0) {
        showToast("❌ Tồn kho không hợp lệ", "error");
        return;
    }
    if (payload.displacement < 50) {
        showToast("❌ Dung tích cc phải ≥ 50", "error");
        return;
    }

    try {
        const res = await fetch(API_BASE_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const err = await res.json();
            showToast("❌ " + (err.errorMessage || "Lỗi không xác định"), "error");
            return;
        }

        console.log("DỮ LIỆU LƯU:", await res.clone().json());
        showToast("✔ Thêm xe máy thành công!", "success");

        setTimeout(() => {
            window.location.href = "motorbikes-admin.html";
        }, 900);

    } catch (error) {
        console.error(error);
        showToast("⚠ Không thể kết nối server!", "error");
    }
}
