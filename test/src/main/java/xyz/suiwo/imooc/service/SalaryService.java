package xyz.suiwo.imooc.service;

import xyz.suiwo.imooc.beans.Bean;

@Bean
public class SalaryService {
    public Integer calSalary(Integer experience){
        return experience * 5;
    }
}
