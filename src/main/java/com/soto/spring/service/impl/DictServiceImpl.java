package com.soto.spring.service.impl;

import com.soto.spring.dao.DictMapper;
import com.soto.spring.model.SysDict;
import com.soto.spring.service.DictService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.xml.ws.soap.Addressing;
import java.util.List;

@Service
public class DictServiceImpl  implements DictService {

    @Autowired
    private DictMapper dictMapper;


    @Override
    public SysDict findById(@NotNull Long id) {
        return dictMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysDict> findBySysDict(SysDict sysDict, Integer offset, Integer limit) {
        RowBounds rowBounds = RowBounds.DEFAULT;
        if (offset != null && limit != null) {
            rowBounds = new RowBounds(offset, limit);
        }
        return dictMapper.selectBySysDict(sysDict, rowBounds);
    }

    @Override
    public boolean saveOrUpdate(SysDict sysDict) {
        if (sysDict.getId() == null) {
            return dictMapper.insert(sysDict) == 1;
        }
        return dictMapper.updateById(sysDict) == 1;
    }

    @Override
    public boolean deleteById(Long id) {
        return dictMapper.deleteById(id) == 1;
    }

}
