<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        .title td{
            float: left;
            width:100px;
            height:30px;
            line-height: 30px;
            align-content: center;
        }
        .sendDate{
            width:300px!important;
        }
        .saveOrderButton{
            background-color: #3bb7ff;
            display: block;
            margin: auto;
            width:40px;
            height:30px;
        }
    </style>
</head>
<body>

<table>
    <thead>
        <tr class="title">
            <td>
                id<span><input type="checkbox" class="selectThis" value="id"/></span>
            </td>
            <td class="sendDate">
                发送日期<span><input type="checkbox" class="selectThis" value="sendDate"/></span>
            </td>
            <td>
                手机号<span><input type="checkbox" class="selectThis"  value="phone"/></span>
            </td>
            <td>
                验证码<span><input type="checkbox" class="selectThis"  value="codeContent"/></span>
            </td>
            <td>
                提交时间<span><input type="checkbox" class="selectThis"  value="subDate"/></span>
            </td>
            <td>
                来源<span><input type="checkbox" class="selectThis"  value="source"/></span>
            </td>
        </tr>
    </thead>
    <tbody>
        <s:iterator  value="verCodeList"    id="a"  >
        <tr  class="title">
            <td>${id}</td>
            <td class="sendDate">${sendDate}</td>
            <td>${phone}</td>
            <td>${codeContent}</td>
            <td>${subDate}</td>
            <td>${source}</td>
        </tr>
        </s:iterator>
    </tbody>
</table>
<%--<input type="button" class="saveOrderButton" value="预览" onclick="showOUtResult();"/>--%>
<input type="button" class="saveOrderButton" value="导出"/>
<script src="../../js/jquery.min.js"></script>
<script src="../../js/healifeCommon.js" ></script>
<script>
    $(function () {
        /**
         * 导出数据到excel表格，并实现下载功能
         */
        function outExcelDate(){
            //获取到所有选中需要展示的字段
            var a = $(".selectThis");
            var array = [];
            for(var i = 0 ; i < a.length ; i++){
                if($(a[i]).is(':checked')){
                    array.push($(a[i]).val());
                }
            }
            //console.log(JSON.stringify(array));

            var date = JSON.stringify(array);
            //location.href = "../outDate/outDateToExcel?selectTitle="+date;

            var form=$("<form>");//定义一个form表单
            form.attr("style","display:none");
            form.attr("target","");
            form.attr("method","post");
            form.attr("action","../outDate/outDateToExcel");
            var input1=$("<input>");
            input1.attr("type","hidden");
            input1.attr("name","data");
            input1.attr("value",date);
            $("body").append(form);//将表单放置在web中
            form.append(input1);
            form.submit();//表单提交
        }

        $(".saveOrderButton").click(function () {
            outExcelDate();
        });
    })
</script>
</body>
</html>