package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProBidderMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.model.business.ProBidder;
import cn.com.project.data.model.business.ProQuotation;
import cn.com.project.data.model.obj.Corporate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
     * 询价明细列表（询价本身不存在明细（信息比较简单），所谓明细就是询价条目明细）
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

}
