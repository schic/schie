/**
 *
 */
package com.schic.schie.modules.home;

import com.alibaba.fastjson.JSON;
import com.jeespring.common.config.Global;
import com.jeespring.common.spring.SpringUtils;
import com.jeespring.common.utils.DateUtils;
import com.jeespring.common.utils.bean.BeanUtils;
import com.schic.schie.modules.exbatchlog.entity.ExBatchLog;
import com.schic.schie.modules.exjob.entity.ExJobLog;
import com.schic.schie.modules.home.utils.NodeBgUtils;
import com.schic.schie.modules.home.utils.NodePicUtils;
import com.schic.schie.modules.home.utils.WSMessageCmdConst;
import com.schic.schie.modules.nodes.entity.ExNode;
import com.schic.schie.modules.onlinenode.entity.OnlineNode;
import com.schic.schie.modules.onlinenode.service.IOnlineNodeService;
import com.schic.schie.modules.websocket.HomeWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public final class HomeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeManager.class);

    private static final int SEND_SECONDS = 5;

    private static final String BEGIN_TIME = Global.getConfig("exjob.homebegin", "00:00:00");

    private static AtomicLong lastBeginTime = new AtomicLong(0);

    /**
     * 节点更新时间
     */
    private static AtomicLong updateTime = new AtomicLong(0);

    //key=节点id
    private static final ConcurrentMap<String, OnlineNodeVo> nodes = new ConcurrentHashMap<>();

    static {
        lastBeginTime.set(getBeginTime().getTime());
        //初始化所有节点
        initNodes();

        //启动timer
        // 1、每隔指定秒把变化的数据发送到客户端;
        // 2、定时检测是否重新初始化节点
        startTimerSendChanged();

        //mc无法使用通道定时发送消息
        //暂时每隔1分钟去调用mc的接口来判断是否在线，这就要求mc都能网络通
        startTimerCheckOnline();
    }

    /**
     * 获取当前数据开始时间
     *
     * @return
     */
    private static Date getBeginTime() {
        Date begin = DateUtils.parseDate(DateUtils.getDate() + " " + BEGIN_TIME);
        if (begin.getTime() > new Date().getTime()) {
            return DateUtils.addDays(begin, -1);
        }
        return begin;
    }

    private static void startTimerSendChanged() {
        Timer timer = new Timer("websocket检查变化发送到客户端定时器", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendChangedNodes();
                } catch (Exception e) {
                    LOGGER.error("websocket发送变化数据失败", e);
                }
                try {
                    checkReInitNodes();
                } catch (Exception e) {
                    LOGGER.error("websocket检测初始化节点失败", e);
                }
            }
        }, 0, 1000 * SEND_SECONDS);
    }

    private static void startTimerCheckOnline() {
        Timer timer = new Timer("websocket检测mc节点在线定时器", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkOnline();
                } catch (Exception e) {
                    LOGGER.error("websocket检测mc节点在线失败", e);
                }
            }
        }, 0, 60000);
    }

    /**
     * mc节点的在线检测
     */
    private static void checkOnline() {
        Iterator<Map.Entry<String, OnlineNodeVo>> iterator = nodes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, OnlineNodeVo> entry = iterator.next();
            OnlineNodeVo onlineNodeVo = entry.getValue();
            if (NodeOnlineCheck.check(entry.getKey())) {
                if (!onlineNodeVo.getOnline().getAndSet(true)) {
                    onlineNodeVo.getChanged().set(true);
                }
            } else {
                if (onlineNodeVo.getOnline().getAndSet(false)) {
                    onlineNodeVo.getChanged().set(true);
                }
            }
        }


    }


    /**
     * 检测是否需要重新初始化节点
     */
    private static void checkReInitNodes() {
        Date begin = getBeginTime();
        if (begin.getTime() <= lastBeginTime.get()) {
            return;
        }
        lastBeginTime.set(begin.getTime());
        initNodes();
        //初始化完成后，需要全部发给客户端
    }

    /**
     * 发送改变的节点信息到客户端
     */
    private static void sendChangedNodes() {
        List<OnlineNodeVo> list = new ArrayList<>();
        Iterator<Map.Entry<String, OnlineNodeVo>> iterator = nodes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, OnlineNodeVo> entry = iterator.next();
            if (entry.getValue().getChanged().getAndSet(false)) {
                list.add(entry.getValue());
            }
        }
        if (!list.isEmpty()) {
            HomeWebSocket.sendInfo(JSON.toJSONString(new WSMessageBody(WSMessageCmdConst.CMD_UPDATENODE, list,
                    DateUtils.formatDateTime(getLastBeginTime()), DateUtils.formatDateTime(new Date(updateTime.get())))));
        }
    }

    private static void initNodes() {
        try {
            updateTime.set(new Date().getTime());
            nodes.clear();
            IOnlineNodeService onlineNodeService = SpringUtils.getBean(IOnlineNodeService.class);
            OnlineNode onlineNode = new OnlineNode();
            onlineNode.setBeginCreateDate(getLastBeginTime());
            List<OnlineNode> listNode = onlineNodeService.findList(onlineNode);
            for (int i = 0; i < listNode.size(); i++) {
                OnlineNode onlineNode1 = listNode.get(i);
                OnlineNodeVo onlineNodeVo = new OnlineNodeVo();
                BeanUtils.copyBeanProp(onlineNodeVo, onlineNode1);
                onlineNodeVo.getTasksOk().set(onlineNode1.getTasksOk() == null ? 0 : onlineNode1.getTasksOk());
                onlineNodeVo.getTasksErr().set(onlineNode1.getTasksErr() == null ? 0 : onlineNode1.getTasksErr());
                onlineNodeVo.getExNums().set(onlineNode1.getExNums() == null ? 0 : onlineNode1.getExNums());
                onlineNodeVo.getErrs().set(onlineNode1.getErrs() == null ? 0 : onlineNode1.getErrs());
                onlineNodeVo.setPicName(NodePicUtils.get());
                onlineNodeVo.setBgColor(NodeBgUtils.get());
                nodes.put(onlineNodeVo.getId(), onlineNodeVo);
            }
        } catch (Exception e) {
            LOGGER.error("初始化节点失败", e);
        }
    }

    private HomeManager() {
        //
    }

    //返回所有节点的信息，用于有新的websocket连接时返回
    public static String list() {
        List<OnlineNodeVo> list = new ArrayList<>();
        Iterator<Map.Entry<String, OnlineNodeVo>> iterator = nodes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, OnlineNodeVo> entry = iterator.next();
            if (entry.getValue().getDeleted().get()) {
                continue;
            }
            list.add(entry.getValue());
        }
        return JSON.toJSONString(new WSMessageBody(WSMessageCmdConst.CMD_LISTALL, list,
                DateUtils.formatDateTime(getLastBeginTime()), DateUtils.formatDateTime(new Date(updateTime.get()))));
    }

    /**
     * 增加任务调度日志、用于交换任务调度日志保存后调用
     *
     * @param exJobLog
     */
    public static void addExJobLog(ExJobLog exJobLog) {
        try {
            if (!nodes.containsKey(exJobLog.getResNodeId())) {
                return;
            }
            updateTime.set(new Date().getTime());
            OnlineNodeVo node = nodes.get(exJobLog.getResNodeId());
            if (ExJobLog.STATUS_OK.equals(exJobLog.getStatus())) {
                node.getTasksOk().incrementAndGet();
            } else {
                node.getTasksErr().incrementAndGet();
            }
            node.getChanged().set(true);
        } catch (Exception e) {
            LOGGER.error("节点监控添加任务调度日志失败", e);
        }
    }

    public static void updateTasks(String nodeid, int nums) {
        if (!nodes.containsKey(nodeid)) {
            return;
        }
        OnlineNodeVo onlineNode = nodes.get(nodeid);
        if (onlineNode.getTasks().getAndSet(nums) != nums) {
            updateTime.set(new Date().getTime());
            onlineNode.getChanged().set(true);
        }
    }

    /**
     * 增加新的批量数据交换日志，用于批量数据交换日志保存后调用
     *
     * @param exBatchLog
     */
    public static void addBatchLog(ExBatchLog exBatchLog) {
        try {
            //没有节点则忽略
            if (!nodes.containsKey(exBatchLog.getResNodeId())) {
                return;
            }
            updateTime.set(new Date().getTime());
            OnlineNodeVo node = nodes.get(exBatchLog.getResNodeId());
            //交换量只统计执行正确，源的数据量
            if (ExBatchLog.ISERR_ERR.equals(exBatchLog.getIsErr())) {
                node.getErrs().incrementAndGet();
                node.getChanged().set(true);
                return;
            }

            if (exBatchLog.getSrcRows() > 0) {
                node.getExNums().getAndAdd(exBatchLog.getSrcRows());
                node.getChanged().set(true);
            }
        } catch (Exception e) {
            LOGGER.error("节点监控添加批量数据交换日志失败", e);
        }
    }

    /**
     * 更新交换节点，用于新增编辑节点时调用
     *
     * @param exNode
     */
    public static void updateNode(ExNode exNode) {
        OnlineNodeVo onlineNode = null;
        if (!nodes.containsKey(exNode.getId())) {
            onlineNode = new OnlineNodeVo();
            onlineNode.setId(exNode.getId());
            onlineNode.setName(exNode.getName());
            onlineNode.setPicName(NodePicUtils.get());
            onlineNode.setBgColor(NodeBgUtils.get());
            nodes.put(onlineNode.getId(), onlineNode);
        } else {
            onlineNode = nodes.get(exNode.getId());
            if (exNode.getName().equals(onlineNode.getName())) {
                return;
            }
            onlineNode.setName(exNode.getName());
        }
        updateTime.set(new Date().getTime());
        onlineNode.getChanged().set(true);
    }

    /**
     * 删除交换节点，用于删除节点时调用
     *
     * @param exNode
     */
    public static void delNode(ExNode exNode) {
        if (!nodes.containsKey(exNode.getId())) {
            return;
        }
        updateTime.set(new Date().getTime());
        OnlineNodeVo onlineNode = nodes.get(exNode.getId());
        onlineNode.getDeleted().set(true);
        onlineNode.getChanged().set(true);
    }

    /**
     * 节点在线，用于收到节点在线消息时调用
     *
     * @param nodeid
     */
    public static void online(String nodeid) {
        if (!nodes.containsKey(nodeid)) {
            return;
        }
        OnlineNodeVo onlineNode = nodes.get(nodeid);
        if (!onlineNode.getOnline().getAndSet(true)) {
            updateTime.set(new Date().getTime());
            onlineNode.getChanged().set(true);
        }
    }

    /**
     * 节点离线，用于指定时间内无在线消息时调用
     *
     * @param nodeid
     */
    public static void offline(String nodeid) {
        if (!nodes.containsKey(nodeid)) {
            return;
        }
        OnlineNodeVo onlineNode = nodes.get(nodeid);
        if (onlineNode.getOnline().getAndSet(false)) {
            updateTime.set(new Date().getTime());
            onlineNode.getChanged().set(true);
        }
    }

    public static Date getLastBeginTime() {
        return new Date(lastBeginTime.get());
    }

}
