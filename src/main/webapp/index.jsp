<%@ page import="com.mj.util.Dbutil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<header>
    <meta charset="utf-8">
    <title>java maven web</title>
</header>
<body>
<%
    Dbutil db = new Dbutil();
    pageContext.setAttribute("testone",db.query("st"));
%>
<hr>
<c:forEach items="${testone}" var="m">
    ${m.name}<br>
</c:forEach>
<hr>
<a href="show">show</a><br><br>
<a href="ab">ab</a><br><br>
<h2>Hello World!</h2>
<%
    out.print(2*2);
%>
<hr>
<c:forEach begin="1" end="10" var="i">
    <h3>${i}</h3>
</c:forEach>
<hr>
</body>
</html>
