<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>通道管理</title>
    <%@ include file="/WEB-INF/views/include/headMeta.jsp" %>
    <%@ include file="/WEB-INF/views/include/headCss.jsp" %>
    <%@ include file="/WEB-INF/views/include/headJs.jsp" %>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
</head><style>
    .notice {
        width: 100%;
        border: 1px solid #ddd;
        /* 盒子的总宽度w=298+1+1=300px */
        height: 1000px;
        overflow: hidden;
        /* 点击导航栏第一个或最后一个标签时，超出的部分隐藏*/
        margin: 10px;
    }

    .notice-tit {
        position: relative;
        height: 28px;
    }

    .notice-tit ul {
        position: absolute;
        width: 301px;
        left: -1px;
        /* 当点击第一个导航栏标签时，左边边框会与大盒子的边框发生叠加，解决的方法利用定位让两个边框重合叠加在一起 */
        height: 27px;
        background: #f7f7f7;
        border-bottom: 1px solid #ddd;
    }

    .notice-tit ul li {
        float: left;
        width: 100px;
        padding: 0 1px;
        height: 27px;
        text-align: center;
        line-height: 27px;
        cursor: pointer;
    }

    .notice-tit ul li.selected {
        border-left: 1px solid #ddd;
        border-right: 1px solid #ddd;
        background: #373737;
        padding: 0;
        border-bottom: 1px solid #fff;
        color:#bce8f1;
    }

    .notice-con div {
        height: 600px;
        padding: 20px;
        overflow-y: auto;
        display: none;
    }
    .logo td{
        font-size: 20px;
    }


</style>


<body  style="padding:0px;margin:0px" >
<div class="notice">
    <div class="notice-tit" id="notice-tit">
        <ul>
            <li class="selected">当前通道状态</li>
            <li>历史通道状态</li>
        </ul>
    </div>
    <a id="btnBack" class="btn btn-default">返回</a>
    <div class="notice-con" id="notice-con">
        <div style="display:block">
            <table width="100%" cellpadding="0" cellspacing="0" border="1px" class="memb-table" id="datas" >
            </table>

        </div>
        <div><table width="100%" cellpadding="0" cellspacing="0" border="1px" class="memb-table" id="leftdatas"  class="table table-hover table-condensed dataTables-example dataTable">
        </table></div>

    </div>
</div>
<input type="hidden" value="<%=request.getAttribute("aaa")%>" id="aaa" style="display: none"/>
</body>

<script type="text/javascript">
    var id="${id}";
    $(document).ready(function () {
        onload();
        $('#notice-tit li').click(function() {
            $(this).siblings().removeClass('selected');
            $(this).addClass('selected');
            var index = $(this).index();  // 获取当前点击元素的下标
            // alert(index);
            $('#notice-con div').hide();
            $('#notice-con div').eq(index).show();
        })


    });



    function td_click(pid) {
        //获取ID号
        var id= pid.charAt(pid.length-1);
        // var data = "templatechildren"+id;
        //字符串拼接，模糊查询
        var idd = $('tr[id^=templatechildren'+id+']');
        if (idd[0].style.display=="none"){
            for (var i = 0; i <idd.length ; i++) {
                document.getElementById("templatechildren"+id+"-"+i).style.display="";
                document.getElementById("lefttemplatechildren"+id+"-"+i).style.display="";
            }
            return;
        }
        if (idd[0].style.display==""){
            for (var i = 0; i <idd.length ; i++) {
                document.getElementById("templatechildren"+id+"-"+i).style.display="none";
                document.getElementById("lefttemplatechildren"+id+"-"+i).style.display="none";
            }
            return;
        }
        // alert(idd[0].id+"===="+idd.length);

    }

    function onload() {
            $.ajax({
                url: "${ctx}/exchange/exChannel/treeData",
                type: "post",
                dataType: "json",
                data:{'id':id},
                success: function(data){
                    var datalist = JSON.parse(data.Channel);
                    //console.log(datalist);
                    var source=datalist;
                    //j =  通道个数
                    var j = datalist.list.dashboardStatus.length;
                    if(datalist.list.dashboardStatus && j == undefined){
                    	j = 1;
                    }
                    var newBody="";
                    newBody += "<tr class='logo' id=\"childtemplate\"><td id=\"pname\">"+"Name"+"</td>"
                        +"  <td id=\"pStatus\">"+"Status"+"</td>"
                        +"<td id=\"pReceived\">"+"Received"+"</td>"
                        +"<td id=\"pFiltered\">"+"Filtered"+"</td>"
                        +"<td id=\"pQueued\">"+"Queued"+"</td>"
                        +"<td id=\"pSent\">"+"Sent"+"</td>"
                        +"<td id=\"pErrored\">"+"Errored"+"</td></tr>"
                    ;
                    $("#datas").append(newBody);
                    $("#leftdatas").append(newBody);
                    for (var i = 0; i < j ; i++) {
                    	var dashboardStatus;
                    	if(j == 1){
                    		dashboardStatus = source.list.dashboardStatus[i];
                    		if(!dashboardStatus){
                    			dashboardStatus = source.list.dashboardStatus;
                    		}
                    	}
                    	
                        //当前通道
                        var tbBody = "";
                        tbBody += "<tr class=\"cdata"+i+"\" id=\"ptemplate"+i+"\"><td id=\"name"+i+"\" onClick=\"td_click(this.id)\">"+dashboardStatus.name+"</td>"
                            +"<td id=\"Status\">"+dashboardStatus.state+"</td>"
                            +"<td id=\"Received\">"+dashboardStatus.statistics.entry[0].long+"</td>"
                            +"<td id=\"Filtered\">"+dashboardStatus.statistics.entry[1].long+"</td>"
                            +"<td id=\"Queued\">"+dashboardStatus.queued+"</td>"
                            +"<td id=\"Sent\">"+dashboardStatus.statistics.entry[2].long+"</td>"
                            +"<td id=\"Errored\">"+dashboardStatus.statistics.entry[3].long+"</td>"
                            +"</tr>"
                        ;
                        $("#datas").append(tbBody);
                        var lefttbBody = "";
                        lefttbBody += "<tr class=\"cdata"+i+"\" id=\"ptemplate"+i+"\"><td id=\"name"+i+"\" onClick=\"td_click(this.id)\">"+dashboardStatus.name+"</td>"
                            +"<td id=\"Status\">"+dashboardStatus.state+"</td>"
                            +"<td id=\"Received\">"+dashboardStatus.lifetimeStatistics.entry[0].long+"</td>"
                            +"<td id=\"Filtered\">"+dashboardStatus.lifetimeStatistics.entry[1].long+"</td>"
                            +"<td id=\"Queued\">"+dashboardStatus.queued+"</td>"
                            +"<td id=\"Sent\">"+dashboardStatus.lifetimeStatistics.entry[2].long+"</td>"
                            +"<td id=\"Errored\">"+dashboardStatus.lifetimeStatistics.entry[3].long+"</td>"
                            +"</tr>"
                        ;
                        $("#leftdatas").append(lefttbBody);
                        // 通道下 Source，Destination个数
                        var h = dashboardStatus.childStatuses.dashboardStatus.length;
                        var result = new Array();
                        result=dashboardStatus.childStatuses.dashboardStatus;
                        for (var t = 0; t <h ; t++) {
                            var tbBody = "";
                            // $nbsp;
                            tbBody += "<tr id=\"templatechildren"+i+"-"+t+"\"><td id=\"name\"> "+"\xa0\xa0\xa0"+result[t].name+"</td>"
                                +"<td id="+t+"\"Status\">"+result[t].state+"</td>"
                                +"<td id=\"Received\">"+result[t].statistics.entry[0].long+"</td>"
                                +"<td id=\"Filtered\">"+result[t].statistics.entry[1].long+"</td>"
                                +"<td id=\"Queued\">"+result[t].queued+"</td>"
                                +"<td id=\"Sent\">"+result[t].statistics.entry[2].long+"</td>"
                                +"<td id=\"Errored\">"+result[t].statistics.entry[3].long+"</td>"
                                +"</tr>";
                            $("#datas").append(tbBody);

                            var lefttbBody = "";
                            var leftresult=dashboardStatus.childStatuses.dashboardStatus[t].lifetimeStatistics;
                            // \xa0空格符
                            lefttbBody += "<tr id=\"lefttemplatechildren"+i+"-"+t+"\"><td id=\"name\"> "+"\xa0\xa0\xa0"+result[t].name+"</td>"
                                +"<td id="+t+"\"Status\">"+result[t].state+"</td>"
                                +"<td id=\"Received\">"+leftresult.entry[0].long+"</td>"
                                +"<td id=\"Filtered\">"+leftresult.entry[1].long+"</td>"
                                +"<td id=\"Queued\">"+result[t].queued+"</td>"
                                +"<td id=\"Sent\">"+leftresult.entry[2].long+"</td>"
                                +"<td id=\"leftErrored"+i+"-"+t+"\">"+leftresult.entry[3].long+"</td>"
                                +"</tr>"
                            ;
                            // if (leftresult.entry[3].long!=0){
                            // document.getElementById("leftErrored"+i+"-"+t).style.color="#ff645d";
                            // $("leftErrored"+i+'-'+t).css("color","#ff645d");
                            // }
                            $("#leftdatas").append(lefttbBody);
                            
                            //默认展开
                            //document.getElementById("lefttemplatechildren"+i+"-"+t+"").style.display="none";
                            //document.getElementById("templatechildren"+i+"-"+t+"").style.display="none";
                        }

                    }
                    },
                error:function(data) {
                	top.layer.msg("连接失败或MC用户名密码错误，请求失败");
                    console.log(data);
                }
            });
    }
</script>
</html>
<%@ include file="/WEB-INF/views/include/footJs.jsp" %>


<script src="/staticViews/viewBase.js"></script>