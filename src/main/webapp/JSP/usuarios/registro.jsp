<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Crear Cuenta" />
</jsp:include>

<div class="container-main container-center">
    <div class="form-card form-card-lg">
        <h2 class="form-title">Registro de Usuario</h2>

        <c:if test="${not empty error}">
            <div class="alert-error">${error}</div>
        </c:if>

        <form id="formRegistro" action="${contexto}/UsuarioController" method="post" enctype="multipart/form-data" onsubmit="return validarFormulario()">
            <input type="hidden" name="accion" value="registrarUsuario">

            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Email *</label>
                    <input type="email" id="email" name="email" class="form-input" required onblur="comprobarEmail()">
                    <span id="emailMsg" style="font-size:0.8rem; display:block; margin-top:5px;"></span>
                </div>
                <div></div>
            </div>

            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Contraseña *</label>  
                    <input type="password" id="password" name="password" class="form-input" required onkeyup="comprobarPassword()">
                </div>
                <div class="form-group">
                    <label class="form-label">Repetir Contraseña *</label>
                    <input type="password" id="passwordRepetir" class="form-input" required onkeyup="comprobarPassword()">
                    <span id="passStatus" style="font-size:0.8rem; display:block; margin-top:5px;"></span>
                </div>
            </div>

            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Nombre *</label>                 
                    <input type="text" name="nombre" class="form-input" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Apellidos</label>
                    <input type="text" name="apellidos" class="form-input" required>
                </div>
            </div>

            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">NIF (Solo números - max 8) *</label>
                    <input type="text" id="nif" name="nif" class="form-input" maxlength="9" placeholder="Ej: 12345678" onblur="calcularLetraNIF()" required pattern="[0-9]{8}[A-Za-z]?" title="Introduce 8 números">
                    <span id="nifStatus" style="font-size:0.8rem; color: #4ade80;"></span>
                </div>
                <div class="form-group">
                    <label class="form-label">Teléfono</label>
                    <input type="text" name="telefono" class="form-input">
                </div>   
            </div>

            <div class="form-group">
                <label class="form-label">Dirección</label>
                <input type="text" name="direccion" class="form-input" required>
            </div>

            <div class="form-grid-3">
                <div class="form-group"> 
                    <label class="form-label">C. Postal (5 dígitos) *</label>
                    <input type="text" id="cp" name="codigoPostal" class="form-input" maxlength="5" required pattern="[0-9]{5}" title="Debe tener 5 dígitos">
                </div>
                <div class="form-group">            
                    <label class="form-label">Localidad</label>
                    <input type="text" name="localidad" class="form-input" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Provincia</label>             
                    <input type="text" name="provincia" class="form-input" required>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Avatar</label>
                <div class="file-upload-box">              
                    <input type="file" name="avatar" accept="image/*" class="file-input" onchange="previewAvatar(event)">
                </div>
                <img id="avatarPreview" src="#" title="avatar" alt="Vista previa" style="display:none; width:80px; height:80px; border-radius:50%; object-fit:cover; margin-top:10px; border:2px solid #007bff;">
            </div>

            <button type="submit" id="btnRegistro" class="btn-primary btn-block" style="margin-top:20px;">Registrarse</button>
        </form>

        <div style="margin-top: 20px; text-align: center;">
            <form action="${contexto}/UsuarioController" method="post" style="display:inline;">
                <input type="hidden" name="accion" value="login">
                <button type="submit" style="background:none; border:none; color:var(--header-footer-bg); text-decoration:underline; cursor:pointer; font-size:1rem; padding:0;">
                    ¿Ya tienes cuenta? Inicia sesión
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    // Creamos una constante global en JS para pasarle la ruta de Java al archivo externo
    const CONTEXT_PATH = '${contexto}';
</script>
<script src="${contexto}/JS/registro.js"></script>

<jsp:include page="/INC/pie.jsp" />

<jsp:include page="/INC/pie.jsp" />