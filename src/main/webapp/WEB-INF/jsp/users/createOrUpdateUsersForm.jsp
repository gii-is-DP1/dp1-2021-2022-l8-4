<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
   
<kingoftokyo:layout pageName="Users">
        <jsp:body>
            <h2>
                <c:choose>
                    <c:when test="${user['new']}">
                        New User
                    </c:when>
                    <c:otherwise>
                        Edit User
                    </c:otherwise>
                </c:choose>
            </h2>
            <form:form modelAttribute="user" class="form-horizontal" >
                <div class="form-group has-feedback">
                    <c:choose>
                        <c:when test="${user['new']}">
                            <kingoftokyo:inputField label="Username" name="username"/>
                        </c:when>
                        <c:otherwise>
                            <kingoftokyo:inputFieldReadOnly label="Username" name="username"/>
                        </c:otherwise>
                    </c:choose>
                    <kingoftokyo:inputField label="Email" name="email"/>
                    <kingoftokyo:inputField label="Password" name="password"/>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="hidden" name="id" value="${Users.id}"/>
                        <button class="btn btn-default" type="submit">Save user</button>
                    </div>
                </div>
            </form:form>
        </jsp:body>
</kingoftokyo:layout>