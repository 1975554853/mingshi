package com.example.springLearning.domain;

import com.example.springLearning.config.SYSTEM_CONFIG;
import com.example.springLearning.config.SYSTEM_DTO;
import com.example.springLearning.config.SYSTEM_MESSAGE;
import com.example.springLearning.dao.OfficeInformationDao;
import com.example.springLearning.pojo.OfficeInformation;
import com.example.springLearning.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "OfficeInformationService")
public class OfficeInformationService {

    @Autowired
    OfficeInformationDao officeInformationDao;

    @Modifying
    @Cacheable("AddInformation")
    @Transactional
    public SYSTEM_DTO AddInformation(String txt) {
        if (StringUtils.isBlank(txt)){
            return SYSTEM_DTO.GET_RESULT(false,SYSTEM_MESSAGE.ERROR_CLASS);
        }
        User user = SYSTEM_CONFIG.getUser();
        Integer officeId = user.getOfficeId();
        List<OfficeInformation> list = officeInformationDao.findByOfficeId(officeId);
        if (list != null){
            officeInformationDao.deleteByOfficeId(officeId);
        }
        OfficeInformation officeInformation = new OfficeInformation();
        officeInformation.setContent(txt);
        officeInformation.setOfficeId(officeId);
        OfficeInformation mation = officeInformationDao.save(officeInformation);
        if (mation ==null){
            return SYSTEM_DTO.GET_RESULT(false,SYSTEM_MESSAGE.ERROR_CLASS);
        }
        return SYSTEM_DTO.GET_RESULT(true,SYSTEM_MESSAGE.SUCCESS_SYSTEM);

    }
    @Modifying
    @Cacheable("selectInformation")
    @Transactional
    public Map selectInformation() {
        User user = SYSTEM_CONFIG.getUser();
        Integer officeId = user.getOfficeId();
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(1, 1);
        List<OfficeInformation> list = officeInformationDao.findByOfficeId(officeId);
        PageInfo pageInfo = new PageInfo(list);
        return SYSTEM_CONFIG.page(pageInfo);
    }

    public String findInfoByOfficeId(Integer officeId) {
        OfficeInformation officeInformation = officeInformationDao.findOfficeInformationByOfficeId(officeId);
        if(officeInformation==null){
            return "暂无工作室简介";
        }

        return officeInformation.getContent()==null ? "暂无工作室简介" : officeInformation.getContent();
    }
}
