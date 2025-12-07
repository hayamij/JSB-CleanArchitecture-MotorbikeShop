// motorbikes-admin.js
const API_BASE_URL = "http://localhost:8080/api/motorbikes";

async function searchMotorbikes() {

    const keyword = document.getElementById("keyword").value.trim();
    const brand   = document.getElementById("brand").value.trim();
    const model   = document.getElementById("model").value.trim();
    const color   = document.getElementById("color").value.trim();
    const minCC   = document.getElementById("minCC").value.trim();
    const maxCC   = document.getElementById("maxCC").value.trim();

    // Nếu tất cả rỗng -> load full như ban đầu
    if (!keyword && !brand && !model && !color && !minCC && !maxCC) {
        loadMotorbikes();
        return;
    }

    const tbody = document.getElementById("motorbikeTableBody");

    tbody.innerHTML = `
        <tr><td colspan="10" style="padding:25px;text-align:center;color:#666;">Đang tìm kiếm...</td></tr>
    `;

    // CHỈ append những param có giá trị
    const params = new URLSearchParams();
    if (keyword) params.append("keyword", keyword);
    if (brand)   params.append("brand", brand);
    if (model)   params.append("model", model);
    if (color)   params.append("color", color);
    if (minCC)   params.append("minCC", minCC);
    if (maxCC)   params.append("maxCC", maxCC);

    try {
        const res = await fetch(`${API_BASE_URL}/search?` + params.toString());
        const data = await res.json();

        if (!data || data.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="10" style="padding:25px; text-align:center; color:#999;">
                        Không tìm thấy xe nào phù hợp
                    </td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = data.map(item => `
            <tr>
                <td>${item.id}</td>
                <td><img src="${item.imageUrl}" class="motorbike-img"></td>
                <td><strong>${item.name}</strong></td>
                <td>${item.brand}</td>
                <td>${item.model}</td>
                <td>${item.displacement}</td>
                <td>${item.year}</td>
                <td>${item.stock}</td>
                <td>${formatCurrency(item.price)}</td>
                <td class="actions">
                    <button class="btn-edit" onclick="editMotorbike(${item.id})">✏️ Sửa</button>
                    <button class="btn-delete" onclick="deleteMotorbike(${item.id})">🗑 Xóa</button>
                </td>
            </tr>
        `).join("");

    } catch (err) {
        console.error(err);
        tbody.innerHTML = `
            <tr>
                <td colspan="10" style="padding:25px; text-align:center; color:red;">
                    Lỗi khi tìm kiếm
                </td>
            </tr>
        `;
    }
}



document.addEventListener("DOMContentLoaded", () => {
    updateAdminName();
    loadMotorbikes();
});

function updateAdminName() {
    const username = sessionStorage.getItem("username") || "Admin";
    document.getElementById("adminName").textContent = username;
}

function formatCurrency(value) {
    return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND"
    }).format(value || 0);
}

async function loadMotorbikes() {
    const tbody = document.getElementById("motorbikeTableBody");

    try {
        const res = await fetch(API_BASE_URL);
        const data = await res.json();

        if (!data || data.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="9" style="text-align:center; padding: 30px; color:#999;">
                        Không có xe máy nào
                    </td>
                </tr>`;
            return;
        }

        tbody.innerHTML = data.map(bike => `
          <tr>
              <td>${bike.id}</td>

              <td>
                  <img src="${bike.imageUrl}" class="motorbike-img">
              </td>

              <td><strong>${bike.name}</strong></td>
              <td>${bike.brand}</td>
              <td>${bike.model}</td>
              <td>${bike.displacement}</td>
              <td>${bike.year}</td>
              <td>${bike.stock}</td>
              <td>${formatCurrency(bike.price)}</td>

              <td class="actions">
                  <button class="btn-edit" onclick="editMotorbike(${bike.id})">✏️ Sửa</button>
                  <button class="btn-delete" onclick="deleteMotorbike(${bike.id})">🗑 Xóa</button>
              </td>
          </tr>
      `).join("");


    } catch (err) {
        console.error("Lỗi load danh sách xe máy:", err);

        tbody.innerHTML = `
            <tr>
                <td colspan="9" style="text-align:center; padding: 30px; color:#e74c3c;">
                    Lỗi khi tải dữ liệu
                </td>
            </tr>`;
    }

}
function editMotorbike(id) {
window.location.href = `edit-motorbike.html?id=${id}`;
}

async function deleteMotorbike(id) {
    if (!confirm("Bạn có chắc chắn muốn xóa xe máy này?")) {
        return;
    }

    try {
        const res = await fetch(`${API_BASE_URL}/delete/${id}`, {
            method: "DELETE"
        });

        if (!res.ok) {
            const err = await res.json();
            showToast("❌ " + (err.errorMessage || "Lỗi khi xóa"), "error");
            return;
        }

        showToast("✔ Xóa thành công!", "success");

        // reload lại danh sách
        loadMotorbikes();

    } catch (err) {
        console.error(err);
        showToast("⚠ Không thể kết nối server!", "error");
    }
}
