<#import "template.ftl" as layout>

<@layout.registrationLayout
displayInfo=false
displayMessage=!messagesPerField.existsError('username')
;
section
>

    <#if section = "header">
        <!-- header hidden -->
    </#if>

    <#if section = "form">

        <div class="mok-page">

            <!-- 로고 -->
            <div class="mok-logo-wrap">
                <img src="${url.resourcesPath}/img/mockio-logo.png"
                     class="mok-logo" alt="mockio">
            </div>

            <!-- 카드 -->
            <div class="mok-card">
                <div class="mok-body">

                    <!-- 전역 메시지 (성공/에러 공통) -->
                    <#if message?has_content && !messagesPerField?has_content>
                        <div class="mok-${message.type?lower_case == 'error'?then('error', 'info')}">
                            ${kcSanitize(message.summary)?no_esc}
                        </div>
                    </#if>

                    <form id="kc-reset-password-form"
                          action="${url.loginAction}"
                          method="post"
                          onsubmit="return disableResetButton();">

                        <!-- 안내 문구 -->
                        <div class="mok-description">
                            비밀번호를 재설정할 수 있는 링크를
                            가입하신 이메일 주소로 보내드립니다.
                        </div>

                        <!-- 아이디 / 이메일 -->
                        <#assign usernameError = messagesPerField.exists('username')>
                        <div class="mok-field">
                            <input type="text"
                                   id="username"
                                   name="username"
                                   placeholder="아이디 또는 이메일"
                                   value="${(auth?has_content && auth.showUsername())?then(auth.attemptedUsername, '')}"
                                   class="<#if usernameError>mok-input-error</#if>"
                            />
                            <#if usernameError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('username'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <!-- 버튼 -->
                        <button type="submit" class="mok-btn" id="reset-btn">
                            비밀번호 재설정 메일 보내기
                        </button>

                    </form>

                </div>
            </div>

            <!-- 하단 링크 -->
            <div class="mok-links">
                <a href="${url.loginUrl}">로그인으로 돌아가기</a>
            </div>

        </div>

    </#if>
<script>
    function disableResetButton() {
        var btn = document.getElementById('reset-btn');
        if (!btn) return true;

        // 이미 눌렸으면 다시 전송 막기
        if (btn.dataset.clicked === 'true') {
            return false;
        }

        btn.dataset.clicked = 'true';
        btn.disabled = true;
        btn.innerText = '메일 전송 중...';
        return true;
    }
</script>
</@layout.registrationLayout>
