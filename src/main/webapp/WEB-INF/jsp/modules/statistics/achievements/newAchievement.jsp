<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
   
<petclinic:layout pageName="Users">
    <jsp:body>
        <h2>Editar logro</h2>
        <form:form modelAttribute="achievement" class="form-horizontal" >
            <div class="form-group has-feedback">
                <petclinic:inputField label="Nombre" name="name"/>
                <petclinic:inputField label="Descripcion" name="description"/>
                <petclinic:inputField label="Puntos de recompensa" name="rewardPoints"/>
                <form:select itemLabel="name" path="metric" items="${metrics}" />
                <petclinic:inputField label="Meta" name="goal"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Guardar logro</button>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>