package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.FileHelper;
import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProOrderMapper;
import cn.com.project.data.dao.business.ProQuotationMapper;
import cn.com.project.data.model.business.ProOrder;
import cn.com.project.data.model.business.ProQuotation;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 报价管理
 */
@CrossOrigin
@RequestMapping("quotation")
@Controller
public class QuotationController {

    private Logger logger = LoggerFactory.getLogger(QuotationController.class);

    @Autowired
    ProQuotationMapper quotationMapper;
    @Autowired
    ProOrderMapper orderMapper;

    private final String templatePath = "quotation/";

    /**
     * 询价明细列表（询价本身不存在明细（信息比较简单），所谓明细就是询价条目明细）
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String oid = request.getParameter("oid");
        ProQuotation quotation = quotationMapper.selectByOid(oid);
        if (null == quotation) {
            quotation = new ProQuotation();
            quotation.setOid(oid);
        }
        modelAndView.addObject("quotation", quotation);
        modelAndView.setViewName(templatePath+"quotationEdit");
        return modelAndView;
    }

    /**
     * 报价 信息
     * @param modelAndView
     * @param request
     * @return
     */
    @RequestMapping("/info")
    public ModelAndView toInfo(ModelAndView modelAndView, HttpServletRequest request) {
        String oid = request.getParameter("oid");
        String pid = request.getParameter("pid");
        // 传了bid按bid查，否则按oid查
        ProQuotation quotation = StringUtils.isNotBlank(pid) ? quotationMapper.selectByPrimaryKey(pid) : quotationMapper.selectByOid(oid);
        modelAndView.addObject("quotation", quotation);
        modelAndView.setViewName(templatePath+"quotationInfo");
        return modelAndView;
    }

    /**
     * 询价明细列表（询价本身不存在明细（信息比较简单），所谓明细就是询价条目明细）
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProQuotation quotation, HttpServletRequest request) {

        if (null == quotation || StringUtils.isBlank(quotation.getOid())){
            return new ResponseResult(false, "关键信息空");
        }
        ProOrder order = orderMapper.selectByPrimaryKey(quotation.getOid());
        if(null == order){
            return new ResponseResult(false, "未找到订单");
        }
        if(ServletFileUpload.isMultipartContent(request)){
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("cannex");
            // -- 这一步是处理给客户的报价单
            if(files.size() > 0){
                String localPath; // 本地存储完整路径
                String fileName; // 文件名
                String localName; // 本地存储名
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID()+fileName.substring(fileName.indexOf("."));
                        //文件存储到本地
                        localPath = FileHelper.uploadSingleFile(files.get(0), localName, null);
                        quotation.setCustomerAnnexName(files.get(0).getOriginalFilename());
                        quotation.setCustomerAnnexPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // -- 这一步是处理自留的报价明细
            files = multipartRequest.getFiles("sannex");
            if(files.size() > 0){
                String localPath; // 本地存储完整路径
                String fileName; // 文件名
                String localName; // 本地存储名
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID()+fileName.substring(fileName.indexOf("."));
                        //文件存储到本地
                        localPath = FileHelper.uploadSingleFile(files.get(0), localName, null);
                        quotation.setSelfAnnexName(files.get(0).getOriginalFilename());
                        quotation.setSelfAnnexPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isBlank(quotation.getQid())) {
            quotation.setQid(CommonUtils.createUUID());
            int res = quotationMapper.insertSelective(quotation);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        } else {
            int res = quotationMapper.updateByPrimaryKey(quotation);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 下载附件
     * @param request
     * @param response
     */
    @RequestMapping("/downloadAnnex/{annexId}")
    public void annexDownload(@PathVariable("annexId") String annexId, HttpServletRequest request, HttpServletResponse response){
        ProQuotation quotation = quotationMapper.selectByAnnexId(annexId);
        if (null == quotation) {
            return;
        }
        String downloadName = annexId.equals(quotation.getCustomerAnnexPath()) ? quotation.getCustomerAnnexName() : quotation.getSelfAnnexName();
        try {
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(downloadName, "utf-8"));
            response.setContentType("multipart/form-data");
            OutputStream out = response.getOutputStream();
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(FileHelper.FILE_SAVE_PATH+annexId);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
