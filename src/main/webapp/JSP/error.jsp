<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<%
    Integer codigo = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String imgError = "error500.jpg";
    String titulo = "Error del Servidor";
    String mensaje = "Ha ocurrido un error inesperado. Por favor, inténtalo más tarde.";

    if (codigo != null && codigo == 404) {
        imgError = "error404.jpg";
        titulo = "Página no encontrada";
        mensaje = "La ruta que buscas no existe o ha cambiado de sitio.";
    }
%>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Error <%= codigo%>" />
</jsp:include>

<div class="container-main container-center">
    <div class="msg-card">

        <img src="${contexto}/imagen/errores/<%= imgError%>" 
             alt="Error <%= codigo%>" 
             class="msg-img"
             onerror="this.src='${contexto}/imagen/logo/MundoHardware..png'">

        <h2 class="msg-title error-text"><%= titulo%></h2>

        <p class="msg-body">
            <%= mensaje%>
        </p>

        <form action="${contexto}/FrontController" method="post">
            <input type="hidden" name="accion" value="inicio">
            <button type="submit" class="btn-primary btn-block">
                <i class="fas fa-home"></i> Volver al Inicio
            </button>
        </form>

        <% if (exception != null) {%>
        <div class="error-debug-box">
            <strong>Detalles técnicos:</strong><br>
            <%= exception.getMessage()%>
        </div>
        <% }%>
    </div>
</div>

<jsp:include page="/INC/pie.jsp" />