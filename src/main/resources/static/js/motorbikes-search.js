async function searchMotorbikes() {
    const params = new URLSearchParams();

    const keyword = document.getElementById("keyword").value;
    const brand = document.getElementById("brand").value;
    const model = document.getElementById("model").value;
    const color = document.getElementById("color").value;
    const minCC = document.getElementById("minCC").value;
    const maxCC = document.getElementById("maxCC").value;

    if (keyword) params.append("keyword", keyword);
    if (brand) params.append("brand", brand);
    if (model) params.append("model", model);
    if (color) params.append("color", color);
    if (minCC) params.append("minCC", minCC);
    if (maxCC) params.append("maxCC", maxCC);

    try {
        const response = await fetch(`/api/motorbikes/search?${params.toString()}`);
        const data = await response.json();

        motorbikes = data.map(item => ({
            id: item.id,
            name: item.name,
            description: item.description,
            price: item.price,
            stock: item.stock,
            imageUrl: item.imageUrl,
            brand: item.brand,
            model: item.model,
            color: item.color,
            year: item.year,
            displacement: item.displacement
        }));

        renderMotorbikes();

    } catch (error) {
        console.error("Search error:", error);
        alert("Không thể tìm kiếm xe máy.");
    }
}
