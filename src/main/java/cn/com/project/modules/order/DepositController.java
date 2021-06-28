package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.business.ProDepositMapper;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.model.business.ProBidder;
import cn.com.project.data.model.business.ProDeposit;
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
 * 投标保证金
 */
@CrossOrigin
@RequestMapping("deposit")
@Controller
public class DepositController {
    private Logger logger = LoggerFactory.getLogger(DepositController.class);

    @Autowired
    ProDepositMapper depositMapper;
    @Autowired
    CorporateMapper corporateMapper;

    private final String templatePath = "deposit/";

    /**
     * 去编辑
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String did = request.getParameter("did");
        String oid = request.getParameter("oid");
        ProDeposit deposit = depositMapper.selectByPrimaryKey(did);
        if (null == deposit) {
            deposit = new ProDeposit();
            deposit.setOid(oid);
        }
        List<Corporate> corporates = corporateMapper.selectByCondition(null);
        modelAndView.addObject("deposit", deposit);
        modelAndView.addObject("corporates", corporates);
        modelAndView.setViewName(templatePath+"depositEdit");
        return modelAndView;
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProDeposit deposit) {

        if (StringUtils.isBlank(deposit.getDid())) {
            deposit.setDid(CommonUtils.createUUID());
            int res = depositMapper.insertSelective(deposit);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        } else {
            int res = depositMapper.updateByPrimaryKey(deposit);
            if (res <= 0) {
                return new ResponseResult(false);
            }
        }
        return new ResponseResult(true);
    }

    @RequestMapping("/list")
    public ModelAndView toList(ProDeposit deposit, ModelAndView modelAndView) {
        List<ProDeposit> deposits = depositMapper.selectByCondition(deposit);
        modelAndView.addObject("deposits", deposits);
        modelAndView.setViewName(templatePath+"depositList");
        return modelAndView;
    }
}
