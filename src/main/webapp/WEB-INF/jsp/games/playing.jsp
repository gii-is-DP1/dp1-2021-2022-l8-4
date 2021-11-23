<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                        <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                        <petclinic:layout pageName="dices">

                            <c:if test="${!isPlayerInGame}">
                                <h1>MODO ESPECTADOR</h1>
                            </c:if>
                            <h2>
                                Jugadores vivos: 
                                <c:out value="${game.playersAlive().size()}" /> /
                                <c:out value="${game.playersAmount()}" /> 
                            </h2> 
                            
                            <h2>
                                Turno actual: 
                                <c:out value="${game.turn}" />
                            </h2>
                            <h2>
                                Es el turno de:
                                <c:out value="${actualPlayerTurn}" />
                            </h2> 

                            <h1>Tirada de dados</h1>
                            
                            <c:if test="${isPlayerTurn}">
                                <form:form modelAttribute="roll">
                                    <span>
                                        Cantidad de tiradas realizadas: 
                                        <c:out value="${roll.rollAmount}" /> / <c:out value="${roll.maxThrows}" />
                                    </span>
                            
                                    
                                    
                                    <c:if test="${!roll.rollFinished()}">
                                        <c:if test="${roll.rollAmount != 0}">
                                            <table>
                                                <tr>
                                                    <td>
                                                        <form:checkboxes items="${roll.values}" path="keep"/>  
                                                    </td>
                                                </tr>
                                            </table>
                                
                                        </c:if>

                                
                                        <input type="hidden" value="${roll.rollAmount}" name="rollAmount" ></input>
                                    
                                        <input type="hidden" value="False" name="newTurn" ></input>
                                        <br>
                                        <input type="submit" value="REALIZAR TIRADA DE DADOS" >
                                    </c:if>

                                    <input type="hidden" value="${roll.rollAmount}" name="rollAmount" ></input>
                                    <input type="hidden" value="${turnList}" name="turnList" ></input>
                                
                                    <c:if test="${roll.rollFinished()}">
                                        <h2>FINAL TURNO</h2>
                                        <input type="hidden" value="True" name="newTurn" ></input>
                                        <input type="submit" value="FINALIZAR TURNO" >
                                    </c:if>
                                </form:form>
                            </c:if>
                            
                            <table id="dicesTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                    <c:forEach items="${roll.values}" var="dice">
                                        
                                            <td>                                                   
                                                    <c:out value="${dice}" /> 
                                            </td>
                                        
                                    </c:forEach>
                                    </tr>
                                </tbody>
                            </table>

                            <input type="hidden" value="${turnList}" name="turnList" ></input>

                            <input type="hidden" value="${roll.values}" name="values" ></input>
                            





                            <h2>Players</h2>

                            <table id="playersTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Monster Name</th>
                                        <th style="width: 120px;">Life Points</th>
                                        <th style="width: 120px">Victory Points</th>
                                        <th style="width: 120px">Energy Points</th>
                                        <th style="width: 150px">Location</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${players}" var="player">
                                        <tr>
                                            <td>
                                                <c:out value="${player.monsterName}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${player.lifePoints}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${player.victoryPoints}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${player.energyPoints}" />
                                            </td>
                                            <td>
                                                <c:out value="${player.location}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>


                            <table id="cardsTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Card Name</th>
                                        <th style="width: 120px;">Cost</th>
                                        <th style="width: 120px">Type</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${cards}" var="card">
                                        <tr>
                                            <td>
                                                <c:out value="${card.name}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${card.cost}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${card.type}" />
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>


                        </petclinic:layout>