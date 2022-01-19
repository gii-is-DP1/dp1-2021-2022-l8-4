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
                                        Nuevo usuario
                                    </h2>
                                    <form:form modelAttribute="user" class="form-horizontal">
                                        <div class="form-group has-feedback">
                                            <kingoftokyo:inputField label="Nombre de usuario" name="username" />
                                            <kingoftokyo:inputField label="Contrasena" name="password" />
                                            <kingoftokyo:inputField label="Email" name="email" />
                                            <input type="hidden" name="version" value="${user.version}" />
                                            <input type="hidden" value="${maxTurns}" name="maxTurnsTokyo"></input>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-offset-2 col-sm-10">
                                                <input type="hidden" name="id" value="${Users.id}" />
                                                <button class="btn btn-default" type="submit">Guardar</button>
                                            </div>
                                        </div>
                                    </form:form>
                                </jsp:body>
                            </kingoftokyo:layout>