var emailValido = false;

// AJAX Comprobar si el email existe
function comprobarEmail() {
    let email = document.getElementById("email").value;
    let msg = document.getElementById("emailMsg");

    if (email.length > 5) {
        let datos = new URLSearchParams();
        datos.append('accion', 'checkEmail');
        datos.append('email', email);

        // Usamos la constante CONTEXT_PATH definida en el JSP
        fetch(CONTEXT_PATH + '/AjaxController', {
            method: 'POST',
            body: datos
        })
                .then(response => response.json())
                .then(data => {
                    if (data.existe) {
                        msg.innerHTML = "<span style='color:#ff6b6b;'>Este email ya está registrado</span>";
                        emailValido = false; // Bloquea el envio
                    } else {
                        msg.innerHTML = "<span style='color:#4ade80;'>Email disponible</span>";
                        emailValido = true;  // Permite el envio
                    }
                })
                .catch(error => console.error('Error AJAX:', error));
    } else {
        msg.innerHTML = "";
        emailValido = false;
    }
}

// Contraseñas iguales
function comprobarPassword() {
    let pass1 = document.getElementById("password").value;
    let pass2 = document.getElementById("passwordRepetir").value;
    let msg = document.getElementById("passStatus");

    if (pass2.length > 0) {
        if (pass1 !== pass2) {
            msg.innerHTML = "<span style='color:#ff6b6b;'>Las contraseñas no coinciden</span>";
            return false;
        } else {
            msg.innerHTML = "<span style='color:#4ade80;'>Las contraseñas coinciden</span>";
            return true;
        }
    } else {
        msg.innerHTML = "";
        return false;
    }
}

// letra NIF
function calcularLetraNIF() {
    let nifInput = document.getElementById("nif");
    let val = nifInput.value;
    let nifStatus = document.getElementById("nifStatus");

    if (val.length > 0 && val.length < 8) {
        nifStatus.innerHTML = "<span style='color:#ff6b6b;'>Son 8 números obligatorios</span>";
        return;
    }

    if (val.length === 8 && !isNaN(val)) {
        nifStatus.innerHTML = "";
        let datos = new URLSearchParams();
        datos.append('accion', 'calcNIF');
        datos.append('dni', val);

        // Usamos la constante CONTEXT_PATH definida en el JSP
        fetch(CONTEXT_PATH + '/AjaxController', {
            method: 'POST',
            body: datos
        })
                .then(response => response.json())
                .then(data => {
                    if (data.letra) {
                        nifInput.value = val + data.letra;
                    }
                });
    }
}

function validarFormulario() {
    var cp = document.getElementById("cp").value;
    if (!/^\d{5}$/.test(cp) || parseInt(cp) > 52006) {
        alert("El Código Postal debe tener 5 números y no ser mayor a 52006.");
        document.getElementById("cp").focus();
        return false;
    }

    var tel = document.querySelector("input[name='telefono']").value;
    if (tel.length > 0 && !/^[679]\d{8}$/.test(tel)) {
        alert("El teléfono debe tener 9 dígitos y empezar por 6, 7 o 9.");
        document.querySelector("input[name='telefono']").focus();
        return false;
    }

    var nif = document.getElementById("nif").value;
    if (nif.length !== 9) {
        alert("El NIF debe contener exactamente 8 números y su letra correspondiente.");
        document.getElementById("nif").focus();
        return false;
    }

    if (!comprobarPassword()) {
        alert("Las contraseñas no coinciden. Por favor, revísalas.");
        document.getElementById("passwordRepetir").focus();
        return false;
    }

    if (!emailValido) {
        alert("El email introducido ya pertenece a otra cuenta o no es válido.");
        document.getElementById("email").focus();
        return false;
    }

    return true;
}

// vista previa avatar
function previewAvatar(event) {
    var reader = new FileReader();
    reader.onload = function () {
        var preview = document.getElementById('avatarPreview');
        preview.src = reader.result;
        preview.style.display = 'block';
    };
    if (event.target.files[0]) {
        reader.readAsDataURL(event.target.files[0]);
    }
}