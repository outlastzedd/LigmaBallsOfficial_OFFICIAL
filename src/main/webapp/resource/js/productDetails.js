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

    // Elements for quantity and price updates
    const displayQuantity = document.getElementById('displayQuantity');
    const increaseButton = document.querySelector('.increase');
    const decreaseButton = document.querySelector('.decrease');
    const selectedQuantity = document.getElementById('selectedQuantity');
    const buyNowQuantity = document.getElementById('buyNowQuantity');
    const selectedPrice = document.getElementById('selectedPrice');
    const selectedDiscountedPrice = document.getElementById('selectedDiscountedPrice');
    const buyNowPrice = document.getElementById('buyNowPrice');
    const buyNowDiscountedPrice = document.getElementById('buyNowDiscountedPrice');
    const selectedSize = document.getElementById('selectedSize');
    const buyNowSize = document.getElementById('buyNowSize');
    const selectedProductSizeColorID = document.getElementById('selectedProductSizeColorID');
    const buyNowProductSizeColorID = document.getElementById('buyNowProductSizeColorID');
    const displayPrice = document.getElementById('displayPrice');

    // Get discount from JSP (ensure it’s a number)
    let discount = parseFloat('${product.discount}') || 0;

    // Lấy giá gốc ban đầu của sản phẩm (product.price) từ phần tử ẩn
    const rawPrice = document.getElementById('formattedPrice').textContent;
    let basePrice = parseFloat(rawPrice);
    if (isNaN(basePrice)) {
        basePrice = 0; // Đặt mặc định là 0 nếu không phải số
        console.log("Warning: product.price is not a valid number. Defaulting to 0.");
        document.querySelector('.size-selector').style.display = 'none';
        document.querySelector('.quantity-box').style.display = 'none';
        document.querySelector('.button-container').style.display = 'none';
        return; // Dừng thực thi JavaScript nếu giá không hợp lệ
    }

    // Initialize values
    if (displayQuantity && selectedQuantity && buyNowQuantity) {
        displayQuantity.value = selectedQuantity.value || 1;
        buyNowQuantity.value = selectedQuantity.value || 1;
    }
    if (displayPrice && selectedDiscountedPrice) {
        displayPrice.textContent = parseFloat(selectedDiscountedPrice.value).toLocaleString('vi-VN') + ' ₫';
    }

    // Cập nhật giá, kích thước và productSizeColorID khi chọn kích thước
    document.querySelectorAll('.size-option').forEach(button => {
        button.addEventListener('click', function () {
            // Update selected class
            document.querySelectorAll('.size-option').forEach(btn => btn.classList.remove('selected'));
            this.classList.add('selected');

            // Get size data
            const size = this.getAttribute('data-size');
            const productSizeColorID = this.getAttribute('data-product-size-color-id');
            let price = basePrice;
            if (size === 'XL') {
                price += 50000;
            } else if (size === 'XXL') {
                price += 100000;
            }
            const discountedPrice = price - (price * discount / 100);

            // Cập nhật giá hiển thị với định dạng
            const priceElement = document.querySelector('.price');
            priceElement.innerHTML = new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                }).format(discountedPrice) +
                `<span id="displayPrice" style="display: none;">${discountedPrice}</span>`;

            // Update "Add to Cart" form
            selectedSize.value = size;
            selectedPrice.value = price;
            selectedDiscountedPrice.value = discountedPrice;
            selectedProductSizeColorID.value = productSizeColorID;

            // Update "Buy Now" form
            buyNowSize.value = size;
            buyNowPrice.value = price;
            buyNowDiscountedPrice.value = discountedPrice;
            buyNowProductSizeColorID.value = productSizeColorID;

            console.log("Selected size: " + size);
            console.log("Selected price: " + price);
            console.log("Selected discounted price: " + discountedPrice);
            console.log("Selected productSizeColorID: " + productSizeColorID);
        });
    });

    // Cập nhật số lượng khi nhấn nút tăng
    if (increaseButton) {
        increaseButton.addEventListener('click', function (event) {
            event.preventDefault();
            let value = parseInt(displayQuantity.value) || 1;
            value += 1;
            displayQuantity.value = value;
            selectedQuantity.value = value;
            buyNowQuantity.value = value;
            console.log("Quantity increased to: " + value);
        });
    }

    // Cập nhật số lượng khi nhấn nút giảm
    if (decreaseButton) {
        decreaseButton.addEventListener('click', function (event) {
            event.preventDefault();
            let value = parseInt(displayQuantity.value) || 1;
            if (value > 1) {
                value -= 1;
                displayQuantity.value = value;
                selectedQuantity.value = value;
                buyNowQuantity.value = value;
                console.log("Quantity decreased to: " + value);
            }
        });
    }
});

// Handle manual quantity input
if (displayQuantity) {
    displayQuantity.addEventListener('input', function () {
        let value = parseInt(this.value) || 1;
        if (value < 1) value = 1;
        this.value = value;
        selectedQuantity.value = value;
        buyNowQuantity.value = value;
        console.log("Quantity updated to: " + value);
    });
}

// Suggested products scroll buttons
const container = document.querySelector(".suggested-products");
const btnLeft = document.querySelector(".scroll-btn.left");
const btnRight = document.querySelector(".scroll-btn.right");

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

if (btnLeft) {
    btnLeft.addEventListener("click", () => {
        container.scrollBy({left: -250, behavior: "smooth"});
        setTimeout(updateScrollButtons, 300);
    });
}

if (btnRight) {
    btnRight.addEventListener("click", () => {
        container.scrollBy({left: 250, behavior: "smooth"});
        setTimeout(updateScrollButtons, 300);
    });
}

updateScrollButtons();

// Full image overlay
const productImage = document.querySelector(".product-image img");
const overlay = document.querySelector(".image-overlay");
const fullImage = document.querySelector(".full-image");
const closeBtn = document.querySelector(".close-btn");

if (productImage && overlay && fullImage && closeBtn) {
    productImage.addEventListener("click", () => {
        fullImage.src = productImage.src;
        overlay.style.display = "flex";
    });

    closeBtn.addEventListener("click", () => {
        overlay.style.display = "none";
    });

    overlay.addEventListener("click", (e) => {
        if (e.target === overlay) {
            overlay.style.display = "none";
        }
    });
}

// Form validation
function validateForm(form) {
    const quantity = form.querySelector('input[name="quantity"]').value;
    if (!quantity || quantity.trim() === "" || isNaN(quantity) || parseInt(quantity) < 1) {
        alert("Vui lòng nhập số lượng hợp lệ (tối thiểu 1)!");
        return false;
    }
    return true;
}