package com.springapp.mvc;

import com.alibaba.fastjson.JSONArray;
import com.springapp.classes.DateUtil;
import com.springapp.entity.Activity;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 11369 on 2016/11/24.
 */
@Controller
@RequestMapping(value = "back")
public class BackController extends BaseController {
    private static SimpleDateFormat sdf_YYYY_M=new SimpleDateFormat("yyyy-M");
    private static SimpleDateFormat sdf_YYYY_M_dd=new SimpleDateFormat("yyyy-M-d");

    @RequestMapping(value = "/getListByMonth",method = RequestMethod.GET)
    @ResponseBody
    public String getListByMonth(String year,String month){
        if(month==null||month.equals("")||year==null||year.equals(""))
            return "Parameter_Error";
        List<Activity>returnList=null;
        String get_date=year+"-"+month;
        try{
            Date query_date=sdf_YYYY_M.parse(get_date);
            Long query_time1=query_date.getTime();
            Long query_time2= DateUtil.getMonthEndM(get_date);
            returnList=activityDao.getListByMonth(query_time1,query_time2);
            if(returnList==null)
                return "Parameter_Error";
        }catch (Exception e){
            return "Parameter_Error";
        }
        return JSONArray.toJSONString(returnList);
    }

    @RequestMapping(value = "/getListByDate",method = RequestMethod.GET)
    @ResponseBody
    public String getListByDate(String date){
        if(date==null||date.equals(""))
            return "Parameter_Error";
        List<Activity>returnList=activityDao.getListByDate(date);
        if(returnList==null)
            return "Parameter_Error";
        return JSONArray.toJSONString(returnList);
    }
    @RequestMapping(value = "/getListByTitle",method = RequestMethod.GET)
    @ResponseBody
    public String getListByTitle(String title){
        if(title==null||title.equals(""))
            return "Parameter_Error";
        List<Activity>returnList=activityDao.getListByTitle(title);
        if(returnList==null)
            return "Parameter_Error";
        return JSONArray.toJSONString(returnList);
    }
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public ModelAndView info( @PathVariable("id") Long id){
        ModelAndView modelAndView=new ModelAndView();
        if(id==null){
            modelAndView.setViewName("result");
        }else{
            try {
                Activity activity=activityDao.get(Activity.class,id);
                modelAndView.addObject("object",activity);
                modelAndView.setViewName("sample");
            }catch (Exception e){
                modelAndView.setViewName("result");
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public ModelAndView add(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("back/activityAdd");
        return modelAndView;
    }
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String title,String content,String fromDate,String todate){
        if(title==null||title.equals("")||content==null||content.equals("")||fromDate==null||fromDate.equals("")||todate==null||todate.equals("")){
            return "Parameter_Error";
        }
        try {
            Activity activity=new Activity();
            Long fromTime=sdf_YYYY_M_dd.parse(fromDate).getTime();
            Long toTime=sdf_YYYY_M_dd.parse(todate).getTime();
            activity.setTitle(title);
            activity.setContent(content);
            activity.setFromDate(fromDate);
            activity.setFromTime(fromTime);
            activity.setToDate(todate);
            activity.setToTime(toTime);
            activity.setCreateTime(System.currentTimeMillis());
            activityDao.save(activity);
            return "success";
        }catch (Exception e){
            return "Parameter_Error";
        }
    }
    @RequestMapping(value = "/activityList",method = RequestMethod.GET)
    public ModelAndView activityList(){
        ModelAndView modelAndView=new ModelAndView();
        List<Activity> activities=activityDao.getList();
        modelAndView.addObject("list",activities);
        modelAndView.setViewName("back/activityList");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") Long id){
        ModelAndView modelAndView=new ModelAndView();
        Activity activity=activityDao.get(Activity.class,id);
        modelAndView.addObject("item",activity);
        modelAndView.setViewName("edit");
        return modelAndView;
    }
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public String delete(Long id){
        if(id==null)
            return "fail";
        Activity activity=activityDao.get(Activity.class,id);
        if(activity==null)
            return "fail";
        activityDao.delete(activity);
        return "success";
    }
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit(Long id,String title,String content,String fromDate,String todate){
        if(id==null||title==null||title.equals("")||content==null||content.equals("")||fromDate==null||fromDate.equals("")||todate==null||todate.equals("")){
            return "Parameter_Error";
        }
        try {
            Activity activity=activityDao.get(Activity.class,id);
            Long fromTime=sdf_YYYY_M_dd.parse(fromDate).getTime();
            Long toTime=sdf_YYYY_M_dd.parse(todate).getTime();
            activity.setTitle(title);
            activity.setContent(content);
            activity.setFromDate(fromDate);
            activity.setFromTime(fromTime);
            activity.setToDate(todate);
            activity.setToTime(toTime);
            activityDao.update(activity);
            return "success";
        }catch (Exception e){
            return "Parameter_Error";
        }
    }
    /**
     * 文件上传
     * @param request {@link HttpServletRequest}
     * @param response {@link javax.servlet.http.HttpServletResponse}
     * @return json response
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //文件保存本地目录路径



        String realPath=request.getSession().getServletContext().getRealPath("/")+"WEB-INF/pages/"+"attached"+"/";
        System.out.println("realPath:"+realPath);
        String savePath = realPath;
        //文件保存目录URL
        String saveUrl = request.getContextPath() + "/attached"+"/";

        if(!ServletFileUpload.isMultipartContent(request)){
            return getError("请选择文件。");
        }

        //检查目录
		/*File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			return getError("上传目录不存在。");
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
			return getError("上传目录没有写权限。");
		}*/

        String dirName = request.getParameter("dir");
        if (dirName == null) {
            dirName = "image";
        }

        //定义允许上传的文件扩展名
        Map<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        /*
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,xml,txt,zip,rar,gz,bz2");*/

        if(!extMap.containsKey(dirName)){
            return getError("目录名不正确。");
        }
        //创建文件夹
        savePath += dirName + "/";
        saveUrl += dirName + "/";
        File saveDirFile = new File(savePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        savePath += ymd + "/";
        saveUrl += ymd + "/";
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        //最大文件大小
        long maxSize = 1000000;

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException fe) {
            return getError("接收文件异常。");
        }
        Iterator itr = items.iterator();
        while (itr.hasNext()) {
            FileItem item = (FileItem) itr.next();
            String fileName = item.getName();
            if (!item.isFormField()) {
                //检查文件大小
                if(item.getSize() > maxSize){
                    return getError("上传文件大小超过限制。");
                }
                //检查扩展名
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
                    return getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
                try{
                    File uploadedFile = new File(savePath, newFileName);
                    item.write(uploadedFile);
                }catch(Exception e){
                    return getError("上传文件失败。");
                }

                Map<String, Object> succMap = new HashMap<String, Object>();
                succMap.put("error", 0);
                succMap.put("url", saveUrl + newFileName);

                return succMap;
            }
        }

        return null;
    }

    private Map<String, Object> getError(String errorMsg) {
        Map<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("error", 1);
        errorMap.put("message", errorMsg);
        return errorMap;
    }

    /**
     * 文件空间
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return json
     */
    @RequestMapping(value = "/fileManager")
    @ResponseBody
    public Object fileManager(HttpServletRequest request, HttpServletResponse response) {
        //根目录路径，可以指定绝对路径
        String realPath=System.getProperty("search.root")+"attached"+"/";
        System.out.println("realPath:"+realPath);

        String rootPath = realPath;
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl  = request.getContextPath() + "/attached"+"/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        String dirName = request.getParameter("dir");
        if (dirName != null) {
            if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dirName)){
                return "Invalid Directory name.";
            }
            rootPath += dirName + "/";
            rootUrl += dirName + "/";
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        }
        //根据path参数，设置各路径和URL
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

        //不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
            return "Access is not allowed.";
        }
        //最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            return "Parameter is not valid.";
        }
        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if(!currentPathFile.isDirectory()){
            return "Directory does not exist.";
        }

        //遍历目录取的文件信息
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        if(currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if(file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if(file.isFile()){
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        if ("size".equals(order)) {
            Collections.sort(fileList, new BackController.SizeComparator());
        } else if ("type".equals(order)) {
            Collections.sort(fileList, new BackController.TypeComparator());
        } else {
            Collections.sort(fileList, new BackController.NameComparator());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("moveup_dir_path", moveupDirPath);
        result.put("current_dir_path", currentDirPath);
        result.put("current_url", currentUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);

        return result;
    }

    private class NameComparator implements Comparator<Map<String, Object>> {
        public int compare(Map<String, Object> hashA, Map<String, Object> hashB) {
            if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
                return 1;
            } else {
                return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
            }
        }
    }

    private class SizeComparator implements Comparator<Map<String, Object>> {
        public int compare(Map<String, Object> hashA, Map<String, Object> hashB) {
            if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
                return 1;
            } else {
                if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
                    return 1;
                } else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    private class TypeComparator implements Comparator<Map<String, Object>> {
        public int compare(Map<String, Object> hashA, Map<String, Object> hashB) {
            if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
                return -1;
            } else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
                return 1;
            } else {
                return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
            }
        }
    }
}
