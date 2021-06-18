package cn.com.project.modules.user;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.sys.SysMenuMapper;
import cn.com.project.data.model.sys.SysMenu;
import cn.com.project.modules.user.service.AuthorityService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜单管理控制层
 */
@CrossOrigin
@RequestMapping("menu")
@Controller
public class MenuController {

    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    SysLogComponent sysLogComponent;

    private String templatePath = "auth/menu/";

    /**
     * 跳转菜单管理首页
     */
    @RequestMapping("/index")
    public String toIndex() {
        return templatePath+"menuIndex";
    }

    /**
     * 所有菜单（不分页）
     * @param sysMenu
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseResult getList(SysMenu sysMenu) {
        List<SysMenu> sysMenus = sysMenuMapper.selectByCondition(sysMenu);
        return new ResponseResult(sysMenus);
    }

    /**
     * 一个菜单的详情
     * @param mid
     */
    @RequestMapping("/detail/{mid}")
    public ResponseResult getMenuDetail(@PathVariable("mid") String mid) {
        if (StringUtils.isBlank(mid)) {
            return new ResponseResult(false, "参数为空");
        }
        SysMenu sysMenu = sysMenuMapper.selectByPrimaryKey(mid);
        return new ResponseResult(sysMenu);
    }

    /**
     * 更新（新增或修改）一个菜单信息
     * @param sysMenu
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(SysMenu sysMenu, HttpServletRequest request) {
        if (null == sysMenu || StringUtils.isBlank(sysMenu.getMname())) {
            return new ResponseResult(false, "关键信息为空");
        }
        if(StringUtils.isBlank(sysMenu.getMid())) {
            sysMenu.setMid(CommonUtils.createUUID());
            // 默认父级为0
            if (StringUtils.isBlank(sysMenu.getPid())) {
                sysMenu.setPid("0");
            }
            int res = sysMenuMapper.insertSelective(sysMenu);
            if(res <= 0) {
                return new ResponseResult(false, "新增失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "菜单新增", JSONObject.toJSONString(sysMenu), "SysMenu", request);
        } else {
            SysMenu sysMenu1 = sysMenuMapper.selectByPrimaryKey(sysMenu.getMid());
            if (null == sysMenu1) {
                return new ResponseResult(false, "未找到菜单");
            }
            // 修改
            int res = sysMenuMapper.updateByPrimaryKey(sysMenu);
            if(res <= 0) {
                return new ResponseResult(false, "修改失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "菜单修改", JSONObject.toJSONString(sysMenu), "SysMenu", request);
        }
        return new ResponseResult(sysMenu);
    }

    /**
     * 删除一个
     * @param mid
     */
    @PostMapping("/del/{mid}")
    @ResponseBody
    public ResponseResult doDelMenu(@PathVariable("mid") String mid) {
        if (StringUtils.isBlank(mid)) {
            return new ResponseResult(false, "关键信息为空");
        }
        boolean res = authorityService.delMenu(mid);
        if (res) {
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
        List<SysMenu> menus = JSONObject.parseArray(datas, SysMenu.class);
        int res = sysMenuMapper.sortNo(menus);
        if(res > 0){
            return "success";
        }else{
            return "failed";
        }
    }
}
