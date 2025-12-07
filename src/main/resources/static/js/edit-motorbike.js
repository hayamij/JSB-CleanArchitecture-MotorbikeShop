console.log("EDIT-MOTORBIKE.JS LOADED");

const API_BASE_URL = "http://localhost:8080/api/motorbikes";

document.addEventListener("DOMContentLoaded", () => {

    // Lấy id từ ?id=...
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    if (!id) {
        showToast("❌ Thiếu id xe máy cần sửa", "error");
        return;
    }

    // Load thông tin xe máy
    loadMotorbike(id);

    // Lắng nghe submit form
    document.getElementById("editMotorbikeForm")
        .addEventListener("submit", (e) => submitForm(e, id));
});


/* ---------------------------
   LOAD MOTORBIKE FOR EDIT
----------------------------*/
async function loadMotorbike(id) {
    try {
        const res = await fetch(API_BASE_URL);
        const data = await res.json();

        const bike = data.find(b => String(b.id) === String(id));

        if (!bike) {
            showToast("❌ Không tìm thấy xe máy với mã " + id, "error");
            return;
        }

        // Đổ dữ liệu vào form
        document.getElementById("name").value = bike.name || "";
        document.getElementById("price").value = bike.price || 0;
        document.getElementById("brand").value = bike.brand || "";
        document.getElementById("model").value = bike.model || "";
        document.getElementById("color").value = bike.color || "";
        document.getElementById("year").value = bike.year || 2024;
        document.getElementById("displacement").value = bike.displacement || 110;
        document.getElementById("stock").value = bike.stock || 0;
        document.getElementById("imageUrl").value = bike.imageUrl || "";
        document.getElementById("description").value = bike.description || "";

    } catch (err) {
        console.error("Lỗi load xe máy:", err);
        showToast("⚠ Lỗi khi tải dữ liệu xe máy", "error");
    }
}


/* ---------------------------
       SUBMIT UPDATE
----------------------------*/
async function submitForm(e, id) {
    e.preventDefault();
    console.log("SUBMIT UPDATE", id);

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
        displacement: Number(document.getElementById("displacement").value)
    };

    // ==== VALIDATE ====
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
        const res = await fetch(`${API_BASE_URL}/update/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const err = await res.json();
            showToast("❌ " + (err.errorMessage || "Lỗi khi cập nhật"), "error");
            return;
        }

        const data = await res.json();
        console.log("UPDATED:", data);

        showToast("✔ Cập nhật xe máy thành công!", "success");

        setTimeout(() => {
            window.location.href = "motorbikes-admin.html";
        }, 900);

    } catch (error) {
        console.error(error);
        showToast("⚠ Không thể kết nối server!", "error");
    }
}
