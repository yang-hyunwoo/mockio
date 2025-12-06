<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false; section>

<#-- header 섹션은 숨김 -->
    <#if section = "header">
    </#if>

    <#if section = "form">

        <div class="mok-page">

            <!-- 로고 -->
            <div class="mok-logo-wrap">
                <img src="${url.resourcesPath}/img/mockio-logo.png"
                     class="mok-logo" alt="mockio" />
            </div>

            <!-- 카드 -->
            <div class="mok-card">
                <div class="mok-body">

                    <form id="kc-passwd-update-form"
                          action="${url.loginAction}"
                          method="post">

                        <!-- 안내 문구 -->
                        <div class="mok-description">
                            새 비밀번호를 설정해 주세요.
                        </div>

                        <#assign newPwError = messagesPerField.exists('password-new')>
                        <#assign confirmError = messagesPerField.exists('password-confirm')>

                        <!-- 새 비밀번호 -->
                        <div class="mok-field">
                            <input id="password-new"
                                   name="password-new"
                                   type="password"
                                   placeholder="새 비밀번호"
                                   class="<#if newPwError>mok-input-error</#if>" />
                            <#if newPwError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('password-new'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <!-- 새 비밀번호 확인 -->
                        <div class="mok-field">
                            <input id="password-confirm"
                                   name="password-confirm"
                                   type="password"
                                   placeholder="새 비밀번호 확인"
                                   class="<#if confirmError>mok-input-error</#if>" />
                            <#if confirmError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <!-- 전역 메시지 (정책 위반 등) -->
                        <#if message?has_content && !messagesPerField?has_content>
                            <#assign msgType = (message.type!'')?lower_case />
                            <div class="<#if msgType == 'error'>mok-error<#else>mok-info</#if>">
                                ${kcSanitize(message.summary)?no_esc}
                            </div>
                        </#if>

                        <button type="submit" class="mok-btn">
                            비밀번호 변경하기
                        </button>

                    </form>

                </div>
            </div>
        </div>

    </#if>

    <#if section = "info">
    </#if>

</@layout.registrationLayout>
