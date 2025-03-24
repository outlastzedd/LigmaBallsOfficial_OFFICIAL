 //thay đổi hình ảnh
 function changeImage(element) {
                let mainImage = document.getElementById('mainImage');
                mainImage.src = element.src;
                mainImage.style.width = "400px"; // Đảm bảo kích thước không thay đổi
                mainImage.style.height = "300px";
            }
            document.addEventListener("DOMContentLoaded", function () {
                // Xử lý thay đổi ảnh
                const thumbnails = document.querySelectorAll(".thumbnail");
                const mainImage = document.getElementById("mainImage");

                if (thumbnails.length > 0 && mainImage) {
                    thumbnails.forEach((thumbnail) => {
                        thumbnail.addEventListener("click", function () {
                            mainImage.src = this.src;
                        });
                    });
                }

                // Xử lý tăng/giảm số lượng
                const quantityBox = document.querySelector(".quantity-box");
                if (quantityBox) {
                    const decreaseBtn = quantityBox.querySelector(".decrease");
                    const increaseBtn = quantityBox.querySelector(".increase");
                    const quantityInput = quantityBox.querySelector("input");

                    function updateQuantity(change) {
                        let value = parseInt(quantityInput.value) || 1;
                        quantityInput.value = Math.max(1, value + change);
                    }

                    decreaseBtn.addEventListener("click", function () {
                        updateQuantity(-1);
                    });

                    increaseBtn.addEventListener("click", function () {
                        updateQuantity(1);
                    });
                }
            });
            
//điều hướng ở phần gợi y            
document.addEventListener("DOMContentLoaded", () => {
    let container = document.querySelector(".suggested-products");
    let btnLeft = document.querySelector(".scroll-btn.left");
    let btnRight = document.querySelector(".scroll-btn.right");

    function updateScrollButtons() {
        if (container.scrollLeft <= 0) {
            btnLeft.style.display = "none";
        } else {
            btnLeft.style.display = "block";
        }
        if (container.scrollLeft + container.clientWidth >= container.scrollWidth) {
            btnRight.style.display = "none";
        } else {
            btnRight.style.display = "block";
        }
    }

    btnLeft.addEventListener("click", () => {
        container.scrollBy({ left: -250, behavior: "smooth" });
        setTimeout(updateScrollButtons, 300);
    });

    btnRight.addEventListener("click", () => {
        container.scrollBy({ left: 250, behavior: "smooth" });
        setTimeout(updateScrollButtons, 300);
    });

    updateScrollButtons();
});



//xử lí chọn size
document.addEventListener("DOMContentLoaded", function () {
    const sizeOptions = document.querySelectorAll(".size-option");
    const priceDisplay = document.querySelector(".price span");
    const originalPriceDisplay = document.querySelector(".original-price");

    sizeOptions.forEach(option => {
        option.addEventListener("click", function () {
            // Xóa class 'selected' khỏi tất cả các nút
            sizeOptions.forEach(btn => btn.classList.remove("selected"));

            // Thêm class 'selected' vào nút được chọn
            this.classList.add("selected");

            // Cập nhật giá theo size
            let newPrice = parseFloat(this.getAttribute("data-price"));
            let originalPrice = newPrice * 1.344; // Giá gốc tăng theo tỷ lệ 1.344
            
            priceDisplay.textContent = `$${newPrice.toFixed(2)}`;
            originalPriceDisplay.textContent = `$${originalPrice.toFixed(2)}`;
        });
    });
});



//hien thi toan bo anh khi nhap chuot
document.addEventListener("DOMContentLoaded", () => {
    const productImage = document.querySelector(".product-image img");
    const overlay = document.querySelector(".image-overlay");
    const fullImage = document.querySelector(".full-image");
    const closeBtn = document.querySelector(".close-btn");

    // Khi nhấp vào ảnh
    productImage.addEventListener("click", () => {
        fullImage.src = productImage.src; // Gán nguồn ảnh đầy đủ
        overlay.style.display = "flex"; // Hiển thị overlay
    });

    // Khi nhấp vào nút đóng hoặc overlay
    closeBtn.addEventListener("click", () => {
        overlay.style.display = "none"; // Ẩn overlay
    });

    overlay.addEventListener("click", (e) => {
        if (e.target === overlay) { // Chỉ ẩn khi nhấp ngoài ảnh
            overlay.style.display = "none";
        }
    });
});
