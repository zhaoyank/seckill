package com.kaishengit.mapper;

import com.kaishengit.entity.AccountProductExample;
import com.kaishengit.entity.AccountProductKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountProductMapper {
    long countByExample(AccountProductExample example);

    int deleteByExample(AccountProductExample example);

    int deleteByPrimaryKey(AccountProductKey key);

    int insert(AccountProductKey record);

    int insertSelective(AccountProductKey record);

    List<AccountProductKey> selectByExample(AccountProductExample example);

    int updateByExampleSelective(@Param("record") AccountProductKey record, @Param("example") AccountProductExample example);

    int updateByExample(@Param("record") AccountProductKey record, @Param("example") AccountProductExample example);
}