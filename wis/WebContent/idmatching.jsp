<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dao.*" %>
<%@ page import="management.*" %>
<%@ page import="list.*" %>
<%@ page import="dto.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
  <head>
   
    <title>GrouperService</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
	
	<script type="text/javascript">
	
   function init(){
	   	$("#dialog").append("<table border='1'>");
    	  $("#dialog").append("<tr width='30%'>");
    	  $("#dialog").append("<td width='10%'>회원id</td>");
    	  $("#dialog").append("<td width='20%'>cgmid</td>");
    	  $("#dialog").append("</tr>");
    	  
   }
   
   function post_to_url(path, params, method){
       method = method || "post";
       var form = document.createElement("form");
       alert(params);
       form.setAttribute("method", method);
       form.setAttribute("action", path);
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "check");
        hiddenField.setAttribute("value", params);
		 form.appendChild(hiddenField);
       
       document.body.appendChild(form);
       form.submit();
   }
   
 
	function search(mail){
		$("#dialog").empty();
		init();
		$.ajax({
		      type:"POST",
		      url:"memberview.do",
		      data:"mail="+mail,
		      dataType: "json",
		      success: function(msg){  //리턴 받은것 
			      	for(var i in msg){
			      		$("#dialog").append("<tr>");
			      		$("#dialog").append("<td>"+msg[i].teamName+"</td>");
			      		$("#dialog").append("<td>"+msg[i].power+"</td>");
			      		$("#dialog").append("</tr>");
			     	 }
			      	$("#dialog").append("</table>");
			      	$('#dialog').dialog("open");
		      }
		  });	
	}
	
	function get_chked_values(){
		  var chked_val = "";
		  $(":checkbox[name='check']:checked").each(function(pi,po){
		    chked_val += ","+po.value;
		  });
		  if(chked_val!="")chked_val = chked_val.substring(1);
		  return chked_val;
		}
	
	function delconfirm(){
		var ok=confirm('정말로 삭제하시겠습니까?');
		if(ok){
			 var check= get_chked_values();
			 post_to_url('memberdelete.do',check);
			return true;
		}
		else{
			return false;
		}
			
		
	}
	
	    $(document).ready(function() {
            $('#dialog').dialog({
                buttons: [{text: "OK", click: addDataToTable}],
                modal: true,
                autoOpen: false,
                width: 480,
                height:300
            });
            
            function addDataToTable() {
                $('#dialog').dialog("close");
            }
        });


	  function showDia(){
              $('#dialog').dialog("open");
	  }
	

	</script>
	
	
    <!-- Le styles -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="./assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="./assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="./assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="./assets/ico/apple-touch-icon-57-precomposed.png">
    <link rel="shortcut icon" href="./assets/ico/favicon.png">

<style type="text/css">
body {
	text-align: center;
	width: 900px;
}

div {
	width: 800px;
	text-align: center;
	display: inline-block;
}

fieldset {
	width: 300px;
	display: inline-block;
}
</style>
</head>
<body>
	<div>
		<h1>With Schedule 관리프로그램</h1>
		<div id="dd">
			<form method="post" >
				<p>
					이메일<input type="text" name="inputText" id="searchText" /> <input
					type="submit" value="검색하기" id="searchBtn" />				</p>
			</form>
			<table border="1px" width="100%" id="membertb">
				<tr align="center" bgColor="#ffffff" id="tbhead">
					<td width="10%" height="35">회원id</td>
					<td width="10%" height="35">cgmid</td>
				</tr>
				<c:forEach var="ci" items="${gcmmap}">
					<tr>
						<td>${ci.key}</td>
						<td>${ci.value}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>
