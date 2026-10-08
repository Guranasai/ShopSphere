/**
 * 
 */
const API_URL = "/api/products";

let editingProductId = null;


// ===============================
// GET ALL PRODUCTS
// ===============================

function getProducts() {

    fetch(API_URL)

        .then(response => response.json())

        .then(products => {

            const table = document.getElementById("productTable");

            table.innerHTML = "";

            products.forEach(product => {

                table.innerHTML += `

                    <tr>

                        <td>${product.id}</td>

                        <td>${product.name}</td>

                        <td>${product.description}</td>

                        <td>${product.price}</td>

                        <td>${product.quantity}</td>

                        <td>${product.category}</td>

                        <td>

                            <button onclick="editProduct(${product.id})">
                                Edit
                            </button>

                            <button onclick="deleteProduct(${product.id})">
                                Delete
                            </button>

                        </td>

                    </tr>

                `;

            });

        })

        .catch(error => {

            console.error("Error fetching products:", error);

        });
}



// ===============================
// ADD PRODUCT
// ===============================

function addProduct() {

    const product = {

        name: document.getElementById("name").value,

        description: document.getElementById("description").value,

        price: document.getElementById("price").value,

        quantity: document.getElementById("quantity").value,

        category: document.getElementById("category").value

    };


    fetch(API_URL, {

        method: "POST",

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(product)

    })

        .then(response => response.json())

        .then(data => {

            alert("Product added successfully!");

            clearForm();

            getProducts();

        })

        .catch(error => {

            console.error("Error adding product:", error);

        });

}



// ===============================
// EDIT PRODUCT
// ===============================

function editProduct(id) {

    editingProductId = id;


    fetch(`${API_URL}/${id}`)

        .then(response => response.json())

        .then(product => {

            document.getElementById("name").value = product.name;

            document.getElementById("description").value =
                product.description;

            document.getElementById("price").value =
                product.price;

            document.getElementById("quantity").value =
                product.quantity;

            document.getElementById("category").value =
                product.category;

        })

        .catch(error => {

            console.error("Error getting product:", error);

        });

}



// ===============================
// UPDATE PRODUCT
// ===============================

function updateProduct() {

    if (editingProductId === null) {

        alert("Please select a product to edit.");

        return;

    }


    const product = {

        name: document.getElementById("name").value,

        description: document.getElementById("description").value,

        price: document.getElementById("price").value,

        quantity: document.getElementById("quantity").value,

        category: document.getElementById("category").value

    };


    fetch(`${API_URL}/${editingProductId}`, {

        method: "PUT",

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(product)

    })

        .then(response => response.json())

        .then(data => {

            alert("Product updated successfully!");

            editingProductId = null;

            clearForm();

            getProducts();

        })

        .catch(error => {

            console.error("Error updating product:", error);

        });

}



// ===============================
// DELETE PRODUCT
// ===============================

function deleteProduct(id) {

    if (!confirm("Are you sure you want to delete this product?")) {

        return;

    }


    fetch(`${API_URL}/${id}`, {

        method: "DELETE"

    })

        .then(response => response.text())

        .then(message => {

            alert(message);

            getProducts();

        })

        .catch(error => {

            console.error("Error deleting product:", error);

        });

}



// ===============================
// CLEAR FORM
// ===============================

function clearForm() {

    document.getElementById("name").value = "";

    document.getElementById("description").value = "";

    document.getElementById("price").value = "";

    document.getElementById("quantity").value = "";

    document.getElementById("category").value = "";

}



// ===============================
// LOAD PRODUCTS WHEN PAGE OPENS
// ===============================

getProducts();