package cn.com.project.modules.system;

import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.sys.SysDictsMapper;
import cn.com.project.data.model.sys.SysDicts;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统字典管理
 */
@CrossOrigin
@RequestMapping("dict")
@Controller
public class DictionaryController {
    @Autowired
    SysDictsMapper sysDictsMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private String templatePath = "dict/";
    /**
     * 跳转管理首页
     */
    @RequestMapping("/index")
    public String toIndex() {
        return templatePath+"dictIndex";
    }

    /**
     * 所有菜单（不分页）
     * @param sysDicts
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseResult getList(SysDicts sysDicts) {
        if (null == sysDicts.getState()) {
            sysDicts.setState((byte)1);
        }
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(sysDicts);
        return new ResponseResult(sysDictsList);
    }

    /**
     * 新增一个信息
     * @param sysDicts
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult doAdd(SysDicts sysDicts, HttpServletRequest request) {
        if (null == sysDicts || StringUtils.isBlank(sysDicts.getDname()) || StringUtils.isBlank(sysDicts.getPcode())) {
            return new ResponseResult(false, "关键信息为空");
        }
        SysDicts sysDicts1 = sysDictsMapper.selectByPrimaryKey(sysDicts.getDcode());
        if (null != sysDicts1) {
            return new ResponseResult(false, "字典编码重复，请重新设置");
        }
        int res = sysDictsMapper.insertSelective(sysDicts);
        if(res <= 0) {
            return new ResponseResult(false, "新增失败");
        }
        sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "字典新增", JSONObject.toJSONString(sysDicts), "SysDicts", request);
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 修改一个信息
     * @param sysDicts
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(SysDicts sysDicts, HttpServletRequest request) {
        if (null == sysDicts || StringUtils.isBlank(sysDicts.getDname()) || StringUtils.isBlank(sysDicts.getPcode())) {
            return new ResponseResult(false, "关键信息为空");
        }
        SysDicts sysDicts1 = sysDictsMapper.selectByPrimaryKey(sysDicts.getDcode());
        if (null == sysDicts1) {
            return new ResponseResult(false, "未找到字典");
        }
        // 修改
        int res = sysDictsMapper.updateByPrimaryKey(sysDicts);
        if(res <= 0) {
            return new ResponseResult(false, "修改失败");
        }
        sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "字典修改", JSONObject.toJSONString(sysDicts), "SysDicts", request);
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 删除一个
     * @param dcode
     */
    @PostMapping("/del/{dcode}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("dcode") String dcode) {
        if (StringUtils.isBlank(dcode)) {
            return new ResponseResult(false, "关键信息为空");
        }
        SysDicts sysDicts = new SysDicts();
        sysDicts.setDcode(dcode);
        sysDicts.setState((byte)0);
        int res = sysDictsMapper.updateByPrimaryKeySelective(sysDicts);
        if (res > 0) {
            return new ResponseResult(true, "操作成功");
        }
        return new ResponseResult(false, "操作失败");
    }

    /**
     * 排序
     * @param request
     * @return
     */
    @RequestMapping("sort")
    @ResponseBody
    public String sort(HttpServletRequest request){
        String datas = request.getParameter("datas");
        if(StringUtils.isBlank(datas)){
            return "data is empty";
        }
        List<SysDicts> dicts = JSONObject.parseArray(datas, SysDicts.class);
        int res = sysDictsMapper.sortNo(dicts);
        if(res > 0){
            return "success";
        }else{
            return "failed";
        }
    }
}
