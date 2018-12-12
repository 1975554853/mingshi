package com.example.springLearning.domain;

import com.example.springLearning.dao.OfficeDao;
import com.example.springLearning.pojo.Office;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public HashMap selectOffice(Integer page,Integer limit){
        PageHelper.startPage(page,limit);
        HashMap hashMap =new HashMap();
        try {
            List<Office> offices = officeDao.selectOffice();
            PageInfo pageInfo = new PageInfo(offices);
            if (offices != null) {
                hashMap.put("status",0);
                hashMap.put("message","");
                hashMap.put("total",pageInfo.getTotal());
                hashMap.put("data",pageInfo.getList());
                return hashMap;
            }
        }catch (Exception e){
            return hashMap;
        }
        return hashMap;
    }

    //删除office
    public boolean deleteOffice(Integer id){
        try {
            Integer line = officeDao.deleteOffice(id);
            if (line > 0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
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
