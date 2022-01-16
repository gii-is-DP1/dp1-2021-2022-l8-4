<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
   
<kingoftokyo:layout pageName="Achievements">
    <jsp:body>
        <h2>Editar logro</h2>
        <form:form modelAttribute="achievement" class="form-horizontal" >
            <div class="form-group has-feedback">
                <kingoftokyo:inputField label="Nombre" name="name"/>
                <kingoftokyo:inputField label="Descripcion" name="description"/>
                <kingoftokyo:inputField label="Puntos de recompensa" name="rewardPoints"/>
                <kingoftokyo:inputField label="Meta" name="goal"/>
                <form:select itemLabel="name" path="metric" items="${metrics}" style="margin-left: 17.2%; margin-bottom: 5%;"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Guardar logro</button>
                </div>
            </div>
        </form:form>
    </jsp:body>
</kingoftokyo:layout>