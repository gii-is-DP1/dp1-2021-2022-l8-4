<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="userskot">
    <h2>
        <c:if test="${userkot['new']}">New </c:if> user
    </h2>
    <form:form modelAttribute="userkot" class="form-horizontal" id="add-userkot-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Username" name="username"/>
            <petclinic:inputField label="Email" name="email"/>
            <petclinic:inputField label="Password" name="password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${userkot['new']}">
                        <button class="btn btn-default" type="submit">Create user</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update user</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
