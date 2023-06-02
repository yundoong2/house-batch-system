package com.myproject.housebatch.job.lawd;

import com.myproject.housebatch.core.entity.Lawd;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * 파일에서 읽어온 데이터를 Lawd 데이터와 매핑 시켜주는 Mapper
 * @author cyh68
 * @since 2023-06-03
 **/
public class LawdFieldSetMapper implements FieldSetMapper<Lawd> {

    public static final String LAWD_CD = "lawdCd";
    public static final String LAWD_DONG = "lawdDong";
    public static final String EXIST = "exist";
    private static final String EXIST_TRUE = "존재";

    @Override
    public Lawd mapFieldSet(FieldSet fieldSet) throws BindException {
        Lawd lawd = new Lawd();
        lawd.setLawdCd(fieldSet.readString(LAWD_CD));
        lawd.setLawdDong(fieldSet.readString(LAWD_DONG));
        lawd.setExist(fieldSet.readBoolean(EXIST, EXIST_TRUE));
        return lawd;
    }
}
