<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=false; section>

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

                    <!-- 전역 에러 (필드 이외 에러) -->
                    <#if message?has_content && !messagesPerField?has_content>
                        <div class="mok-error">
                            ${kcSanitize(message.summary)?no_esc}
                        </div>
                    </#if>

                    <form id="kc-register-form" action="${url.registrationAction}" method="post">
                        <input type="hidden" id="provider" name="user.attributes.provider" value="normal" />
                        <!-- 아이디 -->
                        <#assign usernameError = messagesPerField.exists('username')>
                        <div class="mok-field">
                            <input type="text"
                                   id="username"
                                   name="username"
                                   placeholder="아이디"
                                   value="${(register.formData.username!'')}"
                                   class="<#if usernameError>mok-input-error</#if>"
                            />
                            <#if usernameError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('username'))?no_esc}
                                </div>
                            </#if>
                        </div>
                        <#assign nameError = messagesPerField.exists('name')>
                        <div class="mok-field">
                            <input type="text"
                                   id="name"
                                   name="name"
                                   placeholder="이름"
                                   value="${(register.formData.name!'')}"
                                   class="<#if nameError>mok-input-error</#if>"
                            />
                            <#if nameError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('name'))?no_esc}
                                </div>
                            </#if>
                        </div>
                        <!-- 성(lastName) -->
<#--                        <#assign lastNameError = messagesPerField.exists('lastName')>-->
<#--                        <div class="mok-field">-->
<#--                            <input type="text"-->
<#--                                   id="lastName"-->
<#--                                   name="lastName"-->
<#--                                   placeholder="성"-->
<#--                                   value="${(register.formData.lastName!'')}"-->
<#--                                   class="<#if lastNameError>mok-input-error</#if>"-->
<#--                            />-->
<#--                            <#if lastNameError>-->
<#--                                <div class="mok-field-error-text">-->
<#--                                    ${kcSanitize(messagesPerField.get('lastName'))?no_esc}-->
<#--                                </div>-->
<#--                            </#if>-->
<#--                        </div>-->

                        <!-- 이름(firstName) -->
<#--                        <#assign firstNameError = messagesPerField.exists('firstName')>-->
<#--                        <div class="mok-field">-->
<#--                            <input type="text"-->
<#--                                   id="firstName"-->
<#--                                   name="firstName"-->
<#--                                   placeholder="이름"-->
<#--                                   value="${(register.formData.firstName!'')}"-->
<#--                                   class="<#if firstNameError>mok-input-error</#if>"-->
<#--                            />-->
<#--                            <#if firstNameError>-->
<#--                                <div class="mok-field-error-text">-->
<#--                                    ${kcSanitize(messagesPerField.get('firstName'))?no_esc}-->
<#--                                </div>-->
<#--                            </#if>-->
<#--                        </div>-->

                        <!-- 이메일 -->
                        <#assign emailError = messagesPerField.exists('email')>
                        <div class="mok-field">
                            <input type="email"
                                   id="email"
                                   name="email"
                                   placeholder="이메일"
                                   value="${(register.formData.email!'')}"
                                   class="<#if emailError>mok-input-error</#if>"
                            />
                            <#if emailError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('email'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <!-- 비밀번호 -->
                        <#assign passwordError = messagesPerField.exists('password')>
                        <div class="mok-field">
                            <input type="password"
                                   id="password"
                                   name="password"
                                   placeholder="비밀번호"
                                   class="<#if passwordError>mok-input-error</#if>"
                            />
                            <#if passwordError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('password'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <!-- 비밀번호 확인 -->
                        <#assign confirmError = messagesPerField.exists('password-confirm')>
                        <div class="mok-field">
                            <input type="password"
                                   id="password-confirm"
                                   name="password-confirm"
                                   placeholder="비밀번호 확인"
                                   class="<#if confirmError>mok-input-error</#if>"
                            />
                            <#if confirmError>
                                <div class="mok-field-error-text">
                                    ${kcSanitize(messagesPerField.get('password-confirm'))?no_esc}
                                </div>
                            </#if>
                        </div>

                        <!-- 버튼 -->
                        <button type="submit" class="mok-btn">회원가입</button>

                    </form>

                </div>
            </div>
        </div>

    </#if>

</@layout.registrationLayout>
