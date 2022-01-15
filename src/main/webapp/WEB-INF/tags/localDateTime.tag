<%@ attribute name="dateTimeValue" required="true" description="Parsed localDateTime variable" type="java.time.LocalDateTime" %>
<%@ tag import="java.time.format.DateTimeFormatter,java.time.format.FormatStyle" %>

<div>
    <%
        String parsedDate = dateTimeValue.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)); 
        out.print(parsedDate);
    %>
</div>