<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false displayMessage=false; section>

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

                        <#-- Remember Me 체크박스 -->
                        <#if realm.rememberMe?? && realm.rememberMe>
                            <div class="mok-remember">
                                <label class="mok-remember-label">
                                    <input id="rememberMe"
                                           name="rememberMe"
                                           type="checkbox"
                                           class="mok-remember-checkbox"
                                           <#if login.rememberMe?? && login.rememberMe>checked</#if> />

                                    <span class="mok-remember-custom"></span>
                                    로그인 상태 유지
                                </label>
                            </div>
                        </#if>

                        <#-- 전역 메시지: 성공 / 에러 구분해서 표시 -->
                        <#if message?has_content>
                            <#assign msgType = (message.type!'')?lower_case />
                            <div class="<#if msgType == 'error'>mok-error<#else>mok-info</#if>">
                                ${kcSanitize(message.summary)?no_esc}
                            </div>
                        </#if>

                        <button type="submit" class="mok-btn">로그인</button>
                    </form>

                    <#if social?? && social.providers?has_content>
                        <div class="mok-social-wrap">
                            <div class="mok-social-buttons">
                                <#list social.providers as provider>
                                    <a class="mok-social-btn mok-social-${provider.alias}"
                                       href="${provider.loginUrl}">
                                        <#-- 아이콘 -->
                                        <#if provider.alias == "google">
                                            <img src="${url.resourcesPath}/img/icon-google.svg"
                                                 alt="Google" class="mok-social-icon" />
                                            <span>Google로 시작하기</span>
                                        <#elseif provider.alias == "kakao">
                                            <img src="${url.resourcesPath}/img/icon-kakao.png"
                                                 alt="Kakao" class="mok-social-icon" />
                                            <span>Kakao로 시작하기</span>
                                        <#elseif provider.alias == "naver">
                                            <img src="${url.resourcesPath}/img/icon-naver.png"
                                                 alt="Naver" class="mok-social-icon" />
                                            <span>Naver로 시작하기</span>
                                        <#else>
                                            <span>${provider.displayName}로 시작하기</span>
                                        </#if>
                                    </a>
                                </#list>
                            </div>
                        </div>
                    </#if>


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
