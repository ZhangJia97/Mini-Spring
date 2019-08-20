package xyz.suiwo.imooc.controller;

import xyz.suiwo.imooc.beans.Autowired;
import xyz.suiwo.imooc.service.SalaryService;
import xyz.suiwo.imooc.web.mvc.Controller;
import xyz.suiwo.imooc.web.mvc.RequestMapping;
import xyz.suiwo.imooc.web.mvc.RequestParam;

@Controller
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @RequestMapping("/getSalary.json")
    public int getSalary(@RequestParam("name") String name, @RequestParam("experience") String experience){
        return salaryService.calSalary(Integer.valueOf(experience));
    }
}
