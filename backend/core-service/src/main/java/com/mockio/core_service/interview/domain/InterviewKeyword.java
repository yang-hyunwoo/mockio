package com.mockio.core_service.interview.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interview_keyword")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setting_id", nullable = false)
    private UserInterviewSetting interviewSetting;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    private InterviewKeyword(UserInterviewSetting interviewSetting, String keyword) {
        this.interviewSetting = interviewSetting;
        this.keyword = keyword;
    }

    public static InterviewKeyword create(UserInterviewSetting interviewSetting, String keyword) {
        return new InterviewKeyword(interviewSetting, keyword);
    }
}
