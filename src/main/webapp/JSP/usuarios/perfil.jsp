<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<c:if test="${empty sessionScope.usuario}">
    <c:redirect url="/UsuarioController?accion=login"/>
</c:if>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Mi Perfil" />
</jsp:include>

<div class="container-main container-center">
    <div class="form-card form-card-lg">

        <h2 class="form-title">Mi Perfil</h2>

        <c:if test="${not empty mensaje}"><div class="alert-message">${mensaje}</div></c:if>
        <c:if test="${not empty error}"><div class="alert-error">${error}</div></c:if>

            <form action="${contexto}/UsuarioController" method="post" enctype="multipart/form-data" onsubmit="return validarPerfil()">
            <input type="hidden" name="accion" value="actualizarPerfil">

            <img id="imgPerfil" 
                 src="${contexto}/imagen/avatar/${not empty sessionScope.usuario.avatar ? sessionScope.usuario.avatar : 'default.png'}" 
                 class="profile-avatar-img" style="align-items: center"
                 onerror="this.src='${contexto}/imagen/avatar/default.png'">
            <div class="profile-upload-container">
                <label class="form-label center-text">Cambiar avatar</label>
                <div class="file-upload-box">
                    <input type="file" name="avatar" accept="image/*" class="file-input" onchange="previewAvatar(event)">
                </div>
            </div>

            <h3 class="form-section-title"><i class="fas fa-lock"></i> Datos del perfil</h3>
            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-input form-input-readonly" value="${sessionScope.usuario.email}" readonly>
                </div>
                <div class="form-group">
                    <label class="form-label">NIF</label>
                    <input type="text" class="form-input form-input-readonly" value="${sessionScope.usuario.nif}" readonly>
                </div>

                <div class="form-group" style="grid-column: 1 / -1;">
                    <label class="form-label">Último Acceso</label>
                    <fmt:formatDate value="${sessionScope.usuario.ultimoAcceso}" pattern="dd/MM/yyyy HH:mm:ss" var="fechaUltimoAcceso" />
                    <input type="text" class="form-input form-input-readonly" 
                           value="${not empty fechaUltimoAcceso ? fechaUltimoAcceso : 'Este es tu primer acceso a la plataforma'}" readonly>
                </div>
            </div>

            <h3 class="form-section-title"><i class="fas fa-key"></i> Cambiar Contraseña</h3>
            <div class="form-group">
                <label class="form-label">Contraseña Actual</label>
                <input type="password" name="passwordActual" class="form-input">
            </div>
            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Nueva Contraseña</label>
                    <input type="password" id="password" name="passwordNueva" class="form-input" onkeyup="comprobarPassword()">
                </div>
                <div class="form-group">
                    <label class="form-label">Repetir Nueva Contraseña</label>
                    <input type="password" id="passwordRepetir" name="passwordRepetir" class="form-input" onkeyup="comprobarPassword()">
                    <span id="passStatus" style="font-size:0.8rem; display:block; margin-top:5px;"></span>
                </div>
            </div>

            <h3 class="form-section-title"><i class="fas fa-user-edit"></i> Información Personal</h3>
            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Nombre</label>
                    <input type="text" name="nombre" class="form-input" value="${sessionScope.usuario.nombre}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Apellidos</label>
                    <input type="text" name="apellidos" class="form-input" value="${sessionScope.usuario.apellidos}">
                </div>
            </div>
            <div class="form-group">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-input" 
                       value="${sessionScope.usuario.telefono}" 
                       pattern="[679][0-9]{8}" 
                       title="Debe tener 9 dígitos y empezar por 6, 7 o 9">
            </div>

            <h3 class="form-section-title"><i class="fas fa-map-marker-alt"></i> Dirección de Envío</h3>
            <div class="form-group">
                <label class="form-label">Dirección</label>
                <input type="text" name="direccion" class="form-input" value="${sessionScope.usuario.direccion}">
            </div>
            <div class="form-grid-3">
                <div class="form-group">
                    <label class="form-label">Código Postal</label>
                    <input type="text" name="codigoPostal" class="form-input" 
                           value="${sessionScope.usuario.codigoPostal}" 
                           maxlength="5" 
                           pattern="[0-9]{5}" 
                           title="Debe tener 5 dígitos numéricos">
                </div>
                <div class="form-group">
                    <label class="form-label">Localidad</label>
                    <input type="text" name="localidad" class="form-input" 
                           value="${sessionScope.usuario.localidad}">
                </div>
                <div class="form-group">
                    <label class="form-label">Provincia</label>
                    <input type="text" name="provincia" class="form-input" 
                           value="${sessionScope.usuario.provincia}">
                </div>
            </div>

            <div class="profile-actions" style="margin-top:30px;">
                <button type="submit" class="btn-primary btn-save">
                    <i class="fas fa-save"></i> Guardar Cambios
                </button>
            </div>
        </form>

        <div style="margin-top:40px; border-top:1px solid #444; padding-top:20px;">
            <form action="${contexto}/UsuarioController" method="post">
                <input type="hidden" name="accion" value="logout">
                <button type="submit" class="btn-secondary btn-block">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                </button>
            </form>
        </div>

    </div>
</div>

<script src="${contexto}/JS/perfil.js"></script>

<jsp:include page="/INC/pie.jsp" />