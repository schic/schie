package com.schic.schie.modules.epidemic.web;

import com.jeespring.common.json.AjaxJson;
import com.jeespring.common.web.AbstractBaseController;
import com.schic.schie.modules.common.ExcelUtils;
import com.schic.schie.modules.epidemic.entity.*;
import com.schic.schie.modules.epidemic.service.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 疫情数据采集Controller
 *
 * @author leodeyang
 * @version 2020-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/epidemic/datacollection")
public class DataConnectionController extends AbstractBaseController {

    @Autowired
    private IPatientsService patientsService;
    @Autowired
    private IPhqAddrService phqAddrService;
    @Autowired
    private IPtSequenService ptSequenService;
    @Autowired
    private IPfDataService pfDataService;
    @Autowired
    private IPHomeService pHomeService;
    @Autowired
    private IPCarService pCarService;
    @Autowired
    private IPTrainService pTrainService;
    @Autowired
    private IPFlightService pFlightService;

    /**
     * 进入数据采集 页面
     * @param model
     * @param request
     * @return
     */
    @RequiresPermissions("epidemic:datacollection:index")
    @RequestMapping(value = { "index" })
    public String index(Model model, HttpServletRequest request) {
        return "modules/epidemic/datacjIndex";
    }

    /**
     * 扫描目录下的所有excel文件
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "searchFile", method = RequestMethod.POST)
    public AjaxJson serarchFile(HttpServletRequest request){
        AjaxJson j = new AjaxJson();
        String message = "扫描目录成功!请继续";
        String diskPath = request.getParameter("diskPath");
        try {
            List flist = ExcelUtils.searchPath(diskPath);
            //2.遍历文件绝对路径
            ArrayList<Object> list = new ArrayList<>();
            if (flist!=null && !flist.isEmpty()){
                for (Object o : flist) {
                    list.add(o.toString());
                }
            }else {
                message = "目录下没有excel文件！请重新确认目录";
                j.setSuccess(false);
            }
            LinkedHashMap<String,Object> map = new LinkedHashMap<>();
            map.put("fileList",list);
            j.setBody(map);
        }catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            message = "扫描目录失败!请重新确认目录";
        }
        j.setMsg(message);
        return j;
    }

    /**
     * 解析文件名 以及 文件内容 入库
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "parseFile",method = RequestMethod.POST)
    public AjaxJson parseFile(HttpServletRequest request){
        AjaxJson j = new AjaxJson();
        String message = "符合要求的文件解析入库成功!!!";
        //1.获取到前台传来的文件路径 字符串
        String strPaths = request.getParameter("strPaths");
        String[] split = strPaths.split(",");
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        ArrayList<Object> list1 = new ArrayList<>();
        ArrayList<Object> list2 = new ArrayList<>();
        int patientNum = 0;
        int phqAddrNum = 0;
        int ptSequenNum = 0;
        int pfDataNum = 0;
        int phomeNum = 0;
        int pcarNum = 0;
        int ptrainNum = 0;
        int pflightNum = 0;
        //2.开始解析每一条具体的绝对路径
        for (int i = 0; i < split.length; i++) {
            System.out.println("======"+split[i]);
            try {
                //1.第一种文件  以  疑似 确诊 阳性 阴性 结尾
                if (split[i].endsWith("疑似.xls") ||split[i].endsWith("疑似.xlsx") || split[i].endsWith("确诊.xls") || split[i].endsWith("确诊.xlsx") || split[i].endsWith("阳性.xlsx") || split[i].endsWith("阳性.xls") || split[i].endsWith("阴性.xlsx") || split[i].endsWith("阴性.xls")){
                    //获取文件名  构建主表对象 病患对象 入库
                    String substring = split[i].substring(split[i].lastIndexOf("\\")+1);
                    Patients patient = new Patients();
                    String[] splits = substring.split("-");
                    patient.setPname(splits[1]);
                    patient.setPtel(splits[2]);
                    patient.setOfShizhou(splits[0]);
                    patient.setCreateDate(new Date());
                    if (splits[3].contains("确诊") || splits[3].contains("阳性")){
                        patient.setPtype("1");
                    }
                    if (splits[3].contains("疑似") || splits[3].contains("阴性")){
                        patient.setPtype("0");
                    }
                    patientsService.save(patient);
                    patientNum = patientNum + 1;
                    Patients patient2 = patientsService.get(patient);
                    HashMap hashMap = parseData(patient2.getId(), split[i]);
                    Object num1 = hashMap.get("phqAddrNum");
                    if (num1 != null){
                        phqAddrNum = phqAddrNum + (int)num1;
                    }
                    Object num2 = hashMap.get("ptSequenNum");
                    if (num2 != null){
                        ptSequenNum = ptSequenNum + (int)num2;
                    }
                    Object num3 = hashMap.get("pfDataNum");
                    if (num3 != null){
                        pfDataNum = pfDataNum + (int)num3;
                    }
                    list1.add(split[i]);
                }else if (split[i].endsWith("户.xls") ||split[i].endsWith("户.xlsx") || split[i].endsWith("车.xls") || split[i].endsWith("车.xlsx") || split[i].endsWith("班.xlsx") || split[i].endsWith("班.xls")){
                    //直接解析入库
                    HashMap hashMap2 = parseData2(split[i]);
                    Object numhu = hashMap2.get("phomeNum");
                    if (numhu != null){
                        phomeNum = phomeNum + (int)numhu;
                    }
                    Object numche = hashMap2.get("pcarNum");
                    if (numche != null){
                        pcarNum = pcarNum + (int)numche;
                    }
                    Object numhuoche = hashMap2.get("ptrainNum");
                    if (numhuoche != null){
                        ptrainNum = ptrainNum + (int)numhuoche;
                    }
                    Object numfj = hashMap2.get("pflightNum");
                    if (numfj != null){
                        pflightNum = pflightNum + (int)numfj;
                    }
                    list1.add(split[i]);
                }else {
                    list2.add(split[i]);
                }
            }catch (Exception e){
                e.printStackTrace();
                //logger.debug(e.getMessage());
                //j.setSuccess(false);
                //message = "文件解析入库失败!!!";
                //break;
            }
        }
        j.setMsg(message);
        map.put("okList",list1);
        map.put("failList",list2);
        map.put("patientNum",patientNum);
        map.put("phqAddrNum",phqAddrNum);
        map.put("ptSequenNum",ptSequenNum);
        map.put("pfDataNum",pfDataNum);
        map.put("phomeNum",phomeNum);
        map.put("pcarNum",pcarNum);
        map.put("ptrainNum",ptrainNum);
        map.put("pflightNum",pflightNum);
        j.setBody(map);
        return j;
    }

    /**
     * 解析第一种数据文件中的内容
     * @param id 插入主表 病患id
     * @param s 该excel文件的绝对路径
     */
    private HashMap parseData(String id, String s) {
        //1根据具体的文件生成对应的工作对象 xls格式是HSSFWorkbook xlsx格式是XSSFWorkbook
        Workbook wb = null;
        try {
            wb = ExcelUtils.creatWorkBook(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> hashMap = new HashMap<>();
        //2.获取所有的工作表的的数量
        Integer numOfSheet = ExcelUtils.getNumOfSheet(wb);
        for (int i = 0; i < numOfSheet; i++) {
            //获取一个sheet也就是一个工作本。
            Sheet sheet = null;
            if (wb != null) { sheet = wb.getSheetAt(i); }
            if(sheet == null) continue;
            int lastRowNum = sheet.getLastRowNum();
            if(lastRowNum == 0) continue;
            String sheetName = sheet.getSheetName();
            try {
                if (sheetName.contains("高频地址")){
                    int phqAddrNum = parsePhqAddr(sheet, id);
                    hashMap.put("phqAddrNum",phqAddrNum);
                }
                if (sheetName.contains("时序高频")){
                    int ptSequenNum = parsePtSequen(sheet, id);
                    hashMap.put("ptSequenNum",ptSequenNum);
                }
                if (sheetName.contains("全量数据")){
                    int pfDataNum = parsePfData(sheet, id);
                    hashMap.put("pfDataNum",pfDataNum);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return hashMap;
    }

    /**
     * 解析第二种数据文件中的内容
     * @param s 该excel文件的绝对路径
     */
    private HashMap parseData2(String s) {
        //1根据具体的文件生成对应的工作对象 xls格式是HSSFWorkbook xlsx格式是XSSFWorkbook
        Workbook wb = null;
        try {
            wb = ExcelUtils.creatWorkBook(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> hashMap = new HashMap<>();
        //2.获取所有的工作表的的数量 这里的文件都只有一个sheet
        Integer numOfSheet = ExcelUtils.getNumOfSheet(wb);
        for (int i = 0; i < numOfSheet; i++) {
            Sheet sheet = null;
            if (wb != null) { sheet = wb.getSheetAt(i); }
            if(sheet == null) continue;
            int lastRowNum = sheet.getLastRowNum();
            if(lastRowNum == 0) continue;
            try{
                if (s.endsWith("户.xls") ||s.endsWith("户.xlsx")){
                    int phomeNum = parsePHome(sheet);
                    hashMap.put("phomeNum",phomeNum);
                }
                if (s.endsWith("汽车.xls") ||s.endsWith("汽车.xlsx")){
                    int pcarNum = parsePCar(sheet);
                    hashMap.put("pcarNum",pcarNum);
                }
                if (s.endsWith("火车.xls") ||s.endsWith("火车.xlsx")){
                    int ptrainNum = parsePTrain(sheet);
                    hashMap.put("ptrainNum",ptrainNum);
                }
                if (s.endsWith("航班.xls") ||s.endsWith("航班.xlsx")){
                    int pflightNum = parsePFlight(sheet);
                    hashMap.put("pflightNum",pflightNum);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  hashMap;
    }

    /**
     * 解析第一个附表 高频地址 入库
     * @param sheet
     * @param id
     */
    private int parsePhqAddr(Sheet sheet, String id) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PhqAddr phqAddr = new PhqAddr();
            phqAddr.setPid(id);
            phqAddr.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) {
                continue;
            }
            if (j == 1){continue;}
            //4.获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:phqAddr.setPtel(String.valueOf(value1));break;
                    case 1:phqAddr.setHqAddr(String.valueOf(value1));break;
                    case 2:phqAddr.setStayHours(Integer.valueOf(String.valueOf(value1)));break;
                }
            }
            try {
                phqAddrService.save(phqAddr);
                num = num +1 ;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }
    /**
     * 解析第二个附表 时序高频 入库
     * @param sheet
     * @param id
     */
    private int parsePtSequen(Sheet sheet, String id) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PtSequen ptSequen = new PtSequen();
            ptSequen.setPid(id);
            ptSequen.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) {
                continue;
            }
            if (j == 1){continue;}
            //4.获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:ptSequen.setCjDate(String.valueOf(value1));break;
                    case 1:ptSequen.setOfShizhou(String.valueOf(value1));break;
                    case 2:ptSequen.setOfQuxian(String.valueOf(value1));break;
                    case 3:ptSequen.setStationAddr(String.valueOf(value1));break;
                    case 4:ptSequen.setStayTimes(Integer.valueOf(String.valueOf(value1)));break;
                }
            }
            try {
                ptSequenService.save(ptSequen);
                num = num+1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }
    /**
     * 解析第三个附表 全量数据 入库
     * @param sheet
     * @param id
     */
    private int parsePfData(Sheet sheet, String id) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PfData pfData = new PfData();
            pfData.setPid(id);
            pfData.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) {
                continue;
            }
            if (j == 1){continue;}
            //4.获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:pfData.setPtel(String.valueOf(value1));break;
                    case 1:pfData.setCjDate(String.valueOf(value1));break;
                    case 2:pfData.setOfShizhou(String.valueOf(value1));break;
                    case 3:pfData.setOfQuxian(String.valueOf(value1));break;
                    case 4:pfData.setStationAddr(String.valueOf(value1));break;
                }
            }
            try {
                pfDataService.save(pfData);
                num = num + 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 解析同户表 入库
     * @param sheet
     */
    private int parsePHome(Sheet sheet) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PHome pHome = new PHome();
            pHome.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) { continue; }
            if (j == 1){ continue;}
            //获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:pHome.setSfId(String.valueOf(value1));break;
                    case 1:pHome.setThName(String.valueOf(value1));break;
                    case 2:pHome.setHuhao(String.valueOf(value1));break;
                    case 3:pHome.setHRelation(String.valueOf(value1));break;
                    case 4:pHome.setProvince(String.valueOf(value1));break;
                    case 5:pHome.setJcDate(String.valueOf(value1));break;
                    case 6:pHome.setLxTel(String.valueOf(value1));break;
                    case 7:pHome.setTelStation(String.valueOf(value1));break;
                    case 8:pHome.setPtype((String.valueOf(value1).contains("确诊"))?"1":"0");break;
                    case 9:pHome.setOfShizhou(String.valueOf(value1));break;
                    case 10:pHome.setOfQuxian(String.valueOf(value1));break;
                    case 11:pHome.setRespArea(String.valueOf(value1));break;
                    case 12:pHome.setPhone(String.valueOf(value1));break;
                    case 13:pHome.setHabitation(String.valueOf(value1));break;
                }
            }
            try {
                pHomeService.save(pHome);
                num = num + 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 解析同汽车表 入库
     * @param sheet
     */
    private int parsePCar(Sheet sheet) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PCar pCar = new PCar();
            pCar.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) { continue; }
            if (j == 1){ continue;}
            //获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:pCar.setType(String.valueOf(value1));break;
                    case 1:pCar.setBeginDate(String.valueOf(value1));break;
                    case 2:pCar.setName(String.valueOf(value1));break;
                    case 3:pCar.setIdNumber(String.valueOf(value1));break;
                    case 4:pCar.setCarPosition(String.valueOf(value1));break;
                    case 5:pCar.setProvince(String.valueOf(value1));break;
                    case 6:pCar.setCode(String.valueOf(value1));break;
                    case 7:pCar.setLxTel(String.valueOf(value1));break;
                    case 8:pCar.setOfShizhou(String.valueOf(value1));break;
                    case 9:pCar.setOfQuxian(String.valueOf(value1));break;
                    case 10:pCar.setRespArea(String.valueOf(value1));break;
                    case 11:pCar.setPhone(String.valueOf(value1));break;
                    case 12:pCar.setHabitation(String.valueOf(value1));break;
                }
            }
            try {
                pCarService.save(pCar);
                num = num + 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 解析同火车表 入库
     * @param sheet
     */
    private int parsePTrain(Sheet sheet) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PTrain pTrain = new PTrain();
            pTrain.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) { continue; }
            if (j == 1){ continue;}
            //获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:pTrain.setBeginDate(String.valueOf(value1));break;
                    case 1:pTrain.setTrainNum(String.valueOf(value1));break;
                    case 2:pTrain.setStartSta(String.valueOf(value1));break;
                    case 3:pTrain.setArrivalSta(String.valueOf(value1));break;
                    case 4:pTrain.setArrivalDate(String.valueOf(value1));break;
                    case 5:pTrain.setCarriageNum(String.valueOf(value1));break;
                    case 6:pTrain.setSeatNum(String.valueOf(value1));break;
                    case 7:pTrain.setName(String.valueOf(value1));break;
                    case 8:pTrain.setIdNumber(String.valueOf(value1));break;
                    case 9:pTrain.setBingliIdNumber(String.valueOf(value1));break;
                    case 10:pTrain.setProvince(String.valueOf(value1));break;
                    case 11:pTrain.setLxTel(String.valueOf(value1));break;
                    case 12:pTrain.setTelStation(String.valueOf(value1));break;
                    case 13:pTrain.setPtype((String.valueOf(value1).contains("确诊"))?"1":"0");break;
                    case 14:pTrain.setOfShizhou(String.valueOf(value1));break;
                    case 15:pTrain.setOfQuxian(String.valueOf(value1));break;
                    case 16:pTrain.setRespArea(String.valueOf(value1));break;
                    case 17:pTrain.setPhone(String.valueOf(value1));break;
                    case 18:pTrain.setHabitation(String.valueOf(value1));break;
                }
            }
            try {
                pTrainService.save(pTrain);
                num = num + 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 解析同航班表 入库
     * @param sheet
     */
    private int parsePFlight(Sheet sheet) {
        //1.获取一个sheet有多少Row
        int lastRowNum = sheet.getLastRowNum();
        //2.统计多少条数据
        int num = 0;
        Row row ;
        for (int j  = 1; j <= lastRowNum; j++) {
            PFlight pFlight = new PFlight();
            pFlight.setCreateDate(new Date());
            row = sheet.getRow(j);
            if(row == null) { continue; }
            if (j == 1){ continue;}
            //获取一个Row有多少Cell
            short lastCellNum = row.getLastCellNum();
            for (int k = 0; k <= lastCellNum; k++) {
                if(row.getCell(k)==null) { continue; }
                Object value1 = getCellValue1(row, k);
                switch (k){
                    case 0:pFlight.setIdNumber(String.valueOf(value1));break;
                    case 1:pFlight.setName(String.valueOf(value1));break;
                    case 2:pFlight.setBeginDate(String.valueOf(value1));break;
                    case 3:pFlight.setArrivalSta(String.valueOf(value1));break;
                    case 4:pFlight.setFlightNum(String.valueOf(value1));break;
                    case 5:pFlight.setSeatNum(String.valueOf(value1));break;
                    case 6:pFlight.setProvince(String.valueOf(value1));break;
                    case 7:pFlight.setLxTel(String.valueOf(value1));break;
                    case 8:pFlight.setOfShizhou(String.valueOf(value1));break;
                    case 9:pFlight.setOfQuxian(String.valueOf(value1));break;
                    case 10:pFlight.setRespArea(String.valueOf(value1));break;
                    case 11:pFlight.setPhone(String.valueOf(value1));break;
                    case 12:pFlight.setHabitation(String.valueOf(value1));break;
                    case 13:pFlight.setTelStation(String.valueOf(value1));break;
                    case 14:pFlight.setSfId(String.valueOf(value1));break;
                }
            }
            try {
                pFlightService.save(pFlight);
                num = num + 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return num;
    }


    /**
     * 获取单元格值
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue1(Row row, int column) {
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    val = cell.getNumericCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    val = cell.getCellFormula();
                } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }
}
