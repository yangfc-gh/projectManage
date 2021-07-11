package cn.com.project.modules.corporate;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ExcelUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.obj.CorporateMapper;
import cn.com.project.data.dao.sys.SysDictsMapper;
import cn.com.project.data.model.obj.Corporate;
import cn.com.project.data.model.sys.SysDicts;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 公司主体管理
 */
@CrossOrigin
@RequestMapping("corporate")
@Controller
public class CorporateController {
    @Autowired
    CorporateMapper corporateMapper;
    @Autowired
    SysDictsMapper sysDictsMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "corporate/";
    /**
     * 跳转管理首页
     */
    @RequestMapping("/index")
    public ModelAndView toIndex(ModelAndView modelAndView) {
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        List<SysDicts> ztlx = sysDictsList.stream().filter(d -> "ZTLX".equals(d.getPcode())).collect(Collectors.toList());
        modelAndView.addObject("dict_ztlx", ztlx);
        modelAndView.setViewName(templatePath+"corporateIndex");
        return modelAndView;
    }

    /**
     * 跳转编辑首页
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String cid = request.getParameter("cid");
        if (StringUtils.isNotBlank(cid)) {
            Corporate corporate = corporateMapper.selectByPrimaryKey(cid);
            modelAndView.addObject("corporateInfo", corporate);
        }
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        List<SysDicts> ztlx = sysDictsList.stream().filter(d -> "ZTLX".equals(d.getPcode())).collect(Collectors.toList());
        modelAndView.addObject("dict_ztlx", ztlx);
        modelAndView.setViewName(templatePath+"corporateEdit");
        return modelAndView;
    }

    /**
     * 所有
     * @param corporate
     */
    @RequestMapping("/list")
    public ModelAndView getList(Corporate corporate, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        corporate.setStatus("1"); // 只让查询到未删除的吧
        List<Corporate> corporates = corporateMapper.selectByCondition(corporate);
        PageInfo<Corporate> resInfo = new PageInfo<Corporate>(corporates);
        List<SysDicts> sysDictsList = sysDictsMapper.selectByCondition(null);
        Map<String, String> ztlxMap = sysDictsList.stream().filter(d -> "ZTLX".equals(d.getPcode())).collect(Collectors.toMap(SysDicts::getDcode, SysDicts::getDname));
        for (Corporate corporate1 : resInfo.getList() ) {
            corporate1.setCtypeName(ztlxMap.get(corporate1.getCtype()));
        }
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+"corporateList");
        return modelAndView;
    }

    /**
     * 更新（新增或修改）一个信息
     * @param corporate
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(Corporate corporate, HttpServletRequest request) {
        if (null == corporate || StringUtils.isBlank(corporate.getName())) {
            return new ResponseResult(false, "关键信息为空");
        }
        if(StringUtils.isBlank(corporate.getCid())) {
            corporate.setCid(CommonUtils.createUUID());
            int res = corporateMapper.insertSelective(corporate);
            if(res <= 0) {
                return new ResponseResult(false, "新增失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "新增主体", JSONObject.toJSONString(corporate), "Place", request);
        } else {
            Corporate corporate1 = corporateMapper.selectByPrimaryKey(corporate.getCid());
            if (null == corporate1) {
                return new ResponseResult(false, "未找到该主体");
            }
            // 修改
            int res = corporateMapper.updateByPrimaryKey(corporate);
            if(res <= 0) {
                return new ResponseResult(false, "修改失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改主体", JSONObject.toJSONString(corporate1)+"——"+JSONObject.toJSONString(corporate), "Place", request);
        }
        return new ResponseResult(true, "操作成功");
    }


    /**
     * 删除
     * @param cid
     */
    @PostMapping("/del/{cid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("cid") String cid, HttpServletRequest request) {
        Corporate corporate = corporateMapper.selectByPrimaryKey(cid);
        if (null == cid) {
            return new ResponseResult(false, "未找到主体");
        }
        Corporate corporate1 = new Corporate();
        corporate1.setCid(cid);
        corporate1.setStatus("0");
        int res = corporateMapper.updateByPrimaryKeySelective(corporate1);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除主体", cid, "Corporate", request);
        } else {
            return new ResponseResult(true, "删除（操作数据）失败");
        }
        return new ResponseResult(true, "操作成功");
    }

    /**
     * 导出
     * @param place
     * @param response
     */
    /*@RequestMapping("/export")
    public void doExport(Place place, HttpServletRequest request, HttpServletResponse response){
        List<Place> places = placeMapper.selectByCondition(place);
        // 所有字典
        String[] headers = {"批次","地址","数量","入库时间"};
        List<List<Object>> datas = new ArrayList<>(); // 所有数据
        List<Object> data = null; // 一条数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] tempKZ = null;
        for (Place place1 : places) {
            data = new ArrayList<>();
            data.add(place1.getPno());// 批次
            data.add(place1.getAddress()); //
            data.add(place1.getAmount()); //
            data.add(null != place1.getCtime() ? simpleDateFormat.format(place1.getCtime()) : ""); //
            datas.add(data);
        }

        try {
            String path = ExcelUtils.writeExcel(0,"地址库", headers, datas);
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode("地址库.xls", "utf-8"));
            OutputStream out = response.getOutputStream();
            // inputStream：读文件，前提是这个文件必须存在，要不就会报错
            InputStream is = new FileInputStream(path);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
