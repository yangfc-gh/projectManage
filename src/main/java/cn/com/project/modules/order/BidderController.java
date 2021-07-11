package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.FileHelper;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.business.ProBidderMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.model.business.ProBidder;
import cn.com.project.data.model.obj.Corporate;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 投标参与方
 */
@CrossOrigin
@RequestMapping("bidder")
@Controller
public class BidderController {
    private Logger logger = LoggerFactory.getLogger(BidderController.class);

    @Autowired
    ProBidderMapper bidderMapper;
    @Autowired
    CorporateMapper corporateMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "bidder/";

    /**
     * 去编辑
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String bid = request.getParameter("bid");
        String oid = request.getParameter("oid");
        ProBidder bidder = bidderMapper.selectByPrimaryKey(bid);
        if (null == bidder) {
            bidder = new ProBidder();
            bidder.setOid(oid);
        }
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        ProBidder finalBidder = bidder;
        corporates = corporates.stream().filter(c -> "1".equals(c.getStatus()) || StringUtils.equals(finalBidder.getCorporateId(), c.getCid())).collect(Collectors.toList());
        modelAndView.addObject("bidder", bidder);
        modelAndView.addObject("corporates", corporates);
        modelAndView.setViewName(templatePath+"bidderEdit");
        return modelAndView;
    }

    /**
     * 去编辑
     */
    @RequestMapping("/list")
    public ModelAndView toList(ProBidder bidder, ModelAndView modelAndView, HttpServletRequest request) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProBidder> bidders = bidderMapper.selectByCondition(bidder);
        PageInfo<ProBidder> resInfo = new PageInfo<ProBidder>(bidders);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+"bidderList");
        return modelAndView;
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProBidder bidder, HttpServletRequest request) {

        if(ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
            List<MultipartFile> files = multipartRequest.getFiles("attachment");
            // -- 这一步是处理给客户的报价单
            if (files.size() > 0) {
                String fileName; // 文件名
                String localName; // 本地存储名
                try {
                    fileName = files.get(0).getOriginalFilename();
                    if (StringUtils.isNotBlank(fileName)) {
                        localName = CommonUtils.createUUID() + fileName.substring(fileName.indexOf("."));
                        //文件存储到本地
                        FileHelper.uploadSingleFile(files.get(0), localName, null);
                        bidder.setAnnexName(fileName);
                        bidder.setAnnexPath(localName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isBlank(bidder.getBid())) {
            bidder.setBid(CommonUtils.createUUID());
            int res = bidderMapper.insertSelective(bidder);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "添加竞标参与方", JSONObject.toJSONString(bidder), "Bidder", request);
        } else {
            int res = bidderMapper.updateByPrimaryKey(bidder);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改竞标参与方", JSONObject.toJSONString(bidder), "Bidder", request);
        }
        return new ResponseResult(true);
    }
    /**
     * 删除
     */
    @RequestMapping("/del/{bid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("bid") String bid, HttpServletRequest request) {
        ProBidder bidder = bidderMapper.selectByPrimaryKey(bid);
        if (null == bidder) {
            new ResponseResult(false, "未找到参与方信息");
        }
        int res = bidderMapper.deleteByPrimaryKey(bid);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除竞标参与方", JSONObject.toJSONString(bidder), "Bidder", request);
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }

    /**
     * 下载附件
     * @param request
     * @param response
     */
    @RequestMapping("/downloadAnnex/{annexId}")
    public void payementAnnexDownload(@PathVariable("annexId") String annexId, HttpServletRequest request, HttpServletResponse response) {
        ProBidder bidder = bidderMapper.selectByAnnexId(annexId);
        if (null == bidder) {
            return;
        }
        String downloadName = bidder.getAnnexName();
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
