package com.soto.crud.test;

import com.soto.crud.bean.Employee;
import com.soto.crud.dao.DepartmentMapper;
import com.soto.crud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试dao层工作
 * 使用spring单元测试，可以自动注入组件
 * 1.导入SpringTest模块
 * 2.@ContextConfiguration指定spring配置文件位置
 * 3.直接Autowired
 */
@ContextConfiguration(locations={"classpath:applicationContext_1.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MapperTest {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSession sqlSession;

    /**
     * 测试DepartmentMapper
     */
    @Test
    public void testCRUD() {
        System.out.println(departmentMapper);

//        1.插入几个部门
//        departmentMapper.insertSelective(new Department(null, "开发部"));
//        departmentMapper.insertSelective(new Department(null, "测试部"));

//        2.生成员工数据，测试员工插入
//        employeeMapper.insertSelective(new Employee(null, "soto", "M", "kkkk@qq.com",1));

//        3.批量插入多个员工；批量，使用可以执行批量操作的sqlSession
//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        for (int i = 0; i < 1000; i++) {
//            String uid = UUID.randomUUID().toString().substring(0, 5);
//            mapper.insertSelective(new Employee(null, uid, "M", uid+"ess@qq.com", 1));
//        }
//        System.out.println("批量完成");

//        4.查询员工
        List<Employee> employeeList = employeeMapper.selectByExampleWithDept(null);
        for (Employee e : employeeList) {
            System.out.println("Id:" + e.getEmpId()+"\n=======>Name: "+e.getEmpName());
        }
    }
}
