// static/js/script.js

function updateHiddenQuantity() {
	const quantity = document.getElementById("quantity").value;
	document.getElementById("hiddenQuantity").value = quantity;
}

function deleteItem(productId) {
	const form = document.createElement('form');
	form.method = 'post';
	form.action = `/deleteItem?productId=${productId}`;
	document.body.appendChild(form);
	form.submit();
}
