<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<jsp:include page="/INC/cabecera.jsp">
    <jsp:param name="titulo" value="Aviso" />
</jsp:include>

<div class="container-main container-center">
    <div class="msg-card msg-card-info">

        <div class="msg-icon-wrapper">
            <i class="fas fa-info-circle"></i>
        </div>

        <h2 class="msg-title">
            ${not empty titulo ? titulo : 'Aviso Importante'}
        </h2>

        <p class="msg-body">
            ${not empty mensaje ? mensaje : 'Operación realizada correctamente.'}
        </p>

        <form action="${contexto}/FrontController" method="post">
            <input type="hidden" name="accion" value="inicio">
            <button type="submit" class="btn-primary btn-block">
                Continuar
            </button>
        </form>
    </div>
</div>

<jsp:include page="/INC/pie.jsp" />