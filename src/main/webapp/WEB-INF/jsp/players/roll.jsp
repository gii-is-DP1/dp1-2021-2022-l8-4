<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                        <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                        <petclinic:layout pageName="dices">
                            <h2>Roll</h2>
                            
                            <c:out value="${rollAmount}" />
                            <form:form modelAttribute="player">
                                <table>
                                    <tr>
                                        <td>
                                            <form:checkboxes items="${dices}" path="keep"/>  
                                        </td>
                                    </tr>
                                </table>
                                <input type="hidden" value="${rollAmount}" name="rollAmount" ></input>
                               <input type="submit" label="CONSERVAR DADOS" >
                            </form:form>

                            <table id="dicesTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${dices}" var="dice">
                                        <tr>
                                            <td>                                                   
                                                    <c:out value="${dice}" /> 
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </petclinic:layout>