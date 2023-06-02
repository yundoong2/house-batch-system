package com.myproject.housebatch.core.service;

import com.myproject.housebatch.core.entity.Lawd;
import com.myproject.housebatch.core.repository.LawdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 동 코드 Migration 처리 Business Logic
 * @author cyh68
 * @since 2023-06-03
 **/
@Service
@RequiredArgsConstructor
public class LawdService {

    private final LawdRepository lawdRepository;

    /**
     * Lawd Entity를 파라미터로 받아 DB에 저장
     * <p>
     *     DB에 동 코드가 이미 있는 경우 update,
     *     없는 경우 insert
     * </p>
     * @param lawd {@link Lawd}
     * @author cyh68
     * @since 2023-06-03
     **/
    @Transactional
    public void upsert(Lawd lawd) {
        //데이터가 존재하면 update, 없으면 insert
        Lawd saved = lawdRepository.findByLawdCd(lawd.getLawdCd())
                .orElseGet(Lawd::new);
        saved.setLawdCd(lawd.getLawdCd());
        saved.setLawdDong(lawd.getLawdDong());
        saved.setExist(lawd.getExist());
        lawdRepository.save(saved);
    }
}
