package com.soto.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soto.crud.bean.Employee;
import com.soto.crud.bean.Msg;
import com.soto.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 处理CRUD请求
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkuser")
    public Msg checkuse(@RequestParam("empName") String empName) {
        //判断用户名不否为合法表达式
        String regx = "(^[a-zA-Z0-9_-]{5,16}$)|(^[\u4e00-\u9fa5]{2,5}$)";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg", "用户名可以为2-5位中文或者6-16位英文和数字组合");
        }

        //数据库用户名重复校验
        boolean b =  employeeService.checkUser(empName);
        if (b) {
            return Msg.success().add("va_msg", "用户名可用");
        }else {
            return Msg.fail().add("va_msg", "用户名不可用");
        }
    }


    /**
     * 员工保存
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(Employee employee) {
        employeeService.saveEmp(employee);
        return Msg.success();
    }

    /**
     * 导入jackson包
     * @param pn
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn) {
//        这不是一个分页查询
//        引入PageHelper插件
//        在查询之前只需要调用,传入页码，以及每页大小
//        startPage后面紧跟的查询就是一个分页查询
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();
//        使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
//        封装了详细的分页信息，包括我们查出的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps,5);
        return Msg.success().add("pageInfo", page);
    }


    /**
     * 查询员工数据(分页查询)
     *
     * @return
     */
//    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn,
                          Model model) {
//        这不是一个分页查询
//        引入PageHelper插件
//        在查询之前只需要调用,传入页码，以及每页大小
//        startPage后面紧跟的查询就是一个分页查询
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeService.getAll();
//        使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
//        封装了详细的分页信息，包括我们查出的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps,5);
        model.addAttribute("pageInfo", page);
        return "list";
    }

}
