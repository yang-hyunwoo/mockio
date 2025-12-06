<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false; section>

    <#if section = "header">
    </#if>

    <#if section = "form">

        <div class="mok-page">

            <!-- 로고 -->
            <div class="mok-logo-wrap">
                <img src="${url.resourcesPath}/img/mockio-logo.png"
                     class="mok-logo" alt="mockio" />
            </div>

            <div class="mok-card">
                <div class="mok-body">

                    <div class="mok-error">
                        <#-- 서버에서 온 메시지가 있으면 우선 사용 -->
                        <#if message?has_content>
                            ${kcSanitize(message.summary)?no_esc}
                        <#else>
                            링크가 만료되었거나 더 이상 유효하지 않습니다.<br/>
                            다시 비밀번호 재설정을 요청해 주세요.
                        </#if>
                    </div>

                    <div class="mok-description">
                        잠시 후 메인 화면으로 이동합니다.
                    </div>

                    <button class="mok-btn"
                            onclick="window.location.href='http://localhost:3000';">
                        메인으로 가기
                    </button>

                </div>
            </div>

        </div>

        <script>
            // 3초 뒤 자동 이동
            setTimeout(function () {
                window.location.href = 'http://localhost:3000';
            }, 3000);
        </script>

    </#if>

    <#if section = "info">
    </#if>

</@layout.registrationLayout>
