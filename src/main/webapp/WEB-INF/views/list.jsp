<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.lang.String.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>员工列表</title>

    <%
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>
    <!-- 引入样式 -->
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
</head>
<body>
<%--搭建显示页面--%>
<div class="container">
    <%--标题--%>
    <div class="row">
        <div class="col-md-12">
            <h1>SSM-CRUD</h1>
        </div>
    </div>
    <%--按钮--%>
    <div class="row">
        <div class="col-md-4 col-md-offset-8">
            <button type="button" class="btn btn-primary">新增</button>
            <button type="button" class="btn btn-danger">删除</button>
        </div>

    </div>
    <%--显示数据--%>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover">
                <tr>
                    <th>#</th>
                    <th>empName</th>
                    <th>gender</th>
                    <th>email</th>
                    <th>deptName</th>
                    <th>操作</th>
                </tr>
                <tr>
                    <th>1</th>
                    <th>ww</th>
                    <th>M</th>
                    <th>sss@qq.com</th>
                    <th>部门</th>
                    <th>
                        <button type="button" class="btn btn-primary btn-sm" aria-label="Left Align">
                            <span class="glyphicon glyphicon-penci" aria-hidden="true"></span>
                            编辑
                        </button>
                        <button type="button" class="btn btn-danger btn-sm" aria-label="Left Align">
                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                            删除
                        </button>
                    </th>
                </tr>
            </table>
        </div>

    </div>
    <%--显示分页信息--%>
    <div class="row">
        <%--分页文字信息--%>
        <div class="col-md-6">
            当前记录数:
        </div>
         <%--分页条信息>--%>
         <div class="col-md-6">
             <nav aria-label="Page navigation">
                 <ul class="pagination">

                     <li><a href="#">首页</a></li>
                     <li>
                         <a href="#" aria-label="Previous">
                             <span aria-hidden="true">&laquo;</span>
                         </a>
                     </li>
                     <li><a href="#">1</a></li>
                     <li><a href="#">2</a></li>
                     <li><a href="#">3</a></li>
                     <li><a href="#">4</a></li>
                     <li><a href="#">5</a></li>
                     <li>
                         <a href="#" aria-label="Next">
                             <span aria-hidden="true">&raquo;</span>
                         </a>
                     </li>
                     <li><a href="#">末页</a></li>
                 </ul>
             </nav>
         </div>

    </div>


</div>
