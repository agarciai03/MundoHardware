// vista previa avatar
function previewAvatar(event) {
    var reader = new FileReader();
    reader.onload = function () {
        document.getElementById('imgPerfil').src = reader.result;
    };
    if (event.target.files[0]) {
        reader.readAsDataURL(event.target.files[0]);
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

function validarPerfil() {
    // Validar Código Postal
    var cp = document.querySelector("input[name='codigoPostal']").value;
    if (cp && (!/^\d{5}$/.test(cp) || parseInt(cp) > 52006)) {
        alert("El Código Postal debe tener 5 números y no ser mayor a 52006.");
        document.querySelector("input[name='codigoPostal']").focus();
        return false; // Bloquea el envío
    }

    //Validar Teléfono
    var tel = document.querySelector("input[name='telefono']").value;
    if (tel && !/^[679]\d{8}$/.test(tel)) {
        alert("El teléfono debe tener 9 dígitos y empezar por 6, 7 o 9.");
        document.querySelector("input[name='telefono']").focus();
        return false; // Bloquea el envío
    }

    // Validar Contraseñas
    let pass1 = document.getElementById("password").value;
    let pass2 = document.getElementById("passwordRepetir").value;

    if (pass1.length > 0 || pass2.length > 0) {
        if (!comprobarPassword()) {
            alert("Las contraseñas nuevas no coinciden. Por favor, revísalas.");
            document.getElementById("passwordRepetir").focus();
            return false;
        }
    }

    // Si todo está bien, permite el envío 
    return true;
}