package com.spring.springboot.controller;

import com.spring.springboot.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class UploadimgController {
    //跳转到上传文件的页面   跳转页面不能用@RestController注解
    @RequestMapping(value="/gouploadimg", method = RequestMethod.GET)
    public String goUploadImg() {
        //跳转到 templates 目录下的 uploadimg.html
        return "uploadimg";
    }

    //处理文件上传
    @RequestMapping(value="/testuploadimg", method = RequestMethod.POST)
    public @ResponseBody
    String uploadImg(@RequestParam("file") MultipartFile file,
                     HttpServletRequest request) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        /*System.out.println("fileName-->" + fileName);
        System.out.println("getContentType-->" + contentType);*/
        //String filePath = request.getSession().getServletContext().getRealPath("imgupload/");

        File nowFile = new File("");
        //获取项目路径
        String canonicalPath = null;
        try {
            canonicalPath = nowFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File fileImage = new File(canonicalPath);
        //获取项目父级路径 + 系统斜杠 +uploadImg文件夹
        String filePath = fileImage.getParent() + File.separator + "uploadImg" + File.separator;
        File fileCreate = new File(filePath);

        if (fileCreate.mkdirs()) {//文件夹不存在就创建

        }

        System.out.println(filePath);
        System.out.println(fileName);
        System.out.println(file.getName());
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }
        //返回json
        return "uploadimg success";
    }
}
