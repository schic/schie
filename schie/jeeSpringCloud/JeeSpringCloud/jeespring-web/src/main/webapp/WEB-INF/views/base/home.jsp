<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="default"/>
    <!--%@ include file="/WEB-INF/views/include/head.jsp"%-->
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta name="author" content="http://www.jeespring.org/"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10,IE=11" />
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <!-- 引入jquery插件 -->
    <link href="${ctxStatic}/common/jeespring.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/common/css/style.css?v=3.2.0" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet">
    <!--script type="text/javascript">
        $(document).ready(function() {WinMove();});
    </script-->
</head>
<body class="gray-bg">
</div>
<div class="row  border-bottom white-bg dashboard-header">
    <div class="col-sm-12">
        <blockquote class="text-info" style="font-size:14px">大众认同、大众参与、成就大众、大众分享的开发平台。
            JeeSpring开发平台采用 SpringBoot+Redis+ActiveMQ+SpringMVC + MyBatis + BootStrap + Apache Shiro + Ehcache 开发组件的基础架构.
            JeeSpring基于SpringBoot+SpringMVC+Mybatis+Redis+SpringCloud微服务分布式代码生成的敏捷开发系统架构。项目代码简洁,注释丰富,上手容易,还同时集中分布式、微服务,同时包含许多基础模块(用户管理,角色管理,部门管理,字典管理等10个模块。成为大众认同、大众参与、成就大众、大众分享的开发平台。JeeSpring官方qq群(328910546)。代码生成前端界面、底层代码（spring mvc、mybatis、Spring boot、Spring Cloud、微服务的生成）、安全框架、视图框架、服务端验证、任务调度、持久层框架、数据库连接池、缓存框架、日志管理、IM等核心技术。努力用心为大中小型企业打造全方位J2EE企业级平台ORM/Redis/Service仓库开发解决方案。一个RepositoryService仓库就直接实现dubbo、微服务、基础服务器对接接口和实现。
            努力用心为大中小型企业打造全方位J2EE企业级平台开发解决方案。
            Spring Boot/Spring cloud微服务是利用云平台开发企业应用程序的最新技术，它是小型、轻量和过程驱动的组件。微服务适合设计可扩展、易于维护的应用程序。它可以使开发更容易，还能使资源得到最佳利用。<br>
            <a class="btn btn-white btn-bitbucket" href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud" target="_blank">
                <i class="fa fa-cloud"> </i> 访问码云
            </a>            <a class="btn btn-white btn-bitbucket" href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/wikis/pages" target="_blank">
                <i class="fa fa-home"></i> 在线文档
            </a>
            <a class="btn btn-white btn-bitbucket" href="http://yocity.imwork.net:10858/admin?login" target="_blank">
                <i class="fa fa-cloud"> </i> 访问演示版
            </a>
            <a class="btn btn-white btn-bitbucket" href="http://www.jeespring.icoc.bz/" target="_blank">
                <i class="fa fa-home"></i> 访问官网
            </a>
            <a class="btn btn-white btn-bitbucket" href="https://jeespring.kf5.com/hc/community/topic/" target="_blank">
                <i class="fa fa-home"></i> 社区论坛
            </a>
            <a class="btn btn-white btn-bitbucket" href="https://jeespring.kf5.com/hc/request/guest/" target="_blank">
                <i class="fa fa-home"></i> 提交咨询
            </a>
            <a class="btn btn-white btn-bitbucket" href="https://jeespring.kf5.com/hc/" target="_blank">
                <i class="fa fa-home"></i> 帮助
            </a>
            <br>
            开源版：<br>包含定时任务调度、服务器监控、平台监控、异常邮件监控、服务器Down机邮件监控、平台设置、开发平台、邮件监控、图表监控、地图监控、单点登录、Redis分布式高速缓存、ActiveMQ队列、会员、营销、在线用户、日志、在线人数、访问次数、调用次数、直接集群、接口文档、生成模块、代码实例、安装视频、教程文档、springCloud、RedisMQ队列（待开发）、代码生成(增删改查单表、redis高速缓存对接代码、增删改查云接口、dubbo、图表统计、地图统计、vue.js)、工作流、Dubbo、CMS<br>
            企业版：<br>包含开源版、代码生成(单表、主附表、树表、列表和表单、增删改查云接口、redis高速缓存对接代码、dubbo、图表统计、地图统计、vue.js)<br><a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/wikis/pages?title=JeeSpringCloud-%E4%BC%81%E4%B8%9A%E7%89%88&parent=" class="btn btn-white btn-bitbucket" target="_blank"><i class="fa fa-cloud"></i>企业版源码获取(开源不易！)</a><br>互联网整合资源：<br>
            兑吧积分平台；极光推送；凡科营销和站点；活动汇；现场活动平台；有投票活动平台；云客服平台（客服模块、论坛社区、提交咨询、帮助）；<br>系统配置可开启正式版！功能全开源！每周周一再更新！<br></blockquote>
        <hr>
    </div>
    <div class="col-sm-12"><div class="alert alert-warning" style="width: 90%;">捐赠榜:<br>  快乐医生^_^捐赠170元;厚德静邦捐赠10元;饮水思源捐赠100元;狼灵捐赠66元;zhengfk捐赠20元;Dobukle捐赠20元;成长!捐赠100元; jackkang捐赠20元;睡睡鱼捐赠20元;芋头捐赠30元;明枫捐赠168元; Lucifer捐赠168元;等等...</div>

    </div><div class="col-sm-3">
    <h2>JeeSpringCloud支付宝红包</h2>
    <!--small>移动设备访问请扫描以下二维码：</small-->
    <small>【JeeSpringCloud支付宝红包】打开支付宝首页搜“598352815”领红包，领到大红包的小伙伴赶紧使用哦！</small>
    <img src="https://images.gitee.com/uploads/images/2018/1206/183122_78bcfa61_132236.jpeg" style="width:70%">
    <br>
</div>
    <div class="col-sm-3">
        <h2>JeeSpring框架</h2>
        <p>JeeSpring框架为你所想，为你所做。同时支持移动客户端访问。系统会陆续更新一些实用功能。</p>
        <p>
            <b>当前版本：</b><span>v2.5.0</span>
        </p>
        <p>
            <span class="label label-warning">¥免费开源</span>
            <span class="label label-warning">文档视频齐全</span>
        </p>
        <p>
            <i class="fa fa-qq"></i> JeeSpring官方QQ群：</p><p>
        <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=b07802df867a4047251ac62b35698ed2d94b31f95599609eff4eabcbe361c402">一群:328910546(已满)</a>
    </p>
        <p>
            <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=c22bb08a809d458e50b6e21f1a3f2845755fca5984895c5538292fceab1655b2">二群：756355483（群内领资料）</a>
        </p>
        <p>
            <i class="fa fa-qq"></i> JeeSpring官方QQ群(VIP)：
            <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=8b9e4ac088244d5c852b999156a3489b08fd5d3adea750b7d494f76904f563fe">558699173</a>
        </p>
        <p>
            <a class="btn btn-white btn-bitbucket" href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud" target="_blank">
                <i class="fa fa-cloud"> </i> 访问码云
            </a>
            <a class="btn btn-white btn-bitbucket" href="http://yocity.imwork.net:10858/admin?login" target="_blank">
                <i class="fa fa-cloud"> </i> 访问演示版
            </a>
            <a class="btn btn-white btn-bitbucket" href="http://www.jeespring.icoc.bz/" target="_blank">
                <i class="fa fa-home"></i> 访问官网
            </a>
            <a class="btn btn-white btn-bitbucket" href="https://jeespring.kf5.com/hc/community/topic/" target="_blank">
                <i class="fa fa-home"></i> 社区论坛
            </a>
            <a class="btn btn-white btn-bitbucket" href="https://jeespring.kf5.com/hc/request/guest/" target="_blank">
                <i class="fa fa-home"></i> 提交咨询
            </a>
            <a class="btn btn-white btn-bitbucket" href="https://jeespring.kf5.com/hc/" target="_blank">
                <i class="fa fa-home"></i> 帮助
            </a>
        </p>
        <p>
    </div>
    <div class="col-sm-3">
        <div class="alert alert-warning" style="width: 80%;">请作者喝杯茶。(开源不易！)</div>
        <img src="https://images.gitee.com/uploads/images/2018/1024/095334_ba74dce3_132236.jpeg" width="80%" alt="请使用手机支付宝扫码支付">
        </p>
    </div>
    <div class="col-sm-3">
        <h4>技术选型：</h4>
        <ol>
            <li>核心框架：Spring Boot。</li>
            <li>安全框架：Apache Shiro。</li>
            <li>持久层框架：MyBatis。</li>
            <li>代码生成:代码生成（前端界面、底层代码、微服务的生成）,根据表生成对应的Entity,Service,Dao,Action,JSP,Redis,APP接口等。</li>
            <li>数据库连接池：Druid。</li>
            <li>工具类：Fastjson。</li>
            <li>更多……</li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>平台视频介绍</h5>
                    <div style="text-align: center;font-size: 14px;color: #666;">${oaNotify.title}</div><br/>
                </div>
                <div class="ibox-content">
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpringCloud介绍.docx</a><br><br>
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpringCloud介绍功能初级.docx</a><br><br>
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpringCloud中级培训文档.docx</a><br><br>
                    <video id="vid1" loop="loop" width="100%" height="100%" onended=""  muted="muted" autobuffer="autobuffer" preload="auto" oncontextmenu="return false" data-hasaudio=""
                           style="background-color: white;opacity: 1;visibility: visible;height: 100%;width: 100%;object-fit:cover;object-position: center center;"
                           src="../staticViews/index/images/flat-avatar1.mp4" controls></video>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>平台视频部署</h5>
                    <div style="text-align: center;font-size: 14px;color: #666;">${oaNotify.title}</div><br/>
                </div>
                <div class="ibox-content">
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpringCloud部署初级.docx</a><br><br>
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpringCloud部署高级.docx</a><br><br>
                    <video id="vid2" loop="loop" width="100%" height="100%" onended="" muted="muted" autobuffer="autobuffer" preload="auto" oncontextmenu="return false" data-hasaudio=""
                           style="background-color: white;opacity: 1;visibility: visible;height: 100%;width: 100%;object-fit:cover;object-position: center center;"
                           src="../staticViews/index/images/flat-avatar1.mp4" controls></video>
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpring部署初级.mp4</a><br><br>
                    <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud/attach_files" target="_blank">JeeSpring部署高级.mp4</a><br><br>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>平台视频教程</h5>
                    <div style="text-align: center;font-size: 14px;color: #666;">${oaNotify.title}</div><br/>
                </div>
                <div class="ibox-content">
                    <video id="vid3" loop="loop" width="100%" height="100%" onended=""  muted="muted" autobuffer="autobuffer" preload="auto" oncontextmenu="return false" data-hasaudio=""
                           style="background-color: white;opacity: 1;visibility: visible;height: 100%;width: 100%;object-fit:cover;object-position: center center;"
                           src="../staticViews/index/images/flat-avatar1.mp4" controls></video>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-4">

            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>公告通知</h5>
                    <div style="text-align: center;font-size: 14px;color: #666;">${oaNotify.title}</div><br/>
                </div>
                <div class="ibox-content">
                    <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                        <ol>
                            <c:forEach items="${pageOaNotify.list}" var="oaNotify">
                                <li>${fns:abbr(oaNotify.title,50)}         ${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
                                    <fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li></br>
                            </c:forEach>
                        </ol>
                        <hr>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>升级日志</h5> <span class="label label-primary">K+</span>
                </div>
                <div class="ibox-content">
                    <div class="panel-body">
                        <div class="panel-group" id="version">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h5 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#version" href="#v1.0"></a><code class="pull-right"></code>
                                    </h5>
                                </div>
                                <div id="v2.0" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <ol>
                                            <c:forEach items="${pageOaNotifyLog.list}" var="oaNotify">
                                                <li>${fns:abbr(oaNotify.title,50)}         ${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
                                                    <fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li></br>
                                            </c:forEach>
                                        </ol>
                                        <hr>
                                        ${indexTopicsShowList.description}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>技术支持</h5>
                </div>
                <div class="ibox-content">
                    <ol>
                        <c:forEach items="${pageOaNotifyTechnology.list}" var="oaNotify">
                            <li>${fns:abbr(oaNotify.title,50)}         ${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
                                <fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li></br>
                        </c:forEach>
                    </ol>
                    <hr>


                </div>
            </div>
         </div>
     </div>
    <div class="row">
        <div class="col-sm-4 ui-sortable">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>JeeSpring 技术特点</h5>
                </div>
                <div class="ibox-content" style="display: block;">
                    <ol>
                        <li>使用目前流行的多种web技术，包括spring boot spring mvc、mybatis。</li><li>Spring cloud 分布式、微服务、集群、zookeper、nignx。</li><li>代码生成（前端界面、底层代码、微服务的生成）,根据表生成对应的Entity,Service,Dao,Action,JSP,Redis,APP接口等,。</li><li>RepositoryORM仓库,提供ORM接口和多种实现,可进行配置实现。</li><li>RepositoryRedis仓库,提供Redis接口和多种实现,可进行配置实现。可以配置调用单机、redis、云redis对接。</li><li>RepositoryService仓库,提供Service接口和多种实现,可进行配置实现。可以配置调用dubbo、微服务、基础服务器对接接口和实现。</li><li>查询过滤器，查询功能自动生成，后台动态拼SQL追加查询条件；支持多种匹配方式（全匹配/模糊查询/包含查询/不匹配查询） </li><li>系统日志监控，详细记录操作日志，可支持追查表修改日志</li><li>WebSocket集成：集成在线聊天系统。</li><li>提供常用工具类封装，日志、缓存、验证、字典、组织机构等，常用标签（taglib），获取当前组织机构、字典等数据。</li><li>连接池监视：监视当期系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。</li><li>支持数据库: Mysql,Oracle数据库的支持，但不限于数据库，平台留有其它数据库支持接口等</li><li>要求JDK1.6+</li>
                    </ol>
                </div>
            </div>

        </div>
        <div class="col-sm-4 ui-sortable">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>开源授权</h5>
                </div>
                <div class="ibox-content">
                    <ol>
                        <li>已开源的代码，授权协议采用 AGPL v3 + Apache Licence v2 进行发行。</li>
                        <li>您可以免费使用、修改和衍生代码，但不允许修改后和衍生的代码做为闭源软件发布。</li>
                        <li>修改后和衍生的代码必须也按照AGPL协议进行流通，对修改后和衍生的代码必须向社会公开。</li><h2 id="-">版本表：</h2>
                        <table class="table-bordered table-striped" style="width:100%">
                            <thead>
                            <tr>
                                <th style="width:40%">服务/功能</th>
                                <th style="width:30%">开源版</th>
                                <th style="width:30%">企业版</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>即时沟通群</td>
                                <td>官方交流群</td>
                                <td>VIP讨论组</td>
                            </tr>
                            <tr>
                                <td>程序代码</td>
                                <td>码云</td>
                                <td>云盘</td>
                            </tr>
                            <tr>
                                <td>底层代码</td>
                                <td>全开源</td>
                                <td>全开源</td>
                            </tr>
                            <tr>
                                <td>商业使用</td>
                                <td>可以商用</td>
                                <td>可以商用</td>
                            </tr>
                            <tr>
                                <td>视频文档教程</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>安全性相关设置</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>自定义视图皮肤</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* 在线作业调度</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* CMS</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* 工作流</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* 队列</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* 高速缓存Redis</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* dubbo</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* 代码生成(单表)<br>(列表和表单、增删改查云接口、redis高速缓存对接代码、图表统计、地图统计、vue.js)</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* 代码生成(主附表、树表)<br>(列表和表单、增删改查云接口、redis高速缓存对接代码、图表统计、地图统计、vue.js)</td>
                                <td>×</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>* CAS单点登录</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>** Session集群</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>** 服务器监控</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>** 异常邮件监控</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            </tr>
                            <tr>
                                <td>** 服务器Down机邮件监控</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            </tr>
                            <tr>
                                <td>** 直接集群</td>
                                <td>√</td>
                                <td>√</td>
                            </tr>
                            <tr>
                                <td>** 捐献获取</td>
                                <td>免费</td>
                                <td>捐献168元(良心价！开源不易！)</td>
                            </tr>
                            </tbody>
                        </table>
                        (捐献168元！良心价！开源不易！)<br>
                        <img src="https://images.gitee.com/uploads/images/2018/1024/095334_ba74dce3_132236.jpeg" width="50%" alt="请使用手机支付宝扫码支付"><br>
                        *****扫码时请备注您的邮箱！*****<br>
                        *****项目文件下载地址将以邮件发送给您！*****<br>
                        *****如需获取帮助请加JeeSpringCloud的QQ群<br>
                    </ol>
                    <hr>
                </div>
            </div>
        </div>
        <div class="col-sm-4 ui-sortable">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>联系信息</h5>
                </div>
                <div class="ibox-content">
                    <p>
                        <i class="fa fa-send-o"></i> 网址：<a href="http://www.jeespring.icoc.bz" target="_blank">http://www.jeespring.icoc.bz/</a>
                    </p>
                    <p>
                        <a class="btn btn-success btn-outline" href="https://gitee.com/JeeHuangBingGui/JeeSpring" target="_blank">
                            <i class="fa fa-cloud"> </i> 访问码云
                        </a>
                    </p>
                    <p>
                        <i class="fa fa-qq"></i> <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=b07802df867a4047251ac62b35698ed2d94b31f95599609eff4eabcbe361c402">JeeSpring官方QQ群：328910546（已满）</a>
                    </p>
                    <p>
                        <i class="fa fa-qq"></i> <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=c22bb08a809d458e50b6e21f1a3f2845755fca5984895c5538292fceab1655b2">二群：756355483（群内领资料）</a>
                    </p>
                    <p>
                        <i class="fa fa-qq"></i> JeeSpring官方QQ群(VIP)：558699173
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>