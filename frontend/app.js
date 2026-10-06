let products = [];
let cart = [];

async function loadProducts() {
  const response = await fetch("/api/products");
  if (!response.ok) throw new Error("Unable to load products");

  products = await response.json();
  document.getElementById("status").textContent = `${products.length} products available`;

  document.getElementById("productGrid").innerHTML = products.map(p => `
    <article class="card">
      <div class="product-image">${p.image}</div>
      <div class="card-content">
        <div class="category">${escapeHtml(p.category)}</div>
        <h3>${escapeHtml(p.name)}</h3>
        <p class="description">${escapeHtml(p.description)}</p>
        <div class="price">₹${Number(p.price).toFixed(2)}</div>
        <button class="primary-btn add-btn" onclick="addToCart(${p.id})">Add to Cart</button>
      </div>
    </article>`).join("");
}

function escapeHtml(v) {
  return String(v)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function addToCart(id) {
  const product = products.find(p => p.id === id);
  if (!product) return;

  const existing = cart.find(item => item.product.id === id);
  if (existing) {
    existing.quantity += 1;
  } else {
    cart.push({ product, quantity: 1 });
  }
  updateCart();
}

function updateCart() {
  const count = cart.reduce((sum, item) => sum + item.quantity, 0);
  document.getElementById("cartCount").textContent = count;

  document.getElementById("cartItems").innerHTML = cart.length
    ? cart.map(item => `
      <div class="cart-row">
        <span>${escapeHtml(item.product.name)} × ${item.quantity}</span>
        <strong>₹${(item.product.price * item.quantity).toFixed(2)}</strong>
      </div>`).join("")
    : "<p>Your cart is empty.</p>";

  const total = cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);
  document.getElementById("cartTotal").textContent = total.toFixed(2);
}

document.getElementById("cartButton").onclick = () => {
  document.getElementById("cartModal").classList.remove("hidden");
  updateCart();
};

document.getElementById("closeCart").onclick = () =>
  document.getElementById("cartModal").classList.add("hidden");

document.getElementById("checkoutButton").onclick = async () => {
  if (!cart.length) return;

  const payload = {
    items: cart.map(item => ({
      productId: item.product.id,
      quantity: item.quantity
    }))
  };

  const response = await fetch("/api/orders", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const data = await response.json();

  document.getElementById("orderMessage").textContent =
    response.ok
      ? `✓ ${data.message}. Total ₹${data.total}`
      : data.error || "Unable to create order";

  if (response.ok) {
    cart = [];
    updateCart();
  }
};

loadProducts().catch(() => {
  document.getElementById("status").textContent = "Backend unavailable";
});
