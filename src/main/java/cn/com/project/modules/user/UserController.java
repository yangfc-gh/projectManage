package cn.com.project.modules.user;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.sys.SysUserMapper;
import cn.com.project.data.dao.sys.SysUserRoleMapper;
import cn.com.project.data.model.sys.SysUser;
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
 * 用户管理控制层
 */
@CrossOrigin
@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysLogComponent sysLogComponent;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    private final String templatePath = "auth/user/";
    /**
     * 跳转用户管理首页
     */
    @RequestMapping("/index")
    public String toIndex() {
        return templatePath+"userIndex";
    }

    /**
     * 跳转编辑首页
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String uid = request.getParameter("uid");
        if (StringUtils.isNotBlank(uid)) {
            SysUser sysUser = sysUserMapper.selectByPrimaryKey(uid);
            modelAndView.addObject("userInfo", sysUser);
        }
        modelAndView.setViewName(templatePath+"userEdit");
        return modelAndView;
    }

    /**
     * 去修改密码
     */
    @RequestMapping("/toModifyPwd")
    public ModelAndView toModifyPwd(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.setViewName(templatePath+"passwordEdit");
        return modelAndView;
    }
    /**
     * 修改密码
     */
    @RequestMapping("/modifyPwd")
    @ResponseBody
    public ResponseResult doModifyPwd(HttpServletRequest request) {
        String opwd = request.getParameter("opwd");
        String npwd = request.getParameter("npwd");
        if (StringUtils.isBlank(opwd) || StringUtils.isBlank(npwd)) {
            return new ResponseResult(false, "参数为空");
        }
        SysUser user = (SysUser)request.getSession().getAttribute("userInfo");
        SysUser user1 = sysUserMapper.selectByPrimaryKey(user.getUid());
        if (!opwd.equals(user1.getPassword())){
            return new ResponseResult(false, "原密码不正确");
        }
        SysUser user2 = new SysUser();
        user2.setUid(user.getUid());
        user2.setPassword(npwd);
        int res = sysUserMapper.updateByPrimaryKeySelective(user2);
        if (res > 0) {
            return new ResponseResult(true, "修改成功");
        } else {
            return new ResponseResult(false, "修改失败");
        }
    }
    /**
     * 所有（不分页）
     * @param sysMenu
     */
    @RequestMapping("/list")
    public ModelAndView getList(SysUser sysMenu, ModelAndView modelAndView) {
        List<SysUser> sysUsers = sysUserMapper.selectByCondition(sysMenu);
        modelAndView.addObject("users", sysUsers);
        modelAndView.setViewName(templatePath+"userList");
        return modelAndView;
    }

    /**
     * 所有有效（状态为1）用户
     */
    @RequestMapping("/all")
    @ResponseBody
    public ResponseResult getAll(SysUser sysUser) {
        List<SysUser> sysUsers = sysUserMapper.selectByCondition(sysUser);
        return new ResponseResult(sysUsers);
    }
    /**
     * 一个的详情
     * @param uid
     */
    @RequestMapping("/detail/{uid}")
    @ResponseBody
    public ResponseResult getDetail(@PathVariable("uid") String uid) {
        if (StringUtils.isBlank(uid)) {
            return new ResponseResult(false, "参数为空");
        }
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(uid);
        return new ResponseResult(sysUser);
    }

    /**
     * 跳转配置菜单页面
     */
    @RequestMapping("/grantRole/{uid}")
    public ModelAndView grantMenu(@PathVariable("uid") String uid, ModelAndView modelAndView) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(uid);
        modelAndView.addObject("userInfo", sysUser);
        modelAndView.setViewName(templatePath+"userRole");
        return modelAndView;
    }
    /**
     * 一个用户的所有角色
     * @param uid
     */
    @RequestMapping("/roles/{uid}")
    @ResponseBody
    public ResponseResult getRoleUser(@PathVariable("uid") String uid) {
        if (StringUtils.isBlank(uid)) {
            return new ResponseResult(false, "参数为空");
        }
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByUser(uid);
        return new ResponseResult(sysUserRoles);
    }
    /**
     * 更新（新增或修改）一个信息
     * @param sysUser
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(SysUser sysUser, HttpServletRequest request) {
        if (null == sysUser || StringUtils.isBlank(sysUser.getName())) {
            return new ResponseResult(false, "关键信息为空");
        }
        if(StringUtils.isBlank(sysUser.getUid())) {
            sysUser.setUid(CommonUtils.createUUID());
            int res = sysUserMapper.insertSelective(sysUser);
            if(res <= 0) {
                return new ResponseResult(false, "新增失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "菜单用户", JSONObject.toJSONString(sysUser), "SysUser", request);
        } else {
            SysUser sysUser1 = sysUserMapper.selectByPrimaryKey(sysUser.getUid());
            if (null == sysUser1) {
                return new ResponseResult(false, "未找到用户");
            }
            // 修改
            int res = sysUserMapper.updateByPrimaryKey(sysUser);
            if(res <= 0) {
                return new ResponseResult(false, "修改失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改用户", JSONObject.toJSONString(sysUser), "SysUser", request);
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 删除一个
     * @param uid
     */
    @PostMapping("/del/{uid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("uid") String uid, HttpServletRequest request) {
        if (StringUtils.isBlank(uid)) {
            return new ResponseResult(false, "关键信息为空");
        }
        int res = sysUserMapper.deleteByPrimaryKey(uid);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除用户", uid, "SysUser", request);
            return new ResponseResult(true, "操作成功");
        }
        return new ResponseResult(false, "操作失败");
    }

    /**
     * 删除一个
     * @param uid
     */
    @PostMapping("/state/{uid}")
    @ResponseBody
    public ResponseResult changeState(@PathVariable("uid") String uid, HttpServletRequest request) {
        String newState = request.getParameter("newState");
        if (StringUtils.isBlank(newState) || (!"1".equals(newState) && !"0".equals(newState))) {
            return new ResponseResult(false, "新状态为空或不正确");
        }
        SysUser user = sysUserMapper.selectByPrimaryKey(uid);
        if (null == user) {
            return new ResponseResult(false, "未找到用户");
        }
        SysUser user1 = new SysUser();
        user1.setUid(uid);
        user1.setState(Byte.valueOf(newState));
        int res = sysUserMapper.updateByPrimaryKeySelective(user1);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "变更用户状态", JSONObject.toJSONString(user), "SysUser", request);
            return new ResponseResult(true, "操作成功");
        }
        return new ResponseResult(false, "操作失败");
    }

    /**
     * 用户 关联 角色
     */
    @PostMapping("/linkRole/{uid}")
    @ResponseBody
    public ResponseResult doLinkRole(@PathVariable("uid") String uid, HttpServletRequest request) {
        String userRoles = request.getParameter("params");
        List<SysUserRole> sysUserRoles = null;
        if(StringUtils.isNotBlank(userRoles)) {
            sysUserRoles = JSONObject.parseArray(userRoles, SysUserRole.class);
        }
        if (null != sysUserRoles && !sysUserRoles.isEmpty()) {
            for (SysUserRole tmp : sysUserRoles) {
                if (StringUtils.isBlank(tmp.getRid()) || StringUtils.isBlank(tmp.getUid())) {
                    return new ResponseResult(false, "关键信息为空");
                }
            }
            Map<String, List<String>> collect = sysUserRoles.stream().collect(Collectors.groupingBy(SysUserRole::getUid, Collectors.mapping(SysUserRole::getUid, Collectors.toList())));
            if (collect.keySet().size() > 1){
                return new ResponseResult(false, "多个不同的用户id");
            }
        }
        boolean res = authorityService.userRelationRole(uid, sysUserRoles);
        if (res){
            return new ResponseResult(true, "操作成功");
        }
        return new ResponseResult(false, "操作失败");
    }
}
