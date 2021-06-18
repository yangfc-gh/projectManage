package cn.com.project.common;

import cn.com.project.data.dao.sys.SysLogMapper;
import cn.com.project.data.model.sys.SysLog;
import cn.com.project.data.model.sys.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志服务，记录系统关键操作日志
 */
@Component
public class SysLogComponent {
    @Autowired
    SysLogMapper sysLogMapper;

    /**
     * 数据操作类型：其它
     */
    public static String OPT_OTH = "0";
    /**
     * 数据操作类型：增
     */
    public static String OPT_ADD = "1";
    /**
     * 数据操作类型：删
     */
    public static String OPT_DEL = "2";
    /**
     * 数据操作类型：改
     */
    public static String OPT_UPD = "3";
    /**
     * 数据操作类型：审
     */
    public static String OPT_VER = "4";
    /**
     * 写日志表
     * @param logType 日志类型0其它1增2删3改
     * @param resume 日志概述
     * @param describe 详细
     * @param entity 操作的实体对象
     * @param optuser
     */
    /**
     * 写日志表
     * @param logType 日志类型，自定义，只要统一就好
     * @param resume 日志描述
     * @param describe 详细
     * @param request
     */
    public void writeLog(String logType, String resume, String describe, String entity, HttpServletRequest request){
        SysLog slog = new SysLog();
        slog.setLogid(CommonUtils.createUUID());
        slog.setLogtype(logType);
        slog.setDescribe(describe);
        slog.setEntity(entity);
        SysUser userMap =  (SysUser) request.getSession().getAttribute("userInfo");
        StringBuffer detail = new StringBuffer(resume);
        detail.append(",");
        if(null != userMap){
            slog.setOptuser(userMap.getUid());
            detail.append("操作人：");
            detail.append(userMap.getName());
            detail.append(",");
        }
        detail.append("IP：").append(IpUtil.getRequestRealIp(request));
        slog.setResume(detail.toString());
        sysLogMapper.insert(slog);
    }
}
