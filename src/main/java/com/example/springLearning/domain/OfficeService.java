package com.example.springLearning.domain;

import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {
    @Autowired
    private OfficeDao officeDao;

    /**
     * @param office
     * @return
     * 添加工作室
     */
    public boolean insertOffice(Office office){
        try {
            officeDao.save(office);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    //查询所有office
    public List<Office> selectOffice(){
        try {
            List<Office> offices = officeDao.selectOffice();
            if (offices != null) {
                return offices;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    /*//修改工作室信息
    public boolean updateOffice(Office office){
        try {
            Integer line = officeDao.updateOffice(office);
            if (line > 0) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }*/
}
