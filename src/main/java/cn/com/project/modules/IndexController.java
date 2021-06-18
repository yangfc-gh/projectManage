package cn.com.project.modules;

import cn.com.project.data.dao.sys.SysMenuMapper;
import cn.com.project.data.dao.sys.SysUserMapper;
import cn.com.project.data.dao.sys.SysUserRoleMapper;
import cn.com.project.data.model.sys.SysMenu;
import cn.com.project.data.model.sys.SysUser;
import cn.com.project.data.model.sys.SysUserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    @Autowired
    SysMenuMapper sysMenuMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    @RequestMapping("/")
    public ModelAndView defalut(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        return login(modelAndView, request, response);
    }
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("userInfo", null);
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @RequestMapping("home")
    public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request) {
        SysUser user = (SysUser)request.getSession().getAttribute("userInfo");
        Map<String, String> params = new HashMap<>();
        params.put("pid", "0");
        params.put("uid", user.getUid());
        List<SysMenu> sysMenus = sysMenuMapper.selectByPid(params);
        modelAndView.addObject("menus", sysMenus);
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @RequestMapping("login")
    public ModelAndView login(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        SysUser user = (SysUser)request.getSession().getAttribute("userInfo");
        if (null != user) {
            try {
                response.sendRedirect(request.getContextPath()+"/home");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping("doLogin")
    public ModelAndView doLogin(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {

        SysUser user = (SysUser)request.getSession().getAttribute("userInfo");
        if (null != user) {
            try {
                response.sendRedirect(request.getContextPath()+"/home");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String loginname = request.getParameter("loginname");
        String password = request.getParameter("password");

        boolean isValid = false;
        if (StringUtils.isBlank(loginname) || StringUtils.isBlank(password)) {
            modelAndView.addObject("message", "用户名或密码为空");
        } else {
            SysUser sysUser = sysUserMapper.selectByLoginname(loginname);
            if (null == sysUser) {
                modelAndView.addObject("message", "登录名不存在");
            } else if (!password.equals(sysUser.getPassword())) {
                modelAndView.addObject("message", "密码错误");
            } else {
                List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByUser(sysUser.getUid());
                sysUser.setRoleids(null != sysUserRoles && !sysUserRoles.isEmpty() ? sysUserRoles.stream().map(SysUserRole::getRid).collect(Collectors.toList()) : null);
                HttpSession session = request.getSession();
                sysUser.setPassword("");
                session.setAttribute("userInfo", sysUser);
                isValid = true;
            }
        }
        if (isValid) {
            try {
                response.sendRedirect(request.getContextPath()+"/home");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @RequestMapping("testSession")
    @ResponseBody
    public String test() {
        return "success";
    }

}
