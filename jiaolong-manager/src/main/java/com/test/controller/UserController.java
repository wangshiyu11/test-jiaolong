package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.test.ResponseResult;
import com.test.entity.RoleInfo;
import com.test.entity.User;
import com.test.exception.LoginException;
import com.test.jwt.JWTUtils;
import com.test.randm.VerifyCodeUtils;
import com.test.service.UserService;
import com.test.utils.MD5;
import com.test.utils.UID;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ：
 * @description :TODO
 * @date ：Created in 2019/8/5 14:24
 */
@Controller
public class UserController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;
    String addr;
    /**
     * 获取用户列表
     */
    @RequestMapping("userByList")
    @ResponseBody
    public PageInfo<User> userList(@RequestBody Map<String,Object> map){
        System.out.println("-----------分页-------------");
        System.out.println("++++++++++++"+map.get("xingbie"));
        PageInfo<User> page1 = userService.userList(
                Integer.valueOf(map.get("page").toString()),
                Integer.valueOf(map.get("pageSize").toString()),
                map.get("name").toString(),
                map.get("dt1").toString(),
                map.get("dt2").toString(),
                map.get("xingbie").toString());
        return page1;
    }

    /**
     * 上传图片
     */

    @PostMapping("addPic")
    @ApiOperation("文件上传的接口")
    public void addPic(@Param("file")MultipartFile file) throws IOException {

        System.out.println("进入图片方法");
        String imgUrl = "F:\\tu\\" + file.getOriginalFilename();
        File file2 = new File(imgUrl);

        //可自定义大小
        Thumbnails.of(file2).scale(0.25f).toFile(file2.getAbsolutePath()+"_25.jpg");
        System.out.println("图片的方法===="+file2.getAbsolutePath()+"_25.jpg");
        this.addr =file.getOriginalFilename();
    }


    /**
     * 用户删除
     */
    @RequestMapping("del")
    @ResponseBody
    public void del(@RequestBody User user){
        userService.del(user.getId());
    }

    /**
     * 用户添加
     */
    @RequestMapping("add")
    @ResponseBody
    public int add(@RequestBody User user){
        System.out.println("----------"+user);
        if(user!=null){
            String password = MD5.encryptPassword(user.getPassword(), "lcg");
            user.setPassword(password);
            System.out.println(password);
        }else{
            System.out.println("密码保存错误");
        }
        int i=userService.add(user);
        return i;
    }

    /**
     * 修改
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public int update(@RequestBody User user){
        int i = userService.update(user);
        return i;
    }

    @RequestMapping("findByloginName")
    @ResponseBody
    public int  findByloginName(@RequestBody Map<String,Object> map){
        System.out.println(map.get("loginName").toString());
        int i=userService.findByloginName(map.get("loginName").toString());
        return i;
    }

    /*//这是把数据导出到本地保存为Excel文件的方法
    public static ResponseEntity<byte[]> exportJobLevelExcel(List<User> allJobLevels) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();//创建一个Excel文件

        //创建Excel文档属性，必不可少。少了的话，getDocumentSummaryInformation()方法就会返回null
        workbook.createInformationProperties();
        DocumentSummaryInformation info = workbook.getDocumentSummaryInformation();
        info.setCompany("KYO Ltd.");//设置公司信息
        info.setManager("kyo");//设置管理者
        info.setCategory("职称表");//设置文件名

        //设置文件中的日期格式
        HSSFCellStyle datecellStyle = workbook.createCellStyle();
        datecellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));//这个文件的日期格式和平时的不一样

        //创建表单
        HSSFSheet sheet = workbook.createSheet("test");
        HSSFRow r0 = sheet.createRow(0);//创建第一行
        HSSFCell c0 = r0.createCell(0);// 创建列
        HSSFCell c1 = r0.createCell(1);// 创建列
        HSSFCell c2 = r0.createCell(2);// 创建列
        HSSFCell c3 = r0.createCell(3);// 创建列
        HSSFCell c4 = r0.createCell(4);// 创建列
        HSSFCell c5 = r0.createCell(5);// 创建列
        HSSFCell c6 = r0.createCell(6);// 创建列

        c0.setCellValue("编号");
        c1.setCellValue("登录名");
        c2.setCellValue("用户名");
        c3.setCellValue("密码");
        c4.setCellValue("电话");
        c5.setCellValue("性别");

        User user=new User();
            HSSFRow row = sheet.createRow(i + 1);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(user.getId());
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(user.getLoginName());
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(user.getTitlelevel());
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(datecellStyle);//让日期格式数据正确显示
            cell3.setCellValue(user.getCreatedate());
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(user.getEnabled()?"是":"否");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",
                new String("职称表.xls".getBytes("UTF-8"),"iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        workbook.write(baos);

        ResponseEntity<byte[]> entity = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);

        return entity;
    }
*/


    @RequestMapping("addExcel")
    @ApiOperation("这是 UserInfoController 上传文件的方法")
    public void addExcel(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        //创建数组
        ArrayList<User> users = new ArrayList<>();
        //打开HSSFWorkbook对象XSSFWorkbook
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //获取表单所有行
        int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows();
        //向数据库导入数据
        for (int i = 1; i < physicalNumberOfRows; i++) {
            XSSFRow row = sheetAt.getRow(i);
            //创建对象
            User userInfo = new User();
            //第一列 如果Id为自增第一行列为空，从第二列开始，样式同导出
            XSSFCell c1 = row.getCell(1);
            System.out.println(c1+"========c1=============");
            //装入对象
            userInfo.setUserName(c1.getStringCellValue());
            //第二列
            XSSFCell c3 = row.getCell(2);
            System.out.println(c3+"=========c3============");
            //装入对象;
            userInfo.setLoginName(c3.getStringCellValue());
            //第三列
            XSSFCell c4 = row.getCell(3);
            System.out.println(c4+"==========c4===========");
            //装入对象
            row.getCell(3).setCellType(c4.CELL_TYPE_STRING);
            userInfo.setPassword(c4.getStringCellValue());
            //第四列
            XSSFCell c5 = row.getCell(4);
            System.out.println(c5+"==========c5===========");
            //装入对象
            row.getCell(4).setCellType(c5.CELL_TYPE_STRING);
            userInfo.setTel(c5.getStringCellValue());
            //第五列
            XSSFCell c6 = row.getCell(5);
            System.out.println(c6+"==========c6===========");
            //装入对象
            row.getCell(5).setCellType(c6.CELL_TYPE_STRING);
            userInfo.setSex(Integer.parseInt(c6.getStringCellValue()));
            //把对象装入数组
            users.add(userInfo);
        }
        //循环数组
        users.forEach(userInfo -> {
            //把数值装入增加方法中
            userService.add(userInfo);
        });
    }


}
