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
                                        Editar usuario
                                    </h2>
                                    <form:form modelAttribute="user" class="form-horizontal">
                                        <div class="form-group has-feedback">
                                            
                                            <kingoftokyo:inputFieldReadOnly label="Nombre de usuario" name="username" />
                                            <input type="hidden" name="password" value="${user.password}" />
                                            <kingoftokyo:inputField label="Email" name="email" />
                                            <h2 style="margin-left: 1.5%;">Para modificar tu contrasena:</h2>

                                            <div class="col-sm-6" style="margin-left:25px">
                                                <div class="row" style="display: inline;">
                                                    <label for= "oldPassword" style="margin-right:4%">Antigua contrasena  </label>
                                                    <input id="oldPassword" name="oldPassword" />    
                                                </div>
                                                <div class="row"  style="display: inline;">
                                                    <label for= "newPassword" style="margin-right:6%">Nueva contrasena  </label>
                                                    <input id="newPassword" name="newPassword" />
                                                </div>
                                            </div>
                                            

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