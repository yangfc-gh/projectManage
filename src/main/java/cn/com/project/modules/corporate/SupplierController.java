package cn.com.project.modules.corporate;

import cn.com.project.common.CommonUtils;
import cn.com.project.common.ResponseResult;
import cn.com.project.common.SysLogComponent;
import cn.com.project.data.dao.obj.SupplierMapper;
import cn.com.project.data.model.obj.Supplier;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * 供应商管理
 */
@CrossOrigin
@RequestMapping("supplier")
@Controller
public class SupplierController {
    @Autowired
    SupplierMapper supplierMapper;
    @Autowired
    SysLogComponent sysLogComponent;

    private final String templatePath = "supplier/";
    /**
     * 跳转管理首页
     */
    @RequestMapping("/index")
    public String toIndex(ModelAndView modelAndView) {
        return templatePath+"supplierIndex";
    }

    /**
     * 跳转编辑首页
     */
    @RequestMapping("/toEdit")
    public ModelAndView toEdit(ModelAndView modelAndView, HttpServletRequest request) {
        String sid = request.getParameter("sid");
        if (StringUtils.isNotBlank(sid)) {
            Supplier supplier = supplierMapper.selectByPrimaryKey(sid);
            modelAndView.addObject("supplierInfo", supplier);
        }
        modelAndView.setViewName(templatePath+"supplierEdit");
        return modelAndView;
    }

    /**
     * 所有
     * @param supplier
     */
    @RequestMapping("/list")
    public ModelAndView getList(Supplier supplier, ModelAndView modelAndView) {
        PageHelper.startPage(Integer.valueOf(1), 500);//不分页，一页默认最多展示500条，在这使用分页的目的是获取总行数
        supplier.setStatus("1"); // 只让查询到未删除的吧
        List<Supplier> suppliers = supplierMapper.selectByCondition(supplier);
        PageInfo<Supplier> resInfo = new PageInfo<Supplier>(suppliers);
        modelAndView.addObject("resInfo", resInfo);
        modelAndView.setViewName(templatePath+"supplierList");
        return modelAndView;
    }

    /**
     * 更新（新增或修改）一个信息
     * @param supplier
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseResult doUpdate(Supplier supplier, HttpServletRequest request) {
        if (null == supplier || StringUtils.isBlank(supplier.getName())) {
            return new ResponseResult(false, "关键信息为空");
        }
        if(StringUtils.isBlank(supplier.getSid())) {
            supplier.setSid(CommonUtils.createUUID());
            int res = supplierMapper.insertSelective(supplier);
            if(res <= 0) {
                return new ResponseResult(false, "新增失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_ADD, "新增供应商", JSONObject.toJSONString(supplier), "Supplier", request);
        } else {
            Supplier supplier1 = supplierMapper.selectByPrimaryKey(supplier.getSid());
            if (null == supplier1) {
                return new ResponseResult(false, "未找到该供应商");
            }
            // 修改
            int res = supplierMapper.updateByPrimaryKey(supplier);
            if(res <= 0) {
                return new ResponseResult(false, "修改失败");
            }
            sysLogComponent.writeLog(SysLogComponent.OPT_UPD, "修改供应商", JSONObject.toJSONString(supplier1)+"——"+JSONObject.toJSONString(supplier), "Supplier", request);
        }
        return new ResponseResult(true, "操作成功");
    }


    /**
     * 删除
     * @param sid
     */
    @PostMapping("/del/{sid}")
    @ResponseBody
    public ResponseResult doDel(@PathVariable("sid") String sid, HttpServletRequest request) {
        Supplier supplier = supplierMapper.selectByPrimaryKey(sid);
        if (null == sid) {
            return new ResponseResult(false, "未找到供应商");
        }
        Supplier supplier1 = new Supplier();
        supplier1.setSid(sid);
        supplier1.setStatus("0");
        int res = supplierMapper.updateByPrimaryKeySelective(supplier1);
        if (res > 0) {
            sysLogComponent.writeLog(SysLogComponent.OPT_DEL, "删除供应商", sid, "Supplier", request);
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
