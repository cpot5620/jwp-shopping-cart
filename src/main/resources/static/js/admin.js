const modal = document.getElementById('modal');

const showAddModal = () => {
    modal.dataset.formType = 'add';
    modal.style.display = 'block';
};

const showEditModal = (product) => {
    const elements = modal.getElementsByTagName('input');
    for (const element of elements) {
        element.value = product[element.getAttribute('name')];
    }
    modal.dataset.formType = 'edit';
    modal.dataset.productId = product.id;
    modal.style.display = 'block';
};

const hideAddModal = () => {
    modal.style.display = 'none';
    const elements = modal.getElementsByTagName('input');
    for (const element of elements) {
        element.value = '';
    }
}

const form = document.getElementById('form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let product = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        product[key] = value;
    }

    if (modal.dataset.formType === 'edit') {
        product['id'] = modal.dataset.productId;
        updateProduct(product);
        return;
    }

    createProduct(product);
});

const createProduct = (product) => {
    axios.post('/items', {
        name: product.name,
        price: product.price,
        imageUrl: product.imageUrl
    }).then((response) => {
        window.location.reload();
    }).catch((error) => {
        let message = error.response.data.message;

        if (typeof message === 'string') {
            alert(message);
            return;
        }

        let fields = Object.keys(message);
        let alertMessage = "";

        for (let i = 0; i < fields.length; i++) {
            alertMessage += message[fields[i]] + "\n";
        }
        alert(alertMessage)
        console.error(error);
    });
};

const updateProduct = (product) => {
    const {id} = product;

    axios.put('/items/' + id, {
        name: product.name,
        price: product.price,
        imageUrl: product.imageUrl
    }).then((response) => {
        window.location.reload();
    }).catch((error) => {
        let message = error.response.data.message;

        if (typeof message === 'string') {
            alert(message);
            return;
        }

        let fields = Object.keys(message);
        let alertMessage = "";

        for (let i = 0; i < fields.length; i++) {
            alertMessage += message[fields[i]] + "\n";
        }
        alert(alertMessage)
        console.error(error);
    });
};

const deleteProduct = (id) => {
    axios.delete('/items/' + id)
        .then((response) => {
            window.location.reload();
        }).catch((error) => {
        alert(error.response.data.message);
        console.error(error);
    });
};
