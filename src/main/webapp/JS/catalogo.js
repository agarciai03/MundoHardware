// barra de precio
var inputMin = document.getElementById("inputMin");
var inputMax = document.getElementById("inputMax");
var textoMin = document.getElementById("textoMin");
var textoMax = document.getElementById("textoMax");
var barraColor = document.getElementById("barraColor");

function actualizarPrecios() {
    if (!inputMin || !inputMax)
        return;

    var valorMin = parseInt(inputMin.value);
    var valorMax = parseInt(inputMax.value);

    if (valorMin > valorMax - 10) {
        valorMin = valorMax - 10;
        inputMin.value = valorMin;
    }
    if (valorMax < valorMin + 10) {
        valorMax = valorMin + 10;
        inputMax.value = valorMax;
    }

    textoMin.innerText = valorMin + " €";
    textoMax.innerText = valorMax + " €";

    var maximoPosible = parseInt(inputMax.max);
    var porcentajeMin = (valorMin / maximoPosible) * 100;
    var porcentajeMax = (valorMax / maximoPosible) * 100;

    barraColor.style.left = porcentajeMin + "%";
    barraColor.style.width = (porcentajeMax - porcentajeMin) + "%";
}

if (inputMin && inputMax) {
    inputMin.addEventListener("input", actualizarPrecios);
    inputMax.addEventListener("input", actualizarPrecios);
    actualizarPrecios();
}


// modal
function abrirModal(elemento) {
    var id = elemento.getAttribute("data-id");
    var nombre = elemento.getAttribute("data-nombre");
    var marca = elemento.getAttribute("data-marca");
    var precioFmt = elemento.getAttribute("data-preciofmt");
    var descripcion = elemento.getAttribute("data-descripcion");
    var imagen = elemento.getAttribute("data-imagen");
    document.getElementById("modalTitulo").innerText = nombre;
    document.getElementById("modalMarca").innerText = marca;
    document.getElementById("modalPrecio").innerText = precioFmt;
    document.getElementById("modalDescripcion").innerText = descripcion ? descripcion : "Sin descripción disponible.";
    document.getElementById("modalImg").src = imagen;

    document.getElementById("modalInputId").value = id;

    document.getElementById("productoModal").style.display = "block";
    document.body.style.overflow = "hidden";
}
// cerrar modal
function cerrarModal(event) {
    var modal = document.getElementById("productoModal");
    if (event === null || event.target === modal) {
        modal.style.display = "none";
        document.body.style.overflow = "auto";
    }
}
// escape del modal
window.onkeydown = function (event) {
    if (event.key === "Escape")
        cerrarModal(null);
};