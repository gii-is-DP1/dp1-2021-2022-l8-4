<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="Cards">
    
    <jsp:body>
        <h2>Card</h2>
        <form:form modelAttribute="card" class="form-horizontal" action="/cards/save">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Name" name="name"/>
                <petclinic:inputField label="Cost" name="cost"/>
                <petclinic:inputField label="Type" name="type"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${card.id}"/>
                    <button class="btn btn-default" type="submit">Add Card</button>
                </div>
            </div>
        </form:form>
    </jsp:body>

</petclinic:layout>
