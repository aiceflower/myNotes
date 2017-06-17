package cn.itcast.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bean.Employee;
import cn.itcast.service.EmployeeService;

@Controller @Scope("prototype")//这里要指定为多例，因为spring默认为单例，而struts2每次请求会创建一个action，是安全的非单例的。
public class EmployeeManageAction {
	@Resource EmployeeService employeeService;
	private Employee employee;	
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String addUI(){
		return "add";
	}
	
	public String add(){
		employeeService.save(employee);
		ActionContext.getContext().put("message", "保存成功");
		return "message";
	}
}
