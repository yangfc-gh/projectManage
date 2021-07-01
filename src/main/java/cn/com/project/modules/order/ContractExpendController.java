package cn.com.project.modules.order;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.business.ProContractExpendMapper;
import cn.com.project.data.dao.business.ProContractMapper;
import cn.com.project.data.model.business.ProContract;
import cn.com.project.data.model.business.ProContractExpend;
import com.alibaba.fastjson.JSONObject;
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
 * 合同支出费用记录
 */
@CrossOrigin
@RequestMapping("expend")
@Controller
public class ContractExpendController {
    private Logger logger = LoggerFactory.getLogger(ContractExpendController.class);

    @Autowired
    ProContractExpendMapper contractExpendMapper;
    @Autowired
    ProContractMapper contractMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "contract/";
    private final String objName = "expend";

    /**
     * 去编辑
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String cid = request.getParameter("cid"); // 合同id
        String eid = request.getParameter("eid"); // 支出id
        ProContractExpend expend = contractExpendMapper.selectByPrimaryKey(eid);
        if (null == expend) {
            expend = new ProContractExpend();
            expend.setCid(cid);
        }
        modelAndView.addObject("expend", expend);
        modelAndView.setViewName(templatePath+objName+"Edit");
        return modelAndView;
    }

    /**
     * 编辑更新
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(ProContractExpend expend, HttpServletRequest request) {
        if (null == expend || StringUtils.isBlank(expend.getCid())){
            return new ResponseResult(false, "关键信息空");
        }
        ProContract contract = contractMapper.selectByPrimaryKey(expend.getCid());
        if(null == contract){
            return new ResponseResult(false, "未找到合同");
        }
        if (StringUtils.isBlank(expend.getEid())) {
            expend.setEid(CommonUtils.createUUID());
            int res = contractExpendMapper.insertSelective(expend);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "新增支出费用", JSONObject.toJSONString(expend), "ProContractExpend", request);
        } else {
            int res = contractExpendMapper.updateByPrimaryKey(expend);
            if (res <= 0) {
                return new ResponseResult(false);
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改支出费用", JSONObject.toJSONString(expend), "ProContractExpend", request);
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/del/{eid}")
    @ResponseBody
    public ResponseResult doDelete(@PathVariable("eid") String eid, HttpServletRequest request) {
        ProContractExpend expend = contractExpendMapper.selectByPrimaryKey(eid);
        if (null == expend) {
            new ResponseResult(false, "未找到支出费用款项");
        }
        int res = contractExpendMapper.deleteByPrimaryKey(eid);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除支出费用", JSONObject.toJSONString(expend), "ProContractExpend", request);
            return new ResponseResult(true);
        } else {
            return new ResponseResult(false);
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public ModelAndView getList(ProContractExpend expend, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        List<ProContractExpend> expends = contractExpendMapper.selectByCondition(expend);
        PageInfo<ProContractExpend> resInfo = new PageInfo<>(expends);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+objName+"List");
        return modelAndView;
    }

}
