<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false; section>

<#-- header 섹션은 비워둠 (위에서 CSS로 숨김) -->
    <#if section = "header">
    </#if>

<#-- 로그인 폼 섹션 -->
    <#if section = "form">
        <div class="mok-page">

            <!-- 상단 로고 -->
            <div class="mok-logo-wrap">
                <img src="${url.resourcesPath}/img/mockio-logo.png"
                     class="mok-logo" alt="mockio"/>
            </div>

            <!-- 로그인 카드 (탭 없이) -->
            <div class="mok-card">

                <div class="mok-body">

                    <form id="kc-form-login" action="${url.loginAction}" method="post">

                        <div class="mok-field">
                            <input id="username"
                                   name="username"
                                   type="text"
                                   placeholder="아이디"
                                   value="${(login.username!'')}"
                                   autofocus />
                        </div>

                        <div class="mok-field">
                            <input id="password"
                                   name="password"
                                   type="password"
                                   placeholder="비밀번호" />
                        </div>

                        <#-- 🔻 에러를 버튼 바로 위에 표시 -->
                        <#if message?has_content>
                            <div class="mok-error">
                                ${kcSanitize(message.summary)?no_esc}
                            </div>
                        </#if>

                        <button type="submit" class="mok-btn">로그인</button>
                    </form>

                </div>
            </div>

            <!-- 하단 링크 (원하면 이 부분은 나중에 지워도 됨) -->
            <div class="mok-links">
                <a href="${url.loginResetCredentialsUrl}">비밀번호 찾기</a>
                <span>|</span>
                <a href="#">아이디 찾기</a>
                <#if realm.registrationAllowed>
                    <span>|</span>
                    <a href="${url.registrationUrl}">회원가입</a>
                </#if>
            </div>
        </div>
    </#if>

<#-- info 섹션은 사용 안 함 -->
    <#if section = "info">
    </#if>

</@layout.registrationLayout>
