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
                        Nuevo usuario
                    </c:when>
                    <c:otherwise>
                        Editar usuario
                    </c:otherwise>
                </c:choose>
            </h2>
            <form:form modelAttribute="user" class="form-horizontal" >
                <div class="form-group has-feedback">
                    <c:choose>
                        <c:when test="${user['new']}">
                            <kingoftokyo:inputField label="Username" name="username"/>
                            <kingoftokyo:inputField label="Password" name="password"/>
                        </c:when>
                        <c:otherwise>
                            <kingoftokyo:inputFieldReadOnly label="Username" name="username"/>
                            <kingoftokyo:inputFieldReadOnly label="Password" name="password"/>
                            <h4>Para modificar tu contrasena:</h4>
                            <h5>Escribe tu antigua contrasena:</h5>
                            <input  name="oldPassword"/>
                        </c:otherwise>
                    </c:choose>
                    


                    <c:if test="${!user['new']}">
                        <h5>Escribe tu nueva contrasena:</h5>
                        <input  name="newPassword"/>        
                    </c:if>
                    
                    <kingoftokyo:inputField label="Email" name="email"/>
                    
                    <input type="hidden" name="version" value="${user.version}"/>
                    <input type="hidden" value="${maxTurns}" name="maxTurnsTokyo"></input>
                </div>
                
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="hidden" name="id" value="${Users.id}"/>
                        <button class="btn btn-default" type="submit">Guardar</button>
                    </div>
                </div>
            </form:form>
        </jsp:body>
</kingoftokyo:layout>