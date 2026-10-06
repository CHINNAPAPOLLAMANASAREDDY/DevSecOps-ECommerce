let products=[];let cart=[];

async function loadProducts(){
  const response=await fetch("/api/products");
  products=await response.json();
  document.getElementById("status").textContent=`${products.length} products available`;
  document.getElementById("productGrid").innerHTML=products.map(p=>`
    <article class="card">
      <div class="product-image">${p.image}</div>
      <div class="card-content">
        <div class="category">${escapeHtml(p.category)}</div>
        <h3>${escapeHtml(p.name)}</h3>
        <p class="description">${escapeHtml(p.description)}</p>
        <div class="price">₹${p.price.toFixed(2)}</div>
        <button class="primary-btn add-btn" onclick="addToCart(${p.id})">Add to Cart</button>
      </div>
    </article>`).join("");
}

function escapeHtml(v){return String(v).replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll('"',"&quot;").replaceAll("'","&#039;")}
function addToCart(id){const p=products.find(x=>x.id===id);if(p){cart.push(p);updateCart()}}
function updateCart(){
  document.getElementById("cartCount").textContent=cart.length;
  document.getElementById("cartItems").innerHTML=cart.length?cart.map(p=>`<div class="cart-row"><span>${escapeHtml(p.name)}</span><strong>₹${p.price.toFixed(2)}</strong></div>`).join(""):"<p>Your cart is empty.</p>";
  document.getElementById("cartTotal").textContent=cart.reduce((s,p)=>s+p.price,0).toFixed(2);
}
document.getElementById("cartButton").onclick=()=>{document.getElementById("cartModal").classList.remove("hidden");updateCart()};
document.getElementById("closeCart").onclick=()=>document.getElementById("cartModal").classList.add("hidden");
document.getElementById("checkoutButton").onclick=async()=>{
  if(!cart.length)return;
  const r=await fetch("/api/orders",{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({items:cart})});
  const d=await r.json();
  document.getElementById("orderMessage").textContent=r.ok?`✓ ${d.message}. Total ₹${d.total}`:d.error;
  if(r.ok){cart=[];updateCart()}
};
loadProducts().catch(()=>document.getElementById("status").textContent="Backend unavailable");
