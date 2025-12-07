<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false displayMessage=false; section>

    <#if section = "form">
        <script>
            // 바로 로그인 프로세스 재시작 (로그인 화면으로)
            window.location.href = "${url.loginRestartFlowUrl}";
        </script>

        <noscript>
            <div class="mok-page">
                <div class="mok-card">
                    <div class="mok-body">
                        <h1 class="mok-title">${msg("pageExpiredTitle")}</h1>
                        <p class="mok-text">
                            페이지가 만료되었습니다.<br/>
                            아래 버튼을 눌러 다시 로그인해 주세요.
                        </p>
                        <a href="${url.loginRestartFlowUrl}" class="mok-btn">
                            다시 로그인하기
                        </a>
                    </div>
                </div>
            </div>
        </noscript>
    </#if>

</@layout.registrationLayout>
