function actualizarCantidad(idProducto, operacion) {
    let datos = new URLSearchParams();
    datos.append('accion', 'cambiarCantidad');
    datos.append('id', idProducto);
    datos.append('op', operacion);

    fetch(CONTEXT_PATH + '/AjaxController', {
        method: 'POST',
        body: datos
    })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'ok') {
                    if (data.qty === 0) {
                        location.reload();
                    } else {
                        document.getElementById('qty-' + idProducto).innerText = data.qty;
                        const formater = new Intl.NumberFormat('es-ES', {
                            style: 'currency',
                            currency: 'EUR',
                            minimumFractionDigits: 2
                        });
                        document.getElementById('subtotal-' + idProducto).innerText = formater.format(data.rowTotal);
                        document.getElementById('cart-base').innerText = formater.format(data.base);
                        document.getElementById('cart-iva').innerText = formater.format(data.iva);
                        document.getElementById('cart-total').innerText = formater.format(data.total);
                    }
                } else {
                    alert('Error al actualizar la cantidad');
                }
            });
}