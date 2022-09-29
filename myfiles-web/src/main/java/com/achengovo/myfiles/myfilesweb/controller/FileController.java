package com.achengovo.myfiles.myfilesweb.controller;

import com.achengovo.lightning.client.Reference;
import com.achengovo.lightning.client.filter.Filter;
import com.achengovo.lightning.client.loadbalance.WeigthRandomLoadbalanceImpl;
import com.achengovo.myfiles.entity.User;
import com.achengovo.myfiles.entity.UserFile;
import com.achengovo.myfiles.myfilesweb.filter.LogFilter;
import com.achengovo.myfiles.service.FileService;
import com.achengovo.myfiles.utils.CookiesUtils;
import com.achengovo.myfiles.utils.JudgeFileType;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class FileController {
    private FileService fileService;
    public FileController() throws NacosException, InterruptedException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new LogFilter());
        Reference reference = new Reference("127.0.0.1:8848",FileService.class.getName(), "DEFAULT_GROUP",filters);
        fileService= (FileService) reference.createProxy(FileService.class,new WeigthRandomLoadbalanceImpl());
    }

    /**
     * 移动文件
     *
     * @param userFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/moveFile")
    public boolean moveFile(@RequestBody UserFile userFile, HttpServletRequest request) {
        User user=new User();
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        return fileService.moveFile(userFile,user);
    }

    /**
     * 重命名
     * @param userFile
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reName")
    public boolean reName(@RequestBody UserFile userFile, HttpServletRequest request) {
        User user=new User();
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        return fileService.reName(userFile,user);
    }

    /**
     * 删除
     * @param userFile
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delFileById")
    public boolean delFileById(@RequestBody UserFile userFile, HttpServletRequest request) {
        User user=new User();
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        return fileService.delFileById(userFile,user);
    }

    /**
     * 获取文件夹下的文件列表
     * @param dir
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFileList", method = RequestMethod.GET)
    public List<UserFile> getFileList(@RequestParam("dir") String dir, HttpServletRequest request) {
        User user=new User();
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        UserFile userFile = new UserFile();
        userFile.setDir(dir);
        return fileService.getFileByDir(userFile,user);
    }

    /**
     * 下载文件
     * @param userFileId
     * @param request
     * @param session
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download")
    public void download(@RequestParam("userFileId") Integer userFileId, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        String userToken= CookiesUtils.getCookie(request,"userToken");
        User user=new User();
        user.setUserToken(userToken);
        UserFile userFile = new UserFile();
        userFile.setUserFileId(userFileId);
        userFile=fileService.getFileByUserFileId(userFile,user);
        String path = "D:\\MyFiles\\userFile\\"+userFile.getUserId()+"\\"+userFile.getFileLocation();
        //判断系统类型
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            path="/MyFiles/userFile/"+userFile.getUserId()+"/"+userFile.getFileLocation();
        }
        String fileName = URLEncoder.encode(userFile.getUserFileName().trim(), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        System.out.println(path);
        InputStream is = new FileInputStream(path);
        int read = 0;
        byte[] bytes = new byte[200 * 1024];
        OutputStream os = response.getOutputStream();
        while ((read = is.read(bytes)) != -1) {//按字节逐个写入，避免内存占用过高
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();
        is.close();
    }

    /**
     * 根据浏览器的不同进行编码设置，返回编码后的文件名
     */
    public String getEncodeName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String[] IEBrowserKeyWords = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String keyWord : IEBrowserKeyWords) {
            if (userAgent.contains(keyWord)) {
                return URLEncoder.encode(fileName, "UTF-8");
            }
        }
        return new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    }

    /**
     * 上传文件
     * @param uploadfiles
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toUpload", method = RequestMethod.POST)
    public boolean toUpload(
            @RequestParam("uploadfile") List<MultipartFile> uploadfiles,
            @RequestParam("nowDirId") String nowDirId,
            @RequestParam("userId") String userId,
            HttpServletRequest request) {
        User user=new User();
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        //判断上传文件是否存在
        if (uploadfiles.isEmpty()) {
            return false;
        }
        for(MultipartFile file:uploadfiles){
            //文件名
            String originalFilename = file.getOriginalFilename();
//            String dirPath = request.getServletContext().getRealPath("/avatar/");
            //保存地址
            String dirPath = "D:\\MyFiles\\userFile\\"+userId+"\\";
            //判断系统类型
            if(System.getProperty("os.name").toLowerCase().contains("linux")){
                dirPath="/MyFiles/userFile/"+userId+"/";
            }
            File filePath=new File(dirPath);
            //判断路径是否存在，不存在则创建
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            //新文件名(UUID+后缀)
            String newFileName = UUID.randomUUID().toString().replace("-","_")+"_"+originalFilename.substring(originalFilename.lastIndexOf("."));
            try{
                //将文件保存到指定目录
                file.transferTo(new File(dirPath+newFileName));
                UserFile userFile=new UserFile();
                userFile.setDir(nowDirId);
                userFile.setUserFileName(originalFilename);
                userFile.setUserFileSize(String.valueOf(file.getSize()));
                userFile.setFileLocation(newFileName);
//                userFile.setFileType(judgeType(originalFilename));
                userFile.setFileType(JudgeFileType.judgeType(originalFilename));
                return fileService.insertToUserFiles(userFile, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 新建目录
     * fileId, fileType,fileLocation
     */
    @ResponseBody
    @RequestMapping("/newDir")
    public boolean newDir(@RequestBody UserFile userFile,
                         HttpServletRequest request) {
        User user=new User();
        user.setUserToken(CookiesUtils.getCookie(request,"userToken"));
        return fileService.newDir(userFile, user);
    }
}
