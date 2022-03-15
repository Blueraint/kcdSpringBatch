package com.kcd.KCDSpringBatch.mapper;

import com.kcd.KCDSpringBatch.dto.QuartzCronParam;
import com.kcd.KCDSpringBatch.dto.QuartzDailyParam;
import com.kcd.KCDSpringBatch.dto.QuartzFireParam;
import com.kcd.KCDSpringBatch.dto.QuartzParam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuartzParamMapper {
    QuartzParamMapper INSTANCE = Mappers.getMapper(QuartzParamMapper.class);

    /*Quartz Individual params to Common Param Mapper*/
    QuartzParam fireParamToCommon(QuartzFireParam quartzFireParam);

    QuartzParam dailyParamToCommon(QuartzDailyParam quartzFireParam);

    QuartzParam cronParamToCommon(QuartzCronParam quartzFireParam);
}
