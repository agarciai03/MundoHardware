<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Iniciar Sesión" />
</jsp:include>

<div class="container-main container-center">
    <div class="form-card form-card-sm">
        <h2 class="form-title">Iniciar Sesión</h2>

        <c:if test="${not empty error}">
            <div class="alert-error">${error}</div>
        </c:if>

        <form action="${contexto}/UsuarioController" method="post">
            <input type="hidden" name="accion" value="validarLogin">

            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-input" required autofocus>
            </div>

            <div class="form-group">
                <label class="form-label">Contraseña</label>
                <input type="password" name="password" class="form-input" required>
            </div>


            <button type="submit" class="btn-primary btn-block">Entrar</button>
        </form>

        <div style="margin-top: 30px; text-align: center; border-top: 1px solid #444; padding-top: 20px;">
            <p style="margin-bottom: 10px; color: #ccc;">¿No tienes cuenta?</p>

            <form action="${contexto}/UsuarioController" method="POST">
                <input type="hidden" name="accion" value="registro">
                <button type="submit" class="btn-secondary btn-block">
                    Crea una cuenta ahora
                </button>
            </form>
        </div>

    </div>
</div>

<jsp:include page="/INC/pie.jsp" />