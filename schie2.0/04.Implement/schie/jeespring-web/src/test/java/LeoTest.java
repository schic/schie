import com.JeeSpringDriver;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.schic.schie.modules.epidemic.entity.*;
import com.schic.schie.modules.epidemic.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo
 * @description 测试类
 * @date 2019/9/5 17:53
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JeeSpringDriver.class)
public class LeoTest {
    @Autowired
    public IPatientsService patientsService;
    @Autowired
    public IPhqAddrService phqAddrService;
    @Autowired
    public IPtSequenService ptSequenService;
    @Autowired
    public IPfDataService  pfDataService;
    @Autowired
    public IPHomeService pHomeService;
    @Autowired
    public IPCarService pCarService;
    @Autowired
    public IPTrainService pTrainService;
    @Autowired
    public IPFlightService pFlightService;
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testlog(){
    }
    @Test
    public void testData(){
        Instant inst1 = Instant.now();  //当前的时间
        for(int i = 1; i <=2000000; i++){
            Patients patient = new Patients();
            patient.setPname("张三");
            patient.setPtel("1500".concat(String.valueOf(i)));
            patient.setOfShizhou("成都市");
            patient.setSfId("51290419984681564x");
            patient.setPtype("0");
            patient.setCreateDate(new Date());
            patientsService.save(patient);
        }
        Instant inst2 = Instant.now();  //结束时间
        System.out.println("以毫秒计的时间差：" + Duration.between(inst1, inst2).toMillis());
        System.out.println("以秒计的时间差：" + Duration.between(inst1, inst2).getSeconds());
    }

    @Test
    public void test1(){
        PhqAddr phqAddr = new PhqAddr();
        phqAddr.setPid("fba4803a898640e29cd34e7e6f94aced");
        phqAddr.setPtel("15002694794");
        phqAddr.setStayHours(Integer.valueOf("4"));
        phqAddr.setCreateDate(new Date());
        phqAddrService.save(phqAddr);
    }

    @Test
    public void test2(){
        PtSequen ptSequen = new PtSequen();
        ptSequen.setPid("fba4803a898640e29cd34e7e6f94aced");
        ptSequen.setCjDate(new Date().toString());
        ptSequen.setOfShizhou("乌鲁木齐0");
        ptSequen.setOfQuxian("卡里面县");
        ptSequen.setStationAddr("abc地址");
        ptSequen.setStayTimes(Integer.valueOf("5"));
        ptSequen.setCreateDate(new Date());
        ptSequenService.save(ptSequen);
    }

    @Test
    public void test3(){
        Instant inst1 = Instant.now();  //当前的时间
        for(int i = 1; i <=100000; i++){
            PfData pfData = new PfData();
            pfData.setPid("fba".concat(String.valueOf(i)));
            pfData.setCjDate(new Date().toString());
            pfData.setPtel("123456789".concat(String.valueOf(i)));
            pfData.setOfShizhou("乌鲁木齐");
            pfData.setOfQuxian("卡里面县");
            pfData.setStationAddr("abc地址");
            pfData.setCreateDate(new Date());
            pfDataService.save(pfData);
        }
        Instant inst2 = Instant.now();  //结束时间
        System.out.println("以毫秒计的时间差：" + Duration.between(inst1, inst2).toMillis());
        System.out.println("以秒计的时间差：" + Duration.between(inst1, inst2).getSeconds());
    }

    @Test
    public void testPhome(){
        PHome pHome = new PHome();
        pHome.setSfId("510987190000141150");
        pHome.setThName("张三王二麻子");
        pHome.setHuhao("237994874");
        pHome.setHRelation("次女");
        pHome.setProvince("新疆省");
        pHome.setJcDate("20200317");
        pHome.setLxTel("18389175663,18698102974,");
        pHome.setTelStation("lalala啦啦啦啦");
        pHome.setPtype("0");
        pHome.setOfShizhou("四川省攀枝花市");
        pHome.setOfQuxian("四川省攀枝花市仁和区");
        pHome.setRespArea("大河中路社区");
        pHome.setPhone("15003497584");
        pHome.setHabitation("四川省攀枝花市仁和");
        pHome.setCreateDate(new Date());
        pHomeService.save(pHome);
    }

    @Test
    public void testPcar(){
        PCar pCar = new PCar();
        pCar.setType("汽车");
        pCar.setBeginDate("2020-01-30");
        pCar.setName("罗01");
        pCar.setIdNumber("510111196601778819");
        pCar.setCarPosition("成都北门车站(特价)");
        pCar.setProvince("西藏");
        pCar.setCode("1480");
        pCar.setLxTel("112345689756");
        pCar.setOfShizhou("四川省自贡市");
        pCar.setOfQuxian("四川省自贡市高新");
        pCar.setRespArea("梨园责任区");
        pCar.setPhone("15948199754");
        pCar.setHabitation("hahahl啦啦啦");
        pCar.setCreateDate(new Date());
        pCarService.save(pCar);
    }

    @Test
    public void testPTrain(){
        Instant inst1 = Instant.now();  //当前的时间
        for(int i = 1; i <=500000; i++){
            PTrain pTrain = new PTrain();
            pTrain.setBeginDate("20200417");
            pTrain.setTrainNum("C0".concat(String.valueOf(i)));
            pTrain.setStartSta("南充");
            pTrain.setArrivalSta("蓬安");
            pTrain.setArrivalDate("20200417");
            pTrain.setCarriageNum("12");
            pTrain.setSeatNum("0010F");
            pTrain.setName("小明");
            pTrain.setIdNumber("369852147");
            pTrain.setBingliIdNumber("123987");
            pTrain.setProvince("青海省");
            pTrain.setLxTel("15181720114 15881717899 ");
            pTrain.setTelStation("江苏省测试数据");
            pTrain.setPtype("1");
            pTrain.setOfShizhou("四川省南充市测试数据");
            pTrain.setOfQuxian("四川省南充市测试数据");
            pTrain.setRespArea("测试数据");
            pTrain.setPhone("12345697");
            pTrain.setHabitation("测试数据");
            pTrain.setCreateDate(new Date());
            pTrainService.save(pTrain);
        }
        Instant inst2 = Instant.now();  //结束时间
        System.out.println("以毫秒计的时间差：" + Duration.between(inst1, inst2).toMillis());
        System.out.println("以秒计的时间差：" + Duration.between(inst1, inst2).getSeconds());
    }

    @Test
    public void testPFlight(){
        PFlight pFlight = new PFlight();
        pFlight.setIdNumber("110225193308120215");
        pFlight.setName("王二码字");
        pFlight.setBeginDate("2020-02-06");
        pFlight.setArrivalSta("成都/双流");
        pFlight.setFlightNum("CA1407");
        pFlight.setSeatNum("33J");
        pFlight.setProvince("云南省");
        pFlight.setLxTel("15378944953 10149233230 13439442738 ");
        pFlight.setOfShizhou("乌鲁木齐");
        pFlight.setOfQuxian("ta塔里木线");
        pFlight.setRespArea("盛泰社区盛和治安责任区");
        pFlight.setPhone("13084541100");
        pFlight.setHabitation("河北省保定市莲池区瑞祥大街261号18栋4单元103号");
        pFlight.setTelStation("四川省成都市武侯区石羊场街道");
        pFlight.setSfId("510222193306121235");
        pFlight.setCreateDate(new Date());
        pFlightService.save(pFlight);
    }

    @Test
    public void testJW(){
        String address = "";
        try {
            address = java.net.URLEncoder.encode("四川省成都青白江区城厢镇十五里村356号","UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/place/v2/search?"
                +"ak=yourkey&output=json&query=%s&region=全国",address);
        URL myURL = null;
        URLConnection httpsConn = null;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {

        }
        StringBuffer sb = new StringBuffer();
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();;
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
                insr.close();
            }
        } catch (IOException e) {

        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        JSONObject resultJson = JSON.parseObject(sb.toString());
        //resultJson  {"message":"ok","results":[{"uid":"30e1d0bb0c0014f8b6147fe6","name":"攀枝花市","location":{"lng":101.725544,"lat":26.588034}}],"status":0}
        JSONArray jsonArray = (JSONArray)resultJson.get("results");
        System.out.println(jsonArray);
        JSONObject results0Obj = (JSONObject)jsonArray.get(0);
        JSONObject locationObj = (JSONObject)results0Obj.get("location");
        //纬度
        BigDecimal lat = (BigDecimal)locationObj.get("lat");
        //经度
        BigDecimal lng = (BigDecimal)locationObj.get("lng");
        map.put("lat", lat);
        map.put("lng", lng);
        System.out.println(map);
    }
}
