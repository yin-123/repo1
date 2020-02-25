<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2020/2/17
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <title>Title</title>
    <%@ include file="/WEB-INF/common.jsp"%>
    <script src="${path }/resources/js/echarts.js"></script>
</head>
<body>
    <div id="chartmain" style="width: 600px;height: 400px;"></div>
<script type="text/javascript">
    //指定图标的配置和数据
    var option = {
        title:{
            text:'ECharts 数据统计'
        },
        tooltip:{},
        legend:{
            data:['用户来源']
        },
        xAxis:{
            data:["Android","IOS","PC","Ohter"]
        },
        yAxis:{

        },
        series:[{
            name:'访问量',
            type:'bar',
            data:[500,200,360,100]
        }]
    };
    //初始化echarts实例
    var myChart = echarts.init(document.getElementById('chartmain'));

    //使用制定的配置项和数据显示图表
    myChart.setOption(option);
</script>
</body>
</html>
