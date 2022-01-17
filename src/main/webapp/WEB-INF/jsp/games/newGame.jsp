<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                       
                        <kingoftokyo:layout pageName="newGameForm">
                            
                            <form:form modelAttribute="newGame" style="margin-bottom:20%">
                                <div class="col-lg-8" style="margin-left: 37%;">
                                    <div class="row" style="display: initial;">
                                            <kingoftokyo:inputField name="name" label="Nombre de la partida"/>
                                    </div>
                                    
                                    <div class="row" style="display: initial;">
                                        
                                        <kingoftokyo:radiobuttonsField name="maxNumberOfPlayers" items="${{2, 3, 4, 5, 6}}"  label="Numero maximo de jugadores"/>
                                    </div>

                                    <input type="hidden" value="0" name="turn"></input>
                                    
                                    <div class="row" style="display: initial;">
                                        <input type="submit" value="Crear nueva partida"/>
                                    </div>
                                </div>  
                            </form:form>

                    </kingoftokyo:layout>