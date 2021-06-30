package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProBidderMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.model.business.ProBidder;
import cn.com.project.data.model.business.ProEnquiry;
import cn.com.project.data.model.obj.Corporate;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

        if (StringUtils.isBlank(bidder.getBid())) {
            bidder.setBid(CommonUtils.createUUID());
            int res = bidderMapper.insertSelective(bidder);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        } else {
            int res = bidderMapper.updateByPrimaryKey(bidder);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        }
        return new ResponseResult(true);
    }
    /**
     * 删除
     */
    @RequestMapping("/del/{bid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("bid") String bid, ModelAndView modelAndView) {
        ProBidder bidder = bidderMapper.selectByPrimaryKey(bid);
        if (null == bidder) {
            new ResponseResult(false, "未找到参与方信息");
        }
        int res = bidderMapper.deleteByPrimaryKey(bid);
        if (res > 0) {
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }
}
