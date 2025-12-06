(function () {
    const body = document.body;
    const btn = document.getElementById("theme-switch");

    const systemPrefDark = window.matchMedia("(prefers-color-scheme: dark)");

    function applyTheme(mode) {
        body.classList.remove("theme-dark", "theme-light", "theme-auto");

        if (mode === "dark") body.classList.add("theme-dark");
        else if (mode === "light") body.classList.add("theme-light");
        else body.classList.add("theme-auto");
    }

    // 저장된 사용자 설정이 있으면 우선 적용
    const savedTheme = localStorage.getItem("theme-mode");

    if (savedTheme) {
        applyTheme(savedTheme);
    } else {
        // 시스템 테마 자동 반영
        applyTheme(systemPrefDark.matches ? "dark" : "light");
    }

    // 시스템 테마 변경 감지
    systemPrefDark.addEventListener("change", (e) => {
        if (!localStorage.getItem("theme-mode")) {
            applyTheme(e.matches ? "dark" : "light");
        }
    });

    // 토글 버튼 (사용자가 선택하면 저장)
    btn.addEventListener("click", () => {
        const current = body.classList.contains("theme-dark")
            ? "dark"
            : body.classList.contains("theme-light")
                ? "light"
                : "auto";

        let next = "auto";
        if (current === "auto") next = "light";
        else if (current === "light") next = "dark";
        else if (current === "dark") next = "auto";

        if (next === "auto") localStorage.removeItem("theme-mode");
        else localStorage.setItem("theme-mode", next);

        applyTheme(next);
    });
})();
