<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="achievements">
    <jsp:body>
        <h2>Achievement</h2>
        <form:form modelAttribute="achievement" class="form-horizontal" action="/achievements/new">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Title" name="name"/>
                <petclinic:inputField label="Reward points" name="rewardPoints"/>
                <petclinic:inputField label="Condition" name="condition"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="id" value="${achievement.id}"/>
                    <button class="btn btn-default" type="submit">Save achievement</button>
                </div>
            </div>
        </form:form>
    </jsp:body>

</petclinic:layout>