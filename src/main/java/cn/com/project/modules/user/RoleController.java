package cn.com.project.modules.user;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.data.dao.sys.SysRoleMapper;
import cn.com.project.data.dao.sys.SysRoleMenuMapper;
import cn.com.project.data.dao.sys.SysUserRoleMapper;
import cn.com.project.data.model.sys.SysRole;
import cn.com.project.data.model.sys.SysRoleMenu;
import cn.com.project.data.model.sys.SysUserRole;
import cn.com.project.modules.user.service.AuthorityService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理控制层
 */
@CrossOrigin
@RequestMapping("role")
@Controller
public class RoleController {
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    AuthorityService authorityService;

    private String templatePath = "auth/role/";

    /**
     * 跳转角色管理首页
     */
    @RequestMapping("/index")
    public String toIndex() {
        return templatePath+"roleIndex";
    }

    /**
     * 所有有效（状态为1）角色
     */
    @RequestMapping("/all")
    @ResponseBody
    public ResponseResult getAll() {
        SysRole sysRole = new SysRole();
        sysRole.setState((byte)1);
        List<SysRole> sysRoles = sysRoleMapper.selectByCondition(sysRole);
        return new ResponseResult(sysRoles);
    }

    /**
     * 跳转编辑首页
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String rid = request.getParameter("rid");
        if (StringUtils.isNotBlank(rid)) {
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(rid);
            modelAndView.addObject("roleInfo", sysRole);
        }
        modelAndView.setViewName(templatePath+"roleEdit");
        return modelAndView;
    }

    /**
     * 所有角色（不分页）
     * @param sysRole
     */
    @RequestMapping("/list")
    public ModelAndView getList(SysRole sysRole, ModelAndView modelAndView) {
        List<SysRole> sysRoles = sysRoleMapper.selectByCondition(sysRole);
        modelAndView.addObject("roles", sysRoles);
        modelAndView.setViewName(templatePath+"roleList");
        return modelAndView;
    }

    /**
     * 一个角色的详情
     * @param rid
     */
    @RequestMapping("/detail/{rid}")
    @ResponseBody
    public ResponseResult getDetail(@PathVariable("rid") String rid) {
        if (StringUtils.isBlank(rid)) {
            return new ResponseResult(false, "参数为空");
        }
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(rid);
        return new ResponseResult(sysRole);
    }
    /**
     * 跳转配置菜单页面
     */
    @RequestMapping("/grantMenu/{rid}")
    public ModelAndView grantMenu(@PathVariable("rid") String rid, ModelAndView modelAndView) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(rid);
        modelAndView.addObject("roleInfo", sysRole);
        modelAndView.setViewName(templatePath+"roleMenu");
        return modelAndView;
    }
    /**
     * 跳转配置用户页面
     */
    @RequestMapping("/grantUser/{rid}")
    public ModelAndView grantUser(@PathVariable("rid") String rid, ModelAndView modelAndView) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(rid);
        modelAndView.addObject("roleInfo", sysRole);
        modelAndView.setViewName(templatePath+"roleUser");
        return modelAndView;
    }
    /**
     * 一个角色的有权限菜单
     * @param rid
     */
    @RequestMapping("/menus/{rid}")
    @ResponseBody
    public ResponseResult getRoleMenu(@PathVariable("rid") String rid) {
        if (StringUtils.isBlank(rid)) {
            return new ResponseResult(false, "参数为空");
        }
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectByRole(rid);
        return new ResponseResult(sysRoleMenus);
    }
    /**
     * 一个角色关联的用户
     * @param rid
     */
    @RequestMapping("/users/{rid}")
    @ResponseBody
    public ResponseResult getRoleUser(@PathVariable("rid") String rid) {
        if (StringUtils.isBlank(rid)) {
            return new ResponseResult(false, "参数为空");
        }
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByRole(rid);
        return new ResponseResult(sysUserRoles);
    }
    /**
     * 更新（新增或修改）一个信息
     * @param sysRole
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(SysRole sysRole, HttpServletRequest request) {
        if (null == sysRole || StringUtils.isBlank(sysRole.getName())) {
            return new ResponseResult(false, "关键信息为空");
        }
        if(StringUtils.isBlank(sysRole.getRid())) {
            sysRole.setRid(CommonUtils.createUUID());
            int res = sysRoleMapper.insertSelective(sysRole);
            if(res <= 0) {
                return new ResponseResult(false, "新增失败");
            }
//            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "商品新增", JSONObject.toJSONString(goods), "Goods", request);
        } else {
            SysRole sysRole1 = sysRoleMapper.selectByPrimaryKey(sysRole.getRid());
            if (null == sysRole1) {
                return new ResponseResult(false, "未找到菜单");
            }
            // 修改
            int res = sysRoleMapper.updateByPrimaryKey(sysRole);
            if(res <= 0) {
                return new ResponseResult(false, "修改失败");
            }
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 角色 关联 菜单
     */
    @PostMapping("/linkMenu/{rid}")
    @ResponseBody
    public ResponseResult doLinkMenu(@PathVariable("rid") String rid, HttpServletRequest request) {
        String roleMenus = request.getParameter("params");
        List<SysRoleMenu> sysRoleMenus = null;
        if(StringUtils.isNotBlank(roleMenus)) {
            sysRoleMenus = JSONObject.parseArray(roleMenus, SysRoleMenu.class);
        }
        if (null != sysRoleMenus && !sysRoleMenus.isEmpty()) {
            for (SysRoleMenu tmp : sysRoleMenus) {
                if (StringUtils.isBlank(tmp.getRid()) || StringUtils.isBlank(tmp.getMid())) {
                    return new ResponseResult(false, "关键信息为空");
                }
            }
            Map<String, List<String>> collect = sysRoleMenus.stream().collect(Collectors.groupingBy(SysRoleMenu::getRid, Collectors.mapping(SysRoleMenu::getRid, Collectors.toList())));
            if (collect.keySet().size() > 1){
                return new ResponseResult(false, "多个不同的角色id");
            }
        }
        boolean res = authorityService.roleRelationMenu(rid, sysRoleMenus);
        if (res){
            return new ResponseResult(true, "操作成功");
        }
        return new ResponseResult(false, "操作失败");
    }
    /**
     * 角色 关联 用户
     */
    @PostMapping("/linkUser/{rid}")
    @ResponseBody
    public ResponseResult doLinkUser(@PathVariable("rid") String rid, HttpServletRequest request) {
        String roleUsers = request.getParameter("params");
        List<SysUserRole> sysUserRoles = null;
        if(StringUtils.isNotBlank(roleUsers)) {
            sysUserRoles = JSONObject.parseArray(roleUsers, SysUserRole.class);
        }
        if (null != sysUserRoles && !sysUserRoles.isEmpty()) {
            for (SysUserRole tmp : sysUserRoles) {
                if (StringUtils.isBlank(tmp.getRid()) || StringUtils.isBlank(tmp.getUid())) {
                    return new ResponseResult(false, "关键信息为空");
                }
            }
            Map<String, List<String>> collect = sysUserRoles.stream().collect(Collectors.groupingBy(SysUserRole::getRid, Collectors.mapping(SysUserRole::getRid, Collectors.toList())));
            if (collect.keySet().size() > 1){
                return new ResponseResult(false, "多个不同的角色id");
            }
        }
        boolean res = authorityService.roleRelationUser(rid, sysUserRoles);
        if (res){
            return new ResponseResult(true, "操作成功");
        }
        return new ResponseResult(false, "操作失败");
    }
}
