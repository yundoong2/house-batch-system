package com.myproject.housebatch.job.lawd;

import com.myproject.housebatch.core.entity.Lawd;
import com.myproject.housebatch.core.service.LawdService;
import com.myproject.housebatch.job.validator.FilePathParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import static com.myproject.housebatch.job.lawd.LawdFieldSetMapper.*;

/**
 * 동 코드 마이그레이션 배치 작업 클래스
 * @author cyh68
 * @since 2023-06-03
 **/
@Configuration
@RequiredArgsConstructor
@Slf4j
public class LawdInsertJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LawdService lawdService;

    /**
     * @param lawdInsertStep {@link Step}
     * @return Job {@Link Job}
     * @author cyh68
     * @since 2023-06-03
     **/
    @Bean
    public Job lawdInsertJob(Step lawdInsertStep) {
        return jobBuilderFactory.get("lawdInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(new FilePathParameterValidator())
                .start(lawdInsertStep)
                .build();
    }

    /**
     * chunk 기반 Step 정의
     * @param lawdFileItemReader {@link FlatFileItemReader}
     * @param itemWriter {@link ItemWriter}
     * @return Step {@link Step}
     * @author cyh68
     * @since 2023-06-03
     **/
    @Bean
    @JobScope
    public Step lawdInsertStep(FlatFileItemReader<Lawd> lawdFileItemReader,
                               ItemWriter<Lawd> itemWriter) {
        return stepBuilderFactory.get("lawdInsertStep")
                .<Lawd, Lawd>chunk(1000)
                .reader(lawdFileItemReader)
                .writer(itemWriter)
                .build();
    }

    /**
     * 동 코드 텍스트 파일을 읽어오기 위한 ItemReader
     * @param filePath {@link String}
     * @return FlatFileItemReader {@link FlatFileItemReader}
     * @author cyh68
     * @since 2023-06-03
     **/
    @Bean
    @StepScope
    public FlatFileItemReader<Lawd> lawdFileItemReader(
            @Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<Lawd>()
                .name("lawdFileItemReader")
                .delimited()
                .delimiter("\t")
                .names(LAWD_CD, LAWD_DONG, EXIST) //읽어올 필드 이름 정의
                .linesToSkip(1)
                .fieldSetMapper(new LawdFieldSetMapper()) //FieldSetMapper 설정
                .resource(new ClassPathResource(filePath))
                .build();
    }

    /**
     * LawdService의 upsert 메소드를 실행하여 데이터들을 DB에 저장한다.
     * @return ItemWriter {@link ItemWriter}
     * @author cyh68
     * @since 2023-06-03
     **/
    @Bean
    @StepScope
    public ItemWriter<Lawd> itemWriter() {
        return items -> items.forEach(lawdService::upsert);
    }
}
