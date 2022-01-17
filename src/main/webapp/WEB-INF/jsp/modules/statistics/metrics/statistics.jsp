<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

        <kingoftokyo:layout pageName="statistics">
            <div class="container">
                 <div class="row">
                    <h1 style="display: flex;flex-direction: column;justify-content: center;text-align: center; margin-bottom: 0px;
                                margin-bottom: 14px;">
                    ESTADISTICAS</h1>
                </div>
                <div class="row">
                    <div class="col col-sm-6" style="align-items: center;justify-content: center;display: flex;">
                        <h4>
                            Ranking usuarios
                            
                        </h4>
                    </div>
                    <div class="col col-sm-6" style="align-items: center;justify-content: center;display: flex;">
                        <h4>
                            Estadisticas del juego
                        </h4>
                    </div>

                </div>
                <div class="row" >
                    <div class="col col-sm-6">
                        <hr style="margin-right: 17%;margin-left: 18%;">
                    </div>
                    <div class="col col-sm-6">
                        <hr style="margin-left: 17%;margin-right: 13%;">
                    </div>
                </div>
                <div class="row">
                    <div class="col col-sm-3" style="align-items: center;justify-content: center;display: grid;">
                        <h4>
                            Mas partidas ganadas
                        </h4>

                        <table class="table">
                            <tbody>
                                <c:forEach items="${listWinsRanking}" var="list" varStatus="status">
                                    <tr>
                                        <th>
                                            <c:out value="${status.index+1}" />
                                            </a>
                                        </th>
                                        <th>
                                            <c:out value="${list.user.username}" />
                                            </a>
                                        </th>
                                        <th>
                                            <c:out value="${list.score}" />
                                        </th>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            
                        </table>
                        <a href="/statistics/ranking"><button type="button" >Ranking de jugadores</button></a>
                    </div>
                    <div class="col col-sm-3" style="align-items: center;justify-content: center;display: grid;">
                        <h4>
                            Mas puntuacion de logros
                        </h4>
                        <table class="table">
                            <tbody>
                                <c:forEach items="${listScoresRanking}" var="list" varStatus="status">
                                    <tr>
                                        <th>
                                            <c:out value="${status.index+1}" />
                                            </a>
                                        </th>
                                        <th>
                                            <c:out value="${list.user.username}" />
                                            </a>
                                        </th>
                                        <th>
                                            <c:out value="${list.score}" />
                                        </th>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="col col-sm-2" style="align-items: center;justify-content: center;display: grid; margin-left: 5%;">
                        <div class="row" style="align-items: center;justify-content: center;display: grid;">
                            <h4 style="margin-left: 3px;">    
                                Numero de partidas totales que se han jugado
                            </h4>
                            <div class="row" style="align-items: center;justify-content: center;display: grid; 
                            border: 1px #7800bd solid; border-radius: 5px">
                                <h4>
                                     <c:out value="${totalGames}" /> partidas
                                </h4>
                            </div>
                        </div>
                        <div class="row">
                            <h4>    
                                Criatura elegida con mas frecuencia
                            </h4>
                            <div class="row" style="align-items: center;justify-content: center;display: grid;
                            border: 1px #7800bd solid; border-radius: 5px">
                                <h2>
                                    <c:out value="${modaMonstername}" />
                                </h2>
                                <h4>
                                    <img src="${modaMonstericon}" width="80" height="80">
                                </h4>
                            </div>
                        </div>
                        <div class="row">
                            <h4>    
                                Criatura elegida con menos frecuencia
                            </h4>
                            <div class="row" style="align-items: center;justify-content: center;display: grid;
                            border: 1px #7800bd solid; border-radius: 5px">
                                <h2>
                                    <c:out value="${nomodaMonstername}" />
                                </h2>
                                <h4>
                                    <img src="${nomodaMonstericon}" width="80" height="80">
                                </h4>
                            </div>
                        </div>
                    </div>
                    <div class="col col-sm-1" style="margin-left: 5px;"></div>
                    <div class="col col-sm-2" style="align-items: center;justify-content: center;display: grid;">
                        <div class="row" style="align-items: center;justify-content: center;display: grid;">
                            <h4 style="margin-left: 3px;">    
                                Tiempo medio por partida
                            </h4>
                            <div class="row" style="align-items: center;justify-content: center;display: grid;
                            border: 1px #7800bd solid; border-radius: 5px">
                                <h4>
                                     <c:out value="${mediumGameTime}" /> minutos
                                </h4>
                            </div>
                        </div>
                        <div class="row">
                            <h4>    
                                Jugadores que mas tiempo han estado dentro de tokyo
                                <table class="table">
                                    <tbody>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                    </tbody>
                                </table>
                            </h4>
                        </div>
                    </div>
                    

                </div>
                
            </div>
           
        </kingoftokyo:layout>