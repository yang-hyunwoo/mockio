package com.mockio.noti_service.service;

import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_jpa.dto.PageDto;
import com.mockio.noti_service.Mapper.NoticeMapper;
import com.mockio.noti_service.domain.NoticeBoard;
import com.mockio.noti_service.dto.NoticeDetailResDto;
import com.mockio.noti_service.dto.NoticePageResDto;
import com.mockio.noti_service.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_012;


@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 페이징 조회
     * @param pageable
     * @return 공지사항 페이지 객체
     */
    @Transactional(readOnly = true)
    public PageDto<NoticePageResDto> noticeList(Pageable pageable) {
        return PageDto.of(noticeRepository.findByPageNative(pageable), NoticeMapper::from);
    }

    /**
     * 공지사항 상세 조회
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public NoticeDetailResDto noticeDetail(Long id) {
        NoticeBoard noticeBoard = noticeRepository.findNoticeNative(id)
                .orElseThrow(() -> new CustomApiException(ERR_012.getHttpStatus(), ERR_012, ERR_012.getMessage()));
        int sort = noticeBoard.getSort();
        return NoticeMapper.from(noticeBoard,
                noticeRepository.findPrevNoticeNative(sort).orElse(null),
                noticeRepository.findNextNoticeNative(sort).orElse(null));
    }

    /**
     * 메인 공지사항 상세 조회
     * @return
     */
    @Transactional(readOnly = true)
    public NoticeDetailResDto mainNoticeDetail() {
        NoticeBoard noticeBoard = noticeRepository.findMainNotice()
                .orElseThrow(() -> new CustomApiException(ERR_012.getHttpStatus(), ERR_012, ERR_012.getMessage()));
        return NoticeMapper.from(noticeBoard, null, null);
    }
}
